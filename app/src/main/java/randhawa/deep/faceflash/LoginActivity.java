package randhawa.deep.faceflash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
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
    Button linkedIn, fbButton;
    String userName = "";
    SharedPreferences sharedPreferences;
    Editor editor;
    int count = 0;
    private FBLoginFragment fbLoginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        adapter = new SocialAuthAdapter(new ResponseListener());
        linkedIn = (Button) findViewById(R.id.linkedin);
        fbButton = (Button) findViewById(R.id.facebook_auth_button);
        sharedPreferences = getSharedPreferences("randhawa" +
                ".deep" +
                ".faceflash", MODE_PRIVATE);
        final Bundle save = savedInstanceState;
        if (sharedPreferences.getBoolean("ifFirst", true)) {
            editor = sharedPreferences.edit();
            editor.putBoolean("ifFirst", false);
            editor.putBoolean("isLoggedIn", false);
            editor.commit();
        }

        if (savedInstanceState == null) {
            // Add the fragment on initial activity setup
            fbLoginFragment = new FBLoginFragment();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, fbLoginFragment)
                    .commit();
        } else {
            // Or set the fragment from restored state info
            fbLoginFragment = (FBLoginFragment) getSupportFragmentManager()
                    .findFragmentById(android.R.id.content);
        }

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
            editor.putBoolean("isLoggedIn", true);
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

        @Override
        public void onExecute(String s, Object o) {
            List<Contact> contactList = (List<Contact>) o;
            if (contactList != null && contactList.size() > 0) {
                for (Contact c : contactList) {
                    userName = c.getFirstName() + " " +
                            c.getLastName();

                    if (c.getProfileImageURL() != null) {
                        String url = c.getProfileImageURL();
                        editor = sharedPreferences.edit();
                        editor.putString("Name" + count, userName);
                        editor.putString("ImageUrl" + count, url);
                        editor.putInt("Number of times guessed right" + count, 0);
                        editor.putInt("Number of times guessed wrong" + count, 0);
                        editor.commit();
                        count++;
                    } else {
                        continue;
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

