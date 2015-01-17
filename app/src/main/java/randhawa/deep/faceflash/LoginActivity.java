package randhawa.deep.faceflash;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;


public class LoginActivity extends ActionBarActivity {

    SocialAuthAdapter adapter;
    Button linkedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        adapter = new SocialAuthAdapter(new ResponseListener());
        linkedIn = (Button) findViewById(R.id.linkedin);
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

        @Override
        public void onComplete(Bundle values){
           // System.out.println("Hi");
           adapter.getUserProfileAsync(new ProfileDataListener());
        }
        @Override
        public void onCancel(){
            System.out.println("Operation was cancelled");
        }
        @Override
        public void onBack(){
            System.out.println("Operation was cancelled");
        }

        @Override
        public void onError(SocialAuthError socialAuthError) {
            System.out.println(socialAuthError.getMessage());
        }
    }

       public class ProfileDataListener implements SocialAuthListener<Profile> {

           @Override
           public void onExecute(String name, Profile profile){
               Log.d("Custom-UI", "Receiving Data");
               Profile profilemap = profile;
               System.out.println(profilemap.getFirstName() + " " +
                       profilemap.getLastName());
               //System.out.println(profilemap.getGender());
           }

           @Override
           public void onError(SocialAuthError socialAuthError) {
               System.out.println(socialAuthError.getMessage());
           }
       }

}
