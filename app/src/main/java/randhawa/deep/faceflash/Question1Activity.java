package randhawa.deep.faceflash;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.os.Build;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import java.util.Random;

public class Question1Activity extends ActionBarActivity {

    SharedPreferences sharedPreferences;
    Button b0,b1,b2,b3;
    int element;
    int count;
    private String gameType;
    private int streak;
    public int highScore;
    private int[] profiles;
    private int[] profileMemory; // Stores the last 5 % of
    // contacts.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question1);
        sharedPreferences = getSharedPreferences("randhawa" +
                ".deep" +
                ".faceflash", MODE_PRIVATE);
        count = sharedPreferences.getInt("Count", 0);
        element = generateQuestion();
        String name = sharedPreferences.getString("Name" + element, "Borat");
        String urlAdd = sharedPreferences.getString("ImageUrl"+element, "url");
        System.out.println(name);
        System.out.println(element);
        System.out.println(urlAdd);
        /*for (int i = 0; i < count; i++) {
            System.out.println(sharedPreferences.getString("Name"+i,
                    "Base Case"));
            System.out.println(sharedPreferences.getString("ImageUrl"+i,
                    "Base Case"));
            System.out.println(sharedPreferences.getInt("Number of times " +
                    "guessed right"+i,0));
            System.out.println(sharedPreferences.getInt("Number of times " +
                    "guessed wrong"+i,0));

        } */
        profileMemory = new int[5];
        // To check if the next button is clicked.
        boolean next = true;
        // To check if the answer was right or not.
        boolean correct;
        int roundCount = 0;

        correct = this.playQuestion();
        this.askNext();

        while(next){
            this.playQuestion();
            this.askNext();
            roundCount += 1;
        }
    }


    public int generateQuestion(){
        double random = Math.random();
        count = sharedPreferences.getInt("Count", 0);
        if (random < 0.5){
            element = (int) ((random)*((count-1)/30.0));
            System.out.println("Element = " + element);
        }
        else {
            element = (int) ((random)*(count));
        }

        return element;
    }

    public int[] generateChoices(int retrievedProfile){
        int seed = (int) (Math.random()*4);
        int[] choice = new int[3];

        choice[0] = generateRandom(retrievedProfile, retrievedProfile, retrievedProfile, retrievedProfile );
        choice[1] = generateRandom(retrievedProfile, choice[0], retrievedProfile, retrievedProfile);
        choice[2] = generateRandom(retrievedProfile, choice[0], choice[1], retrievedProfile);
        choice[3] = generateRandom(retrievedProfile, choice[0], choice[1], choice[2]);

        choice[seed] = retrievedProfile;

        return choice;
    }


    public boolean playQuestion(){
        Boolean correct;
        int retrievedProfileIndex; // The actual answer
        int userAnswerIndex; // The user's answer
        int[] choices;

        retrievedProfileIndex = this.generateQuestion();
        return correct;
    }

    public void updateDatabase(int profileIndex, boolean correct){


    }

    public Prof


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

    public void checkAnswer(View view) {
        // previously invisible view
        View myView = findViewById(R.id.correctView);

        // get the center for the clipping circle
        int cx = (myView.getLeft() + myView.getRight()) / 2;
        int cy = (myView.getTop() + myView.getBottom()) / 2;

        // get the final radius for the clipping circle
        int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

        // create the animator for this view (the start radius is zero)
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Animator anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

            // make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();
        } else{
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
}
