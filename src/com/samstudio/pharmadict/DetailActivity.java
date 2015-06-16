package com.samstudio.pharmadict;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import com.samstudio.pharmadict.adapter.SearchResultAdapter;
import com.samstudio.pharmadict.entities.Obat;
import com.samstudio.pharmadict.helpers.URLHelper;
import com.samstudio.pharmadict.util.CommonConstants;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DetailActivity extends Activity {
	
	private TextView namaTV, deskripsiTV, indikasiTV, hargaTV, jenisTV;
	private ImageView picIV;
	private String obatId, url;
	private ProgressDialog pDialog;
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
		intentHandler();
		setUrl();
		
		new RequestSingleObat().execute();
	}
	
	private void setUrl() {
		url = CommonConstants.WEB_SERVICE_URL_GET_SINGLE+obatId;
	}

	public void initUI(){
		namaTV = (TextView)findViewById(R.id.nameTV);
		deskripsiTV = (TextView)findViewById(R.id.deskripsiTV);
		indikasiTV = (TextView)findViewById(R.id.indikasiTV);
		hargaTV = (TextView)findViewById(R.id.hargaTV);
		jenisTV = (TextView)findViewById(R.id.jenisTV);
		picIV = (ImageView)findViewById(R.id.picIV);
	}
	
	public void intentHandler(){
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
						obat.setObat_nama(c.getString(CommonConstants.TAG_OBATNAMA));
						obat.setObat_indikasi(c.getString(CommonConstants.TAG_OBATINDIKASI));
						obat.setObat_deskripsi(c.getString(CommonConstants.TAG_OBATDESKRIPSI));
						obat.setObat_harga(c.getString(CommonConstants.TAG_OBATHARGA));
						obat.setObat_jenis(c.getString(CommonConstants.TAG_OBATJENIS));
						obat.setObat_pic(URLHelper.buildURLThumb(c.getString(CommonConstants.TAG_OBATPIC)));
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
			jenisTV.setText(obat.getObat_jenis());
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
