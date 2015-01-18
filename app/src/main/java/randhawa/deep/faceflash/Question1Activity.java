package randhawa.deep.faceflash;
import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import java.util.ArrayList;


public class Question1Activity extends ActionBarActivity {

    SharedPreferences sharedPreferences;
    Button b0, b1, b2, b3;
    private String gameType;    //type of game (facial rec or name rec)
    private int streak;  //current score streak
    public int highScore;      //highscore (need to somehow load this from device memory)
    private ArrayList<Profile> profileArray = new ArrayList<>(); //array of profiles sorted with profile[0] being least recognized and profile[i] best recognized
    private int size;            //size of profile array
    private int[] profiles;      //array of index of theseprofiles
    private Profile[] profileMemory; //remembers the last 5 profiles
    Profile retrievedProfile;
    String textOnClicked;
    ImageView questionImage;
    Boolean next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question1);
        sharedPreferences = getSharedPreferences("randhawa" +
                ".deep" +
                ".faceflash", MODE_PRIVATE);

        b0 = (Button) findViewById(R.id.b0);
        b1 = (Button) findViewById(R.id.b1);
        b2 = (Button) findViewById(R.id.b2);
        b3 = (Button) findViewById(R.id.b3);
        questionImage = (ImageView) findViewById(R.id.qImage);
        try {
            Intent intent = getIntent();
            if (intent.getBooleanExtra("Correct", false)){
                playQuestion();
                return;
            }
        } catch (Exception e) {

        }

        //  String name = sharedPreferences.getString("Name" + element, "Borat");
        //  String urlAdd = sharedPreferences.getString("ImageUrl"+element, "url");

        profileMemory = new Profile[5];
        next = true; //
        boolean correct;
        //  gameType = gametype[0];
        size = sharedPreferences.getInt("Count", 0);
        //populate array of prototypes
        String name;
        String picture;
        for (int i = 0; i < profileArray.size(); i++) {
            name = sharedPreferences.getString("Name" + i, "Borat");
            picture = sharedPreferences.getString("ImageUrl" + i, "url");
            profileArray.add(new Profile(name, picture));
        }

        int roundCount = 0; //keeps track of the rounds if we want to add a status bar

        // gets profiles from facebook
        sharedPreferences = getSharedPreferences("randhawa.deep.faceflash", MODE_PRIVATE);
        String response = sharedPreferences.getString("FB_Response", null);
        while (response.indexOf("https") != -1) {
            response = response.substring(response.indexOf("https"));
            String url = response.substring(response.indexOf("https"), response.indexOf("\""));
            response = response.substring(response.indexOf("name") + "name".length() + 3);

            String userName = response.substring(0, response.indexOf("\""));
            response = response.substring(response.indexOf("\""));
            Profile newPerson = new Profile(userName, url.replace("\\", ""));
            if (newPerson != null) profileArray.add(newPerson);
        }

        playQuestion();


    }

    //plays one question
    public void playQuestion() {
        Boolean correct;
//the actual answer (element was chosen)
        String userAnswer; //the answer
        Profile[] choices;

        retrievedProfile = this.generateQuestion();
        choices = this.generateChoices(retrievedProfile);
        b0.setText(choices[0].getName());
        b1.setText(choices[1].getName());
        b2.setText(choices[2].getName());
        b3.setText(choices[3].getName());

        new DownloadImageTask(questionImage)
                .execute(retrievedProfile.getPicture());
    }



    public Profile generateQuestion() {
        return profileArray.get((int) (Math.random() * profileArray.size()));
    }

    public Profile[] generateChoices(Profile retrievedProfile) {
        int seed = (int) (Math.random() * 4);
        Profile[] choice = new Profile[4];

        choice[0] = generateRandom(retrievedProfile, retrievedProfile, retrievedProfile, retrievedProfile);
        choice[1] = generateRandom(retrievedProfile, choice[0], retrievedProfile, retrievedProfile);
        choice[2] = generateRandom(retrievedProfile, choice[0], choice[1], retrievedProfile);
        choice[3] = generateRandom(retrievedProfile, choice[0], choice[1], choice[2]);

        choice[seed] = retrievedProfile;

        return choice;
    }

    public void updateDatabase(Profile profile, boolean correct) {
        Profile temp;
        profile.setMemoryTier(correct);
        int index = 0;

        for (int j = 0; j < profileArray.size(); j++) {
            if (profile == profileArray.get(j))
                index = j;
        }

        if ((correct == false) && (index + 1 < profileArray.size())) {
            streak = 0;
            temp = profile;
            profileArray.set(index + 1, profile);
            profileArray.set(index, temp);
        } else if ((index - 1) >= 0) {
            streak = streak + 1;
            temp = profileArray.get(index - 1);
            profiles[index - 1] = profiles[index];
            profileArray.set(index, temp);
        }

    }


    public void updateMemory(Profile profile) {
        Profile temp;

        for (int i = 0; i < profileMemory.length - 1; i++) {
            temp = profileMemory[i];
            profileMemory[i] = profile;
            profileMemory[i + 1] = temp;
        }
    }

    public Profile generateRandom(Profile a, Profile b, Profile c, Profile d) {

        Profile generated = profileArray.get((int) (Math.random() * profileArray.size()));

        while ((generated == a) || (generated == b) || (generated == c) || (generated == d))
            generated = profileArray.get((int) (Math.random() * profileArray.size()));

        return generated;

    }

    private Boolean askNext() {
        next = true;
        return next;
    }

    private boolean isRepeat(Profile generatedProfile) {

        for (int i = 0; i < profileMemory.length; i++) {
            if (profileMemory[i] == generatedProfile)
                return true;
        }

        return false;
    }

    public void buttonClicked(View view) { ///--------Called when button pressed
        Button button = (Button) findViewById(view.getId());
        textOnClicked = button.getText().toString();
        Boolean correct;
        // @TODO: Need to get user answer.

        if (textOnClicked.equals(retrievedProfile.getName())) {
            correctAnswer(view);
        } else {
            wrongAnswer(view);
        }

        //this.updateDatabase(retrievedProfile, correct);
        //this.updateMemory(retrievedProfile);
        Profile retrievedProfile;
    }


    private void statusBar() {
        int red = 0;
        int yellow = 0;
        int green = 0;
        int temp;
        int size;

        for (int i = 0; i < profiles.length; i++) {
            temp = profileArray.get(i).getMemoryTier();
            if (temp < 2)
                red += 1;
            if ((temp >= 2) && (temp <= 5))
                yellow += 1;
            if (temp > 5)
                green += 1;
        }

        size = red + yellow + green;
        // code to divide progress bar into red, yellow and green sectoin

        //rewards here:
        if (red == 0) {
            System.out.println("Congratulations! You vaguely recognize everyone in your binder. Keep up the good work! ");
        }

        if (red >= (size * .5)) {
            System.out.println("Congratulations! You vaguely recognize half of everyone in your binder. Keep up the good work! ");
        }

        if (green == size) {
            System.out.println("CONGRATULATIONS. You have master memory.");
        }
    }


    public int generateNumber() {
        double random = Math.random();
        int element;
        int count = sharedPreferences.getInt("Count", 0);
        if (random < 0.5) {
            element = (int) ((random) * (count / 30));
        } else {
            element = (int) ((random) * (count + 1));
        }

        return element;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_question1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void wrongAnswer(View view) {
        Button button = (Button)findViewById(view.getId());

        button.setBackgroundColor(Color.parseColor("#B71C1C"));
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        view.startAnimation(shake);

    }

    public void correctAnswer(View view) {
        // previously invisible view
        View myView = findViewById(R.id.correctView);

        // get the center for the clipping circle
        int cx = (myView.getLeft() + myView.getRight()) / 2;
        int cy = (myView.getTop() + myView.getBottom()) / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

        // create the animator for this view (the start radius is zero)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();
        } else {
            Animation fadeIn = new AlphaAnimation(0, 1);
            fadeIn.setInterpolator(new AccelerateInterpolator()); //add this
            fadeIn.setDuration(1000);
            myView.setAnimation(fadeIn);
            myView.startAnimation(fadeIn);
        }
        Intent intent = new Intent(this, CorrectAnswerActivity.class);
        startActivity(intent);
        finish();
    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}