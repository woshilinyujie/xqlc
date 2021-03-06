package com.rongxun.xqlc.Lock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.PathShape;
import android.view.View;

public class GestureLockView extends View
{
	private static final String TAG = "GestureLockView";
	private int answerLength;
	private Path path;

	public void setAnswerLength(int answerLength) {
		this.answerLength = answerLength;
	}

	/**
	 * GestureLockView的三种状态
	 */
	enum Mode
	{
		STATUS_NO_FINGER, STATUS_FINGER_ON, STATUS_FINGER_UP;
	}

	/**
	 * GestureLockView的当前状态
	 */
	private Mode mCurrentStatus = Mode.STATUS_NO_FINGER;
	
	/**
	 * 宽度
	 */
	private int mWidth;
	/**
	 * 高度
	 */
	private int mHeight;
	/**
	 * 外圆半径
	 */
	private int mRadius;
	/**
	 * 画笔的宽度
	 */
	private int mStrokeWidth = 2;

	/**
	 * 圆心坐标
	 */
	private int mCenterX;
	private int mCenterY;
	private Paint mPaint;

	/**
	 * 箭头（小三角最长边的一半长度 = mArrawRate * mWidth / 2 ）
	 */
	private float mArrowRate = 0.25f;
	private int mArrowDegree = -1;
	private Path mArrowPath;
	/**
	 * 内圆的半径 = mInnerCircleRadiusRate * mRadus
	 * 
	 */
	private float mInnerCircleRadiusRate = 0.25F;

	/**
	 * 四个颜色，可由用户自定义，初始化时由GestureLockViewGroup传入
	 */
	private int mColorNoFingerInner;
	private int mColorNoFingerOutter;
	private int mColorFingerOn;
	private int mColorFingerUp;

