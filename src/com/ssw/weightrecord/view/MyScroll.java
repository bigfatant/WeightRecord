package com.ssw.weightrecord.view;

import com.ssw.weightrecord.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

public class MyScroll extends ViewGroup {

	private static final int SNAP_VELOCITY = 600;
	private static final String TAG = "ScrollLayout";
	public boolean attr1;
	public int attr2;
	private Scroller mScroller; // 滑动控制

	private enum ScrollOrientation {
		HORIZONTAL, VERTICAL
	};

	private ScrollOrientation so;

	/**
	 * 得到属性值
	 * 
	 * @param context
	 * @param attrs
	 */
	private void getAttrs(Context context, AttributeSet attrs) {
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.layout_custom_orientation);
		attr1 = ta
				.getBoolean(R.styleable.layout_custom_orientation_attr1, true);
		attr2 = ta.getInt(R.styleable.layout_custom_orientation_attr2, 0);
		so = ScrollOrientation.values()[ta.getInt(
				R.styleable.layout_custom_orientation_orientation, 1)];
		ta.recycle();
	}

	public MyScroll(Context context) {
		super(context);
		init(context);
	}

	public MyScroll(Context context, AttributeSet attrs) {
		super(context, attrs);
		getAttrs(context, attrs);
		init(context);
	}

	public MyScroll(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		getAttrs(context, attrs);
		init(context);
	}

	private void init(Context context) {
		mCurScreen = 0;
		mScroller = new Scroller(context);
	}

	// 向右滚动
	private void scrollHorizontal() {
		int childLeft = 0;
		final int childCount = getChildCount();

		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				final int childWidth = childView.getMeasuredWidth();
				childView.layout(childLeft, 0, childLeft + childWidth,
						childView.getMeasuredHeight());
				childLeft += childWidth;
			}
		}
	}

	// 向下滚动
	private void scrollVertical() {
		int childTop = 0;
		final int childCount = getChildCount();

		for (int i = 0; i < childCount; i++) {
			final View childView = getChildAt(i);
			if (childView.getVisibility() != View.GONE) {
				final int childHeight = childView.getMeasuredHeight();
				childView.layout(0, childTop, childView.getMeasuredWidth(),
						childHeight + childTop);
				childTop += childHeight;
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int r, int t, int l, int b) {
		if (!changed) {
			return;
		}
		switch (so) {
		case HORIZONTAL:
			scrollHorizontal();
			break;
		case VERTICAL:
			scrollVertical();
			break;
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		final int width = MeasureSpec.getSize(widthMeasureSpec);
		final int height = MeasureSpec.getSize(heightMeasureSpec);
		// final int widthMode = MeasureSpec.getMode(widthMeasureSpec);

		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}

		switch (so) {
		case VERTICAL:
			scrollTo(0, height * mCurScreen);
			break;

		case HORIZONTAL:
			scrollTo(mCurScreen * width, 0);
			break;
		}

	}

	@Override
	public void computeScroll() {
		// TODO Auto-generated method stub
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub

		final int action = event.getAction();
		final float x = event.getX();
		final float y = event.getY();

		switch (action) {
		case MotionEvent.ACTION_DOWN:

			Log.i("", "onTouchEvent  ACTION_DOWN");

			if (mVelocityTracker == null) {
				mVelocityTracker = VelocityTracker.obtain();
				mVelocityTracker.addMovement(event);
			}

			if (!mScroller.isFinished()) {
				mScroller.abortAnimation();
			}

			mLastMotionX = x;
			break;

		case MotionEvent.ACTION_MOVE:
			int deltaX = (int) (mLastMotionX - x);

			if (IsCanMove(deltaX)) {
				if (mVelocityTracker != null) {
					mVelocityTracker.addMovement(event);
				}

				mLastMotionX = x;

				scrollBy(deltaX, 0);
			}

			break;

		case MotionEvent.ACTION_UP:

			int velocityX = 0;
			if (mVelocityTracker != null) {
				mVelocityTracker.addMovement(event);
				mVelocityTracker.computeCurrentVelocity(1000);
				velocityX = (int) mVelocityTracker.getXVelocity();
			}

			if (velocityX > SNAP_VELOCITY && mCurScreen > 0) {
				// Fling enough to move left
				Log.e(TAG, "snap left");
				snapToScreen(mCurScreen - 1);
			} else if (velocityX < -SNAP_VELOCITY
					&& mCurScreen < getChildCount() - 1) {
				// Fling enough to move right
				Log.e(TAG, "snap right");
				snapToScreen(mCurScreen + 1);
			} else {
				snapToDestination();
			}

			if (mVelocityTracker != null) {
				mVelocityTracker.recycle();
				mVelocityTracker = null;
			}

			// mTouchState = TOUCH_STATE_REST;
			break;
		}

		return true;
	}

	private boolean IsCanMove(int deltaX) {

		if (getScrollX() <= 0 && deltaX < 0) {
			return false;
		}

		if (getScrollX() >= (getChildCount() - 1) * getWidth() && deltaX > 0) {
			return false;
		}

		return true;
	}

	public void SetOnViewChangeListener(OnViewChangeListener listener) {
		mOnViewChangeListener = listener;
	}

	public void snapToDestination() {
		final int screenWidth = getWidth();

		final int destScreen = (getScrollX() + screenWidth / 2) / screenWidth;
		snapToScreen(destScreen);
	}

	public void snapToScreen(int whichScreen) {

		// get the valid layout page
		whichScreen = Math.max(0, Math.min(whichScreen, getChildCount() - 1));
		if (getScrollX() != (whichScreen * getWidth())) {

			final int delta = whichScreen * getWidth() - getScrollX();

			mScroller.startScroll(getScrollX(), 0, delta, 0,
					Math.abs(delta) * 2);

			mCurScreen = whichScreen;
			invalidate(); // Redraw the layout

			if (mOnViewChangeListener != null) {
				mOnViewChangeListener.OnViewChange(mCurScreen);
			}
		}
	}

	private OnViewChangeListener mOnViewChangeListener;

	public interface OnViewChangeListener {
		public void OnViewChange(int view);
	}

	int mCurScreen;
	float mLastMotionX;
	VelocityTracker mVelocityTracker;
}
