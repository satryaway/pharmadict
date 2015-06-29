package com.samstudio.pharmadict;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.samstudio.pharmadict.R;
import com.samstudio.pharmadict.util.CommonConstants;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText searchET;
	private Button searchB;
	private String keyword;
	private ImageView bgHomeImageView;
	private static final int RIGHT_TO_LEFT = 1;
	private static final int LEFT_TO_RIGHT = 2;
	private static final int DURATION = 10 * 1000; // TEN SECOND
	private final Matrix mMatrix = new Matrix();
	private float mScaleFactor;
	private RectF mDisplayRect = new RectF();
	private int currentBackground;
	private List<Integer> imagesUrlList = new ArrayList<Integer>();

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
		setCallBack();
	}

	public void initUI() {
		imagesUrlList.add(R.drawable.image1);
		imagesUrlList.add(R.drawable.image2);
		imagesUrlList.add(R.drawable.image3);
		imagesUrlList.add(R.drawable.image4);
		imagesUrlList.add(R.drawable.image5);
		imagesUrlList.add(R.drawable.image6);
		imagesUrlList.add(R.drawable.image7);
		bgHomeImageView = (ImageView) findViewById(R.id.background_view);
		fetchBgImages();
		searchET = (EditText) findViewById(R.id.searchET);
		searchB = (Button) findViewById(R.id.searchB);
	}

	public void setCallBack() {
		searchB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				keyword = searchET.getText().toString();
				if (keyword.length() != 0) {
					Intent intent = new Intent(MainActivity.this,
							SearchResultActivity.class);
					intent.putExtra(CommonConstants.KEYWORD, keyword);
					MainActivity.this.startActivity(intent);
				} else {
					Toast.makeText(MainActivity.this,
							CommonConstants.PLEASE_INPUT_KEYWORD,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void fetchBgImages() {
		changeBackground(randomAnimation());
	}

	private int randomAnimation() {
		Random rand = new Random();
		int max = 2;
		int min = 0;
		return rand.nextInt((max - min) + 1) + min;
	}

	private void changeBackground(final int typeAnimatioin) {
		if (bgHomeImageView != null) {
			setImageBackgroundFromFile(imagesUrlList.get(currentBackground));
			bgHomeImageView.post(new Runnable() {
				@Override
				public void run() {
					animate(typeAnimatioin);
				}
			});
		}
	}

	private void setImageBackgroundFromFile(int fileName) {
		if (bgHomeImageView != null) {
			bgHomeImageView.setImageResource(fileName);
		}
	}

	private void animate(int typeAnimation) {
		if (typeAnimation == RIGHT_TO_LEFT) {
			scale(false);
			updateDisplayRect();
			animate(mDisplayRect.left, mDisplayRect.left
					- (mDisplayRect.right - bgHomeImageView.getWidth()), false);
		} else if (typeAnimation == LEFT_TO_RIGHT) {
			scale(false);
			updateDisplayRect();
			animate(mDisplayRect.left
					- (mDisplayRect.right - bgHomeImageView.getWidth()),
					mDisplayRect.left, false);
		} else {
			scale(true);
			updateDisplayRect();
			animate(0, 0, true);
		}
	}

	private void animate(float from, float to, final boolean zoom) {
		bgHomeImageView.setVisibility(View.INVISIBLE);
		final float verticalFloating = bgHomeImageView.getHeight() / 5;
		setTranslationImage(from, -verticalFloating);

		AnimatorSet animator = new AnimatorSet();
		animator.setInterpolator(new LinearInterpolator());
		if (!zoom) {
			ValueAnimator slideAnimator = ValueAnimator.ofFloat(from, to);
			slideAnimator.setDuration(DURATION);
			slideAnimator
					.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
						@Override
						public void onAnimationUpdate(ValueAnimator animation) {
							setTranslationImage(
									(Float) animation.getAnimatedValue(),
									-verticalFloating);
						}
					});
			animator.play(slideAnimator);
		} else {
			ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(
					bgHomeImageView, "scaleX", 1f + 0.05f, 1f);
			ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(
					bgHomeImageView, "scaleY", 1f + 0.05f, 1f);
			scaleXAnimator.setDuration(DURATION);
			scaleYAnimator.setDuration(DURATION);
			animator.playTogether(scaleXAnimator, scaleYAnimator);
		}

		ObjectAnimator fadeInAnimator = ObjectAnimator.ofFloat(bgHomeImageView,
				"alpha", 0, 1);
		fadeInAnimator.setDuration(0);

		ObjectAnimator fadeOutAnimator = ObjectAnimator.ofFloat(
				bgHomeImageView, "alpha", 1, 0);
		fadeOutAnimator.setDuration(1000);

		AnimatorSet set = new AnimatorSet();
		set.playSequentially(animator, fadeOutAnimator);
		set.addListener(new AnimatorListenerAdapter() {
			@Override
			public void onAnimationStart(Animator animation) {
				// scale(false);
				ViewHelper.setAlpha(bgHomeImageView, 1f);
				bgHomeImageView.setVisibility(View.VISIBLE);
			}

			@Override
			public void onAnimationEnd(Animator animation) {
				currentBackground++;
				if (currentBackground >= imagesUrlList.size()) {
					currentBackground = 0;
				}
				bgHomeImageView.setVisibility(View.INVISIBLE);
				scale(true);
				AnimatorSet s = new AnimatorSet();
				ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(
						bgHomeImageView, "scaleX", 1f, 1f);
				ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(
						bgHomeImageView, "scaleY", 1f, 1f);
				scaleXAnimator.setDuration(0);
				scaleYAnimator.setDuration(0);
				s.playTogether(scaleXAnimator, scaleYAnimator);
				changeBackground(randomAnimation());
			}
		});
		set.start();
	}

	private void setTranslationImage(float x, float y) {
		mMatrix.reset();
		applyScaleOnMatrix(false);
		mMatrix.postTranslate(x, y);
		// updateDisplayRect();
		bgHomeImageView.setImageMatrix(mMatrix);
		setCurrentImageMatrix();
	}

	private void updateDisplayRect() {
		mDisplayRect.set(0, 0, bgHomeImageView.getDrawable()
				.getIntrinsicWidth(), bgHomeImageView.getDrawable()
				.getIntrinsicHeight());
		mMatrix.mapRect(mDisplayRect);
	}

	private void setCurrentImageMatrix() {
		bgHomeImageView.setImageMatrix(mMatrix);
		bgHomeImageView.invalidate();
		bgHomeImageView.requestLayout();
	}

	private void scale(boolean normalScale) {
		mMatrix.reset();
		applyScaleOnMatrix(normalScale);
		setCurrentImageMatrix();
	}

	private void applyScaleOnMatrix(boolean normalScale) {
		float drawableSize = (float) bgHomeImageView.getDrawable()
				.getIntrinsicWidth();
		float imageViewSize = (float) bgHomeImageView.getWidth();
		mScaleFactor = drawableSize > imageViewSize ? (drawableSize / imageViewSize)
				: (imageViewSize / drawableSize);
		mScaleFactor = normalScale ? mScaleFactor : mScaleFactor + 0.05f;
		mMatrix.postScale(mScaleFactor, mScaleFactor);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		} else if (id == R.id.action_login) {
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			MainActivity.this.startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
