package com.rongxun.xqlc.UI;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.rongxun.xqlc.R;


/**
 * An indicator of progress, similar to Android's ProgressBar.
 *
 * @author Todd Davies
 *         <p/>
 *         See MIT-LICENSE.txt for licence details
 */
public class ProgressWheel extends View {

    //Sizes (with defaults)
    private int layoutHeight = 0;
    private int layoutWidth = 0;
    private int fullRadius = 100;
    private int circleRadius = 80;
    private int barLength = 60;
    private int barWidth = 20;
    private int rimWidth = 20;
    private int textSize = 20;
    private float contourSize = 0;

    //Padding (with defaults)
    private int paddingTop = 5;
    private int paddingBottom = 5;
    private int paddingLeft = 5;
    private int paddingRight = 5;

    //Colors (with defaults)
    private int barColor = 0xAA000000;
    private int contourColor = 0xAA000000;
    private int circleColor = 0x00000000;
    private int rimColor = 0xAADDDDDD;
    private int textColor = 0xFF000000;

    //Paints
    private Paint barPaint = new Paint();
    private Paint circlePaint = new Paint();
    private Paint rimPaint = new Paint();
    private Paint textPaint = new Paint();
    private Paint contourPaint = new Paint();

    //Rectangles
    private RectF innerCircleBounds = new RectF();
    private RectF circleBounds = new RectF();
    private RectF circleOuterContour = new RectF();
    private RectF circleInnerContour = new RectF();

    //Animation
    //The amount of pixels to move the bar by on each draw
    private float spinSpeed = 2f;
    //The number of milliseconds to wait in between each draw
    private int delayMillis = 10;
    private float progress = 0;
    boolean isSpinning = false;

    //Other
    private String text = "";
    private String[] splitText = {};

    //决定进度条两端的角度
    boolean c = true;

