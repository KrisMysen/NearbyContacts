package no.bouvet.androidskolen.nearbycontacts;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import no.bouvet.androidskolen.nearbycontacts.models.ModelUpdateListener;
import no.bouvet.androidskolen.nearbycontacts.models.OwnPersonViewModel;
import no.bouvet.androidskolen.nearbycontacts.models.Person;
import no.bouvet.androidskolen.nearbycontacts.models.SelectedPersonViewModel;

public class SelectedPersonFragment extends Fragment implements ModelUpdateListener, View.OnClickListener {


    private static final String TAG = SelectedPersonFragment.class.getSimpleName();
    private TextView personNameTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.selected_person_fragment, container, false);

        Button saveActivityButton = (Button) view.findViewById(R.id.save_to_contacts_button);
        saveActivityButton.setOnClickListener(this);

        personNameTextView = (TextView) view.findViewById(R.id.person_name_textView);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        SelectedPersonViewModel.INSTANCE.setModelUpdateListener(this);
        updateGui(SelectedPersonViewModel.INSTANCE.getPerson());
    }

    @Override
    public void onModelChanged() {
        updateGui(SelectedPersonViewModel.INSTANCE.getPerson());
    }


    private void updateGui(Person person) {

        Log.d(TAG, "Person selected: " + person.getName());

        personNameTextView.setText(person.getName());
    }

    @Override
    public void onClick(View view) {
        Intent contactIntent = new Intent(ContactsContract.Intents.Insert.ACTION);
        contactIntent.setType(ContactsContract.RawContacts.CONTENT_TYPE);

        Person person = OwnPersonViewModel.INSTANCE.getPerson();

        String name = person.getName();
        contactIntent.putExtra(ContactsContract.Intents.Insert.NAME, name);

        startActivityForResult(contactIntent, 1);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 1)
        {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(getActivity(), "Added Contact", Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Cancelled Added Contact", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
