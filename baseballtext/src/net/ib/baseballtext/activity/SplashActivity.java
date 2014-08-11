package net.ib.baseballtext.activity;

import net.ib.baseballtext.R;
import net.ib.baseballtext.base.BaseActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;

public class SplashActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);
		
		Handler handler = new Handler() {
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				startActivity(new Intent (SplashActivity.this, CalendarActivity.class));
				overridePendingTransition(R.anim.fade, R.anim.hold);
				finish();
			}
		};
		handler.sendEmptyMessageDelayed(0, 500);
	}
}