    /**
     * The constructor for the ProgressWheel
     *
     * @param context
     * @param attrs
     */
    public ProgressWheel(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context.obtainStyledAttributes(attrs,
                R.styleable.ProgressWheel));
    }

    /*
     * When this is called, make the view square.
     * From: http://www.jayway.com/2012/12/12/creating-custom-android-views-part-4-measuring-and-how-to-force-a-view-to-be-square/
     * 
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // The first thing that happen is that we call the superclass
        // implementation of onMeasure. The reason for that is that measuring
        // can be quite a complex process and calling the super method is a
        // convenient way to get most of this complexity handled.
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // We can’t use getWidth() or getHight() here. During the measuring
        // pass the view has not gotten its final size yet (this happens first
        // at the start of the layout pass) so we have to use getMeasuredWidth()
        // and getMeasuredHeight().
        int size = 0;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int widthWithoutPadding = width - getPaddingLeft() - getPaddingRight();
        int heightWithoutPadding = height - getPaddingTop() - getPaddingBottom();

        // Finally we have some simple logic that calculates the size of the view 
        // and calls setMeasuredDimension() to set that size.
        // Before we compare the width and height of the view, we remove the padding, 
        // and when we set the dimension we add it back again. Now the actual content 
        // of the view will be square, but, depending on the padding, the total dimensions 
        // of the view might not be.
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        if (heightMode != MeasureSpec.UNSPECIFIED && widthMode != MeasureSpec.UNSPECIFIED) {
            if (widthWithoutPadding > heightWithoutPadding) {
                size = heightWithoutPadding;
            } else {
                size = widthWithoutPadding;
            }
        } else {
            size = Math.max(heightWithoutPadding, widthWithoutPadding);
        }


        // If you override onMeasure() you have to call setMeasuredDimension(). 
        // This is how you report back the measured size.  If you don’t call
        // setMeasuredDimension() the parent will throw an exception and your 
        // application will crash.        
        // We are calling the onMeasure() method of the superclass so we don’t 
        // actually need to call setMeasuredDimension() since that takes care 
        // of that. However, the purpose with overriding onMeasure() was to 
        // change the default behaviour and to do that we need to call 
        // setMeasuredDimension() with our own values.
        setMeasuredDimension(
                size + getPaddingLeft() + getPaddingRight(),
                size + getPaddingTop() + getPaddingBottom());
    }

    /**
     * Use onSizeChanged instead of onAttachedToWindow to get the dimensions of the view,
     * because this method is called after measuring the dimensions of MATCH_PARENT & WRAP_CONTENT.
     * Use this dimensions to setup the bounds and paints.
     */
    @Override
    protected void onSizeChanged(int newWidth, int newHeight, int oldWidth, int oldHeight) {
        super.onSizeChanged(newWidth, newHeight, oldWidth, oldHeight);
        layoutWidth = newWidth;
        layoutHeight = newHeight;
        setupBounds();
        setupPaints();
        invalidate();
    }

    /**
     * Set the properties of the paints we're using to
     * draw the progress wheel
     */
    private void setupPaints() {
        barPaint.setColor(barColor);
        barPaint.setAntiAlias(true);
        barPaint.setStyle(Style.STROKE);
        barPaint.setStrokeWidth(barWidth);
        barPaint.setStrokeCap(Paint.Cap.BUTT);

        rimPaint.setColor(rimColor);
        rimPaint.setAntiAlias(true);
        rimPaint.setStyle(Style.STROKE);
        rimPaint.setStrokeWidth(rimWidth);

        circlePaint.setColor(circleColor);
        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Style.FILL);

        textPaint.setColor(textColor);
        textPaint.setStyle(Style.FILL);
        textPaint.setAntiAlias(true);
        textPaint.setTextSize(textSize);

        contourPaint.setColor(contourColor);
        contourPaint.setAntiAlias(true);
        contourPaint.setStyle(Style.STROKE);
        contourPaint.setStrokeWidth(contourSize);
    }

    /**
     * Set the bounds of the component
     */
    private void setupBounds() {
        // Width should equal to Height, find the min value to setup the circle
        int minValue = Math.min(layoutWidth, layoutHeight);

        // Calc the Offset if needed
        int xOffset = layoutWidth - minValue;
        int yOffset = layoutHeight - minValue;

        // Add the offset
        paddingTop = this.getPaddingTop() + (yOffset / 2);
        paddingBottom = this.getPaddingBottom() + (yOffset / 2);
        paddingLeft = this.getPaddingLeft() + (xOffset / 2);
        paddingRight = this.getPaddingRight() + (xOffset / 2);

        int width = getWidth();
        int height = getHeight();

        innerCircleBounds = new RectF(
                paddingLeft + (1.5f * barWidth),
                paddingTop + (1.5f * barWidth),
                width - paddingRight - (1.5f * barWidth),
                height - paddingBottom - (1.5f * barWidth));
        circleBounds = new RectF(
                paddingLeft + barWidth,
                paddingTop + barWidth,
                width - paddingRight - barWidth,
                height - paddingBottom - barWidth);
        circleInnerContour = new RectF(
                circleBounds.left + (rimWidth / 2.0f) + (contourSize / 2.0f),
                circleBounds.top + (rimWidth / 2.0f) + (contourSize / 2.0f),
                circleBounds.right - (rimWidth / 2.0f) - (contourSize / 2.0f),
                circleBounds.bottom - (rimWidth / 2.0f) - (contourSize / 2.0f));
        circleOuterContour = new RectF(
                circleBounds.left - (rimWidth / 2.0f) - (contourSize / 2.0f),
                circleBounds.top - (rimWidth / 2.0f) - (contourSize / 2.0f),
                circleBounds.right + (rimWidth / 2.0f) + (contourSize / 2.0f),
                circleBounds.bottom + (rimWidth / 2.0f) + (contourSize / 2.0f));

        fullRadius = (width - paddingRight - barWidth) / 2;
        circleRadius = (fullRadius - barWidth) + 1;
    }

    /**
     * Parse the attributes passed to the view from the XML
     *
     * @param a the attributes to parse
     */
    private void parseAttributes(TypedArray a) {
        barWidth = (int) a.getDimension(R.styleable.ProgressWheel_pwBarWidth, barWidth);
        rimWidth = (int) a.getDimension(R.styleable.ProgressWheel_pwRimWidth, rimWidth);
        spinSpeed = (int) a.getDimension(R.styleable.ProgressWheel_pwSpinSpeed, spinSpeed);
        barLength = (int) a.getDimension(R.styleable.ProgressWheel_pwBarLength, barLength);

        delayMillis = a.getInteger(R.styleable.ProgressWheel_pwDelayMillis, delayMillis);
        if (delayMillis < 0) {
            delayMillis = 10;
        }

        // Only set the text if it is explicitly defined
        if (a.hasValue(R.styleable.ProgressWheel_pwText)) {
            setText(a.getString(R.styleable.ProgressWheel_pwText));
        }

        barColor = a.getColor(R.styleable.ProgressWheel_pwBarColor, barColor);
        textColor = a.getColor(R.styleable.ProgressWheel_pwTextColor, textColor);
        rimColor = a.getColor(R.styleable.ProgressWheel_pwRimColor, rimColor);
        circleColor = a.getColor(R.styleable.ProgressWheel_pwCircleColor, circleColor);
        contourColor = a.getColor(R.styleable.ProgressWheel_pwContourColor, contourColor);

        textSize = (int) a.getDimension(R.styleable.ProgressWheel_pwTextSize, textSize);
        contourSize = a.getDimension(R.styleable.ProgressWheel_pwContourSize, contourSize);

        a.recycle();
    }

    //----------------------------------
    //Animation stuff
    //----------------------------------

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Draw the inner circle
        canvas.drawArc(innerCircleBounds, 360, 360, false, circlePaint);
        //Draw the rim
        canvas.drawArc(circleBounds, 360, 360, false, rimPaint);
        //不画圆的边界线，所以屏蔽掉
