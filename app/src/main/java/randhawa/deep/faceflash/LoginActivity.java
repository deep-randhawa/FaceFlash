package randhawa.deep.faceflash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import org.brickred.socialauth.Contact;
import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;

import java.util.List;

import Fragments.FBLoginFragment;


public class LoginActivity extends FragmentActivity {
    SocialAuthAdapter adapter;
    Button linkedIn;
    String userName = "";
    private FBLoginFragment fbLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            fbLoginFragment = new FBLoginFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fbLoginFragment)
                    .commit();
        } else {
            fbLoginFragment = (FBLoginFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }

        setContentView(R.layout.activity_login);

        // resize the fragment


        // linked setup
        adapter = new SocialAuthAdapter(new ResponseListener());
        linkedIn = (Button) findViewById(R.id.linkedin);

        // Calls the authorization page of linkedIn when the button is clicked.
        linkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.authorize(LoginActivity.this,
                        SocialAuthAdapter.Provider.LINKEDIN);
            }
        });

    }


    // Calls the linkedIn API.
    public class ResponseListener implements DialogListener {
        // Defines the event when the login was successful.
        @Override
        public void onComplete(Bundle values) {
            adapter.getUserProfileAsync(new ProfileDataListener());
            adapter.getContactListAsync(new ContactDataListener());
        }

        // Defines the event when the login was cancelled.
        @Override
        public void onCancel() {
            System.out.println("Operation was cancelled");
        }

        // Defines the event when the back button was pressed.
        @Override
        public void onBack() {
            System.out.println("Operation was cancelled");
        }

        // Defines the event when there is an error with authenticating.
        @Override
        public void onError(SocialAuthError socialAuthError) {
            System.out.println(socialAuthError.getMessage());
        }
    }

    public class ProfileDataListener implements SocialAuthListener<Profile> {

        @Override
        public void onExecute(String uName, Profile profile) {
            Profile profilemap = profile;
            // Gets the username.
            userName = profilemap.getFirstName() + " " +
                    profilemap.getLastName();
            String url = profile.getProfileImageURL();
            System.out.println(url);
            sender(userName, getApplicationContext());
        }

        public void sender(String name, Context context) {
            Intent intent = new Intent(LoginActivity.this,
                    HomeScreenActivity.class);
            String userNam = name;
            intent.putExtra("useName", userNam);
            startActivity(intent);
        }

        @Override
        public void onError(SocialAuthError socialAuthError) {
            System.out.println(socialAuthError.getMessage());
        }
    }

    private final class ContactDataListener implements
            SocialAuthListener {

           /*@Override
           public void onExecute(String contacts, List t) {
               List<Contact> contactsList = t;

           } */

        @Override
        public void onExecute(String s, Object o) {
            List<Contact> contactList = (List<Contact>) o;
            if (contactList != null && contactList.size() > 0) {
                for (Contact c : contactList) {
                    Log.d("Custom-UI", "First Name = " + c.getFirstName());
                    Log.d("Custom-UI", "Last Name = " + c.getLastName());
                    if (c.getProfileImageURL() != null) {
                        String url = c.getProfileImageURL();
                        System.out.println(url);
                    } else {
                        System.out.println("Tussed out");
                    }
                }
            }
        }

        @Override
        public void onError(SocialAuthError socialAuthError) {
            System.out.println(socialAuthError.getMessage());
        }
    }

}