package Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;

import java.util.Arrays;

import randhawa.deep.faceflash.HomeScreenActivity;
import randhawa.deep.faceflash.R;

public class FBLoginFragment extends Fragment {

    private final static String TAG = "FBLoginFragment";
    public static Response FB_Response;
    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState sessionState, Exception e) {
            onSessionStateChange(session, sessionState, e);
        }
    };
    private UiLifecycleHelper uiLifecycleHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        uiLifecycleHelper = new UiLifecycleHelper(getActivity(), callback);
        uiLifecycleHelper.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_login, container, false);

        LoginButton loginButton = (LoginButton) view.findViewById(R.id.facebook_auth_button);
        loginButton.setFragment(this);
        loginButton.setReadPermissions(Arrays.asList("email",
                "read_friendlists",
                "user_photos",
                "user_about_me",
                "user_birthday",
                "user_friends"));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // For scenarios where the mainActivity is Launched and the user
        // session is not null , the session state change notification may
        // not be triggered. Trigger if it's open/closed.
        Session session = Session.getActiveSession();
        if (session != null && (session.isClosed() || session.isOpened())) {
            onSessionStateChange(session, session.getState(), null);
        }

        uiLifecycleHelper.onResume();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiLifecycleHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiLifecycleHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiLifecycleHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiLifecycleHelper.onSaveInstanceState(outState);
    }

    private void onSessionStateChange(Session session, SessionState sessionState, Exception exception) {
        if (sessionState.isOpened()) {
            Log.d(TAG, "Logged in...");
            Request request = new Request(session, "me/taggable_friends", null, HttpMethod.GET, new Request.Callback() {
                @Override
                public void onCompleted(Response response) {
                    Log.d("Response", response.toString());
                    FB_Response = response;

                    Intent intent = new Intent(getActivity(), HomeScreenActivity.class);
                    startActivity(intent);
                }
            });
            request.executeAsync();

        } else if (sessionState.isClosed()) {
            Log.d(TAG, "Logged out...");
        }
    }
}
