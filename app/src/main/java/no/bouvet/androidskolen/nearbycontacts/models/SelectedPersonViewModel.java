package no.bouvet.androidskolen.nearbycontacts.models;

public enum SelectedPersonViewModel {

    INSTANCE;

    private Person selectedPerson;
    private ModelUpdateListener modelUpdateListener;

    SelectedPersonViewModel() {
        selectedPerson = new Person("None selected");
    }

    public void setModelUpdateListener(ModelUpdateListener listener) {
        modelUpdateListener = listener;
    }

    public void removeModelUpdateListener(ModelUpdateListener listener) {
        if (modelUpdateListener == listener) {
            modelUpdateListener = null;
        }
    }

    public void setSelectedPerson(Person person) {
        selectedPerson = person;
        fireModelUpdated();
    }

    public Person getPerson() {
        return selectedPerson;
    }


    private void fireModelUpdated() {
        if (modelUpdateListener != null) {
            modelUpdateListener.onModelChanged();
        }
    }
}
