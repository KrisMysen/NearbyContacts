package no.bouvet.androidskolen.nearbycontacts;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NearbyPersonsListFragment extends Fragment implements PersonDetectedListener, AdapterView.OnItemClickListener {

    private Map<String, Person> detectedPersons = new HashMap<>();
    private ListView listView;
    private PersonSelectedListener personSelectedListener;
    private ArrayAdapter<Person> personArrayAdapter;

    public interface PersonSelectedListener {
        void onPersonSelected(Person person);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.nearby_persons_fragment, container, false);

        listView = (ListView) view.findViewById(R.id.nearby_persons_listView);
        personArrayAdapter = new ArrayAdapter<>(getContext(), R.layout.nearby_persons_listview_item, new ArrayList<Person>());
        listView.setAdapter(personArrayAdapter);
        listView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            personSelectedListener = (PersonSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement PersonSelectedListener");
        }
    }

    @Override
    public void onPersonDetected(Person person) {
        detectedPersons.put(person.getName(), person);
        updateAdapterModel();
    }

    @Override
    public void onPersonLost(Person person) {
        detectedPersons.remove(person.getName());
        updateAdapterModel();
    }

    private void updateAdapterModel() {
        List<Person> personList = new ArrayList<>(detectedPersons.values());

        if (personArrayAdapter != null) {
            personArrayAdapter.clear();
            personArrayAdapter.addAll(personList);
            personArrayAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Person person = personArrayAdapter.getItem(i);
        if (personSelectedListener != null) {
            personSelectedListener.onPersonSelected(person);
        }
    }
}
