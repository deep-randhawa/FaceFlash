package randhawa.deep.faceflash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class ModeChoice extends ActionBarActivity {
    /*
    IDEAS:
    -take picture of an entry from array/database/web?
    -take 4 random names

     */

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode_choice);
        sharedPreferences = getSharedPreferences("randhawa" +
                ".deep" +
                ".faceflash", MODE_PRIVATE);
        /*int count = sharedPreferences.getInt("Count", 0);
        System.out.println(count);
        for (int i = 0; i < count;i++){
            System.out.println(sharedPreferences.getString("Name"+i,
                    "Base Case"));
            System.out.println(sharedPreferences.getString("ImageUrl"+i,
                    "Base Case"));
            System.out.println(sharedPreferences.getInt("Number of times " +
                            "guessed right"+i,0));
            System.out.println(sharedPreferences.getInt("Number of times " +
                            "guessed wrong"+i,0)); */
        int count = sharedPreferences.getInt("Count", 0);
        int[] indexArray = new int[count];
        }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mode_choice, menu);
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    public void openQuestion(View view) {
        Intent intent = new Intent(this, Question1Activity.class);
        startActivity(intent);
    }
}
