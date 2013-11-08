package com.ssw.weightrecord;

import com.ssw.weightrecord.view.MyScroll;
import com.ssw.weightrecord.view.MyScroll.OnViewChangeListener;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	LinearLayout llayout;
	MyScroll mScroll;
	LinearLayout animLayout;
	LinearLayout leftLayout;
	LinearLayout rightLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Intent i = new Intent();
		// i.setAction(Intent.ACTION_DIAL);
		// this.startActivity(i);
		mScroll = (MyScroll) findViewById(R.id.myScorll);
		llayout = (LinearLayout) findViewById(R.id.llayout);
		mScroll.SetOnViewChangeListener(listener);
		animLayout = (LinearLayout) findViewById(R.id.animLayout);
		leftLayout = (LinearLayout) animLayout.getChildAt(0);
		rightLayout = (LinearLayout) animLayout.getChildAt(1);

		Button startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setOnClickListener(startBtnL);
	}

	private OnClickListener startBtnL = new OnClickListener() {

		@Override
		public void onClick(View v) {
			mScroll.setVisibility(View.GONE);
			llayout.setVisibility(View.GONE);
			animLayout.setVisibility(View.VISIBLE);
			Animation leftOutAnimation = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.translate_left);

			Animation rightOutAnimation = AnimationUtils.loadAnimation(
					getApplicationContext(), R.anim.translate_right);
			animLayout.getChildAt(0).setAnimation(leftOutAnimation);
			animLayout.getChildAt(1).setAnimation(rightOutAnimation);

			leftOutAnimation.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationStart(Animation animation) {
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationEnd(Animation animation) {
					leftLayout.setVisibility(View.GONE);
					rightLayout.setVisibility(View.GONE);
					animLayout.setVisibility(View.GONE);
					Intent intent = new Intent(MainActivity.this,
							RecordActivity.class);
					MainActivity.this.startActivity(intent);
					MainActivity.this.finish();
					overridePendingTransition(R.anim.zoom_out_enter,
							R.anim.zoom_out_exit);
				}
			});
		}
	};

	private OnViewChangeListener listener = new OnViewChangeListener() {

		@Override
		public void OnViewChange(int index) {
			for (int i = 0; i < llayout.getChildCount(); i++) {
				if (index == i) {
					llayout.getChildAt(i).setEnabled(true);
				} else {
					llayout.getChildAt(i).setEnabled(false);
				}
			}
		}
	};
}
