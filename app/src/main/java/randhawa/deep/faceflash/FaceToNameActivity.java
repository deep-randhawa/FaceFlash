package randhawa.deep.faceflash;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.facebook.Response;

import Fragments.FBLoginFragment;

public class FaceToNameActivity extends Activity {

    private ImageView[] imageViews = new ImageView[4];
    Bitmap bitmap;
    Response FB_Response = FBLoginFragment.FB_Response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_to_name);
        imageViews[0] = (ImageView) findViewById(R.id.imageButton01);
        imageViews[1] = (ImageView) findViewById(R.id.imageButton02);
        imageViews[2] = (ImageView) findViewById(R.id.imageButton03);
        imageViews[3] = (ImageView) findViewById(R.id.imageButton04);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_face_to_name, menu);
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
}
