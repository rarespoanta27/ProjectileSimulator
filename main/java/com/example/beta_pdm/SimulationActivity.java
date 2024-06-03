package com.example.beta_pdm;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class SimulationActivity extends AppCompatActivity {

    private EditText angleInput;
    private EditText speedInput;
    private Button startButton;
    private Button stopButton;
    private Button resumeButton;
    private CustomView customView;
    private List<CustomView.PointF> trajectoryPoints = new ArrayList<>();
    private MediaPlayer mediaPlayer;
    private float simulationSpeedFactor = 1.0f;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable simulationRunnable;
    private boolean isRunning = false;
    private float currentTime = 0;
    private float vx;
    private float vy;
    private float dt;
    private float g = 9.8f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simulation);

        angleInput = findViewById(R.id.angleInput);
        speedInput = findViewById(R.id.speedInput);
        startButton = findViewById(R.id.startButton);
        stopButton = findViewById(R.id.stopButton);
        resumeButton = findViewById(R.id.resumeButton);
        customView = findViewById(R.id.customView);

        mediaPlayer = MediaPlayer.create(this, R.raw.gunshot);

        startButton.setOnClickListener(v -> {
            String angleText = angleInput.getText().toString();
            String speedText = speedInput.getText().toString();
            if (!angleText.isEmpty() && !speedText.isEmpty()) {
                try {
                    float angle = Float.parseFloat(angleText);
                    float speed = Float.parseFloat(speedText);
                    mediaPlayer.start();
                    currentTime = 0;
                    simulateTrajectoryInRealTime(angle, speed);
                } catch (NumberFormatException e) {
                    Toast.makeText(SimulationActivity.this, "Invalid input", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(SimulationActivity.this, "Please enter both angle and speed", Toast.LENGTH_SHORT).show();
            }
        });

        stopButton.setOnClickListener(v -> stopSimulation());

        resumeButton.setOnClickListener(v -> resumeSimulation());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void simulateTrajectoryInRealTime(float angle, float speed) {
        trajectoryPoints.clear();
        customView.setTrajectoryPoints(trajectoryPoints);
        float radianAngle = (float) Math.toRadians(angle);
        vx = speed * (float) Math.cos(radianAngle);
        vy = speed * (float) Math.sin(radianAngle);
        dt = 0.1f;
        isRunning = true;

        simulationRunnable = new Runnable() {
            @Override
            public void run() {
                float x = vx * currentTime;
                float y = vy * currentTime - 0.5f * g * currentTime * currentTime;

                if (y < 0) {
                    isRunning = false;
                    Toast.makeText(SimulationActivity.this, "The trajectory of your chosen projectile with a speed of " + speed + " m/s and an angle of " + angle + " degrees is displayed above.", Toast.LENGTH_LONG).show();
                    return;
                }

                trajectoryPoints.add(new CustomView.PointF(x, y));
                customView.setTrajectoryPoints(trajectoryPoints);

                currentTime += dt;
                if (isRunning) {
                    handler.postDelayed(this, (long) (100 / simulationSpeedFactor));
                }
            }
        };

        handler.post(simulationRunnable);
    }

    private void stopSimulation() {
        isRunning = false;
        handler.removeCallbacks(simulationRunnable);
    }

    private void resumeSimulation() {
        isRunning = true;
        handler.post(simulationRunnable);
    }
}
