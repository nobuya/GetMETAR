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
                Toast.LENGTH_SHORT).show();

        //canvas.drawColor(Color.BLUE);
        canvas.drawColor(Color.DKGRAY);

        //Paint paint = new Paint();
        //paint.setStyle(Paint.Style.FILL);
        //paint.setARGB(255, 100, 100, 100);
        //canvas.drawRect(20, 50, 40, 70, paint);

        drawWind(canvas);
        drawTemperatureAndDewpoint(canvas);
        drawAltimeterSetting(canvas);
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

        int startX = 50;
        int startY = 30;
        int width = 3;
        int height = 15;

        paint.setColor(Color.WHITE);
        for (int i = 0; i <= 40; i++) {
            int x = startX + 6 * i;
            int y = startY + height + 2;
            int y2 = ((i % 10) == 0 )? y + 6 :
                    ((i % 5) == 0) ? y + 3 : y + 2;
            canvas.drawRect(x, y, x + width, y2, paint);
        }
        int textSize = 16;
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setTextSize(textSize);
        paint.setStrokeWidth(2);
        int ts = textSize / 2;
        int ty = startY + height + 10 + textSize;
        canvas.drawText(" 0", startX + 6 * 0  - ts, ty, paint);
        canvas.drawText("10", startX + 6 * 10 - ts, ty, paint);
        canvas.drawText("20", startX + 6 * 20 - ts, ty, paint);
        canvas.drawText("30", startX + 6 * 30 - ts, ty, paint);
        canvas.drawText("40", startX + 6 * 40 - ts, ty, paint);

        int wind = activity.getWindVelocity();
        paint.setARGB(200, 200, 255, 100);
        paint.setStrokeWidth(1);
        for(int i = 0; i <= wind; i++) {
            int x = startX + 6 * i;
            int y = startY;
            canvas.drawRect(x, y, x + width, y + height, paint);
        }
    }

    private void drawWindDirection(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setARGB(200, 200, 255, 100);

        // Wind Direction
        int cx = 200;
        int cy = 220;
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
                if (windDir1 > windDir2) { // 360V090
                    for (int wd = windDir1; wd < 360; wd += 10) {
                        drawWindDirection1(canvas, wd, cx, cy, 100);
                    }
                    for (int wd = 0; wd <= windDir2; wd += 10) {
                        drawWindDirection1(canvas, wd, cx, cy, 100);
                    }
                } else {
                    for (int wd = windDir1; wd <= windDir2; wd += 10) {
                        drawWindDirection1(canvas, wd, cx, cy, 100);
                    }
                }
//                Toast.makeText(context, "variable wind: " + windDir1 + "/" + windDir2,
//                        Toast.LENGTH_LONG).show();
            }
            int windDir = activity.getWindDirection();
            if (windDir != 999) // VRBxxKT
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
        float dy3 = (float) Math.sin(rAngle3) * r3;
        float dx3 = (float) Math.cos(rAngle3) * r3;
        //canvas.drawLine(cx, cy, cx + dx, cy - dy, paint);
        Path path = new Path();
        path.moveTo(cx + dx2, cy - dy2);
        path.lineTo(cx + dx3, cy - dy3);
        path.lineTo(cx - dx, cy + dy);
        paint.setColor(Color.CYAN);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setAlpha(alpha);
        canvas.drawPath(path, paint);
    }

    private void drawTemperatureAndDewpoint(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        int startX = 400;
        int startY = 300;
        int width = 5;
        int height = 4;
        paint.setColor(Color.WHITE);
        for (int i = -10; i <= 40; i++) {
            int x = startX - 2;
            int y = startY - ((height + 1) * i);
            int x2 = (i == 0 ? x - 12 : (((i % 10) == 0 )? x - 8 :
                    ((i % 5) == 0) ? x - 4 : x - 2));
            canvas.drawRect(x2, y, x, y + height, paint);
        }

        int textSize = 16;
        paint.setColor(Color.rgb(255, 255, 255));
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(2);
        paint.setTextSize(textSize);

        int ts2 = textSize / 2;
        canvas.drawText("0", startX - 12 - textSize, startY + ts2, paint);
        canvas.drawText("10", startX - 12 - textSize - ts2,
                startY + ts2 - ((height + 1) * 10), paint);
        canvas.drawText("20", startX - 12 - textSize - ts2,
                startY + ts2 - ((height + 1) * 20), paint);
        canvas.drawText("30", startX - 12 - textSize - ts2,
                startY + ts2 - ((height + 1) * 30), paint);
        canvas.drawText("40", startX - 12 - textSize - ts2,
                startY + ts2 - ((height + 1) * 40), paint);
        canvas.drawText("-10", startX - 12 - textSize - ts2 - ts2,
                startY + ts2 - ((height + 1) * -10), paint);

        int temp = activity.getTemperature();

        paint.setARGB(200, 200, 255, 100);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        for(int i = -10; i <= temp; i++) {
            int x = startX;
            int y = startY - ((height + 1) * i);
            canvas.drawRect(x, y, x + width, y + height, paint);
        }
        paint.setARGB(255, 0, 250, 250);
        int dewpoint = activity.getDewpoint();
        for(int i = -10; i <= dewpoint; i++) {
            int x = startX + width + 1;
            int y = startY - ((height + 1) * i);
            canvas.drawRect(x, y, x + width, y + height, paint);
        }
    }

    // QNH
    private void drawAltimeterSetting(Canvas canvas) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        int startX = 480;
        int startY = 250;
        int originQNH = 1000;
        int width = 5;
        int height = 4;
        paint.setColor(Color.WHITE);
        for (int i = 980; i <= 1030; i++) {
            int x = startX - 2;
            int y = startY - ((height + 1) * i) + (height + 1) * originQNH;
            int x2 = (i == 0 ? x - 12 : (((i % 10) == 0 )? x - 8 :
                    ((i % 5) == 0) ? x - 4 : x - 2));
            canvas.drawRect(x2, y, x, y + height, paint);
        }

        int textSize = 16;
        paint.setColor(Color.rgb(255, 255, 255));
        paint.setAntiAlias(false);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(2);
        paint.setTextSize(textSize);

        int ts2 = textSize / 2;
        int tx0 = startX - 24;
        int y0 = startY + ((height + 1) * (originQNH - 980));
        canvas.drawText("980", tx0 - textSize, y0 + ts2, paint);
        canvas.drawText("990", tx0 - textSize,
                y0 + ts2 - ((height + 1) * 10), paint);
        canvas.drawText("1000", tx0 - textSize - ts2,
                y0 + ts2 - ((height + 1) * 20), paint);
        canvas.drawText("1010", tx0 - textSize - ts2,
                y0 + ts2 - ((height + 1) * 30), paint);
        canvas.drawText("1020", tx0 - textSize - ts2,
                y0 + ts2 - ((height + 1) * 40), paint);
        canvas.drawText("1030", tx0 - textSize - ts2,
                y0 + ts2 - ((height + 1) * 50), paint);


        int qnh = activity.getQNH();
        if (qnh < 980) return;

        paint.setARGB(200, 200, 255, 100);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        for(int i = 980; i <= qnh; i++) {
            int x = startX;
            int y = startY - ((height + 1) * i) + ((height + 1) * originQNH);
            canvas.drawRect(x, y, x + width, y + height, paint);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasuerSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasuerSpec);
    }
}
