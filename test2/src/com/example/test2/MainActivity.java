package com.example.test2;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends Activity {

	private ViewHelper mViewHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mViewHelper = new ViewHelper(this);
		View mainLayout = findViewById(R.id.main_layout);
		mViewHelper.setGlobalSize((ViewGroup) mainLayout);
	}
}