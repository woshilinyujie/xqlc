package com.rongxun.xqlc.UI.tipprogress;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.rongxun.xqlc.R;


public class TipsProgressBar extends View {

    private final Paint mBubblePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//提示框画笔
    private final Paint mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);//三角形画笔
    private final Paint mLinesPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//进度条画笔
    private final Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);//文本画笔

    private final float m1Dip;
    private final float m1Sp;
    private int mProgress;//当前进度
    private int mMax;//总进度

    private int mPrimaryColor;//已完成的颜色
    private int mSecondaryColor;//未完成颜色
    private ProgressFormatter mFormatter;

    private final Rect mBounds = new Rect();//空的矩形
    private final Path mBubble = new Path();//线
    private final Path mTriangle = new Path();

    private static final Interpolator INTERPOLATOR = new DampingInterpolator(60);
    private ValueAnimator mAnimator;
    private float mBounceX;
    private int mStartProgress;
    private boolean mDeferred;

    private int textSize;//字体大小
    private int textColor;//字体颜色
    private int tipBack;//提示框背景


    public TipsProgressBar(final Context context) {
        this(context, null);
    }

    public TipsProgressBar(final Context context, final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TipsProgressBar(
            final Context context,
            final AttributeSet attrs,
            final int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //获得屏幕像素点
        m1Dip = getResources().getDisplayMetrics().density;
        m1Sp = getResources().getDisplayMetrics().scaledDensity;

        /////////////////默认的初始化///////////////
        int max = 0;//总进度
        int progress = 0;//当前进度

        float width = dips(8);//进度条默认宽度

        int primaryColor = 0xFF009688;//已完成进度的颜色
        int secondaryColor = 0xFFDADADA;//未完成进度的颜色

        int txtSize = 12;
        int txtColor = 0xFFFFFFFF;
        int tipBackGround = 0xFF009688;
        //android5.0以上 颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            final TypedValue out = new TypedValue();

            context.getTheme().resolveAttribute(R.attr.colorControlActivated, out, true);
            primaryColor = out.data;
            context.getTheme().resolveAttribute(R.attr.colorControlHighlight, out, true);
            secondaryColor = out.data;
        }

        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.TipsProgressBar, defStyleAttr, 0);

        if (a != null) {
            max = a.getInt(R.styleable.TipsProgressBar_ropeMax, max);
            progress = a.getInt(R.styleable.TipsProgressBar_ropeProgress, progress);
            primaryColor = a.getColor(R.styleable.TipsProgressBar_ropePrimaryColor, primaryColor);
            secondaryColor = a.getColor(R.styleable.TipsProgressBar_ropeSecondaryColor, secondaryColor);
            width = a.getDimension(R.styleable.TipsProgressBar_ropeStrokeWidth, width);
            textColor = a.getColor(R.styleable.TipsProgressBar_ropeTextColor, txtColor);
            textSize = a.getDimensionPixelSize(R.styleable.TipsProgressBar_ropeTextSize, txtSize);
            tipBack = a.getColor(R.styleable.TipsProgressBar_ropeTipBackground, tipBackGround);

            a.recycle();
        }

        mPrimaryColor = primaryColor;
        mSecondaryColor = secondaryColor;

        ///////////////////////////////初始化画笔///////////////////////////////////
        //进度条
        mLinesPaint.setStrokeWidth(width);//宽度
        mLinesPaint.setStyle(Paint.Style.STROKE);//空心
        mLinesPaint.setStrokeCap(Paint.Cap.SQUARE);//两端圆角
        //提示框
        mBubblePaint.setColor(tipBack);//背景白色
        mBubblePaint.setStyle(Paint.Style.FILL);//填充
        mBubblePaint.setPathEffect(new CornerPathEffect(dips(2)));//初始非圆角
        //三角形
        mTrianglePaint.setColor(tipBack);
        mTrianglePaint.setStyle(Paint.Style.FILL);
        mTrianglePaint.setPathEffect(new CornerPathEffect(dips(0)));//初始非圆角

        //字体
        mTextPaint.setColor(textColor);//黑色
        mTextPaint.setTextSize(textSize);//大小
        mTextPaint.setTextAlign(Paint.Align.CENTER);//居中
