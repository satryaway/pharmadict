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

public class EditActivity extends Activity {

	private EditText namaObatET, deskripsiObatET, indikasiObatET, hargaObatET, 
		efekSampingObatET, dosisObatET, perhatianObatET, isiObatET;
	private Spinner jenisObatSP;
	private ImageView picIV;
	private Button updateBT;
	private String idObat, namaObat, deskripsiObat, indikasiObat, jenisObat, picsObat,
			hargaObat, picObat, efekSampingObat, dosisObat, perhatianObat, isiObat, kodeObat;
	private TextView idObatTV;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	private LinearLayout closeEditWrapper;
	private RadioGroup kodeObatRG;
	private RadioButton light, medium, hard;

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
		efekSampingObatET.setText(efekSampingObat);
		dosisObatET.setText(dosisObat);
		perhatianObatET.setText(perhatianObat);
		isiObatET.setText(isiObat);
		hargaObatET.setText(hargaObat);
		jenisObatSP.setSelection(Integer.valueOf(jenisObat));
		display(picIV, picObat);
		
		switch(Integer.valueOf(kodeObat)){
		case 1 : light.setChecked(true);break;
		case 2 : medium.setChecked(true);break;
		default : hard.setChecked(true);break;
		}

	}

	private void handleIntent() {
		Intent intent = getIntent();
		idObat = intent.getStringExtra(CommonConstants.TAG_OBATID);
		namaObat = intent.getStringExtra(CommonConstants.TAG_OBATNAMA);
		deskripsiObat = intent
				.getStringExtra(CommonConstants.TAG_OBATDESKRIPSI);
		indikasiObat = intent.getStringExtra(CommonConstants.TAG_OBATINDIKASI);
		hargaObat = intent.getStringExtra(CommonConstants.TAG_OBATHARGA);
		int tempJenis = Integer.valueOf(intent.getStringExtra(CommonConstants.TAG_OBATJENIS));
		if(tempJenis != 0)tempJenis = tempJenis-1;
		jenisObat = String.valueOf(tempJenis);
		picObat = intent.getStringExtra(CommonConstants.TAG_OBATPIC);
		efekSampingObat = intent.getStringExtra(CommonConstants.TAG_OBATEFEKSAMPING);
		dosisObat = intent.getStringExtra(CommonConstants.TAG_OBATDOSIS);
		perhatianObat = intent.getStringExtra(CommonConstants.TAG_OBATPERHATIAN);
		isiObat = intent.getStringExtra(CommonConstants.TAG_OBATISI);
		kodeObat = intent.getStringExtra(CommonConstants.TAG_OBATKODE);
		picsObat = intent.getStringExtra("obat_pics");

	}

	private void setCallBack() {
		closeEditWrapper.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		updateBT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String updatedNamaObat = namaObatET.getText().toString();
				String updatedDeskripsiObat = deskripsiObatET.getText()
						.toString();
				String updatedIndikasiObat = indikasiObatET.getText()
						.toString();
				String updatedHargaObat = hargaObatET.getText().toString();
				String updatedJenisObat = String.valueOf(jenisObatSP.getSelectedItemPosition()+1);
				String updatedKodeObat;
				SharedPreferences settings = getSharedPreferences(CommonConstants.USERNAME, Context.MODE_PRIVATE);
				
				if(light.isChecked()) updatedKodeObat = "1";
				else if(medium.isChecked()) updatedKodeObat = "2";
				else updatedKodeObat = "3";
				HashMap<String, String> map = new HashMap<String, String>();
				map.put(CommonConstants.TAG_OBATID, idObat);
				map.put(CommonConstants.TAG_OBATNAMA, updatedNamaObat);
				map.put(CommonConstants.TAG_OBATDESKRIPSI, updatedDeskripsiObat);
				map.put(CommonConstants.TAG_OBATINDIKASI, updatedIndikasiObat);
				map.put(CommonConstants.TAG_OBATHARGA, updatedHargaObat);
				map.put(CommonConstants.TAG_OBATJENIS, updatedJenisObat);
				map.put(CommonConstants.TAG_OBATKODE, updatedKodeObat);
				map.put(CommonConstants.TAG_OBATEFEKSAMPING, efekSampingObatET.getText().toString());
				map.put(CommonConstants.TAG_OBATDOSIS, dosisObatET.getText().toString());
				map.put(CommonConstants.TAG_OBATPERHATIAN, perhatianObatET.getText().toString());
				map.put(CommonConstants.TAG_OBATISI, isiObatET.getText().toString());
				map.put(CommonConstants.USERNAME, settings.getString(CommonConstants.USERNAME, ""));
				map.put("obat_pics", picsObat);

				new URLTask(listener, EditActivity.this,
						CommonConstants.WEB_SERVICE_URL_POST_UPDATE_FORM,
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
		updateBT = (Button) findViewById(R.id.updateBT);
		closeEditWrapper = (LinearLayout) findViewById(R.id.close_edit_wrapper);
		kodeObatRG = (RadioGroup) findViewById(R.id.kode_obat_RG);
		light = (RadioButton) findViewById(R.id.kode_obat_ringan_rb);
		medium = (RadioButton) findViewById(R.id.kode_obat_sedang_rb);
		hard = (RadioButton) findViewById(R.id.kode_obat_keras_rb);
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

					Toast.makeText(EditActivity.this,
							CommonConstants.UPDATE_SUCCEED, Toast.LENGTH_SHORT)
							.show();
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
