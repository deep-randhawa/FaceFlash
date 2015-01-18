package randhawa.deep.faceflash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class HomeScreenActivity extends Activity {

    TextView uName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreenactivity);
        Intent i = getIntent();
    }

    public void openPlayScreen(View view) {
        Intent intent = new Intent(this, ModeChoice.class);
        startActivity(intent);
        finish();
    }

    public void openAboutScreen(View view) {

    }
}