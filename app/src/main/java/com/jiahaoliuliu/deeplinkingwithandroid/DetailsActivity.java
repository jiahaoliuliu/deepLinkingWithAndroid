package com.jiahaoliuliu.deeplinkingwithandroid;

import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DetailsActivity extends ActionBarActivity {

    public static final String INTENT_SOURCE_KEY = "com.jiahaoliuliu.deeplinkingwithandroid.sourcekye";
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

        sourceTV = (TextView) findViewById(R.id.source_tv);
        sourceTV.setText(extras.getString(INTENT_SOURCE_KEY));

        // Enables the back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
}
