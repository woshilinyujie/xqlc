package com.rongxun.xqlc.UI.fab;

import android.animation.AnimatorInflater;
import android.animation.StateListAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.AbsListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.rongxun.xqlc.R;

public class FloatingView extends RelativeLayout {


    private boolean mVisible;
    private int mScrollThreshold;
    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();//先加速后减速
    public static final int TRANSLATE_DURATION_MILLIS = 500;//动画持续时间

    public FloatingView(Context context) {
        this(context, null);
    }

    public FloatingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FloatingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @SuppressLint("NewApi")
    private void init(Context context, AttributeSet attributeSet) {
        mVisible = true;
        mScrollThreshold = getResources().getDimensionPixelOffset(R.dimen.fab_scroll_threshold);
        if (hasLollipopApi()) {
            StateListAnimator stateListAnimator = AnimatorInflater.loadStateListAnimator(context,
                    R.animator.fab_press_elevation);
            setStateListAnimator(stateListAnimator);
        }
    }

    private int getMarginRight() {
        int marginRight = 0;
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            marginRight = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
        }
        return marginRight;
    }


    public boolean isVisible() {
        return mVisible;
    }

    public void show() {
        show(true);
    }

    public void hide() {
        hide(true);
    }

    public void show(boolean animate) {
        toggle(true, animate, false);
    }

    public void hide(boolean animate) {
        toggle(false, animate, false);
    }

    private void toggle(final boolean visible, final boolean animate, boolean force) {
        if (mVisible != visible || force) {
            mVisible = visible;
            int width = getWidth();
            if (width == 0 && !force) {
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()) {
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            if (currentVto.isAlive()) {
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true);
                            return true;
                        }
                    });
                    return;
                }
            }
            int translationX = visible ? 0 : width / 2 + getMarginRight();
            if (animate) {

                if (visible != true) {
                    animate().setInterpolator(mInterpolator)
                            .setDuration(TRANSLATE_DURATION_MILLIS)
                            .translationX(translationX)
                            .alpha(0.2f);
                } else {
                    animate().setInterpolator(mInterpolator)
                            .setDuration(TRANSLATE_DURATION_MILLIS)
                            .translationX(translationX)
                            .alpha(1.0f)
                            .start();
                }

            } else {
                animate().translationX(translationX);
            }

            // On pre-Honeycomb a translated view is still clickable, so we need to disable clicks manually
            if (!hasHoneycombApi()) {
                setClickable(visible);
            }
        }
    }

    public void attachToListView(@NonNull AbsListView listView) {
        attachToListView(listView, null, null);
    }

    public void attachToListView(@NonNull AbsListView listView,
                                 ScrollDirectionListener scrollDirectionListener) {
        attachToListView(listView, scrollDirectionListener, null);
    }

    public void attachToRecyclerView(@NonNull RecyclerView recyclerView) {
        attachToRecyclerView(recyclerView, null, null);
    }

    public void attachToRecyclerView(@NonNull RecyclerView recyclerView,
                                     ScrollDirectionListener scrollDirectionListener) {
        attachToRecyclerView(recyclerView, scrollDirectionListener, null);
    }

    public void attachToScrollView(@NonNull ObservableScrollView scrollView) {
        attachToScrollView(scrollView, null, null);
    }

    public void attachToScrollView(@NonNull ObservableScrollView scrollView,
                                   ScrollDirectionListener scrollDirectionListener) {
        attachToScrollView(scrollView, scrollDirectionListener, null);
    }

    public void attachToListView(@NonNull AbsListView listView,
                                 ScrollDirectionListener scrollDirectionListener,
                                 AbsListView.OnScrollListener onScrollListener) {
        AbsListViewScrollDetectorImpl scrollDetector = new AbsListViewScrollDetectorImpl();
        scrollDetector.setScrollDirectionListener(scrollDirectionListener);
        scrollDetector.setOnScrollListener(onScrollListener);
        scrollDetector.setListView(listView);
        scrollDetector.setScrollThreshold(mScrollThreshold);
        listView.setOnScrollListener(scrollDetector);
    }

    public void attachToRecyclerView(@NonNull RecyclerView recyclerView,
                                     ScrollDirectionListener scrollDirectionlistener,
                                     RecyclerView.OnScrollListener onScrollListener) {
        RecyclerViewScrollDetectorImpl scrollDetector = new RecyclerViewScrollDetectorImpl();
        scrollDetector.setScrollDirectionListener(scrollDirectionlistener);
        scrollDetector.setOnScrollListener(onScrollListener);
        scrollDetector.setScrollThreshold(mScrollThreshold);
        recyclerView.addOnScrollListener(scrollDetector);
    }

    public void attachToScrollView(@NonNull ObservableScrollView scrollView,
                                   ScrollDirectionListener scrollDirectionListener,
                                   ObservableScrollView.OnScrollChangedListener onScrollChangedListener) {
        ScrollViewScrollDetectorImpl scrollDetector = new ScrollViewScrollDetectorImpl();
        scrollDetector.setScrollDirectionListener(scrollDirectionListener);
        scrollDetector.setOnScrollChangedListener(onScrollChangedListener);
        scrollDetector.setScrollThreshold(mScrollThreshold);
        scrollView.setOnScrollChangedListener(scrollDetector);
    }

    private boolean hasLollipopApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    private boolean hasJellyBeanApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    private boolean hasHoneycombApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    private static int darkenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.9f;
        return Color.HSVToColor(hsv);
    }

    private static int lightenColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 1.1f;
        return Color.HSVToColor(hsv);
    }

    private class AbsListViewScrollDetectorImpl extends AbsListViewScrollDetector {
        private ScrollDirectionListener mScrollDirectionListener;
        private AbsListView.OnScrollListener mOnScrollListener;

        private void setScrollDirectionListener(ScrollDirectionListener scrollDirectionListener) {
            mScrollDirectionListener = scrollDirectionListener;
        }

        public void setOnScrollListener(AbsListView.OnScrollListener onScrollListener) {
            mOnScrollListener = onScrollListener;
        }

        @Override
        public void onScrollDown() {
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollDown();
            }
        }

        @Override
        public void onScrollUp() {
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollUp();
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                             int totalItemCount) {
            if (mOnScrollListener != null) {
                mOnScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
            }

            super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrollStateChanged(view, scrollState);
            }

            if (listener != null) {
                listener.onSrcolls(view,scrollState);
            }


            switch (scrollState) {
                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE://空闲状态
                    show();
                    break;
                case AbsListView.OnScrollListener.SCROLL_STATE_FLING://滚动状态
                case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL://触摸后滚动
                    hide();
                    break;
            }
        }
    }

    private class RecyclerViewScrollDetectorImpl extends RecyclerViewScrollDetector {
        private ScrollDirectionListener mScrollDirectionListener;
        private RecyclerView.OnScrollListener mOnScrollListener;

        private void setScrollDirectionListener(ScrollDirectionListener scrollDirectionListener) {
            mScrollDirectionListener = scrollDirectionListener;
        }

        public void setOnScrollListener(RecyclerView.OnScrollListener onScrollListener) {
            mOnScrollListener = onScrollListener;
        }

        @Override
        public void onScrollDown() {
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollDown();
            }
        }

        @Override
        public void onScrollUp() {
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollUp();
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrolled(recyclerView, dx, dy);
            }

            super.onScrolled(recyclerView, dx, dy);
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            if (mOnScrollListener != null) {
                mOnScrollListener.onScrollStateChanged(recyclerView, newState);
            }
            switch (newState) {
                case RecyclerView.SCROLL_STATE_IDLE://空闲状态
                    show();
                    break;
                case RecyclerView.SCROLL_STATE_DRAGGING://触摸后滚动
                case RecyclerView.SCROLL_STATE_SETTLING://滚动状态
                    hide();
                    break;
            }
        }
    }

    private class ScrollViewScrollDetectorImpl extends ScrollViewScrollDetector {
        private ScrollDirectionListener mScrollDirectionListener;

        private ObservableScrollView.OnScrollChangedListener mOnScrollChangedListener;

        private void setScrollDirectionListener(ScrollDirectionListener scrollDirectionListener) {
            mScrollDirectionListener = scrollDirectionListener;
        }

        public void setOnScrollChangedListener(ObservableScrollView.OnScrollChangedListener onScrollChangedListener) {
            mOnScrollChangedListener = onScrollChangedListener;
        }

        @Override
        public void onScrollDown() {
            show();
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollDown();
            }
        }

        @Override
        public void onScrollUp() {
            hide();
            if (mScrollDirectionListener != null) {
                mScrollDirectionListener.onScrollUp();
            }
        }

        @Override
        public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
            if (mOnScrollChangedListener != null) {
                mOnScrollChangedListener.onScrollChanged(who, l, t, oldl, oldt);
            }

            super.onScrollChanged(who, l, t, oldl, oldt);
        }
    }


    ////////listview滚动状态监听//////////////////////
    private ListScrollListener listener;

    public void setListener(ListScrollListener listener) {
        this.listener = listener;
    }

    public interface ListScrollListener {

        void onSrcolls(AbsListView view, int scrollState);
    }

}