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

    private Button startDetailsActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lock the screen orientation for simplification
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "OnCreate");
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
            startDetailsActivity(ActivityStartedSource.DEEP_LINKING);
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
        Log.d(TAG, "onNewIntent. The data from the intent is" + intent.getData());
        if (containsDeepLinkingDetails(intent)) {
            startDetailsActivity(ActivityStartedSource.DEEP_LINKING);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "OnPause");
    }

    /**
     * Check if the intent contains deep linking details
     * @param intent The intent to be checked. It cannot be null
     * @return
     *      True if it contains data and the schema and the host match with the deep linking details
     *      False otherwise
     */
    private boolean containsDeepLinkingDetails(Intent intent) {
        Log.d(TAG, "Checkin if the intent contains deeplinking details");
        // Check if the activity has been started by deep linking
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
        startActivity(startDetailsActivityIntent);
    }
}