	public GestureLockView(Context context , int colorNoFingerInner , int colorNoFingerOutter , int colorFingerOn , int colorFingerUp )
	{
		super(context);
		this.mColorNoFingerInner = colorNoFingerInner;
		this.mColorNoFingerOutter = colorNoFingerOutter;
		this.mColorFingerOn = colorFingerOn;
		this.mColorFingerUp = colorFingerUp;
		mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mArrowPath = new Path();

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
	{
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		path = new Path();
		mWidth = MeasureSpec.getSize(widthMeasureSpec);
		mHeight = MeasureSpec.getSize(heightMeasureSpec);

		// 取长和宽中的小值
		mWidth = mWidth < mHeight ? mWidth : mHeight;
		mRadius = mCenterX = mCenterY = mWidth / 2;
		mRadius -= mStrokeWidth / 2;

		// 绘制三角形，初始时是个默认箭头朝上的一个等腰三角形，用户绘制结束后，根据由两个GestureLockView决定需要旋转多少度
		float mArrowLength = mWidth / 2 * mArrowRate;
		mArrowPath.moveTo(mWidth / 2, mStrokeWidth + 2);
		mArrowPath.lineTo(mWidth / 2 - mArrowLength, mStrokeWidth + 2
				+ mArrowLength);
		mArrowPath.lineTo(mWidth / 2 + mArrowLength, mStrokeWidth + 2
				+ mArrowLength);
		mArrowPath.setFillType(Path.FillType.WINDING);

	}

	@Override
	protected void onDraw(Canvas canvas)
	{

		switch (mCurrentStatus)
		{
		case STATUS_FINGER_ON:

			// 绘制外圆 手指触碰
			mPaint.setStyle(Style.STROKE);
			mPaint.setColor(mColorFingerOn);
			mPaint.setStrokeWidth(3);
			canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
			// 绘制内圆
			mPaint.setStyle(Style.FILL);
//			canvas.drawCircle(mCenterX, mCenterY, mRadius
//					* mInnerCircleRadiusRate, mPaint);
			path.reset();
			path.moveTo((mWidth/2-mWidth/6),mHeight/2);
			path.lineTo(mWidth/2,(mHeight/2-mHeight/6));
			path.lineTo((mWidth/2+mWidth/6),mHeight/2);
			path.lineTo(mWidth/2,(mHeight/2+mHeight/6));
			path.lineTo((mWidth/2-mWidth/6),mHeight/2);
			path.setFillType(Path.FillType.WINDING);
			mPaint.setStyle(Style.FILL);
			canvas.drawPath(path,mPaint);
			break;
		case STATUS_FINGER_UP:
			// 绘制外圆  抬起
			mPaint.setColor(mColorFingerUp);
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeWidth(3);
			canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
			// 绘制内圆
//			mPaint.setStyle(Style.FILL);
//			canvas.drawCircle(mCenterX, mCenterY, mRadius
//					* mInnerCircleRadiusRate, mPaint);
			path.reset();
			path.moveTo((mWidth/2-mWidth/6),mHeight/2);
			path.lineTo(mWidth/2,(mHeight/2-mHeight/6));
			path.lineTo((mWidth/2+mWidth/6),mHeight/2);
			path.lineTo(mWidth/2,(mHeight/2+mHeight/6));
			path.lineTo((mWidth/2-mWidth/6),mHeight/2);
			path.setFillType(Path.FillType.WINDING);
			mPaint.setStyle(Style.FILL);

			canvas.drawPath(path, mPaint);


			drawArrow(canvas);

			break;

		case STATUS_NO_FINGER:

			// 绘制外圆  没有手指触碰
			if(answerLength<4 ){
				mPaint.setStyle(Style.STROKE);
				mPaint.setStrokeWidth(3);
				mPaint.setColor(Color.parseColor("#999999"));
				canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
				// 绘制内圆
//				mPaint.setColor(Color.parseColor("#999999"));
//				mPaint.setStyle(Style.FILL);
//				canvas.drawCircle(mCenterX, mCenterY, mRadius
//						* mInnerCircleRadiusRate, mPaint);
				path.reset();
				path.moveTo((mWidth/2-mWidth/6),mHeight/2);
				path.lineTo(mWidth/2,(mHeight/2-mHeight/6));
				path.lineTo((mWidth/2+mWidth/6),mHeight/2);
				path.lineTo(mWidth/2,(mHeight/2+mHeight/6));
				path.lineTo((mWidth/2-mWidth/6),mHeight/2);
				canvas.drawPath(path,mPaint);

			}else {
				mPaint.setStyle(Style.STROKE);
				mPaint.setStrokeWidth(3);
				mPaint.setColor(mColorNoFingerInner);
				canvas.drawCircle(mCenterX, mCenterY, mRadius, mPaint);
				// 绘制内圆
//				mPaint.setColor(mColorNoFingerInner);
//				mPaint.setStyle(Style.FILL);
//				canvas.drawCircle(mCenterX, mCenterY, mRadius
//						* mInnerCircleRadiusRate, mPaint);
				path.reset();
				path.moveTo((mWidth/2-mWidth/6),mHeight/2);
				path.lineTo(mWidth/2,(mHeight/2-mHeight/6));
				path.lineTo((mWidth/2+mWidth/6),mHeight/2);
				path.lineTo(mWidth/2,(mHeight/2+mHeight/6));
				path.lineTo((mWidth/2-mWidth/6),mHeight/2);
				canvas.drawPath(path,mPaint);

			}
			break;

		}

	}

	/**
	 * 绘制箭头
	 * @param canvas
	 */
	private void drawArrow(Canvas canvas)
	{
		if (mArrowDegree != -1)
		{
			mPaint.setStyle(Style.FILL);

			canvas.save();
			canvas.rotate(mArrowDegree, mCenterX, mCenterY);
			canvas.drawPath(mArrowPath, mPaint);

			canvas.restore();
		}

	}

	/**
	 * 设置当前模式并重绘界面
	 * 
	 * @param mode
	 */
	public void setMode(Mode mode)
	{
		this.mCurrentStatus = mode;
		invalidate();
	}

	public void setArrowDegree(int degree)
	{
		this.mArrowDegree = degree;
	}

	public int getArrowDegree()
	{
		return this.mArrowDegree;
	}
}
