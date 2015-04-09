package com.jiahaoliuliu.deeplinkingwithandroid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

    private static final String DEEP_LINKING_SCHEMA = "deeplinkingwithandroid";
    private static final String DEEP_LINKING_HOST = "details";

    private Button startDetailsActivityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lock the screen orientation for simplification
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        startDetailsActivityButton = (Button) findViewById(R.id.start_details_activity_btn);
        startDetailsActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startDetailsActivity(getString(R.string.source_main_activity));
            }
        });
    }

    private void startDetailsActivity(String source) {
        Intent startDetailsActivityIntent = new Intent(MainActivity.this, DetailsActivity.class);
        startDetailsActivityIntent
                .putExtra(DetailsActivity.INTENT_SOURCE_KEY, source);
        startActivity(startDetailsActivityIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Check if the activity has been started by deep linking
        Uri data = getIntent().getData();

        if (data != null
                && DEEP_LINKING_SCHEMA.equalsIgnoreCase(data.getScheme())
                && DEEP_LINKING_HOST.equalsIgnoreCase(data.getHost())) {
            startDetailsActivity(getString(R.string.source_deep_linking));
        }
    }
}
