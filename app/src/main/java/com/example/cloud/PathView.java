package com.example.cloud;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * This class displays the path taken in the UI.
 */
public class PathView extends View {
    // setup initial color
    private final int paintColor = Color.BLACK;
    // defines paint and canvas
    private Paint drawPaint;
    // path of straight lines
    private Path path = new Path();
    
    public PathView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFocusable(true);
        setFocusableInTouchMode(true);
        setupPaint();
    }

    /**
     * Method used for setting up paint with colour and stroke styles.
     */
    private void setupPaint() {
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(5);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    /**
     * Method drawing the created path with our paint.
     * Called when a view is rendered.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path, drawPaint);
    }

    /**
     * Method called when a touch event is dispatched to a view.
     * XXX this can be changed to drawing coordinates we calculate from sensor data
     */
    public boolean onTouchEvent(MotionEvent event) {
        float pointX = event.getX();
        float pointY = event.getY();
        // Check for the event that occurs
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Start a new line in the path
                path.moveTo(pointX, pointY);
                break;
            case MotionEvent.ACTION_MOVE:
                // Draw line between last point and this point
                path.lineTo(pointX, pointY);
                break;
            default:
                return false;
        }

        postInvalidate();   // Indicate view should be redrawn
        return true;
    }
}
