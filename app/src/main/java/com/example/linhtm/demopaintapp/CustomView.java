package com.example.linhtm.demopaintapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by linhtm on 7/31/2018.
 */

public class CustomView extends View {

    // drawing path
    private Path drawPath;

    // defines what to draw
    private Paint canvasPaint;

    // defines how to draw
    private Paint drawPaint;

    // initial color
    private int paintColor = 0xFF660000;

    // canvas - holding pen, holds your drawings
    // and transfers them to the view
    private Canvas drawCanvas;

    // canvas bitmap
    private Bitmap canvasBitmap;

    // brush size
    private float currentBrushSize, lastBrushSize;

    private float maxX, maxY, minX, minY;

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(){
        currentBrushSize = getResources().getInteger(R.integer.medium_size);
        lastBrushSize = currentBrushSize;
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(currentBrushSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
        resetXY();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0 , 0 , canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        if (w <=0 || h <= 0)
            return;
        // create Bitmap of certain w,h
        canvasBitmap = Bitmap.createBitmap(w,h,Bitmap.Config.ARGB_8888);

        // apply bitmap to graphic to start drawing
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                maxX = touchX;
                minX = touchX;

                maxY = touchY;
                minY = touchY;

                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:

                if (maxX < touchX){
                    maxX = touchX;
                }
                if (minX > touchX){
                    minX = touchX;
                }
                if (maxY < touchY){
                    maxY = touchY;
                }
                if (minY > touchY){
                    minY = touchY;
                }

                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:

                if (maxX < touchX){
                    maxX = touchX;
                }
                if (minX > touchX){
                    minX = touchX;
                }
                if (maxY < touchY){
                    maxY = touchY;
                }
                if (minY > touchY){
                    minY = touchY;
                }

                drawPath.lineTo(touchX, touchY);
                drawRetangle();
                drawPath.reset();
                break;
            default:
                return false;
        }
        invalidate();
        return true;
    }

    private void resetXY(){
        maxX = 0;
        maxY = 0;
        minX = 0;
        minY = 0;
    }
    public void drawRetangle(){
        Paint myPaint = new Paint();
        myPaint.setColor(Color.RED);
        myPaint.setStrokeWidth(5);
        drawCanvas.drawRect(minX, maxY, maxX, minY,myPaint);
    }

    public void drawLine(){
        drawCanvas.drawPath(drawPath, drawPaint);
    }

    public void resetLayout(){
        drawCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }
}
