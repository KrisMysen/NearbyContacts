package no.bouvet.androidskolen.nearbycontacts;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SelectedPersonFragment extends Fragment implements ModelUpdateListener {


    private static final String TAG = SelectedPersonFragment.class.getSimpleName();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.selected_person_fragment, container, false);

        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

        SelectPersonViewModel.INSTANCE.setModelUpdateListener(this);
        updateGui(SelectPersonViewModel.INSTANCE.getPerson());
    }

    @Override
    public void onModelChanged() {
        updateGui(SelectPersonViewModel.INSTANCE.getPerson());
    }


    private void updateGui(Person person) {
        Log.d(TAG, "Person selected: " + person.getName());
    }
}
