package com.samstudio.pharmadict.admin;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.ImageLoadingProgressListener;
import com.samstudio.pharmadict.LoginActivity;
import com.samstudio.pharmadict.R;
import com.samstudio.pharmadict.helpers.ResultsListener;
import com.samstudio.pharmadict.helpers.URLTask;
import com.samstudio.pharmadict.util.CommonConstants;

public class EditActivity extends Activity {

	private EditText namaObatET, deskripsiObatET, indikasiObatET, hargaObatET,
			jenisObatET;
	private ImageView picIV;
	private Button updateBT;
	private String idObat, namaObat, deskripsiObat, indikasiObat, jenisObat,
			hargaObat, picObat;
	private TextView idObatTV;
	private ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_activity);
		initImageLoader();
		initUI();
		setCallBack();
		handleIntent();
		putData();
	}

	private void putData() {
		idObatTV.setText(idObat);
		namaObatET.setText(namaObat);
		deskripsiObatET.setText(deskripsiObat);
		indikasiObatET.setText(indikasiObat);
		hargaObatET.setText(hargaObat);
		jenisObatET.setText(jenisObat);
		display(picIV, picObat);

	}

	private void handleIntent() {
		Intent intent = getIntent();
		idObat = intent.getStringExtra(CommonConstants.TAG_OBATID);
		namaObat = intent.getStringExtra(CommonConstants.TAG_OBATNAMA);
		deskripsiObat = intent
				.getStringExtra(CommonConstants.TAG_OBATDESKRIPSI);
		indikasiObat = intent.getStringExtra(CommonConstants.TAG_OBATINDIKASI);
		hargaObat = intent.getStringExtra(CommonConstants.TAG_OBATHARGA);
		jenisObat = intent.getStringExtra(CommonConstants.TAG_OBATJENIS);
		picObat = intent.getStringExtra(CommonConstants.TAG_OBATPIC);

	}

	private void setCallBack() {
		updateBT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String updatedNamaObat = namaObatET.getText().toString();
				String updatedDeskripsiObat = deskripsiObatET.getText()
						.toString();
				String updatedIndikasiObat = indikasiObatET.getText()
						.toString();
				String updatedHargaObat = hargaObatET.getText().toString();
				String updatedJenisObat = jenisObatET.getText().toString();

				HashMap<String, String> map = new HashMap<String, String>();
				map.put(CommonConstants.TAG_OBATID, idObat);
				map.put(CommonConstants.TAG_OBATNAMA, updatedNamaObat);
				map.put(CommonConstants.TAG_OBATDESKRIPSI, updatedDeskripsiObat);
				map.put(CommonConstants.TAG_OBATINDIKASI, updatedIndikasiObat);
				map.put(CommonConstants.TAG_OBATHARGA, updatedHargaObat);
				map.put(CommonConstants.TAG_OBATJENIS, updatedJenisObat);

				new URLTask(listener, EditActivity.this,
						CommonConstants.WEB_SERVICE_URL_POST_UPDATE_FORM,
						CommonConstants.PLEASE_WAIT, map).execute();
			}
		});

	}

	private void initUI() {
		idObatTV = (TextView) findViewById(R.id.idObatTV);
		namaObatET = (EditText) findViewById(R.id.namaObatET);
		deskripsiObatET = (EditText) findViewById(R.id.deskripsiObatET);
		indikasiObatET = (EditText) findViewById(R.id.indikasiObatET);
		hargaObatET = (EditText) findViewById(R.id.hargaObatET);
		jenisObatET = (EditText) findViewById(R.id.jenisObatET);
		picIV = (ImageView) findViewById(R.id.picIV);
		updateBT = (Button) findViewById(R.id.updateBT);
	}

	ResultsListener listener = new ResultsListener() {

		@Override
		public void onResultsSucceeded(String result, Context context) {
			try {
				JSONObject json = new JSONObject(result);
				int success = json.getInt(CommonConstants.TAG_SUCCESS);
				if (success == 1) {
					Intent returnIntent = new Intent();
					setResult(RESULT_OK, returnIntent);
					finish();
				} else {
					Toast.makeText(EditActivity.this,
							CommonConstants.UPDATING_FAILED, Toast.LENGTH_SHORT)
							.show();
				}
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
		}

		@Override
		public void onResultsFailed(Context context) {

		}
	};

	private void initImageLoader() {
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.diskCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();
		if (!imageLoader.isInited())
			imageLoader.init(config);
		imageLoader.handleSlowNetwork(true);
	}

	public static DisplayImageOptions getUILOption() {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true)
				.showImageOnLoading(R.drawable.default_image)
				.showImageForEmptyUri(R.drawable.default_image)
				.showImageOnFail(R.drawable.default_image)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.cacheInMemory(true).cacheOnDisk(true).postProcessor(null)
				.displayer(new FadeInBitmapDisplayer(300))
				.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}

	public void display(ImageView img, String url) {
		imageLoader.displayImage(url, img, getUILOption(),
				new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {

					}
				}, new ImageLoadingProgressListener() {
					@Override
					public void onProgressUpdate(String imageUri, View view,
							int current, int total) {

					}
				});
	}
}
