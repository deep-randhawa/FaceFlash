package randhawa.deep.faceflash;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.facebook.model.GraphObject;

import java.util.ArrayList;


public class Question1Activity extends ActionBarActivity {

    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question1);

        preferences = getSharedPreferences("randhawa.deep.faceflash", MODE_PRIVATE);
        ArrayList<tmpProfile> arrayList = new ArrayList<tmpProfile>();
        String response = preferences.getString("FB_Response", null);
        while (response.indexOf("https") != -1) {
            response = response.substring(response.indexOf("https"));
            String url = response.substring(response.indexOf("https"), response.indexOf("\""));
            response = response.substring(response.indexOf("name") + "name".length() + 3);

            String name = response.substring(0, response.indexOf("\""));
            response = response.substring(response.indexOf("\""));
            tmpProfile newPerson = new tmpProfile(name, url);
            arrayList.add(newPerson);
        }
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

    public void checkAnswer(View view) {
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


    public void wrongAnswer(View view) {
        Button button = (Button) findViewById(view.getId());

        button.setBackgroundColor(Color.parseColor("#B71C1C"));
        Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
        view.startAnimation(shake);

    }

}

class tmpProfile {
    String name;
    String ImageURL;

    public tmpProfile(String name, String URL) {
        this.name = name;
        this.ImageURL = URL;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageURL() {
        return ImageURL;
    }

    public void setImageURL(String imageURL) {
        ImageURL = imageURL;
    }
}
