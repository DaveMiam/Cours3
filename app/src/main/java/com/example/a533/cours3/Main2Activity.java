package com.example.a533.cours3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
    }

    private void moveToConnectActivity() {
        Intent intent = new Intent(this, Main3Activity.class);
        startActivity(intent);

    }

    private void setListener() {
        findViewById(R.id.buttonNext).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                moveToConnectActivity();
            }
        });
    }
}
