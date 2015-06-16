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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.samstudio.pharmadict.adapter.SearchResultAdapter;
import com.samstudio.pharmadict.entities.Obat;
import com.samstudio.pharmadict.helpers.URLHelper;
import com.samstudio.pharmadict.util.CommonConstants;

public class SearchResultActivity extends Activity {
	private ProgressDialog pDialog;
	private JSONParser jParser = new JSONParser();
	private List<Obat> listObat;
	private static String url;
	private String key;
	private JSONArray obatArray = null;
	private TextView noResult;
	private SearchResultAdapter mAdapter;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_result);
		
		Intent intent = getIntent();
		key = intent.getStringExtra(CommonConstants.KEYWORD);
		if (key != null) {
			url = CommonConstants.WEB_SERVICE_URL_SEARCH + key;
		}
		noResult = (TextView)findViewById(R.id.empty_result);
		listObat = new ArrayList<Obat>();

		new RequestObat().execute();
	}

	class RequestObat extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SearchResultActivity.this);
			pDialog.setMessage(CommonConstants.LOADING_CONTENT);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			JSONObject json = jParser.makeHttpRequest(url, CommonConstants.GET, params);
			try {
				int success = json.getInt(CommonConstants.TAG_SUCCESS);
				if (success == 1) {
					obatArray = json.getJSONArray(CommonConstants.TAG_OBAT);
					for (int i = 0; i < obatArray.length(); i++) {
						JSONObject c = obatArray.getJSONObject(i);
						Obat obat = new Obat();
						obat.setObat_id(c.getString(CommonConstants.TAG_OBATID));
						obat.setObat_nama(c.getString(CommonConstants.TAG_OBATNAMA));
						obat.setObat_indikasi(c.getString(CommonConstants.TAG_OBATINDIKASI));
						obat.setObat_deskripsi(c.getString(CommonConstants.TAG_OBATDESKRIPSI));
						obat.setObat_harga(c.getString(CommonConstants.TAG_OBATHARGA));
						obat.setObat_jenis(c.getString(CommonConstants.TAG_OBATJENIS));
						obat.setObat_pic(URLHelper.buildURLThumb(c.getString(CommonConstants.TAG_OBATPIC)));
						
						listObat.add(obat);
					}
				} else {
					listObat = null;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(String file_url) {
			pDialog.dismiss();
			if (listObat != null) {
				mAdapter = new SearchResultAdapter(SearchResultActivity.this, listObat);
				listView = (ListView)findViewById(R.id.listObat);
				listView.setAdapter(mAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent intent = new Intent (SearchResultActivity.this, DetailActivity.class);
						intent.putExtra(CommonConstants.TAG_OBATID, listObat.get(position).getObat_id());
						SearchResultActivity.this.startActivity(intent);
					}
				});
			} else {
				noResult.setVisibility(View.VISIBLE);
			}
		}

	}
}
