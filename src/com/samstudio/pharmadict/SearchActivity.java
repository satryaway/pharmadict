package com.samstudio.pharmadict;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import com.samstudio.pharmadict.DetailActivity;
import com.samstudio.pharmadict.JSONParser;
import com.samstudio.pharmadict.LoginActivity;
import com.samstudio.pharmadict.R;
import com.samstudio.pharmadict.SearchResultActivity;
import com.samstudio.pharmadict.adapter.SearchResultAdapter;
import com.samstudio.pharmadict.admin.EditActivity;
import com.samstudio.pharmadict.entities.Obat;
import com.samstudio.pharmadict.helpers.ResultsListener;
import com.samstudio.pharmadict.helpers.URLHelper;
import com.samstudio.pharmadict.helpers.URLTask;
import com.samstudio.pharmadict.util.CommonConstants;

public class SearchActivity extends Activity {

	private EditText searchET;
	public ListView obatLV;
	private ImageView submitIV, closeSearchIV;
	private Context context;
	private JSONArray obatArray = null;
	private List<Obat> listObat;
	private ProgressDialog pDialog;
	private JSONParser jParser = new JSONParser();
	private SearchResultAdapter mAdapter;
	private ListView listView;
	private String key, url;
	private TextView searchCountTV, keywordTV;
	private boolean isSearchForObat;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity_layout);
		
		handleIntent();
		initUI();
		setCallBack();
		initAdapter();
	}
	
	private void handleIntent(){
		Intent intent = getIntent();
		isSearchForObat = intent.getBooleanExtra(CommonConstants.TAG_OBAT, true);
	}

	private void initAdapter() {
		mAdapter = new SearchResultAdapter(context, new ArrayList<Obat>());
		mAdapter.notifyDataSetChanged();
		listView = (ListView) findViewById(R.id.obatLV);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context, DetailActivity.class);
				intent.putExtra(CommonConstants.TAG_OBATID, listObat
						.get(position).getObat_id());
				startActivity(intent);
			}
		});
	}

	private void initUI() {
		context = this;
		searchET = (EditText) findViewById(R.id.searchET);
		obatLV = (ListView) findViewById(R.id.obatLV);
		submitIV = (ImageView) findViewById(R.id.submitBT);
		listObat = new ArrayList<Obat>();
		closeSearchIV = (ImageView) findViewById(R.id.close_search_iv);
		searchCountTV = (TextView) findViewById(R.id.search_result_total_tv);
		keywordTV = (TextView) findViewById(R.id.keyword_tv);
		TextView searchTitleTV = (TextView) findViewById(R.id.search_title);
		
		if(isSearchForObat){
			url = CommonConstants.WEB_SERVICE_URL_SEARCH;
			searchTitleTV.setText(CommonConstants.OBAT_SEARCH);
		}else{
			url = CommonConstants.WEB_SERVICE_URL_SEARCH_PENGOBATAN;
			searchTitleTV.setText(CommonConstants.INDICATION_SEARCH);
		}
		
		searchET.setBackgroundDrawable(null);
	}

	private void setCallBack() {
		submitIV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listObat = new ArrayList<Obat>();
				key = searchET.getText().toString();
				if (key.length() != 0) {
					new URLTask(listener, context,
							url + key,
							CommonConstants.PLEASE_WAIT).execute();
				} else {
					Toast.makeText(context,
							CommonConstants.PLEASE_INPUT_KEYWORD,
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		closeSearchIV.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}

	ResultsListener listener = new ResultsListener() {

		@Override
		public void onResultsSucceeded(String result, Context context) {
			new RequestObat().execute(result);
		}

		@Override
		public void onResultsFailed(Context context) {

		}
	};

	class RequestObat extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setMessage(CommonConstants.LOADING_CONTENT);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		protected String doInBackground(String... args) {
			JSONObject json;
			try {
				json = new JSONObject(args[0]);
				int success = json.getInt(CommonConstants.TAG_SUCCESS);
				if (success == 1) {
					obatArray = json.getJSONArray(CommonConstants.TAG_OBAT);
					for (int i = 0; i < obatArray.length(); i++) {
						JSONObject c = obatArray.getJSONObject(i);
						Obat obat = new Obat();
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
						obat.setObat_kode(c
								.getString(CommonConstants.TAG_OBATKODE));

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
				mAdapter.updateContent(listObat);
				searchCountTV.setText(""+listObat.size());
				searchCountTV.setVisibility(View.VISIBLE);
				keywordTV.setVisibility(View.VISIBLE);
				keywordTV.setText(key);
			} else {
				listObat = new ArrayList<Obat>();
				mAdapter.updateContent(listObat);
				Toast.makeText(context, CommonConstants.KEYWORD_NOT_FOUND,
						Toast.LENGTH_SHORT).show();
			}
		}
	}
}
