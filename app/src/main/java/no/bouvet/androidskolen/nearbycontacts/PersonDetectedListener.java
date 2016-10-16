package no.bouvet.androidskolen.nearbycontacts;

import no.bouvet.androidskolen.nearbycontacts.models.Person;

public interface PersonDetectedListener {

    void onPersonDetected(Person person);

    void onPersonLost(Person person);

}