//        mTextPaint.setTypeface(Typeface.create("sans-serif-condensed-light", 0));//字体风格

        setMax(max);//设置总进度
        setProgress(progress);//设置当前进度
        setLayerType(LAYER_TYPE_SOFTWARE, null);//开启硬件加速，使绘制更平滑
    }


    @Override
    protected synchronized void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {


        // Recalculate how tall the text needs to be, width is ignored
        //文本显示的内容
        final String progress = getBubbleText();
        mTextPaint.getTextBounds(progress, 0, progress.length(), mBounds);//以矩形为基准测量文本的 大小

        final int bubbleHeight = (int) Math.ceil(getBubbleVerticalDisplacement());//三角指示器+提示框+pading

        final float strokeWidth = getStrokeWidth();//bar 高度
        final int dh = (int) Math.ceil(getPaddingTop() + getPaddingBottom() + strokeWidth);
        setMeasuredDimension(
                getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                resolveSizeAndState(dh + bubbleHeight, heightMeasureSpec, 0));

        // 三角形指示器绘制1(原本是0，我们让他上移3，可以避免圆角缝隙)
        mTriangle.reset();
        mTriangle.moveTo(-getTriangleWidth() / 2f, -dips(1));
        mTriangle.lineTo(getTriangleWidth() / 2f, -dips(1));
        mTriangle.lineTo(0, getTriangleHeight());
        mTriangle.lineTo(-getTriangleWidth() / 2f, -dips(1));
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    protected synchronized void onDraw(final Canvas canvas) {

        final float radius = 0;

        final float bubbleDisplacement = getBubbleVerticalDisplacement();//提示框+三角形 high
        final float top = getPaddingTop() + getStrokeWidth() / 2 + bubbleDisplacement;

        final float left = getPaddingLeft() + radius;
        final float end = getWidth() - getPaddingRight() - radius;

        final float max = getMax();
        final float offset = (max == 0) ? 0 : (getProgress() / max);//进度比
        final float topHeight = getDistTopHeight();//距顶部的高度
        //第二条bar 所占百分比的具体值
        final float progressEnd =
                clamp(lerp(left, end, offset) + (mBounceX * perp(offset)), left, end);

        //画出总进度
        mLinesPaint.setColor(mSecondaryColor);
        canvas.drawLine(progressEnd, top, end, top, mLinesPaint);
        //画出已完成
        mLinesPaint.setColor(mPrimaryColor);
        if (progressEnd == left) {
            //起始位置的小红点
            mLinesPaint.setStyle(Paint.Style.FILL);
//            canvas.drawCircle(left, top, getStrokeWidth() / 2, mLinesPaint);
            mLinesPaint.setStyle(Paint.Style.STROKE);

        } else {
            //修正
            float a=progressEnd - getStrokeWidth() / 2;
            canvas.drawLine(left, top, progressEnd - getStrokeWidth() / 2, top, mLinesPaint);
        }

        //绘制文字
        final String progress = getBubbleText();
        mTextPaint.getTextBounds(progress, 0, progress.length(), mBounds);

        //绘制提示框
        final float bubbleWidth = getBubbleWidth();
        final float bubbleHeight = getBubbleHeight();
        mBubble.reset();
        mBubble.addRect(0, 0, bubbleWidth, bubbleHeight, Path.Direction.CW);

        final float bubbleTop = Math.max(topHeight, 0);
        final float bubbleLeft = clamp(progressEnd - (bubbleWidth / 2), 0,getWidth() - bubbleWidth);
        final int saveCount = canvas.save();
        canvas.translate(bubbleLeft, bubbleTop);
        canvas.drawPath(mBubble, mBubblePaint);

        //画三角形提示器
        final float triangleTop = bubbleHeight;
        final float triangleLeft = clamp(
                progressEnd - bubbleLeft,
                0,
                getWidth() - getTriangleWidth());

        mTriangle.offset(triangleLeft, triangleTop);
        canvas.drawPath(mTriangle, mTrianglePaint);
        mTriangle.offset(-triangleLeft, -triangleTop);

        //文字
        final float textX = bubbleWidth / 2;
        final float textY = bubbleHeight - dips(3);

        canvas.drawText(progress, textX, textY, mTextPaint);

        canvas.restoreToCount(saveCount);
    }

    /**
     * 进度条和文本框之间的距离
     *
     * @return
     */
    private float getDistTopHeight() {
        final float max = getMax();
        final float offset = (max == 0) ? 0 : (getProgress() / max);
        return perp(offset);
    }

    private float getBubbleVerticalDisplacement() {
        return getBubbleMargin() + getBubbleHeight() + getTriangleHeight();
    }

    /**
     * 提示框mar
     *
     * @return
     */
    public float getBubbleMargin() {
        return dips(0);
    }

    /**
     * 提示框 宽
     *
     * @return
     */
    public float getBubbleWidth() {
        return mBounds.width() + /* padding */ dips(12);
    }

    /**
     * 高
     *
     * @return
     */
    public float getBubbleHeight() {
        return mBounds.height() + /* padding */ dips(6);
    }

    /**
     * 三角形指示器宽
     *
     * @return
     */
    public float getTriangleWidth() {
        return dips(10);
    }

    /**
     * 高
     *
     * @return
     */
    public float getTriangleHeight() {
        return dips(6);
    }

    public String getBubbleText() {
        if (mFormatter != null) {
            return mFormatter.getFormattedText(getProgress(), getMax());

        } else {
            final int progress = (int) (100 * getProgress() / (float) getMax());
            return progress + "%";
        }
    }

    public void defer() {
        if (!mDeferred) {
            mDeferred = true;
            mStartProgress = getProgress();
        }
    }

    public void endDefer() {
        if (mDeferred) {
            mDeferred = false;
//            bounceAnimation(mStartProgress);
        }
    }

    public synchronized void setProgress(int progress) {
        //防止数据错乱  校准
        progress = (int) clamp(progress, 0, getMax());
        if (progress == mProgress) {
            return;
        }

//        if (!mDeferred) {
//            bounceAnimation(progress);
//        }
        mProgress = progress;
        postInvalidate();
    }

    public void animateProgress(final int progress) {
        // Speed of animation is interpolated from 0 --> MAX in 2s
        // Minimum time duration is 500ms because anything faster than that is waaaay too quick
        final int startProgress = getProgress();
        final int endProgress = (int) clamp(progress, 0, getMax());
        final int diff = Math.abs(getProgress() - endProgress);
        final long duration = Math.max(500L, (long) (2000L * (diff / (float) getMax())));

        final ValueAnimator animator = ValueAnimator.ofInt(getProgress(), endProgress);
        animator.setDuration(1000);
        animator.setInterpolator(new AccelerateInterpolator());//逐渐快
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(final Animator animation) {
                defer();
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                endDefer();
            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                setProgress((Integer) animation.getAnimatedValue());
            }
        });

        animator.start();
    }

    /**
     * 左右摇摆的动画
     *
     * @param startProgress
     */
    private void bounceAnimation(final int startProgress) {
        // Moving the progress by at least 1/4 of the total distance will invoke
        // the "max" possible slack bouncing at the end progress value
        final int diff = Math.abs(startProgress - mProgress);
        final float diffPercent = Math.min(1f, 4 * diff / (float) getMax());
        if (mAnimator != null) {
            mAnimator.cancel();
        }

//        mAnimator = ValueAnimator.ofFloat(0, diffPercent * getTriangleWidth());
        //固定长度
        mAnimator = ValueAnimator.ofFloat(0, 30);
        mAnimator.setInterpolator(INTERPOLATOR);
        mAnimator.setDuration(2000L);
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                mBounceX = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mAnimator.start();
            }
        }, 1000);

    }

    public int getProgress() {
        return mProgress;
    }

    public void setMax(int max) {
        max = Math.max(0, max);

        if (max != mMax) {
            mMax = max;
            if (mProgress > max) {
                mProgress = max;
            }

            postInvalidate();
        }
    }

    public int getMax() {
        return mMax;
    }

    public void setPrimaryColor(final int color) {
        mPrimaryColor = color;

        invalidate();
    }

    public int getPrimaryColor() {
        return mPrimaryColor;
    }

    public void setSecondaryColor(final int color) {
        mSecondaryColor = color;

        invalidate();
    }

    public int getSecondaryColor() {
        return mSecondaryColor;
    }

    public void setStrokeWidth(final float width) {
        mLinesPaint.setStrokeWidth(width);

        requestLayout();
        invalidate();
    }

    public float getStrokeWidth() {
        return mLinesPaint.getStrokeWidth();
    }

    public void setTypeface(final Typeface typeface) {
        mTextPaint.setTypeface(typeface);

        requestLayout();
        invalidate();
    }

    public void setTextPaint(final Paint paint) {
        mTextPaint.set(paint);

        requestLayout();
        invalidate();
    }

    /**
     * Return a copy so that fields can only be modified through {@link #setTextPaint}
     */
    public Paint getTextPaint() {
        return new Paint(mTextPaint);
    }

    public void setProgressFormatter(final ProgressFormatter formatter) {
        mFormatter = formatter;

        requestLayout();
        invalidate();
    }

    /**
     * @param value
     * @param min
     * @param max
     * @return
     */
    private float clamp(final float value, final float min, final float max) {
        return Math.max(min, Math.min(max, value));
    }

    //计算进度百分比在屏幕上 的数值
    private float perp(float t) {
        // eh, could be more mathematically accurate to use a catenary function,
        // but the max difference between the two is only 0.005
        return (float) (-Math.pow(2 * t - 1, 2) + 1);
    }

    private float lerp(float v0, float v1, float t) {
        return (t == 1) ? v1 : (v0 + t * (v1 - v0));
    }

    /**
     * dp转为px
     *
     * @param dips
     * @return
     */
    private float dips(final float dips) {
        return dips * m1Dip;
    }

    /**
     * sp转为px
     *
     * @param sp
     * @return
     */
    private float sp(final int sp) {
        return sp * m1Sp;
    }

}