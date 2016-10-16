package no.bouvet.androidskolen.nearbycontacts.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.bouvet.androidskolen.nearbycontacts.PersonDetectedListener;

public enum NearbyPersonsListViewModel implements PersonDetectedListener {

    INSTANCE;

    private Map<String, Person> detectedPersons = new HashMap<>();
    private ModelUpdateListener modelUpdateListener;

    public void setModelUpdateListener(ModelUpdateListener listener) {
        modelUpdateListener = listener;
    }

    public void removeModelUpdateListener(ModelUpdateListener listener) {
        if (modelUpdateListener == listener) {
            modelUpdateListener = null;
        }
    }

    public void onPersonDetected(Person person) {
        detectedPersons.put(person.getName(), person);
        fireModelUpdated();
    }

    public void onPersonLost(Person person) {
        detectedPersons.remove(person.getName());
        fireModelUpdated();
    }

    public List<Person> getNearbyPersons() {
        return new ArrayList<>(detectedPersons.values());
    }

    private void fireModelUpdated() {
        if (modelUpdateListener != null) {
            modelUpdateListener.onModelChanged();
        }
    }


}
