package com.samstudio.pharmadict.admin;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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
import com.samstudio.pharmadict.R;
import com.samstudio.pharmadict.helpers.ResultsListener;
import com.samstudio.pharmadict.helpers.URLTask;
import com.samstudio.pharmadict.util.CommonConstants;

public class InputActivity extends Activity {

	private EditText namaObatET, deskripsiObatET, indikasiObatET, hargaObatET,
			efekSampingObatET, dosisObatET, perhatianObatET, isiObatET;
	private Spinner jenisObatSP;
	private ImageView picIV, closeInputIV;
	private Button inputBT;
	private String idObat, namaObat, deskripsiObat, indikasiObat, jenisObat,
			picsObat, hargaObat, picObat, efekSampingObat, dosisObat,
			perhatianObat, isiObat, kodeObat;
	private TextView idObatTV;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private LinearLayout closeEditWrapper;
	private RadioGroup kodeObatRG;
	private RadioButton light, medium, hard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.input_activity);
		initImageLoader();
		initUI();
		setCallBack();
		// handleIntent();
		// putData();
	}

	private void setCallBack() {
		closeInputIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		inputBT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String updatedNamaObat = namaObatET.getText().toString();
				String updatedDeskripsiObat = deskripsiObatET.getText()
						.toString();
				String updatedIndikasiObat = indikasiObatET.getText()
						.toString();
				String updatedHargaObat = hargaObatET.getText().toString();
				String updatedJenisObat = String.valueOf(jenisObatSP
						.getSelectedItemPosition() + 1);
				String updatedKodeObat;
				SharedPreferences settings = getSharedPreferences(CommonConstants.USERNAME, Context.MODE_PRIVATE);
				
				if (light.isChecked())
					updatedKodeObat = "1";
				else if (medium.isChecked())
					updatedKodeObat = "2";
				else
					updatedKodeObat = "3";
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(CommonConstants.TAG_OBATID, idObat);
				map.put(CommonConstants.TAG_OBATNAMA, updatedNamaObat);
				map.put(CommonConstants.TAG_OBATDESKRIPSI, updatedDeskripsiObat);
				map.put(CommonConstants.TAG_OBATINDIKASI, updatedIndikasiObat);
				map.put(CommonConstants.TAG_OBATHARGA, updatedHargaObat);
				map.put(CommonConstants.TAG_OBATJENIS, updatedJenisObat);
				map.put(CommonConstants.TAG_OBATKODE, updatedKodeObat);
				map.put(CommonConstants.TAG_OBATEFEKSAMPING, efekSampingObatET
						.getText().toString());
				map.put(CommonConstants.TAG_OBATDOSIS, dosisObatET.getText()
						.toString());
				map.put(CommonConstants.TAG_OBATPERHATIAN, perhatianObatET
						.getText().toString());
				map.put(CommonConstants.TAG_OBATISI, isiObatET.getText()
						.toString());
				map.put(CommonConstants.USERNAME, settings.getString(CommonConstants.USERNAME, ""));
				map.put("obat_pics", picsObat);

				new URLTask(listener, InputActivity.this,
						CommonConstants.WEB_SERVICE_URL_GET_SINGLE,
						CommonConstants.PLEASE_WAIT, map).execute();
			}
		});

	}

	private void initUI() {
		idObatTV = (TextView) findViewById(R.id.idObatTV);
		namaObatET = (EditText) findViewById(R.id.nama_obat_ET);
		deskripsiObatET = (EditText) findViewById(R.id.deskripsiObatET);
		indikasiObatET = (EditText) findViewById(R.id.indikasiObatET);
		hargaObatET = (EditText) findViewById(R.id.hargaObatET);
		efekSampingObatET = (EditText) findViewById(R.id.efek_samping_obat_ET);
		dosisObatET = (EditText) findViewById(R.id.dosis_obat_ET);
		perhatianObatET = (EditText) findViewById(R.id.perhatian_obat_ET);
		isiObatET = (EditText) findViewById(R.id.isi_obat_ET);
		jenisObatSP = (Spinner) findViewById(R.id.jenisObatSP);
		picIV = (ImageView) findViewById(R.id.picIV);
		inputBT = (Button) findViewById(R.id.inputBT);
		closeEditWrapper = (LinearLayout) findViewById(R.id.close_edit_wrapper);
		kodeObatRG = (RadioGroup) findViewById(R.id.kode_obat_RG);
		light = (RadioButton) findViewById(R.id.kode_obat_ringan_rb);
		medium = (RadioButton) findViewById(R.id.kode_obat_sedang_rb);
		hard = (RadioButton) findViewById(R.id.kode_obat_keras_rb);
		closeInputIV = (ImageView) findViewById(R.id.close_input_iv);
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
					Toast.makeText(InputActivity.this,
							CommonConstants.INPUT_SUCCEED, Toast.LENGTH_SHORT)
							.show();
					finish();
				} else {
					Toast.makeText(InputActivity.this,
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
