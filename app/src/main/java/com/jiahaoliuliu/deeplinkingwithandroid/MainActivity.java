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

    // The deep linking data
    private static final String DEEP_LINKING_SCHEMA = "deeplinkingwithandroid";
    private static final String DEEP_LINKING_HOST = "details";
    private static final String DEEP_LINKING_BACKGROUND_PARAM = "background";


    // The request code of the detailed activity
//    private static final int REQUEST_CODE_DETAILS_ACTIVITY = 1001;

    private Button startDetailsActivityButton;

    // The desired background color for details activity
    private String backgroundColor;

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
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "OnResume");
        if (containsDeepLinkingDetails(getIntent())) {
            // There is not need to check if the data is null at this point because
            // if the data is null, containsDeepLinkingDetails() method will return false

            // Get the background color. If it does not exists, it just return null
            // in this case, the method startDetailsActivity will just ignore it
            backgroundColor = getIntent().getData().getQueryParameter(DEEP_LINKING_BACKGROUND_PARAM);
            startDetailsActivity(ActivityStartedSource.DEEP_LINKING, backgroundColor);
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
        Log.d(TAG, "onNewIntent. The data from the intent is " + intent.getData());

        if (containsDeepLinkingDetails(intent) ) {
            // There is not need to check if the data is null at this point because
            // if the data is null, containsDeepLinkingDetails() method will return false

            // Get the background color. If it does not exists, it just return null
            // in this case, the method startDetailsActivity will just ignore it
            backgroundColor = intent.getData().getQueryParameter(DEEP_LINKING_BACKGROUND_PARAM);
            startDetailsActivity(ActivityStartedSource.DEEP_LINKING, backgroundColor);
            getIntent().setData(null);
            // Else check what is the last opened activity
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
        startDetailsActivity(source, null);
    }

    /**
     * Start the details activity. It requires to identify the source which the data comes.
     * @param source
     * @param backgroundColor The background color that the detail screen should display.
     *                        This parameter is optional(it could be null)
     */
    private void startDetailsActivity(ActivityStartedSource source, String backgroundColor) {
        Log.d(TAG, "Starting details activity. The source is " + source.toString() +
                " and with background color " + backgroundColor);

        Intent startDetailsActivityIntent = new Intent(MainActivity.this, DetailsActivity.class);
        startDetailsActivityIntent
                .putExtra(DetailsActivity.INTENT_SOURCE_KEY, source);

        // The background color is optional
        if (backgroundColor != null) {
            startDetailsActivityIntent.putExtra(DetailsActivity.INTENT_BACKGROUND_COLOR_KEY, backgroundColor);
        }

        startActivity(startDetailsActivityIntent);
    }
}
