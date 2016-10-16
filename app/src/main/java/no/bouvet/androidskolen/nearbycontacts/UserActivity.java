package no.bouvet.androidskolen.nearbycontacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import no.bouvet.androidskolen.nearbycontacts.models.OwnPersonViewModel;
import no.bouvet.androidskolen.nearbycontacts.models.Person;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText userNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        Button startNearbyActivityButton = (Button) findViewById(R.id.start_nearby_activity_button);
        startNearbyActivityButton.setOnClickListener(this);

        userNameEditText = (EditText) findViewById(R.id.user_name_editText);

    }

    @Override
    protected void onStart() {
        super.onStart();

        Person person = OwnPersonViewModel.INSTANCE.getPerson();
        if (person != null) {
            userNameEditText.setText(person.getName());
        }
    }

    @Override
    public void onClick(View view) {
        Person person = createPersonFromInput();
        OwnPersonViewModel.INSTANCE.setPerson(person);

        Intent intent = new Intent(this, NearbyActivity.class);
        startActivity(intent);
    }

    private Person createPersonFromInput() {
        String name = userNameEditText.getText().toString();
        return new Person(name);
    }
}
