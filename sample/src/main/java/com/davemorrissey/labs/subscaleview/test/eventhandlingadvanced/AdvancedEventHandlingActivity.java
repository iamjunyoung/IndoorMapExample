package com.davemorrissey.labs.subscaleview.test.eventhandlingadvanced;

import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.davemorrissey.labs.subscaleview.test.AbstractPagesActivity;
import com.davemorrissey.labs.subscaleview.test.Page;
import com.davemorrissey.labs.subscaleview.test.R;
import com.davemorrissey.labs.subscaleview.test.R.id;

import java.util.Arrays;

import static com.davemorrissey.labs.subscaleview.test.R.string.*;
import static com.davemorrissey.labs.subscaleview.test.R.layout.*;

public class AdvancedEventHandlingActivity extends AbstractPagesActivity {
    private RelativeLayout rl_chatBot;
    private ImageView mAvatarIcon;
    private ImageView trash;
    private Rect outRect = new Rect();

    private int[] location = new int[2];
    private boolean mIsScrolling = false;

    public AdvancedEventHandlingActivity() {
        super(advancedevent_title, pages_activity, Arrays.asList(
                new Page(advancedevent_p1_subtitle, advancedevent_p1_text),
                new Page(advancedevent_p2_subtitle, advancedevent_p2_text),
                new Page(advancedevent_p3_subtitle, advancedevent_p3_text),
                new Page(advancedevent_p4_subtitle, advancedevent_p4_text),
                new Page(advancedevent_p5_subtitle, advancedevent_p5_text)
        ));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SubsamplingScaleImageView imageView = findViewById(id.imageView);
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    Toast.makeText(getApplicationContext(), "Single tap: " + ((int)sCoord.x) + ", " + ((int)sCoord.y), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Single tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    Toast.makeText(getApplicationContext(), "Long press: " + ((int)sCoord.x) + ", " + ((int)sCoord.y), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Long press: Image not ready", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e.getX(), e.getY());
                    Toast.makeText(getApplicationContext(), "Double tap: " + ((int)sCoord.x) + ", " + ((int)sCoord.y), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Double tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            /*
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,float distanceY) {
                // TODO Auto-generated method stub

                if (imageView.isReady()) {
                    PointF sCoord = imageView.viewToSourceCoord(e1.getX(), e1.getY());
                    PointF sCoord2 = imageView.viewToSourceCoord(e2.getX(), e2.getY());

                    Log.d("JYN", "onScroll (" + (int)sCoord.x + "," + ((int)sCoord.y)
                            + ") -> (" + ((int)sCoord2.x) + ", " + ((int)sCoord2.y) + ")");
                } else {
                    Toast.makeText(getApplicationContext(), "onScroll: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;
            }*/
        });

        imageView.setImage(ImageSource.asset("LGSPEV_W2_B1.png"));
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });


        rl_chatBot = (RelativeLayout) findViewById(R.id.rl_chatBot);
        Log.d("JYN", "rl_chatBot " + rl_chatBot);
        mAvatarIcon = findViewById(R.id.bn_avatar);
        trash = findViewById(R.id.trash);

        mAvatarIcon.setOnTouchListener(new MoveViewTouchListener(rl_chatBot));
        trash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_chatBot.setVisibility(View.VISIBLE);
                trash.setVisibility(View.GONE);
                trash.setBackgroundResource(R.drawable.open);
            }
        });
    }

    public class MoveViewTouchListener implements View.OnTouchListener {
        private GestureDetector mGestureDetector;
        private View mView;

        public MoveViewTouchListener(View view) {
            mGestureDetector = new GestureDetector(view.getContext(), mGestureListener);
            mView = view;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            if (event.getAction() == MotionEvent.ACTION_UP && isViewInBounds(trash, (int) event.getRawX(), (int) event.getRawY())) {
                rl_chatBot.setVisibility(View.GONE);
                trash.setVisibility(View.VISIBLE);
                trash.setBackgroundResource(R.drawable.chat_icon);
                mIsScrolling = false;
            }

            if (mGestureDetector.onTouchEvent(event)) {
                return true;
            }
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (mIsScrolling) {
                    mIsScrolling = false;
                    trash.setVisibility(View.GONE);
                }
            }
            return false;
        }

        private GestureDetector.OnGestureListener mGestureListener = new GestureDetector.SimpleOnGestureListener() {
            private float mMotionDownX, mMotionDownY;

            @Override
            public boolean onDown(MotionEvent e) {
                mMotionDownX = e.getRawX() - mView.getTranslationX();
                mMotionDownY = e.getRawY() - mView.getTranslationY();
                Toast.makeText(AdvancedEventHandlingActivity.this, "" + mMotionDownY + " " + mMotionDownY
                        + "/" + e.getRawX() +"," + e.getRawY()
                        + "/" + mView.getTranslationX() + "," + mView.getTranslationY(), Toast.LENGTH_LONG).show();
                return true;
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                mIsScrolling = true;
                trash.setVisibility(View.VISIBLE);
                mView.setTranslationX(e2.getRawX() - mMotionDownX);
                mView.setTranslationY(e2.getRawY() - mMotionDownY);
                return true;
            }
        };
    }

    private boolean isViewInBounds(View view, int x, int y) {
        view.getDrawingRect(outRect);
        view.getLocationOnScreen(location);
        outRect.offset(location[0], location[1]);
        return outRect.contains(x, y);
    }
}
