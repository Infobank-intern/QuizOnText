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
	private int matchListSize = 0;

	private List<Match> matchList;
	private List<Match>	currentMatchList;
	
	private SharedPreferences sharedPref;

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
		
		sharedPref = getSharedPreferences("Pref2", 0);
		final Editor sharedPrefEdit = sharedPref.edit();

		final CharSequence[] teamName = {"SAMSUNG LIONS", "DOOSAN BEARS", "LG TWINS", "NEXEN HEROES",
				"LOTTE GIANTS", "SK WYVERNS", "NC DINOS", "KIA TIGERS", "HANWHA EAGLES"};

		currentTime = System.currentTimeMillis();
		Date date = new Date(currentTime);
		currentMonth = date.getMonth();
		currentDate = date.getDate();

// 하루지나면 초기화
		if (sharedPref.getInt("currentDate", 0) != currentDate) {
			sharedPrefEdit.putInt("matchListSize", 0);
			sharedPrefEdit.putString("firstSelectName", null);
			sharedPrefEdit.putString("secondSelectName", null);
			sharedPrefEdit.putString("thirdSelectName", null);
			sharedPrefEdit.putString("fourthSelectName", null);
		}
		
		MatchByMonth matchByMonth = new MatchByMonth(currentMonth);
		matchByMonth.setDate();
		try {
			Thread.sleep(1500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		matchList = matchByMonth.getMatchList();

		// 오늘 경기 List 만 뽑아 , currentMatchList
		if (matchList != null) {
			for (int i=0; i<matchList.size(); i++) {
				Date matchDate = new Date(matchList.get(i).getMatchDate());
				if( (matchDate.getMonth()==currentMonth) && (matchDate.getDate()==currentDate) ) {
					++matchListSize;
					currentMatchList.add(matchList.get(i));
				}
			}
		}

		Log.i("@@@matchList", matchList + "");
		Log.i("@@@currentMatchList", currentMatchList + "");
		
		
		sharedPrefEdit.putInt("matchListSize", matchListSize);
		sharedPrefEdit.putInt("currentDate", currentDate);
		sharedPrefEdit.commit();
		if (1 <= matchListSize) {
			sharedPrefEdit.putString("firstSelectName", currentMatchList.get(0).getHomeTeamName());
			sharedPrefEdit.commit();
		}
		if (2 <= matchListSize) {
			sharedPrefEdit.putString("secondSelectName", currentMatchList.get(1).getHomeTeamName());
			sharedPrefEdit.commit();
		}
		if (3 <= matchListSize) {
			sharedPrefEdit.putString("thirdSelectName", currentMatchList.get(2).getHomeTeamName());
			sharedPrefEdit.commit();
		}
		if (4 <= matchListSize) {
			sharedPrefEdit.putString("fourthSelectName", currentMatchList.get(3).getHomeTeamName());
			sharedPrefEdit.commit();
		}
		
		
		int whitchTeam = pref.getInt("whitchTeam", 10); // 선호구단 있는지 
		if ( (whitchTeam < 9) && (currentMatchList != null) ) {
			int count = 0;
			for(int i=0; i<currentMatchList.size(); i++) {
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
					handler.sendEmptyMessageDelayed(0, 1200);
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
					handler.sendEmptyMessageDelayed(0, 1200);
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
			handler.sendEmptyMessageDelayed(0, 1200);
		}
	}
}