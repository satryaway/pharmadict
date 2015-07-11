package com.samstudio.pharmadict;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import com.samstudio.pharmadict.entities.Obat;
import com.samstudio.pharmadict.helpers.Seeder;
import com.samstudio.pharmadict.helpers.URLHelper;
import com.samstudio.pharmadict.util.CommonConstants;

public class DetailActivity extends Activity {

	private TextView namaTV, deskripsiTV, indikasiTV, hargaTV, jenisTV,
			efeksampingTV, dosisTV, perhatianTV, isiTV;
	private View kodeV;
	private ImageView picIV, closeDetailIV;
	private String obatId, url;
	private ProgressDialog pDialog;
	private LinearLayout closeDetailWrapper;
	private ImageLoader imageLoader = ImageLoader.getInstance();
	JSONParser jParser = new JSONParser();
	JSONArray products = null;
	Obat obat = new Obat();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_activity);

		initImageLoader();
		initUI();
		setCallBack();
		intentHandler();
		setUrl();

		new RequestSingleObat().execute();
	}

	private void setCallBack() {
		closeDetailWrapper.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		closeDetailIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	private void setUrl() {
		url = CommonConstants.WEB_SERVICE_URL_GET_SINGLE + obatId;
	}

	public void initUI() {
		namaTV = (TextView) findViewById(R.id.namaTV);
		deskripsiTV = (TextView) findViewById(R.id.deskripsiTV);
		indikasiTV = (TextView) findViewById(R.id.indikasiTV);
		hargaTV = (TextView) findViewById(R.id.hargaTV);
		jenisTV = (TextView) findViewById(R.id.jenisTV);
		efeksampingTV = (TextView) findViewById(R.id.efeksampingTV);
		dosisTV = (TextView) findViewById(R.id.dosisTV);
		perhatianTV = (TextView) findViewById(R.id.perhatianTV);
		isiTV = (TextView) findViewById(R.id.isiTV);
		picIV = (ImageView) findViewById(R.id.picIV);
		kodeV = (View) findViewById(R.id.kodeV);
		closeDetailWrapper = (LinearLayout) findViewById(R.id.close_detail_wrapper);
		closeDetailIV = (ImageView) findViewById(R.id.close_detail_iv);
	}

	public void intentHandler() {
		Intent intent = getIntent();
		obatId = intent.getStringExtra(CommonConstants.TAG_OBATID);
	}

	class RequestSingleObat extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(DetailActivity.this);
			pDialog.setMessage("Loading content. Please wait...");
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting All products from url
		 * */
		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jParser.makeHttpRequest(url, "GET", params);
			try {
				int success = json.getInt(CommonConstants.TAG_SUCCESS);
				if (success == 1) {
					products = json.getJSONArray(CommonConstants.TAG_OBAT);
					for (int i = 0; i < products.length(); i++) {
						JSONObject c = products.getJSONObject(i);
						obat.setObat_id(c.getString(CommonConstants.TAG_OBATID));
						obat.setObat_nama(c
								.getString(CommonConstants.TAG_OBATNAMA));
						obat.setObat_indikasi(c
								.getString(CommonConstants.TAG_OBATINDIKASI));
						obat.setObat_deskripsi(c
								.getString(CommonConstants.TAG_OBATDESKRIPSI));
						obat.setObat_harga(c
								.getString(CommonConstants.TAG_OBATHARGA));
						obat.setObat_jenis(c
								.getString(CommonConstants.TAG_OBATJENIS));
						obat.setObat_pic(URLHelper.buildURLThumb(c
								.getString(CommonConstants.TAG_OBATPIC)));
						obat.setObat_efeksamping(c
								.getString(CommonConstants.TAG_OBATEFEKSAMPING));
						obat.setObat_dosis(c
								.getString(CommonConstants.TAG_OBATDOSIS));
						obat.setObat_perhatian(c
								.getString(CommonConstants.TAG_OBATPERHATIAN));
						obat.setObat_isi(c
								.getString(CommonConstants.TAG_OBATISI));
						obat.setObat_kode(c
								.getString(CommonConstants.TAG_OBATKODE));
					}
				} else {

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			display(picIV, obat.getObat_pic());
			namaTV.setText(obat.getObat_nama());
			deskripsiTV.setText(obat.getObat_deskripsi());
			indikasiTV.setText(obat.getObat_indikasi());
			hargaTV.setText(obat.getObat_harga());
			jenisTV.setText(Seeder.setJenisObat(Integer.valueOf(obat
					.getObat_jenis())));
			efeksampingTV.setText(obat.getObat_efeksamping());
			dosisTV.setText(obat.getObat_dosis());
			perhatianTV.setText(obat.getObat_perhatian());
			isiTV.setText(obat.getObat_isi());
			kodeV.setBackgroundResource(Seeder.kodeObat(Integer.valueOf(obat.getObat_kode())));
		}

	}

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
