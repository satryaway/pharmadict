package com.samstudio.pharmadict;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.samstudio.pharmadict.helpers.ResultsListener;
import com.samstudio.pharmadict.helpers.URLTask;
import com.samstudio.pharmadict.util.CommonConstants;

public class LoginActivity extends Activity {

	private EditText usernameET, passwordET;
	private Button loginBT;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		initUI();
		setCallBack();
	}

	private void setCallBack() {
		loginBT.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String username = usernameET.getText().toString();
				String password = passwordET.getText().toString();

				if (username != "" && password != "") {
					// postData(username, password);
					HashMap<String, String> map = new HashMap<String, String>();
					map.put(CommonConstants.USERNAME, username);
					map.put(CommonConstants.PASSWORD, password);
					new URLTask(listener, LoginActivity.this,
							CommonConstants.WEB_SERVICE_URL_POST_FORM,
							CommonConstants.PLEASE_WAIT, map).execute();
				} else {
					Toast.makeText(LoginActivity.this,
							CommonConstants.INPUT_USENAME_AND_PASSWORD,
							Toast.LENGTH_SHORT);
				}

			}
		});
	}

	ResultsListener listener = new ResultsListener() {

		@Override
		public void onResultsSucceeded(String result, Context context) {
			try{
				JSONObject json = new JSONObject(result);
				int success = json.getInt(CommonConstants.TAG_SUCCESS);
				if (success == 1) {
					Toast.makeText(LoginActivity.this, "Berhasil", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(LoginActivity.this, "Gagal", Toast.LENGTH_SHORT).show();
				}
			}catch(JSONException e){
				Log.e("JSON Parser", "Error parsing data " + e.toString());
			}
		}

		@Override
		public void onResultsFailed(Context context) {

		}
	};

	private void initUI() {
		usernameET = (EditText) findViewById(R.id.usernameET);
		passwordET = (EditText) findViewById(R.id.passwordET);
		loginBT = (Button) findViewById(R.id.loginBT);
	}

	public void postData(String username, String password) {
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				CommonConstants.WEB_SERVICE_URL_POST_FORM);

		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request
			HttpResponse response = httpclient.execute(httppost);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}
}
