package com.example.beta_pdm;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CustomView extends View {

    private List<PointF> trajectoryPoints = new ArrayList<>();
    private Paint paintPoint;
    private Paint paintTrajectory;

    public CustomView(Context context) {
        super(context);
        init();
    }

    public CustomView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paintPoint = new Paint();
        paintPoint.setColor(Color.RED);
        paintPoint.setStrokeWidth(20);

        paintTrajectory = new Paint();
        paintTrajectory.setColor(Color.BLACK);
        paintTrajectory.setStrokeWidth(10);
    }

    public void setTrajectoryPoints(List<PointF> points) {
        trajectoryPoints = points;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        PointF prevPoint = null;
        float centerX = canvas.getWidth() / 2f;
        float centerY = canvas.getHeight() / 2f;

        for (PointF point : trajectoryPoints) {
            if (prevPoint != null) {
                canvas.drawLine(centerX + prevPoint.x, centerY - prevPoint.y, centerX + point.x, centerY - point.y, paintTrajectory);
            }
            prevPoint = point;
        }

        if (!trajectoryPoints.isEmpty()) {
            PointF lastPoint = trajectoryPoints.get(trajectoryPoints.size() - 1);
            canvas.drawCircle(centerX + lastPoint.x, centerY - lastPoint.y, 20, paintPoint);
        }
    }

    public static class PointF {
        float x, y;

        public PointF(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}
