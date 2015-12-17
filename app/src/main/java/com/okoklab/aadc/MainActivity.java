package com.okoklab.aadc;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start_animation_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((InterpolatorSampleView)findViewById(R.id.sample_view)).startAnimation();
            }
        });
    }
}
