package com.samstudio.pharmadict;

import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.samstudio.pharmadict.admin.AdminHomeActivity;
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
					Intent intent = new Intent(LoginActivity.this, AdminHomeActivity.class);
					LoginActivity.this.startActivity(intent);
				}else{
					Toast.makeText(LoginActivity.this, CommonConstants.INVALID_INPUT, Toast.LENGTH_SHORT).show();
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

}
