package no.bouvet.androidskolen.nearbycontacts;

public interface PersonDetectedListener {

    void onPersonDetected(Person person);

    void onPersonLost(Person person);

}
