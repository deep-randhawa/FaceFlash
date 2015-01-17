package randhawa.deep.faceflash;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.widget.TextView;

public class HomeScreenActivity extends Activity{

    TextView uName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreenactivity);
        uName = (TextView) findViewById(R.id.nameTextView);
        Intent i = getIntent();
        String userName = i.getStringExtra("useName");
        uName.setText(userName);
    }
}
