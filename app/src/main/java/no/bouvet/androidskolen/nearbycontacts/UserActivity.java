package no.bouvet.androidskolen.nearbycontacts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);

        Button startNearbyActivityButton = (Button) findViewById(R.id.start_nearby_activity_button);
        startNearbyActivityButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, NearbyActivity.class);
        startActivity(intent);
    }
}
