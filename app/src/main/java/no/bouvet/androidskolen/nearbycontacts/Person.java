package no.bouvet.androidskolen.nearbycontacts;

import com.google.gson.Gson;

public class Person {

    private static final Gson gson = new Gson();

    private final String name;

    public Person(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

    public String toJson() {
        return gson.toJson(this);
    }

    public static Person fromJson(String json) {
        return gson.fromJson(json, Person.class);
    }
}
