package no.bouvet.androidskolen.nearbycontacts;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.nearby.messages.BleSignal;
import com.google.android.gms.nearby.messages.Distance;
import com.google.android.gms.nearby.messages.Message;
import com.google.android.gms.nearby.messages.MessageListener;
import com.google.android.gms.nearby.messages.Strategy;
import com.google.android.gms.nearby.messages.SubscribeCallback;
import com.google.android.gms.nearby.messages.SubscribeOptions;
import com.google.android.gms.nearby.messages.devices.NearbyDevice;

import no.bouvet.androidskolen.nearbycontacts.models.NearbyPersonsListViewModel;
import no.bouvet.androidskolen.nearbycontacts.models.OwnPersonViewModel;
import no.bouvet.androidskolen.nearbycontacts.models.Person;
import no.bouvet.androidskolen.nearbycontacts.models.SelectedPersonViewModel;

public class NearbyActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, PersonSelectedListener {

    private final static String TAG = NearbyActivity.class.getSimpleName();
    private final static int REQUEST_RESOLVE_ERROR = 1;

    private MessageListener messageListener;
    private PersonDetectedListener personDetectedListener;
    private GoogleApiClient googleApiClient;
    private Message activeMessage;

    public void setPersonDetectedListener(PersonDetectedListener listener) {
        personDetectedListener = listener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_nearby);

        setupNearbyMessageListener();

        setupNearbyMessagesApi();

        addNearbyPersonsListFragmentIfNotExists();

        setPersonDetectedListener(NearbyPersonsListViewModel.INSTANCE);

    }

    private void addNearbyPersonsListFragmentIfNotExists() {

        NearbyPersonsListFragment nearbyPersonsListFragment = (NearbyPersonsListFragment) getFragmentManager().findFragmentById(R.id.nearby_persons_list_fragment);
        if (nearbyPersonsListFragment == null || !nearbyPersonsListFragment.isInLayout()) {
            nearbyPersonsListFragment = new NearbyPersonsListFragment();

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_holder, nearbyPersonsListFragment);
            fragmentTransaction.commit();
        }
    }

    private void setupNearbyMessageListener() {
        messageListener = new com.google.android.gms.nearby.messages.MessageListener() {
            @Override
            public void onFound(Message message) {
                Log.d(TAG, "[onFound]");
                NearbyDevice[] nearbyDevices = message.zzbxl();

                for (NearbyDevice nearbyDevice : nearbyDevices) {
                    Log.d(TAG, nearbyDevice.zzbxr());
                }

                String messageAsJson = new String(message.getContent());
                Log.d(TAG, "Found message: " + messageAsJson);

                Person person = Person.fromJson(messageAsJson);
                firePersonDetected(person);
            }

            @Override
            public void onDistanceChanged(Message message, Distance distance) {
                Log.i(TAG, "Distance changed, message: " + message + ", new distance: " + distance);
            }

            @Override
            public void onBleSignalChanged(Message message, BleSignal bleSignal) {
                Log.i(TAG, "Message: " + message + " has new BLE signal information: " + bleSignal);
            }

            @Override
            public void onLost(Message message) {
                Log.d(TAG, "[onLost]");
                String messageAsJson = new String(message.getContent());
                Log.d(TAG, "Lost sight of message: " + messageAsJson);

                Person person = Person.fromJson(messageAsJson);
                firePersonLost(person);
            }
        };
    }

    private void setupNearbyMessagesApi() {
        googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(com.google.android.gms.nearby.Nearby.MESSAGES_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "[onStart]");
        googleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d(TAG, "[onStop]");

        if (googleApiClient.isConnected()) {
            unpublish();
            unsubscribe();
            googleApiClient.disconnect();
        }
    }

    private void publish(Person person) {

        Log.i(TAG, "[publish] Publishing information about person: " + person.getName());
        String json = person.toJson();
        activeMessage = new Message(json.getBytes());
        com.google.android.gms.nearby.Nearby.Messages.publish(googleApiClient, activeMessage);
    }

    private void unpublish() {

        Log.i(TAG, "[unpublish] ");
        if (activeMessage != null) {
            com.google.android.gms.nearby.Nearby.Messages.unpublish(googleApiClient, activeMessage);
            activeMessage = null;
        }
    }

    private void subscribe() {
        Log.i(TAG, "Subscribing.");

        Strategy strategy = new Strategy.Builder().setDiscoveryMode(Strategy.DISCOVERY_MODE_DEFAULT).setTtlSeconds(Strategy.TTL_SECONDS_DEFAULT).setDistanceType(Strategy.DISTANCE_TYPE_EARSHOT).build();

        SubscribeCallback callback = new SubscribeCallback() {
            @Override
            public void onExpired() {
                super.onExpired();
                Log.d(TAG, "[onExpired] subscribing anew...");
                subscribe();
            }
        };
        SubscribeOptions options = new SubscribeOptions.Builder().setStrategy(strategy).setCallback(callback).build();

        com.google.android.gms.nearby.Nearby.Messages.subscribe(googleApiClient, messageListener, options);
    }

    private void unsubscribe() {
        Log.i(TAG, "[unsubscribe].");
        com.google.android.gms.nearby.Nearby.Messages.unsubscribe(googleApiClient, messageListener);
    }


    private void publishPersonInternally() {
        Log.d(TAG, "[publishPersonInternally]");
        if (googleApiClient.isConnected()) {
            publish(OwnPersonViewModel.INSTANCE.getPerson());
            subscribe();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "[onConnected]");
        publishPersonInternally();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "GoogleApiClient disconnected with cause: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, "GoogleApiClient connection failed", e);
            }
        } else {
            Log.e(TAG, "GoogleApiClient connection failed");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            if (resultCode == Activity.RESULT_OK) {
                googleApiClient.connect();
            } else {
                Log.e(TAG, "GoogleApiClient connection failed. Unable to resolve.");
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void firePersonDetected(Person person) {
        if (personDetectedListener != null) {
            personDetectedListener.onPersonDetected(person);
        }
    }

    private void firePersonLost(Person person) {
        if (personDetectedListener != null) {
            personDetectedListener.onPersonLost(person);
        }
    }

    @Override
    public void onPersonSelected(Person person) {
        Log.d(TAG, "Person selected: " + person.getName());

        SelectedPersonFragment selectedPersonFragment = (SelectedPersonFragment) getFragmentManager().findFragmentById(R.id.selected_person_fragment);

        if (selectedPersonFragment == null || !selectedPersonFragment.isInLayout()) {
            SelectedPersonFragment newFragment = new SelectedPersonFragment();
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment_holder, newFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

        SelectedPersonViewModel.INSTANCE.setSelectedPerson(person);

    }
}
