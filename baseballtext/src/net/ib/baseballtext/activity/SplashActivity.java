package net.ib.baseballtext.activity;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.quizon.network.link.match.GetMatchByMonthLink;
import net.ib.baseballtext.R;
import net.ib.baseballtext.match.MatchByMonth;
import net.ib.baseballtext.util.ViewHelper;
import net.ib.quizon.api.match.GetMatchByMonthReq;
import net.ib.quizon.api.match.GetMatchByMonthRes;
import net.ib.quizon.domain.match.Match;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

public class SplashActivity extends Activity {
	private ViewHelper mViewHelper;

	private long currentTime;
	private int currentMonth;
	private int currentDate;

	private List<Match> matchList;
	private List<Match>	currentMatchList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_splash);

		mViewHelper = new ViewHelper(this);
		View splashLayout = findViewById(R.id.splash_layout);
		mViewHelper.setGlobalSize((ViewGroup) splashLayout);

		matchList = new ArrayList<Match>();
		currentMatchList = new ArrayList<Match>();

		final SharedPreferences pref = getSharedPreferences("Pref1", 0);
		final Editor prefEdit = pref.edit();

		final CharSequence[] teamName = {"SAMSUNG LIONS", "DOOSAN BEARS", "LG TWINS", "NEXEN HEROES",
				"LOTTE GIANTS", "SK WYVERNS", "NC DINOS", "KIA TIGERS", "HANWHA EAGLES"};

		currentTime = System.currentTimeMillis();
		Date date = new Date(currentTime);
		currentMonth = date.getMonth();
		currentDate = date.getDate();

		MatchByMonth matchByMonth = new MatchByMonth(currentMonth);
		matchByMonth.setDate();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		matchList = matchByMonth.getMatchList();

		if (matchList != null) {
			for (int i=0; i<matchList.size(); i++) {
				Date matchDate = new Date(matchList.get(i).getMatchDate());
				if( (matchDate.getMonth()==currentMonth) && (matchDate.getDate()==currentDate) ) {
//					Log.i("matchList.get(i)", matchList.get(i) + "");
					currentMatchList.add(matchList.get(i));
				}
			}
		}

		int whitchTeam = pref.getInt("whitchTeam", 10); 
		if ( (whitchTeam < 9) && (currentMatchList != null) ) {
			int count = 0;
			for(int i=0; i<currentMatchList.size(); i++) {
//				Log.i("여기는 올거고", "여기는 올거고");
//				Log.i("currentMatchList.get(i).getHomeTeamName()", "*" + currentMatchList.get(i).getHomeTeamName() + "*");
//				Log.i("teamName[whitchTeam]", "*" + (String) teamName[whitchTeam] + "*");
//				Log.i("currentMatchList.get(i).getAwayTeamName()", "*" + currentMatchList.get(i).getAwayTeamName() + "*");
//				Log.i("count", count + "");
				if ((currentMatchList.get(i).getHomeTeamName().equals((String) teamName[whitchTeam])) || (currentMatchList.get(i).getAwayTeamName().equals((String) teamName[whitchTeam]))) {
					Log.i("for 안", "TRUE");
					final Intent intent = new Intent(SplashActivity.this, MainActivity.class);
					intent.putExtra("selectMatch", i);
					Handler handler = new Handler() {
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							startActivity(intent);
							overridePendingTransition(R.anim.fade, R.anim.hold);
							finish();
						}
					};
					handler.sendEmptyMessageDelayed(0, 1000);
					break;
				}
				count ++;
				if (count == currentMatchList.size()) {
					Log.i("for 안", "FALSE");
					final Intent intent1 = new Intent(SplashActivity.this, CalendarActivity.class);
					Handler handler = new Handler() {
						public void handleMessage(Message msg) {
							super.handleMessage(msg);
							startActivity(intent1);
							overridePendingTransition(R.anim.fade, R.anim.hold);
							finish();
						}
					};
					handler.sendEmptyMessageDelayed(0, 1000);
					break;
				}
			}
		} 
		else {
			Log.i("for 밖", "-------");
			Handler handler = new Handler() {
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
					startActivity(new Intent (SplashActivity.this, CalendarActivity.class));
					overridePendingTransition(R.anim.fade, R.anim.hold);
					finish();
				}
			};
			handler.sendEmptyMessageDelayed(0, 1000);
		}
	}
}