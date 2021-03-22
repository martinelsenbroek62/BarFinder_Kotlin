package com.lemust.ui.base;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Camera;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.ViewConfigurationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.Scroller;

import com.lemust.utils.SystemUtils;

import java.lang.reflect.Field;

public class BounceViewPager extends ViewPager {
    private final static int LONG_ANIM_DURATION = 850;
    private final static int SHORT_ANIM_DURATION = 150;
    private final static int DEFAULT_OVERSCROLL_ANIMATION_DURATION = 200;

    private GestureDetector xScrollDetector;
    private FixedSpeedScroller scroller;

    class XScrollDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return Math.abs(distanceX) > Math.abs(distanceY);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            final int action = ev.getAction() & MotionEventCompat.ACTION_MASK;
            switch (action) {
                case MotionEvent.ACTION_DOWN: {
                    lastMotionX = ev.getX();
                    activePointerId = MotionEventCompat.getPointerId(ev, 0);
                    break;
                }
                case MotionEventCompat.ACTION_POINTER_DOWN: {
                    final int index = MotionEventCompat.getActionIndex(ev);
                    lastMotionX = MotionEventCompat.getX(ev, index);
                    activePointerId = MotionEventCompat.getPointerId(ev, index);
                    break;
                }
            }

            if (xScrollDetector.onTouchEvent(ev)) {
                super.onInterceptTouchEvent(ev);
                return true;
            }

            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            return false;
        }
    }

    private final static int INVALID_POINTER_ID = -1;

    private class OverScrollEffect {
        private float overScroll;
        private Animator animator;

        public void setPull(final float deltaDistance) {
            overScroll = deltaDistance;
            invalidateVisibleChildren();
        }

        private void onRelease() {
            if (animator != null && animator.isRunning()) {
                animator.addListener(new Animator.AnimatorListener() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        startAnimation();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }
                });
                animator.cancel();
            } else {
                startAnimation();
            }
        }

        private void startAnimation() {
            animator = ObjectAnimator.ofFloat(this, "pull", overScroll, 0);
            animator.setInterpolator(new DecelerateInterpolator());
            final float scale = Math.abs(-overScroll);
            animator.setDuration((long) (overScrollAnimationDuration * scale));
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });

            animator.start();
        }

        private boolean isOverScrolling() {
            if (scrollPosition == 0 && overScroll < 0) {
                return true;
            }
            final boolean isLast = (getAdapter().getCount() - 1) == getCurrentItem();
            return isLast && overScroll > 0;
        }

    }

    final private OverScrollEffect overscrollEffect = new OverScrollEffect();
    final private Camera camera = new Camera();

    private float lastMotionX;
    private int activePointerId;
    private int scrollPosition;
    private float scrollPositionOffset;
    final private int touchSlop;

    private float overScrollTranslation;
    private int overScrollAnimationDuration;

    public BounceViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        xScrollDetector = new GestureDetector(getContext(), new XScrollDetector());

        setStaticTransformationsEnabled(true);
        final ViewConfiguration configuration = ViewConfiguration.get(context);
        touchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
        super.setOnPageChangeListener(new MyOnPageChangeListener());
        init();
    }

    private void init() {
        // overScrollTranslation = UiUtil().getDisplayWidth() / 3;
        overScrollTranslation = SystemUtils.INSTANCE.getDisplayWidth() / 3;
        overScrollAnimationDuration = DEFAULT_OVERSCROLL_ANIMATION_DURATION;

        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            scroller = new FixedSpeedScroller(getContext());
            setShortAnimDuration();
            mScroller.set(this, scroller);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
        }
    }

    private void invalidateVisibleChildren() {
        for (int i = 0; i < getChildCount(); i++) {
            getChildAt(i).invalidate();
        }
    }

    private class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            scrollPosition = position;
            scrollPositionOffset = positionOffset;
            invalidateVisibleChildren();
        }

        @Override
        public void onPageSelected(int position) {
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            if (state == SCROLL_STATE_IDLE) {
                scrollPositionOffset = 0;
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        boolean callSuper = false;

        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                callSuper = true;
                lastMotionX = ev.getX();
                activePointerId = MotionEventCompat.getPointerId(ev, 0);
                break;
            }
            case MotionEventCompat.ACTION_POINTER_DOWN: {
                callSuper = true;
                final int index = MotionEventCompat.getActionIndex(ev);
                lastMotionX = MotionEventCompat.getX(ev, index);
                activePointerId = MotionEventCompat.getPointerId(ev, index);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (activePointerId != INVALID_POINTER_ID) {
                    final int activePointerIndex = MotionEventCompat.findPointerIndex(ev, activePointerId);
                    final float x = MotionEventCompat.getX(ev, activePointerIndex);
                    final float deltaX = lastMotionX - x;
                    final float oldScrollX = getScrollX();
                    final int width = getWidth();
                    final int widthWithMargin = width + getPageMargin() - (getPaddingLeft() + getPaddingRight());
                    final int lastItemIndex = getAdapter().getCount() - 1;
                    final int currentItemIndex = getCurrentItem();
                    final float leftBound = Math.max(0, (currentItemIndex - 1) * widthWithMargin);
                    final float rightBound = (Math.min(currentItemIndex + 1, lastItemIndex)) * (widthWithMargin);
                    final float scrollX = oldScrollX + deltaX;
                    if (scrollPositionOffset == 0) {
                        if (scrollX < leftBound) {
                            if (leftBound == 0) {
                                final float over = deltaX + touchSlop;
                                overscrollEffect.setPull(over / width);
                            }
                        } else if (scrollX > rightBound) {
                            if (rightBound == lastItemIndex * widthWithMargin) {
                                final float over = scrollX - rightBound - touchSlop;
                                overscrollEffect.setPull(over / width);
                            }
                        }
                    } else {
                        lastMotionX = x;
                    }
                } else {
                    overscrollEffect.onRelease();
                }
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                callSuper = true;
                activePointerId = INVALID_POINTER_ID;
                overscrollEffect.onRelease();
                break;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK)
                        >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);
                if (pointerId == activePointerId) {
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    lastMotionX = ev.getX(newPointerIndex);
                    activePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                    callSuper = true;
                }
                break;
            }
        }

        return overscrollEffect.isOverScrolling() && !callSuper || super.onTouchEvent(ev);
    }

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        if (child.getWidth() == 0) {
            return false;
        }
        final int position = child.getLeft() / child.getWidth();
        final boolean isFirstOrLast = position == 0 || (position == getAdapter().getCount() - 1);
        if (overscrollEffect.isOverScrolling() && isFirstOrLast) {
            t.clear();
            t.setTransformationType(Transformation.TYPE_MATRIX);

            final float dx = getWidth() / 2;
            final int dy = getHeight() / 2;
            t.getMatrix().reset();
            final float translateX = overScrollTranslation * (overscrollEffect.overScroll > 0 ?
                    Math.min(overscrollEffect.overScroll, 1) : Math.max(overscrollEffect.overScroll, -1));
            camera.save();
            camera.translate(-translateX, 0, 0);
            camera.getMatrix(t.getMatrix());
            t.getMatrix().preTranslate(-dx, -dy);
            t.getMatrix().postTranslate(dx, dy);
            camera.restore();

            this.invalidate();
            return true;
        }
        return false;
    }

    private void setAnimationDuration(int duration) {
        scroller.setDuration(duration);
    }

    public void setLongAnimDuration() {
        setAnimationDuration(LONG_ANIM_DURATION);
    }

    public void setShortAnimDuration() {
        setAnimationDuration(SHORT_ANIM_DURATION);
    }
}

class FixedSpeedScroller extends Scroller {
    private int duration = 150;

    public FixedSpeedScroller(Context context) {
        super(context, new LinearOutSlowInInterpolator());
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        super.startScroll(startX, startY, dx, dy, this.duration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, duration);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}