package com.jiahaoliuliu.deeplinkingwithandroid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DetailsActivity extends ActionBarActivity {

    private static final String TAG = "DetailsActivity";

    /**
     * The source which started this activity
     */
    public enum ActivityStartedSource {
        // The activity has started from Deep linking
        DEEP_LINKING,

        // The activity has started from a manual action on MainActivity
        MAIN_ACTIVITY
    }

    public static final String INTENT_SOURCE_KEY = "com.jiahaoliuliu.deeplinkingwithandroid.sourcekye";
    private LinearLayout mainLayoutLL;
    private TextView sourceTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lock the screen orientation for simplification
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_details);

        // Check if the source is come from intent
        Bundle extras = getIntent().getExtras();
        if (extras == null || !extras.containsKey(INTENT_SOURCE_KEY)) {
            throw new IllegalArgumentException("You must pass the source as the extra of the intent");
        }

        // Enables the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mainLayoutLL = (LinearLayout) findViewById(R.id.main_layout_ll);
        sourceTV = (TextView) findViewById(R.id.source_tv);

        ActivityStartedSource source = (ActivityStartedSource) getIntent().getSerializableExtra(INTENT_SOURCE_KEY);

        // Depending on the source, sourceTV will display a string or another
        switch(source) {
            case DEEP_LINKING:
                mainLayoutLL.setBackgroundColor(
                        getResources().getColor(R.color.background_details_activity_from_deep_linking));
                sourceTV.setText(getString(R.string.source_deep_linking));
                break;
            case MAIN_ACTIVITY:
                mainLayoutLL.setBackgroundColor(
                        getResources().getColor(R.color.background_details_activity_from_main_activity));
                sourceTV.setText(getString(R.string.source_main_activity));
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            // Back to the previous activity
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "On back pressed. Sending back the result");
        setResult(RESULT_OK);
        finish();
    }
}
