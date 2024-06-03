package com.example.beta_pdm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button startSimulationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startSimulationButton = findViewById(R.id.startSimulationButton);

        startSimulationButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SimulationActivity.class);
            startActivity(intent);
        });

    }

    public void callSupp(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + "0740109320"));
        startActivity(intent);
    }

    public void sendEmailToSupp(View view) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:rarespoanta10@gmail.com"));
        startActivity(Intent.createChooser(intent, "Send a mail using"));
    }

    public void aboutUs(View view) {
        Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
        startActivity(intent);
    }
}
