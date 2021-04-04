package com.davemorrissey.labs.subscaleview.test.animation;

import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.AnimationBuilder;
import com.davemorrissey.labs.subscaleview.test.AbstractPagesActivity;
import com.davemorrissey.labs.subscaleview.test.Page;
import com.davemorrissey.labs.subscaleview.test.R;
import com.davemorrissey.labs.subscaleview.test.R.id;
import com.davemorrissey.labs.subscaleview.test.extension.views.PinView;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.EASE_OUT_QUAD;
import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.OnClickListener;
import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.OnTouchListener;
import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.PAN_LIMIT_CENTER;
import static com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView.PAN_LIMIT_INSIDE;
import static com.davemorrissey.labs.subscaleview.test.R.layout.animation_activity;
import static com.davemorrissey.labs.subscaleview.test.R.layout.layout_for_always_on_top;
import static com.davemorrissey.labs.subscaleview.test.R.string.animation_p1_subtitle;
import static com.davemorrissey.labs.subscaleview.test.R.string.animation_p1_text;
import static com.davemorrissey.labs.subscaleview.test.R.string.animation_p2_subtitle;
import static com.davemorrissey.labs.subscaleview.test.R.string.animation_p2_text;
import static com.davemorrissey.labs.subscaleview.test.R.string.animation_p3_subtitle;
import static com.davemorrissey.labs.subscaleview.test.R.string.animation_p3_text;
import static com.davemorrissey.labs.subscaleview.test.R.string.animation_p4_subtitle;
import static com.davemorrissey.labs.subscaleview.test.R.string.animation_p4_text;
import static com.davemorrissey.labs.subscaleview.test.R.string.animation_title;

public class RecyclerViewActivity extends AbstractPagesActivity {
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

    LayoutInflater inflater;
    //
    private PopupWindow mPopupWindow ;

