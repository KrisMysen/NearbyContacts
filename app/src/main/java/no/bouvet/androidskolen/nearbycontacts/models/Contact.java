package no.bouvet.androidskolen.nearbycontacts.models;

import com.google.gson.Gson;

public class Contact {

    private static final Gson gson = new Gson();

    private final String name;

    public Contact(String name) {
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

    public static Contact fromJson(String json) {
        return gson.fromJson(json, Contact.class);
    }
}
