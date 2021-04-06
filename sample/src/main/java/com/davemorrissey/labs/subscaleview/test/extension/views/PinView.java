package com.davemorrissey.labs.subscaleview.test.extension.views;

import android.content.Context;
import android.graphics.*;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.davemorrissey.labs.subscaleview.test.BitmapView;
import com.davemorrissey.labs.subscaleview.test.R;
import com.davemorrissey.labs.subscaleview.test.R.drawable;

import java.util.ArrayList;


public class PinView extends SubsamplingScaleImageView { //SubsamplingScaleImageView

    private final Paint paint = new Paint();
    private final PointF vPin = new PointF();
    public ArrayList<PointF> sPinList;
    //private PointF sPin;
    public ArrayList<Bitmap> poiBitmapList;
    //public ArrayList<BitmapView> poiBitmapViewList; // <-이걸 쓸 수 있을까?

    //private ImageView imageView;
    private Context context;

    public PinView(Context context) {
        this(context, null);
        this.context = context;
    }

    public PinView(Context context, AttributeSet attr) {
        super(context, attr);
        this.context = context;
        sPinList = new ArrayList<>();
        poiBitmapList = new ArrayList<>();
        //poiBitmapViewList = new ArrayList<>();

        //initialise();
    }

    public void setPin(PointF sPin) {
        sPinList.add(sPin);
        //this.sPin = sPin;
        Log.d("JYN", "setPin called");
        initialise();
        //invalidate();
    }

    private void initialise() {
        float density = getResources().getDisplayMetrics().densityDpi;
        Bitmap pin = BitmapFactory.decodeResource(this.getResources(), drawable.pushpin_blue);
        float w = (density/420f) * pin.getWidth();
        float h = (density/420f) * pin.getHeight();
        pin = Bitmap.createScaledBitmap(pin, (int)w, (int)h, true);
        poiBitmapList.add(pin);
        //poiBitmapViewList.add(new BitmapView(context));
        Log.d("JYN", "initialise w " + w + "  h " + h);
        /*
        imageView = new ImageView(contex);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        imageView.setImageResource(R.drawable.pin);
        */
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Don't draw pin before image is ready so it doesn't move around during setup.
        if (!isReady()) {
            return;
        }

        paint.setAntiAlias(true);

        /*
        if (poiBitmapViewList != null && sPinList != null) {
            Log.d("JYN", "poiBitmapViewList size " + poiBitmapViewList.size());
            for (int i = 0; i < poiBitmapViewList.size(); i++) {
                Bitmap pin = poiBitmapViewList.get(i).getBitmap();

                sourceToViewCoord(sPinList.get(i), vPin);
                float vX = vPin.x - (pin.getWidth() / 2);
                float vY = vPin.y - pin.getHeight();
                //canvas.drawBitmap(pin, vX, vY, paint); // 마지막에 touch된 또는 현재 touch된 애를 가장 마지막에 그려줄 수 있을까?

                float density = getResources().getDisplayMetrics().densityDpi;
                float width = (density/420f) * pin.getWidth();
                float height = (density/420f) * pin.getHeight();
                canvas.drawBitmap(
                        pin, // 출력할 bitmap
                        new Rect(0,0, (int)width, (int)height),   // 출력할 bitmap의 지정된 영역을 (sub bitmap)
                        new Rect((int)vX, (int)vY,(int)(vX + width),(int)(vY + height)),  // 이 영역에 출력한다. (화면을 벗어나면 clipping됨)
                        paint);
                Paint textPaint = new Paint();
                textPaint.setTextSize(70);
                textPaint.setUnderlineText(true);
                canvas.drawText("야야야", vX, vY, textPaint);
                Log.d("JYN", "onDraw vX " + vX + "  vY " + vY + "/" + (pin.getWidth() / 2) + "  " + pin.getHeight());
            }
        }
         */

        if (poiBitmapList != null && sPinList != null) {
            //if (sPin != null) {
            //if (sPin != null && pin != null) {
            Log.d("JYN", "poiBitmapList size " + poiBitmapList.size());
            for (int i = 0; i < poiBitmapList.size(); i++) {
                Bitmap pin = poiBitmapList.get(i);

                sourceToViewCoord(sPinList.get(i), vPin);
                float vX = vPin.x - (pin.getWidth() / 2);
                float vY = vPin.y - pin.getHeight();

                //canvas.drawBitmap(pin, vX, vY, paint); // 마지막에 touch된 또는 현재 touch된 애를 가장 마지막에 그려줄 수 있을까?
                float density = getResources().getDisplayMetrics().densityDpi;
                float width = pin.getWidth();
                float height =  pin.getHeight();
                //float width = (density/420f) * pin.getWidth();
                //float height = (density/420f) * pin.getHeight();
                canvas.drawBitmap(
                        pin, // 출력할 bitmap
                        new Rect(0,0, (int)width, (int)height),   // 출력할 bitmap의 지정된 영역을 (sub bitmap)
                        new Rect((int)vX, (int)vY,(int)(vX + width),(int)(vY + height)),  // 이 영역에 출력한다. (화면을 벗어나면 clipping됨)
                        paint);

                Paint textPaint = new Paint();
                textPaint.setTextSize(70);
                textPaint.setUnderlineText(true);
                canvas.drawText("야야야", vX, vY, textPaint);
                Log.d("JYN", "onDraw vX " + vX + "  vY " + vY + "/" + (pin.getWidth() / 2) + "  " + pin.getHeight());
            }
            //imageView.setTranslationX(vX);
            //imageView.setTranslationX(vY);
            //}
        }
    }
}
