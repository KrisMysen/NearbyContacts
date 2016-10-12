package no.bouvet.androidskolen.nearbycontacts;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SelectedPersonFragment extends Fragment  {




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.selected_person_fragment, container, false);

        return view;
    }


    public void showInfoForPerson(Person person) {

    }
}
