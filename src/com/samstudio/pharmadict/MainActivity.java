package com.samstudio.pharmadict;

import com.samstudio.pharmadict.R;
import com.samstudio.pharmadict.util.CommonConstants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText searchET;
	private Button searchB;
	private String keyword;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initUI();
		setCallBack();
	}

	public void initUI() {
		searchET = (EditText) findViewById(R.id.searchET);
		searchB = (Button) findViewById(R.id.searchB);
	}

	public void setCallBack() {
		searchB.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				keyword = searchET.getText().toString();
				if (keyword.length() != 0) {
					Intent intent = new Intent(MainActivity.this, SearchResultActivity.class);
					intent.putExtra(CommonConstants.KEYWORD, keyword);
					MainActivity.this.startActivity(intent);
				} else {
					Toast.makeText(MainActivity.this, CommonConstants.PLEASE_INPUT_KEYWORD,
							Toast.LENGTH_SHORT).show();
				}				
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}else if( id == R.id.action_login){
			Intent intent = new Intent(MainActivity.this, LoginActivity.class);
			MainActivity.this.startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
}
