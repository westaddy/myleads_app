package com.myleads.android.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.myleads.android.R;
import com.myleads.android.activities.RegisterActivity;
import com.myleads.android.custom.PrefManager;


public class MainActivity extends AppCompatActivity {
    protected Button signupBTN, signinBTN;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // check if token exist for this user. If so,launch Dashboard Activity, else, user should proceed to log in
        PrefManager prefManager = new PrefManager(this);
        if (prefManager.getUserId() != null) {
            launchDashboardScreen();
            finish();
        }
        setContentView(R.layout.activity_main);
        signinBTN = (Button) findViewById(R.id.signinBTN);
        signupBTN = (Button) findViewById(R.id.signupBTN);

        //Handle signinBTN
        signinBTN.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        //Handle signUPBTN
        signupBTN.setOnClickListener( new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void launchDashboardScreen() {
        startActivity(new Intent(MainActivity.this, DashboardActivity.class));
        finish();
    }
}
