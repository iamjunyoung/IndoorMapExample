package com.davemorrissey.labs.subscaleview.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class BitmapView extends View {
    Bitmap bitmap;
    float width;
    float height;
    int dx;
    int dy;
    boolean draggableFlag;
    Context context;

    float validDownX = -1;
    float validDownY = -1;

    public BitmapView(Context context) {
        super(context);
        this.context = context;

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pushpin_blue);
        // 얘 뒤져보면 byte[] 에서 bitmap생성하는 것도 있심

        float density = getResources().getDisplayMetrics().densityDpi;
        width = (density/420f) * bitmap.getWidth();
        height = (density/420f) * bitmap.getHeight();
        bitmap = Bitmap.createScaledBitmap(bitmap, (int)width, (int)height, true);

        //width = bitmap.getWidth();
        //height = bitmap.getHeight();
    }

    public BitmapView(Context context, AttributeSet attrs) {
        super(context, attrs);

        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.pushpin_blue);
        // 얘 뒤져보면 byte[] 에서 bitmap생성하는 것도 있심

        float density = getResources().getDisplayMetrics().densityDpi;
        width = (density/420f) * bitmap.getWidth();
        height = (density/420f) * bitmap.getHeight();
        //width = bitmap.getWidth();
        //height = bitmap.getHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d("BitmapView", "onDraw at " + dx + " Y: " + dy);
        canvas.drawBitmap(
                bitmap, // 출력할 bitmap
                new Rect(0,0, (int)width, (int)height),   // 출력할 bitmap의 지정된 영역을 (sub bitmap)
                new Rect(dx, dy,dx + (int)width,dy + (int)height),  // 이 영역에 출력한다. (화면을 벗어나면 clipping됨)
                null);

        //canvas.drawBitmap(bitmap, 0, 0, null); // 마지막에 touch된 또는 현재 touch된 애를 가장 마지막에 그려줄 수 있을까?

        super.onDraw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if ( x > dx && x < dx + (int)width && y > dy && y < dy + (int)height ) {
                    draggableFlag = true;
                    Toast.makeText(context, "Touched at (" + x + ", " + y + ")", Toast.LENGTH_LONG).show();
                    Log.d("TOUCHED", "X: " + x + " Y: " + y);
                    //Bitmap touched
                    validDownX = x;
                    validDownY = y;
                    Log.d("validDown", "X: " + validDownX + " Y: " + validDownY);

                } else {
                    draggableFlag = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                draggableFlag = false;
                validDownX = -1;
                validDownY = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                // 주루룩 drag했을 경우 히스토리가 모두 기록되어서 전달됨
                //if ( x > dx && x < dx + (int)width && y > dy && y < dy + (int)height ) {
                if (draggableFlag) {
                    Log.d("ACTION_MOVE", "X: " + x + " Y: " + y);
                    //Bitmap will be moved

                    int length=event.getHistorySize();
                    float sx, sy, ex, ey;

                    //출처:  [훈트이야기]
                    if (length != 0) {
                        sx = event.getHistoricalX(0);
                        sy = event.getHistoricalY(0);
                        ex = event.getHistoricalX(length-1);
                        ey = event.getHistoricalY(length-1);

                        dx += (int)(ex-sx);
                        dy += (int)(ey-sy);
                    }
                    invalidate();


                    //아래 방법은 잘 안되고 있음
                    // GestureDetector의 drag and drop 예 처럼 drag하는 그대로 옮겨져야 함.
                    /*
                    Log.d("ACTION_MOVE validDown ", validDownX + " " + validDownY);

                    if (validDownX != -1 && validDownY != -1) {
                        //dx += (int)x - ((int) validDownX - dx);
                        //dy += (int)y - ((int) validDownY - dy);
                        dx += (x - validDownX);
                        dy += (y - validDownY);

                        invalidate();
                    }*/

                }
                break;
        }
        return true;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
}
