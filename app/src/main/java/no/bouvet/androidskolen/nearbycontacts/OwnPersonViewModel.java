package no.bouvet.androidskolen.nearbycontacts;

public enum OwnPersonViewModel {

    INSTANCE;

    private Person person;

    public void setPerson(Person person) {
        this.person = person;
    }

    public Person getPerson() {
        return person;
    }

}
