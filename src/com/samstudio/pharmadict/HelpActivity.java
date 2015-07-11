package com.samstudio.pharmadict;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class HelpActivity extends Activity {
	LinearLayout closeWrapper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_layout);
		initUI();
		setCallBack();
	}
	
	public void initUI(){
		closeWrapper = (LinearLayout) findViewById(R.id.close_help_wrapper);
	}
	
	public void setCallBack(){
		closeWrapper.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
}