    public RecyclerViewActivity() {
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
            @Override public void onClick(View v) { RecyclerViewActivity.this.play(); }
        });
        view = findViewById(id.imageView);
        view.setImage(ImageSource.asset("LGSPEV_W2_B1.png"));

        inflater = (LayoutInflater) RecyclerViewActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //

        //
        final GestureDetector gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (view.isReady()) {
                    PointF sCoord = view.viewToSourceCoord(e.getX(), e.getY());
                    //Toast.makeText(getApplicationContext(), "Single tap: " + ((int)sCoord.x) + ", " + ((int)sCoord.y), Toast.LENGTH_SHORT).show();
                    x = (int)sCoord.x;
                    y = (int)sCoord.y;
                    //boolean check = isTouchingPOI(sCoord.x, sCoord.y);

                    int ret = nearestPOIFromAll(sCoord.x, sCoord.y);
                    if (ret != -1) {
                        if (Math.abs(sCoord.x - view.sPinList.get(ret).x) < 30 && Math.abs(sCoord.y - view.sPinList.get(ret).y) < 30) {
                            Log.d("JYN", "Nearest poi index is " + ret + " " + view.sPinList.get(ret).x + ", " + view.sPinList.get(ret).y
                                    + " from your touch (" + (int)sCoord.x + ", " + (int)sCoord.y);
                            focusByPosition(new PointF(view.sPinList.get(ret).x, view.sPinList.get(ret).y));

                            Toast.makeText(getApplicationContext(), "Nearest poi index is "
                                    + ret + " " + view.sPinList.get(ret).x + ", " + view.sPinList.get(ret).y
                                    + " from your touch (" + (int)sCoord.x + ", " + (int)sCoord.y, Toast.LENGTH_SHORT).show();

                            ////

                        }
                    }

                    //mView.setTranslationX(e.getX()); // mView가 사용하는 x, y 와 <--> 지도상에서 사용하는 sCoord.x, sCoord.y를 잘 변환하면 될 것 같음
                    //mView.setTranslationY(e.getY());
                    //mView.setTranslationX(e2.getRawX() - mMotionDownX);
                    //mView.setTranslationY(e2.getRawY() - mMotionDownY);

                    showAllCurrentPOILocation();

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

                    PointF center = new PointF(x, y+20);
                    View popupView = inflater.inflate(layout_for_always_on_top, null);
                    mPopupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    //view.setPopupWindow(popupView, mPopupWindow, center);

                    //popupView 에서 (LinearLayout 을 사용) 레이아웃이 둘러싸고 있는 컨텐츠의 크기 만큼 팝업 크기를 지정
                    mPopupWindow.setFocusable(true);
                    // 외부 영역 선택히 PopUp 종료
                    mPopupWindow.showAtLocation(popupView, Gravity.CENTER, 123, 123);

                    //https://puzzleleaf.tistory.com/48

                    //mView.setTranslationX(vX); // mView가 사용하는 x, y 와 <--> 지도상에서 사용하는 sCoord.x, sCoord.y를 잘 변환하면 될 것 같음
                    //mView.setTranslationY(vY);
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
        view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return gestureDetector.onTouchEvent(motionEvent);
            }
        });

        rl_chatBot = (RelativeLayout) findViewById(id.rl_chatBot);
        Log.d("JYN", "rl_chatBot " + rl_chatBot);
        mAvatarIcon = findViewById(id.bn_avatar);
        trash = findViewById(id.trash);

        mAvatarIcon.setOnTouchListener(new MoveViewTouchListener(rl_chatBot));
        trash.setOnClickListener(new OnClickListener() {
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
            Log.d("JYN", "play to (" + x + "," + y + ")");
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

    private void focusByPosition(PointF p) {
        Random random = new Random();
        if (view.isReady()) {
            float maxScale = view.getMaxScale();
            float minScale = view.getMinScale();
            float scale = (random.nextFloat() * (maxScale - minScale)) + minScale;
            Log.d("JYN", "focusByPosition to (" + p.x + "," + p.y + ")");
            PointF center = new PointF(p.x, p.y);

            AnimationBuilder animationBuilder = view.animateScaleAndCenter(scale, center);
            if (getPage() == 3) {
                animationBuilder.withDuration(2000).withEasing(EASE_OUT_QUAD).withInterruptible(false).start();
            } else {
                animationBuilder.withDuration(750).start();
            }
        }
    }

    public class MoveViewTouchListener implements OnTouchListener {
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
                Toast.makeText(RecyclerViewActivity.this, "icon touch "
                        + mMotionDownY + "," + mMotionDownY
                        + "\n" + e.getRawX() +"," + e.getRawY()
                        + "\n" + mView.getTranslationX() + "," + mView.getTranslationY(), Toast.LENGTH_LONG).show();
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
                Log.d("JYN", "onScroll " + (e2.getRawX() - mMotionDownX) + "," + (e2.getRawY() - mMotionDownY));
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

    /////
    /*
    ArrayList<Integer> candidateList = new ArrayList<>();
    public boolean isTouchingPOI(float x, float y) {
        boolean ret = false;
        Log.d("JYN", "isTouchingPOI in total size " + view.sPinList.size());

        for (int i = 0 ; i < view.sPinList.size() ; i++){
            PointF p = view.sPinList.get(i);
            //if (x > p.x - 3 && x < p.x + 3 && y > p.y - 4 && y < p.y + 4) {
            if (x > p.x - 6 && x < p.x + 6 && y > p.y - 6 && y < p.y + 6) {
                Log.d("JYN", "You touch poi of " + i);
                candidateList.add(i);
                ret = true;
            } else {
                Log.d("JYN", "yayayayayay " + i);
            }
        }
        return ret;
    }

    public int nearestPOI(float x, float y) { // touch position
        int ret = -1;

        double min = Double.MAX_VALUE;
        for (int i = 0 ; i < candidateList.size() ; i++) {
            float x1 = view.sPinList.get(candidateList.get(i)).x;
            float y1 = view.sPinList.get(candidateList.get(i)).y;
            double dist = getDistance(x, y, x1, y1);
            if (min > dist) {
                min = dist;
                ret = candidateList.get(i);
            }
        }
        candidateList.clear();
        return ret;
    }
    */

    public int nearestPOIFromAll(float x, float y) { // touch position
        int ret = -1;

        double min = Double.MAX_VALUE;
        for (int i = 0 ; i < view.sPinList.size() ; i++) {
            PointF p = view.sPinList.get(i);
            double dist = getDistance(x, y, p.x, p.y);
            if (min > dist) {
                min = dist;
                ret = i;
            }
        }
        return ret;
    }

    static double getDistance(float x, float y, float x1, float y1) {
        double d;
        double xd, yd;
        yd = Math.pow((y1-y), 2);
        xd = Math.pow((x1-x), 2);
        d = Math.sqrt(yd + xd);
        return d;
    }

    public void showAllCurrentPOILocation() {
        AtomicInteger i = new AtomicInteger();
        view.sPinList.stream().forEach( x -> Log.d("JYN", "show all " + x + " " + i.getAndIncrement()));
    }
}
