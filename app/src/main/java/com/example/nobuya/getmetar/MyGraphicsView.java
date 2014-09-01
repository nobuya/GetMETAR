package com.example.nobuya.getmetar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

/**
 * Created by nobuya on 2014/08/29.
 */
public class MyGraphicsView extends View {
    private Context context;
    private GetMetarActivity activity;

    public MyGraphicsView(Context _context, AttributeSet attributeSet) {
        super(_context, attributeSet);
        this.context = _context;
        this.activity = (GetMetarActivity) _context;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Toast.makeText(context, "MyGraphicsView.onDraw...",
                Toast.LENGTH_LONG).show();

        //canvas.drawColor(Color.BLUE);
        canvas.drawColor(Color.DKGRAY);

        //Paint paint = new Paint();
        //paint.setStyle(Paint.Style.FILL);
        //paint.setARGB(255, 100, 100, 100);
        //canvas.drawRect(20, 50, 40, 70, paint);

        drawWind(canvas);
    }

    private void drawWind(Canvas canvas) {
        // Wind Velocity
        drawWindVelocity(canvas);
        // Wind Direction
        drawWindDirection(canvas);
    }

    private void drawWindVelocity(Canvas canvas) {
        // Wind Velocity
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setARGB(200, 200, 255, 100);

        int wind = activity.getWindVelocity();
        int startX = 50;
        int startY = 30;
        int width = 5;
        int height = 30;
        for(int i = 0; i < wind; i++) {
            int x = startX + 10 * i;
            int y = startY;
            canvas.drawRect(x, y, x + width, y + height, paint);
        }
        paint.setColor(Color.WHITE);
        for (int i = 0; i < 30; i++) {
            int x = startX + 10 * i;
            int j = i + 1;
            int y = startY + height + 2;
            int y2 = ((j % 10) == 0 )? y + 6 :
                    ((j % 5) == 0) ? y + 3 : y + 2;
            canvas.drawRect(x, y, x + width, y2, paint);
        }
    }

    private void drawWindDirection(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setARGB(200, 200, 255, 100);

        // Wind Direction
        int cx = 200;
        int cy = 180;
        int r = 80;
        int w = 40;
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setColor(Color.WHITE);
        canvas.drawCircle(cx, cy, r, paint);
        paint.setStrokeWidth(1);
        canvas.drawLine(cx - w, cy,      cx + w, cy,     paint);
        canvas.drawLine(cx,      cy - w, cx,      cy + w, paint);

        int textSize = 30;
        paint.setColor(Color.rgb(255, 255, 255));
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(2);
        paint.setTextSize(textSize);

        int ts2 = textSize / 2;
        int tr = r + ts2;
        canvas.drawText("N", cx - ts2,      cy - tr + ts2, paint);
        canvas.drawText("E", cx + tr - ts2, cy + ts2,      paint);
        canvas.drawText("W", cx - tr - ts2, cy + ts2,      paint);
        canvas.drawText("S", cx - ts2,      cy + tr + ts2, paint);

        int windV = activity.getWindVelocity();
        if (windV > 0) {
            if (activity.isVariableWind()) {
                int windDir1 = activity.getWindDirectionV1();
                int windDir2 = activity.getWindDirectionV2();
                for (int wd = windDir1; wd <= windDir2; wd += 10) {
                    drawWindDirection1(canvas, wd, cx, cy, 100);
                }
            }
            int windDir = activity.getWindDirection();
            drawWindDirection1(canvas, windDir, cx, cy, 255);
        }
    }

    private void drawWindDirection1(Canvas canvas, int windDir, int cx, int cy, int alpha) {
        Paint paint = new Paint();
        //paint.setStyle(Paint.Style.FILL);
        //paint.setARGB(200, 200, 255, 100);

        int angle = (450 - windDir) % 360;
        double rAngle = Math.toRadians(angle);
        double rAngle2 = Math.toRadians(angle + 15);
        double rAngle3 = Math.toRadians(angle - 15);
        int r1 = 70;
        float dy = (float) Math.sin(rAngle) * r1;
        float dx = (float) Math.cos(rAngle) * r1;
        int r2 = 70;
        float dy2 = (float) Math.sin(rAngle2) * r2;
        float dx2 = (float) Math.cos(rAngle2) * r2;
        int r3 = 70;
        float dy3 = (float) Math.sin(rAngle3) * r2;
        float dx3 = (float) Math.cos(rAngle3) * r2;
        //canvas.drawLine(cx, cy, cx + dx, cy - dy, paint);
        Path path = new Path();
        path.moveTo(cx + dx2, cy - dy2);
        path.lineTo(cx + dx3, cy - dy3);
        path.lineTo(cx - dx, cy + dy);
        paint.setColor(Color.YELLOW);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAlpha(alpha);
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasuerSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasuerSpec);
    }
}
