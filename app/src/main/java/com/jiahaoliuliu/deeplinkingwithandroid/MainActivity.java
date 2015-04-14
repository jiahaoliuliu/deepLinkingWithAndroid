package com.jiahaoliuliu.deeplinkingwithandroid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.jiahaoliuliu.deeplinkingwithandroid.DetailsActivity.ActivityStartedSource;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";

    private static final String DEEP_LINKING_SCHEMA = "deeplinkingwithandroid";
    private static final String DEEP_LINKING_HOST = "details";

    // The request code of the detailed activity
    private static final int REQUEST_CODE_DETAILS_ACTIVITY = 1001;

    private Button startDetailsActivityButton;

    // Saved the state of the last opened activity
    private enum LastOpenedActivity {
        // This activity
        MAIN_ACTIVITY,

        // Details activity with both variants
        DETAILS_ACTIVITY_NORMAL, DETAILS_ACTIVITY_DEEP_LINKING;
    }

    private LastOpenedActivity lastOpenedActivity = LastOpenedActivity.MAIN_ACTIVITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lock the screen orientation for simplification
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        // check the previous content
        Log.d(TAG, "OnCreate: " + savedInstanceState);
        startDetailsActivityButton = (Button) findViewById(R.id.start_details_activity_btn);
        startDetailsActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDetailsActivity(ActivityStartedSource.MAIN_ACTIVITY);
                lastOpenedActivity = LastOpenedActivity.DETAILS_ACTIVITY_NORMAL;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume");
        if (containsDeepLinkingDetails(getIntent())) {
            startDetailsActivity(ActivityStartedSource.DEEP_LINKING);
            lastOpenedActivity = LastOpenedActivity.DETAILS_ACTIVITY_DEEP_LINKING;
            // Remove the data from intent, so when the app goes back to the
            // main activity from details activity, it won't invoke details
            // activity again.
            getIntent().setData(null);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "OnStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "OnRestart");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d(TAG, "onNewIntent. The data from the intent is " + intent.getData() +
            ", and the last activity is " + lastOpenedActivity);

        if (containsDeepLinkingDetails(intent) ) {
            startDetailsActivity(ActivityStartedSource.DEEP_LINKING);
            lastOpenedActivity = LastOpenedActivity.DETAILS_ACTIVITY_DEEP_LINKING;
            getIntent().setData(null);
        // Else check what is the last opened activity
        } else {
            switch (lastOpenedActivity) {
                case MAIN_ACTIVITY:
                    // Do nothing
                    break;
                case DETAILS_ACTIVITY_NORMAL:
                    // Starts the details activity normally
                    startDetailsActivity(ActivityStartedSource.MAIN_ACTIVITY);
                    break;
                case DETAILS_ACTIVITY_DEEP_LINKING:
                    // Starts the details activity with deep linking
                    startDetailsActivity(ActivityStartedSource.DEEP_LINKING);
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause");
    };

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "OnStop");
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "OnDestroy");
        super.onDestroy();
    }

    /**
     * Check if the intent contains deep linking details
     * @param intent The intent to be checked. It cannot be null
     * @return
     *      True if it contains data and the schema and the host match with the deep linking details
     *      False otherwise
     */
    private boolean containsDeepLinkingDetails(Intent intent) {
        Log.d(TAG, "Checking if the intent contains deeplinking details");
        // Check if the activity has been started by deep l inking
        if (intent == null) {
            throw new NullPointerException("The intent cannot be null when handling deep linking");
        }

        Uri data = intent.getData();
        return (data != null
                && DEEP_LINKING_SCHEMA.equalsIgnoreCase(data.getScheme())
                && DEEP_LINKING_HOST.equalsIgnoreCase(data.getHost()));
    }

    /**
     * Start the details activity. It requires to identify the source which the data comes.
     * @param source
     */
    private void startDetailsActivity(ActivityStartedSource source) {
        Log.d(TAG, "Starting details activity. The source is " + source.toString());

        Intent startDetailsActivityIntent = new Intent(MainActivity.this, DetailsActivity.class);
        startDetailsActivityIntent
                .putExtra(DetailsActivity.INTENT_SOURCE_KEY, source);
        startActivityForResult(startDetailsActivityIntent, REQUEST_CODE_DETAILS_ACTIVITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "On activity result received for the request code: " + requestCode + ", with" +
                "result code: " + resultCode + ", and intent: " + data);
        switch (requestCode) {
            case REQUEST_CODE_DETAILS_ACTIVITY:
                // It is very important have here Result ok, to avoid the follow problem:
                // 1. The user starts the app by using deep linking
                // 2. The user press on the app icon in the mobile device, so, onNewIntent is called
                // 3. The app will finish the detail activity. This makes the details activity to finish
                //    and return the result code 0 (Canceled).
                // If we don't check the result code, the main activity will mark the variable isShowingDeepLinkingData as
                // false, then we will end up showing the main activity when we should show the deep linking activity.
                if (resultCode == RESULT_OK) {
                    Log.d(TAG, "Result received and it is ok");
                    lastOpenedActivity = LastOpenedActivity.MAIN_ACTIVITY;
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
