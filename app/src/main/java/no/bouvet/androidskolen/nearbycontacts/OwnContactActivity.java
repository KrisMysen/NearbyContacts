package no.bouvet.androidskolen.nearbycontacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import no.bouvet.androidskolen.nearbycontacts.models.OwnContactViewModel;
import no.bouvet.androidskolen.nearbycontacts.models.Contact;

public class OwnContactActivity extends AppCompatActivity implements View.OnClickListener {

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

        Contact contact = OwnContactViewModel.INSTANCE.getContact();
        if (contact != null) {
            userNameEditText.setText(contact.getName());
        }
    }

    @Override
    public void onClick(View view) {
        Contact contact = createContactFromInput();
        OwnContactViewModel.INSTANCE.setContact(contact);

        Intent intent = new Intent(this, NearbyActivity.class);
        startActivity(intent);
    }

    private Contact createContactFromInput() {
        String name = userNameEditText.getText().toString();
        return new Contact(name);
    }
}
