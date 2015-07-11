package com.samstudio.pharmadict;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.samstudio.pharmadict.adapter.ObatListAdapter;
import com.samstudio.pharmadict.entities.Obat;
import com.samstudio.pharmadict.helpers.ResultsListener;
import com.samstudio.pharmadict.helpers.URLHelper;
import com.samstudio.pharmadict.helpers.URLTask;
import com.samstudio.pharmadict.util.CommonConstants;

public class ListObatActivity extends Activity {
	private List<Obat> listObat = new ArrayList<Obat>();
	private LinearLayout closeWrapper;
	private ProgressDialog pDialog;
	private JSONArray obatArray = null;
	private ObatListAdapter mAdapter;
	private ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_obat_layout);

		initUI();
		setCallBack();
		initAdapter();
		String url = CommonConstants.WEB_SERVICE_URL_GET_SINGLE;
		new URLTask(listener, this, url, CommonConstants.PLEASE_WAIT).execute();
	}

	private void initUI() {
		closeWrapper = (LinearLayout) findViewById(R.id.close_list_obat_wrapper);
	}
	
	private void setCallBack(){
		closeWrapper.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
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
			pDialog = new ProgressDialog(ListObatActivity.this);
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
						obat.setObat_pic(URLHelper.buildURLThumb(c
								.getString(CommonConstants.TAG_OBATPIC)));

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
			} else {
				listObat = new ArrayList<Obat>();
				mAdapter.updateContent(listObat);
				Toast.makeText(ListObatActivity.this,
						CommonConstants.CONNECTION_TIMED_OUT,
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	private void initAdapter() {
		mAdapter = new ObatListAdapter(ListObatActivity.this,
				new ArrayList<Obat>());
		mAdapter.notifyDataSetChanged();
		listView = (ListView) findViewById(R.id.obatLV);
		listView.setAdapter(mAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(ListObatActivity.this,
						DetailActivity.class);
				intent.putExtra(CommonConstants.TAG_OBATID,
						listObat.get(position).getObat_id());
				startActivity(intent);
			}
		});
	}
}
