package com.samstudio.pharmadict;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.samstudio.pharmadict.util.CommonConstants;

public class MainActivity extends Activity {

	private EditText searchET;
	private Button searchB;
	private String keyword;
	private ImageView bgHomeImageView, listObatIV, aboutIV, exitIV;
	private ImageView searchObatIV, searchPengobatanIV, helpIV;
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

		imagesUrlList.add(R.drawable.drug);
		imagesUrlList.add(R.drawable.drug2);
		imagesUrlList.add(R.drawable.drug3);
		imagesUrlList.add(R.drawable.drug4);
		imagesUrlList.add(R.drawable.drug5);

		bgHomeImageView = (ImageView) findViewById(R.id.background_view);
		fetchBgImages();

		searchObatIV = (ImageView) findViewById(R.id.search_obat_iv);
		helpIV = (ImageView) findViewById(R.id.help_iv);
		aboutIV = (ImageView) findViewById(R.id.about_iv);
		exitIV = (ImageView) findViewById(R.id.exit_iv);
		searchPengobatanIV = (ImageView) findViewById(R.id.search_pengobatan_iv);
		listObatIV = (ImageView) findViewById(R.id.list_obat_iv);
		/*
		 * searchET = (EditText) findViewById(R.id.searchET); searchB = (Button)
		 * findViewById(R.id.searchB);
		 */
	}

	public void setCallBack() {
		/*
		 * searchB.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { keyword =
		 * searchET.getText().toString(); if (keyword.length() != 0) { Intent
		 * intent = new Intent(MainActivity.this, SearchResultActivity.class);
		 * intent.putExtra(CommonConstants.KEYWORD, keyword);
		 * MainActivity.this.startActivity(intent); } else {
		 * Toast.makeText(MainActivity.this,
		 * CommonConstants.PLEASE_INPUT_KEYWORD, Toast.LENGTH_SHORT).show(); } }
		 * });
		 */
		listObatIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						ListObatActivity.class);
				startActivity(intent);
			}
		});

		searchObatIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						SearchActivity.class);
				intent.putExtra(CommonConstants.TAG_OBAT, true);
				startActivity(intent);
			}
		});

		searchPengobatanIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this,
						SearchActivity.class);
				intent.putExtra(CommonConstants.TAG_OBAT, false);
				startActivity(intent);
			}
		});

		aboutIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						AboutActivity.class);
				startActivity(intent);
			}
		});
		
		helpIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MainActivity.this,
						HelpActivity.class);
				startActivity(intent);
			}
		});
		
		exitIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
	            alert.setTitle("Konfirmasi").setMessage("Apakah kamu yakin ingin keluar?")
	            .setNegativeButton(android.R.string.no, null)
	            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

	                public void onClick(DialogInterface arg0, int arg1) {
	                    finish();
	                }
	            }).create().show();
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
