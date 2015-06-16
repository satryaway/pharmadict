package com.samstudio.pharmadict.helpers;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import com.samstudio.pharmadict.util.CommonConstants;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

public class URLTask extends AsyncTask<Void, Void, String> {
	private final ProgressDialog progress;
	private ResultsListener listener;
	private String url;
	private String progressMessage;
	private Context context;
	private HashMap<String, String> map;
	private enum RQST_TYPE {POST, GET}
	private RQST_TYPE rqstType;

	public URLTask(ResultsListener listener, Context context, String url, String progressMessage) {
		this.listener = listener;
		this.url = url;
		progress = new ProgressDialog(context);
		this.progressMessage = progressMessage;
		this.context = context;
		this.rqstType = RQST_TYPE.GET;
	}

	public URLTask(ResultsListener listener, Context context, String url, String progressMessage, HashMap<String, String> params) {
		this.listener = listener;
		this.url = url;
		progress = new ProgressDialog(context);
		this.progressMessage = progressMessage;
		this.context = context;
		this.rqstType = RQST_TYPE.POST;
		this.map = params;
	}

	@Override
	protected void onPreExecute() {

		if (progressMessage != null) {
			progress.setMessage(progressMessage);
			progress.setCancelable(false);
			progress.show();
		}
	}

	@Override
	protected String doInBackground(Void... voids) {
		String response = "";
		DefaultHttpClient client = new DefaultHttpClient();
		HttpParams httpParameters = client.getParams();
		HttpConnectionParams.setConnectionTimeout(httpParameters, CommonConstants.CONNECTION_TIMEOUT_IN_SEC * 1000);
		HttpConnectionParams.setSoTimeout(httpParameters, CommonConstants.CONNECTION_TIMEOUT_IN_SEC * 1000);

		if (rqstType == RQST_TYPE.GET){
			HttpGet httpGet = new HttpGet(url);
			try {
				HttpResponse execute = client.execute(httpGet);
				InputStream content = execute.getEntity().getContent();
				BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
				String s = "";
				while ((s = buffer.readLine()) != null) {
					response += s;
				}
			} catch (Exception e) {
			}
		} else {
			response = postData();
		}

		return response;
	}

 	public String postData() {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(this.url);
		String response = "";

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			Iterator it = map.entrySet().iterator();

			for (int i = 0; i < map.size(); i++){
				Map.Entry<String, Object>pairs = (Map.Entry<String, Object>) it.next();
				nameValuePairs.add(new BasicNameValuePair(pairs.getKey(), (String) pairs.getValue()));
			}

			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			try {
				HttpResponse execute = httpclient.execute(httppost);
				InputStream content = execute.getEntity().getContent();
				BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
				String s = "";
				while ((s = buffer.readLine()) != null) {
					response += s;
				}
			} catch (Exception e) {
			}

			return response;

		} catch (Exception e) {
			return "";
		}
	}

	@Override
	protected void onPostExecute(String result) {
		if (progress.isShowing() && progress != null)
			try {
				progress.dismiss();
			} catch (Exception ignored) {
			}
		if (result.equals(""))
			listener.onResultsFailed(context);
		else
			listener.onResultsSucceeded(result, context);
	}
}