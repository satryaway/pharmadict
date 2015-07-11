package com.samstudio.pharmadict.admin;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.samstudio.pharmadict.JSONParser;
import com.samstudio.pharmadict.LoginActivity;
import com.samstudio.pharmadict.R;
import com.samstudio.pharmadict.adapter.SearchResultAdapter;
import com.samstudio.pharmadict.entities.Obat;
import com.samstudio.pharmadict.helpers.ResultsListener;
import com.samstudio.pharmadict.helpers.URLHelper;
import com.samstudio.pharmadict.helpers.URLTask;
import com.samstudio.pharmadict.util.CommonConstants;

public class AdminHomeActivity extends Activity {

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
	private TextView searchCountTV, keywordTV, inputTV;
	private boolean isSearchForObat;
	private PopupWindow popup;
	private OnClickListener updateOnClick, deleteOnClick, dismissPopup;
	private int position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_activity_layout);

		handleIntent();
		initUI();
		setCallBack();
		initAdapter();
	}

	private void handleIntent() {
		Intent intent = getIntent();
		isSearchForObat = intent
				.getBooleanExtra(CommonConstants.TAG_OBAT, true);
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
				showPopup(position);
			}
		});

		updateOnClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				popup.dismiss();
				Intent intent = new Intent(context, EditActivity.class);
				intent.putExtra(CommonConstants.TAG_OBATID,
						listObat.get(position).getObat_id());
				intent.putExtra(CommonConstants.TAG_OBATNAMA,
						listObat.get(position).getObat_nama());
				intent.putExtra(CommonConstants.TAG_OBATDESKRIPSI, listObat
						.get(position).getObat_deskripsi());
				intent.putExtra(CommonConstants.TAG_OBATINDIKASI,
						listObat.get(position).getObat_indikasi());
				intent.putExtra(CommonConstants.TAG_OBATJENIS,
						listObat.get(position).getObat_jenis());
				intent.putExtra(CommonConstants.TAG_OBATHARGA,
						listObat.get(position).getObat_harga());
				intent.putExtra(CommonConstants.TAG_OBATPIC,
						listObat.get(position).getObat_pic());
				intent.putExtra(CommonConstants.TAG_OBATEFEKSAMPING, listObat
						.get(position).getObat_efeksamping());
				intent.putExtra(CommonConstants.TAG_OBATDOSIS,
						listObat.get(position).getObat_dosis());
				intent.putExtra(CommonConstants.TAG_OBATPERHATIAN, listObat
						.get(position).getObat_perhatian());
				intent.putExtra(CommonConstants.TAG_OBATISI,
						listObat.get(position).getObat_isi());
				intent.putExtra(CommonConstants.TAG_OBATKODE,
						listObat.get(position).getObat_kode());
				intent.putExtra("obat_pics", listObat.get(position)
						.getObat_pics());
				startActivityForResult(intent, 1);
			}
		};

		deleteOnClick = new OnClickListener() {

			@Override
			public void onClick(View v) {
				popup.dismiss();
				new AlertDialog.Builder(AdminHomeActivity.this)
						.setTitle("Delete")
						.setMessage(
								"Apakah anda yakin akan menghapus "
										+ listObat.get(position).getObat_nama()
										+ "?")
						.setPositiveButton(android.R.string.yes,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										new URLTask(
												deletionListener,
												AdminHomeActivity.this,
												CommonConstants.WEB_SERVICE_URL_DELETE_OBAT
														+ listObat.get(position).getObat_id(),
												CommonConstants.PLEASE_WAIT)
												.execute();
									}
								}).setNegativeButton("Tidak", null).show();
			}
		};

		dismissPopup = new OnClickListener() {

			@Override
			public void onClick(View v) {
				popup.dismiss();
			}
		};
	}

	ResultsListener deletionListener = new ResultsListener() {

		@Override
		public void onResultsSucceeded(String result, Context context) {
			try {
				JSONObject json = new JSONObject(result);
				int success = json.getInt(CommonConstants.TAG_SUCCESS);
				if (success == 1) {
					Toast.makeText(AdminHomeActivity.this,
							CommonConstants.DELETE_SUCCEED, Toast.LENGTH_SHORT)
							.show();
					listObat = new ArrayList<Obat>();
					new URLTask(listener, context,
							CommonConstants.WEB_SERVICE_URL_SEARCH + key,
							CommonConstants.PLEASE_WAIT).execute();
				} else {
					Toast.makeText(AdminHomeActivity.this,
							CommonConstants.DELETE_FAILED, Toast.LENGTH_SHORT)
							.show();
				}
			} catch (JSONException e) {
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
		}

		@Override
		public void onResultsFailed(Context context) {
			// TODO Auto-generated method stub

		}
	};

	private void showPopup(int position) {
		this.position = position;
		try {
			LayoutInflater inflater = (LayoutInflater) this
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.action_popup_layout,
					(ViewGroup) findViewById(R.id.action_popup_wrapper));

			popup = new PopupWindow(layout, LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, true);
			popup.showAtLocation(layout, Gravity.CENTER, 0, 0);

			TextView actionUpdateTV = (TextView) layout
					.findViewById(R.id.action_update_tv);
			TextView deleteUpdateTV = (TextView) layout
					.findViewById(R.id.action_delete_tv);
			TextView dismissTV = (TextView) layout
					.findViewById(R.id.cancel_popup_tv);

			actionUpdateTV.setOnClickListener(updateOnClick);
			deleteUpdateTV.setOnClickListener(deleteOnClick);
			layout.setOnClickListener(dismissPopup);
			dismissTV.setOnClickListener(dismissPopup);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		listObat = new ArrayList<Obat>();
		new URLTask(listener, context, CommonConstants.WEB_SERVICE_URL_SEARCH
				+ key, CommonConstants.PLEASE_WAIT).execute();
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
		inputTV = (TextView) findViewById(R.id.input_tv);
		inputTV.setVisibility(View.VISIBLE);

		if (isSearchForObat) {
			url = CommonConstants.WEB_SERVICE_URL_SEARCH;
			searchTitleTV.setText(CommonConstants.OBAT_SEARCH);
		} else {
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
					new URLTask(listener, context, url + key,
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

		inputTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AdminHomeActivity.this,
						InputActivity.class);
				startActivity(intent);
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
						obat.setObat_efeksamping(c
								.getString(CommonConstants.TAG_OBATEFEKSAMPING));
						obat.setObat_dosis(c
								.getString(CommonConstants.TAG_OBATDOSIS));
						obat.setObat_perhatian(c
								.getString(CommonConstants.TAG_OBATPERHATIAN));
						obat.setObat_isi(c
								.getString(CommonConstants.TAG_OBATISI));
						obat.setObat_pics(c
								.getString(CommonConstants.TAG_OBATPIC));

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
				searchCountTV.setText("" + listObat.size());
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
