package com.jiahaoliuliu.deeplinkingwithandroid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class MainActivity extends ActionBarActivity {

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
                Intent startDetailsActivityIntent = new Intent(MainActivity.this, DetailsActivity.class);
                startDetailsActivityIntent
                        .putExtra(DetailsActivity.INTENT_SOURCE_KEY, getString(R.string.source_main_activity));
                startActivity(startDetailsActivityIntent);
            }
        });
    }
}
