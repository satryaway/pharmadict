package com.samstudio.pharmadict;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class AboutActivity extends Activity{
	LinearLayout closeAboutWrapper, authorWrapper;
	Button showMoreBtn, loginAdmintBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about_layout);
		initUI();
		setCallBack();
	}
	
	private void initUI(){
		closeAboutWrapper = (LinearLayout)findViewById(R.id.close_about_wrapper);
		authorWrapper = (LinearLayout)findViewById(R.id.author_wrapper);
		showMoreBtn = (Button) findViewById(R.id.show_more_btn);
		loginAdmintBtn = (Button) findViewById(R.id.login_admin_btn);
	}
	
	private void setCallBack(){
		closeAboutWrapper.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		showMoreBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(authorWrapper.getVisibility() == View.INVISIBLE){
					authorWrapper.setVisibility(View.VISIBLE);
				}else{
					authorWrapper.setVisibility(View.INVISIBLE);
				}
			}
		});
		
		loginAdmintBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AboutActivity.this, LoginActivity.class);
				startActivity(intent);
			}
		});
	}

}