//        canvas.drawArc(circleOuterContour, 360, 360, false, contourPaint);
//        canvas.drawArc(circleInnerContour, 360, 360, false, contourPaint);
        //Draw the bar
        if (c) {

            if (progress >= 335.0) {
                barPaint.setStrokeCap(Paint.Cap.BUTT);
            } else {
                barPaint.setStrokeCap(Paint.Cap.ROUND);
            }
        } else {
            //默认圆角
            barPaint.setStrokeCap(Paint.Cap.ROUND);
        }

        if (isSpinning) {
            canvas.drawArc(circleBounds, progress - 90, barLength, false, barPaint);
        } else {
            canvas.drawArc(circleBounds, -90, progress, false, barPaint);
        }
        //Draw the text (attempts to center it horizontally and vertically)
        float textHeight = textPaint.descent() - textPaint.ascent();
        float verticalTextOffset = (textHeight / 2) - textPaint.descent();

        for (String line : splitText) {
            float horizontalTextOffset = textPaint.measureText(line) / 2;
            canvas.drawText(
                    line,
                    this.getWidth() / 2 - horizontalTextOffset,
                    this.getHeight() / 2 + verticalTextOffset,
                    textPaint);
        }
        if (isSpinning) {
            scheduleRedraw();
        }
    }

    private void scheduleRedraw() {
        progress += spinSpeed;
        if (progress > 360) {
            progress = 0;
        }
        postInvalidateDelayed(delayMillis);
    }

    /**
     * Check if the wheel is currently spinning
     */
    public boolean isSpinning() {
        return isSpinning;
    }

    /**
     * Reset the count (in increment mode)
     */
    public void resetCount() {
        progress = 0;
        setText("0%");
        invalidate();
    }

    /**
     * Turn off startSpinning mode
     */
    public void stopSpinning() {
        isSpinning = false;
        progress = 0;
        postInvalidate();
    }


    /**
     * Puts the view on spin mode
     */
    public void startSpinning() {
        isSpinning = true;
        postInvalidate();
    }

    /**
     * Increment the progress by 1 (of 360)
     */
    public void incrementProgress() {
        incrementProgress(1);
    }

    public void incrementProgress(int amount) {
        isSpinning = false;
        progress += amount;
        if (progress > 360)
            progress %= 360;
        postInvalidate();
    }


    /**
     * Set the progress to a specific value
     */
    public void setProgress(float i) {
        isSpinning = false;
        progress = i;
        postInvalidate();
    }

    /**
     * @param c 是否开启动态的改变进度条两端的角度（圆角，非圆角变化）
     * @param i
     */
    public void setProgress(boolean c, float i) {
        this.c = c;
        isSpinning = false;
        progress = i;
        postInvalidate();
    }

    /**
     * 延时 减速画进度条
     *
     * @param i
     */
    public void setDelayAnimationProgress(float i) {
        isSpinning = false;
        progress = i;
        final ValueAnimator animator = ValueAnimator.ofFloat(0, progress);
        animator.setDuration(1100);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(final Animator animation) {
                // TODO: 2017/7/5 0005
            }

            @Override
            public void onAnimationEnd(final Animator animation) {
                // TODO: 2017/7/5 0005

            }
        });

        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(final ValueAnimator animation) {
                float f = (float) animation.getAnimatedValue();
                int p = (int) ((f / 360.0f) * 100);
                setText(p + "%");
                setProgress(f);
            }
        });

        animator.start();
    }

    //----------------------------------
    //Getters + setters
    //----------------------------------

    /**
     * Set the text in the progress bar
     * Doesn't invalidate the view
     *
     * @param text the text to show ('\n' constitutes a new line)
     */
    public void setText(String text) {
        this.text = text;
        splitText = this.text.split("\n");
    }

    public int getCircleRadius() {
        return circleRadius;
    }

    public void setCircleRadius(int circleRadius) {
        this.circleRadius = circleRadius;
    }

    public int getBarLength() {
        return barLength;
    }

    public void setBarLength(int barLength) {
        this.barLength = barLength;
    }

    public int getBarWidth() {
        return barWidth;
    }

    public void setBarWidth(int barWidth) {
        this.barWidth = barWidth;

        if (this.barPaint != null) {
            this.barPaint.setStrokeWidth(this.barWidth);
        }
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;

        if (this.textPaint != null) {
            this.textPaint.setTextSize(this.textSize);
        }
    }

    public int getPaddingTop() {
        return paddingTop;
    }

    public void setPaddingTop(int paddingTop) {
        this.paddingTop = paddingTop;
    }

    public int getPaddingBottom() {
        return paddingBottom;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.paddingBottom = paddingBottom;
    }

    public int getPaddingLeft() {
        return paddingLeft;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.paddingLeft = paddingLeft;
    }

    public int getPaddingRight() {
        return paddingRight;
    }

    public void setPaddingRight(int paddingRight) {
        this.paddingRight = paddingRight;
    }

    public int getBarColor() {
        return barColor;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;

        if (this.barPaint != null) {
            this.barPaint.setColor(this.barColor);
        }
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;

        if (this.circlePaint != null) {
            this.circlePaint.setColor(this.circleColor);
        }
    }

    public int getRimColor() {
        return rimColor;
    }

    public void setRimColor(int rimColor) {
        this.rimColor = rimColor;

        if (this.rimPaint != null) {
            this.rimPaint.setColor(this.rimColor);
        }
    }

    public Shader getRimShader() {
        return rimPaint.getShader();
    }

    public void setRimShader(Shader shader) {
        this.rimPaint.setShader(shader);
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;

        if (this.textPaint != null) {
            this.textPaint.setColor(this.textColor);
        }
    }

    public float getSpinSpeed() {
        return spinSpeed;
    }

    public void setSpinSpeed(float spinSpeed) {
        this.spinSpeed = spinSpeed;
    }

    public int getRimWidth() {
        return rimWidth;
    }

    public void setRimWidth(int rimWidth) {
        this.rimWidth = rimWidth;

        if (this.rimPaint != null) {
            this.rimPaint.setStrokeWidth(this.rimWidth);
        }
    }

    public int getDelayMillis() {
        return delayMillis;
    }

    public void setDelayMillis(int delayMillis) {
        this.delayMillis = delayMillis;
    }

    public int getContourColor() {
        return contourColor;
    }

    public void setContourColor(int contourColor) {
        this.contourColor = contourColor;

        if (contourPaint != null) {
            this.contourPaint.setColor(this.contourColor);
        }
    }

    public float getContourSize() {
        return this.contourSize;
    }

    public void setContourSize(float contourSize) {
        this.contourSize = contourSize;

        if (contourPaint != null) {
            this.contourPaint.setStrokeWidth(this.contourSize);
        }
    }

    public int getProgress() {
        return (int) progress;
    }
}
