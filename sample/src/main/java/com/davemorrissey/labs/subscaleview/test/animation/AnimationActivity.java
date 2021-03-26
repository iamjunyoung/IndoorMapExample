package com.davemorrissey.labs.subscaleview.test.animation;

import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.AnimationBuilder;
import com.davemorrissey.labs.subscaleview.test.AbstractPagesActivity;
import com.davemorrissey.labs.subscaleview.test.Page;
import com.davemorrissey.labs.subscaleview.test.R;
import com.davemorrissey.labs.subscaleview.test.R.id;
import com.davemorrissey.labs.subscaleview.test.eventhandlingadvanced.AdvancedEventHandlingActivity;
import com.davemorrissey.labs.subscaleview.test.extension.views.PinView;

import java.util.Arrays;
import java.util.Random;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.*;
import static com.davemorrissey.labs.subscaleview.test.R.string.*;
import static com.davemorrissey.labs.subscaleview.test.R.layout.*;

public class AnimationActivity extends AbstractPagesActivity {

    private PinView view;
    ///
    private View mView;
    private RelativeLayout rl_chatBot;
    private ImageView mAvatarIcon;
    private ImageView trash;
    private Rect outRect = new Rect();

    private int[] location = new int[2];
    private boolean mIsScrolling = false;

    int x;
    int y;

    public AnimationActivity() {
        super(animation_title, animation_activity, Arrays.asList(
                new Page(animation_p1_subtitle, animation_p1_text),
                new Page(animation_p2_subtitle, animation_p2_text),
                new Page(animation_p3_subtitle, animation_p3_text),
                new Page(animation_p4_subtitle, animation_p4_text)
        ));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        findViewById(id.play).setOnClickListener(new OnClickListener() {
            @Override public void onClick(View v) { AnimationActivity.this.play(); }
        });
        view = findViewById(id.imageView);
        view.setImage(ImageSource.asset("LGSPEV_W2_B1.png"));
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (view.isReady()) {
                    PointF sCoord = view.viewToSourceCoord(e.getX(), e.getY());
                    Toast.makeText(getApplicationContext(), "Single tap: " + ((int)sCoord.x) + ", " + ((int)sCoord.y), Toast.LENGTH_SHORT).show();
                    //x = (int)sCoord.x;
                    //y = (int)sCoord.y;
                } else {
                    Toast.makeText(getApplicationContext(), "Single tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
            @Override
            public void onLongPress(MotionEvent e) {
                if (view.isReady()) {
                    PointF sCoord = view.viewToSourceCoord(e.getX(), e.getY());
                    Toast.makeText(getApplicationContext(), "Long press: " + ((int)sCoord.x) + ", " + ((int)sCoord.y), Toast.LENGTH_SHORT).show();
                    x = (int)sCoord.x;
                    y = (int)sCoord.y;
                    play();

                    mView.setTranslationX(x); // mView가 사용하는 x, y 와 <--> 지도상에서 사용하는 sCoord.x, sCoord.y를 잘 변환하면 될 것 같음
                    mView.setTranslationY(y);
                } else {
                    Toast.makeText(getApplicationContext(), "Long press: Image not ready", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                if (view.isReady()) {
                    PointF sCoord = view.viewToSourceCoord(e.getX(), e.getY());
                    Toast.makeText(getApplicationContext(), "Double tap: " + ((int)sCoord.x) + ", " + ((int)sCoord.y), Toast.LENGTH_SHORT).show();
                    //x = (int)sCoord.x;
                    //y = (int)sCoord.y;
                } else {
                    Toast.makeText(getApplicationContext(), "Double tap: Image not ready", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        //view.setImage(ImageSource.asset("sanmartino.jpg"));
        view.setOnTouchListener(new View.OnTouchListener() {
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

    @Override
    protected void onPageChanged(int page) {
        if (page == 2) {
            view.setPanLimit(PAN_LIMIT_CENTER);
        } else {
            view.setPanLimit(PAN_LIMIT_INSIDE);
        }
    }

    private void play() {
        Random random = new Random();
        if (view.isReady()) {
            float maxScale = view.getMaxScale();
            float minScale = view.getMinScale();
            float scale = (random.nextFloat() * (maxScale - minScale)) + minScale;
            //PointF center = new PointF(random.nextInt(view.getSWidth()), random.nextInt(view.getSHeight()));
            Log.d("JYN", "play to (" + x + "," + y);
            PointF center = new PointF(x, y);
            view.setPin(center);
            AnimationBuilder animationBuilder = view.animateScaleAndCenter(scale, center);
            if (getPage() == 3) {
                animationBuilder.withDuration(2000).withEasing(EASE_OUT_QUAD).withInterruptible(false).start();
            } else {
                animationBuilder.withDuration(750).start();
            }
        }
    }

    public class MoveViewTouchListener implements View.OnTouchListener {
        private GestureDetector mGestureDetector;
        //private View mView;

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
                Toast.makeText(AnimationActivity.this, "" + mMotionDownY + " " + mMotionDownY
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
