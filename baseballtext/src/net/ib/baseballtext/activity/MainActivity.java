package net.ib.baseballtext.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.quizon.network.link.match.GetMatchListLink;
import net.ib.baseballtext.R;
import net.ib.baseballtext.util.ViewHelper;
import net.ib.baseballtext.view.TextPollingView;
import net.ib.quizon.api.match.GetMatchListReq;
import net.ib.quizon.api.match.GetMatchListRes;
import net.ib.quizon.domain.match.Match;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;


public class MainActivity extends Activity implements OnTouchListener {
//	private final String ACCESS_TOKEN = "b867b048-8f20-4a01-bfc4-53784e4b488e"; // Test
	private final String ACCESS_TOKEN = "35fa9897-c723-44a7-a562-bcabd76b2fc0"; // release
	
	private ViewHelper mViewHelper;

	private Timer timer;
	private TimerTask timerTask = new TimerJob();
	private int delay = 1000;
	private int period = 10000;

	// Data
	private String matchId;
	private List<String> spinnerList = new ArrayList<String>();
	private List<Match> matchTempList = new ArrayList<Match>();
	private static List<Match> matchList	= new ArrayList<Match>();
	private ArrayAdapter<String> adapter;
	private int inning;
	private float pressedX;
	private int getSelectMatch;
	private static int getMatchListSize;
	private String getFirstSelectName;
	private String getSecondSelectName;
	private String getThirdSelectName;
	private String getFourthSelectName;
	
	// JOB
	private TextPollingView pollingView;

	// UI
	private Spinner spinner;
	private TextView baseballText;
	private TextView homeTeamNameText;
	private TextView awayTeamNameText;
	private TextView stadiumText;
	private TextView inningText;
	private TextView homeTeamPointText;
	private TextView awayTeamPointText;
	private ScrollView baseballTextScroll;

	private View titleView;
	private View mainView;

	private int matchNumber;
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mViewHelper = new ViewHelper(this);
		View mainLayout = findViewById(R.id.main_layout);
		mViewHelper.setGlobalSize((ViewGroup) mainLayout);
		
		titleView = findViewById(R.id.title);
		mainView = findViewById(R.id.main);

		baseballText = (TextView) findViewById(R.id.baseballtext);
		homeTeamNameText = (TextView) findViewById(R.id.hometeamname);
		awayTeamNameText = (TextView) findViewById(R.id.awayteamname);
		stadiumText = (TextView) findViewById(R.id.stadium);
		inningText = (TextView) findViewById(R.id.inning);
		homeTeamPointText = (TextView) findViewById(R.id.hometeampoint);
		awayTeamPointText = (TextView) findViewById(R.id.awayteampoint);
		baseballTextScroll = (ScrollView) findViewById(R.id.baseballtextScroll);

		matchTempList = new ArrayList<Match>();
		for (int i=0; i<4; i++) {
			matchTempList.add(null);
		}
//		Log.i("matchTempList.size()", matchTempList.size() + "");
		
		spinner = (Spinner) findViewById(R.id.selectmatchspinner);
		spinner.setPrompt("원하는 경기를 선택하세요");
		spinnerList = new ArrayList<String>();

		mContext = MainActivity.this;
		
		pollingView = new TextPollingView(titleView, mainView, mContext);
		
		SharedPreferences sharedPref = getSharedPreferences("Pref2", 0);
		Editor sharedPrefEdit = sharedPref.edit();

//		getMatchListSize = getIntent().getIntExtra("matchListSize", 0);
		getMatchListSize = sharedPref.getInt("matchListSize", 0);
		
		getSelectMatch = getIntent().getIntExtra("selectMatch", 0);
		
		Log.i("getMatchListSize", getMatchListSize + ""	);
		if (1 <= getMatchListSize) {
//			getFirstSelectName = getIntent().getStringExtra("firstSelectName");
			getFirstSelectName = sharedPref.getString("firstSelectName", null);
			Log.i("getSelectName", getFirstSelectName);
		}
		if (2 <= getMatchListSize) {
//			getSecondSelectName= getIntent().getStringExtra("secondSelectName");
			getSecondSelectName = sharedPref.getString("secondSelectName", null);
			Log.i("getSelectName", getSecondSelectName);
		}
		if (3 <= getMatchListSize) {
//			getThirdSelectName = getIntent().getStringExtra("thirdSelectName");
			getThirdSelectName = sharedPref.getString("thirdSelectName", null);
			Log.i("getSelectName", getThirdSelectName);
		}
		if (4 <= getMatchListSize) {
//			getFourthSelectName = getIntent().getStringExtra("fourthSelectName");
			getFourthSelectName = sharedPref.getString("fourthSelectName", null);
			Log.i("getSelectName", getFourthSelectName);
		}
		
		baseballTextScroll.setOnTouchListener(this);
		
	}
	
	private void setData() {
		new AsyncTask<Void, Void, List<Match>>() {
			@Override
			protected List<Match> doInBackground(Void... params) {
				// network process
				GetMatchListReq getMatchListReq = new GetMatchListReq();
				getMatchListReq.setAccessToken(ACCESS_TOKEN);
				GetMatchListLink getMatchListLink = new GetMatchListLink(getMatchListReq);
				GetMatchListRes matchListRes = getMatchListLink.linkage();

				if (matchListRes != null) {
					return matchListRes.getMatchInfoList();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(List<Match> result) { 
				// ui process
				if (result != null) {
//					Log.i("setData_GetMatchInfoReq_result", result + "");
					spinnerList.clear();

					for (int i=0; i<getMatchListSize; i++) {
						Log.i("result.get(i).getHomeTeamName()", result.get(i).getHomeTeamName());
						if (getFirstSelectName != null && getFirstSelectName.equals(result.get(i).getHomeTeamName())) {
							matchTempList.set(0, result.get(i));
						}
						
						if (getSecondSelectName != null && getSecondSelectName.equals(result.get(i).getHomeTeamName())) {
							matchTempList.set(1, result.get(i));
						}
					
						if (getThirdSelectName != null && getThirdSelectName.equals(result.get(i).getHomeTeamName())) {
							matchTempList.set(2, result.get(i));
						}
						
						if (getFourthSelectName != null && getFourthSelectName.equals(result.get(i).getHomeTeamName())) {
							matchTempList.set(3, result.get(i));
						}
					}
					
					matchList = matchTempList;
//					Log.i("matchTempList", matchTempList.get(0) + "");
//					Log.i("matchTempList", matchTempList.get(1) + "");
//					Log.i("matchTempList", matchTempList.get(2) + "");
//					Log.i("matchTempList", matchTempList.get(3) + "");
//					Log.i("matchTempList", matchTempList.size() + "");
					Log.i("matchList", matchList + "");
					
					
					for (int i=0; i<result.size(); i++) {
						spinnerList.add(matchList.get(i).getHomeTeamName() + " vs " + matchList.get(i).getAwayTeamName());
					}
					
					adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.matchspinner, spinnerList);
					spinner.setAdapter(adapter);
					spinner.setSelection(getSelectMatch);
					Log.i("selectMatch", getSelectMatch + "");
					
					spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
							switch (position) {
							case 0:
								matchNumber = 0;
								getSelectMatch = 0;
								matchId = matchList.get(0).getMatchId();
								homeTeamNameText.setText(matchList.get(0).getHomeTeamName());
								awayTeamNameText.setText(matchList.get(0).getAwayTeamName());
								stadiumText.setText(matchList.get(0).getMatchStadium());
								break;
							case 1:
								matchNumber = 1;
								getSelectMatch = 1;
								matchId = matchList.get(1).getMatchId();
								homeTeamNameText.setText(matchList.get(1).getHomeTeamName());
								awayTeamNameText.setText(matchList.get(1).getAwayTeamName());
								stadiumText.setText(matchList.get(1).getMatchStadium());
								break;
							case 2:
								matchNumber = 2;
								getSelectMatch = 2;
								matchId = matchList.get(2).getMatchId();
								homeTeamNameText.setText(matchList.get(2).getHomeTeamName());
								awayTeamNameText.setText(matchList.get(2).getAwayTeamName());
								stadiumText.setText(matchList.get(2).getMatchStadium());
								break;
							case 3:
								matchNumber = 3;
								getSelectMatch = 3;
								matchId = matchList.get(3).getMatchId();
								homeTeamNameText.setText(matchList.get(3).getHomeTeamName());
								awayTeamNameText.setText(matchList.get(3).getAwayTeamName());
								stadiumText.setText(matchList.get(3).getMatchStadium());
								break;
							default:
								break;
							}
							pollingView.getPresentInning(matchId, matchList, matchNumber);
						}
						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});
				} else {
					baseballText.setText("매치 정보 로딩 실패");
					spinnerList.add("아직 경기가 열리지 않았습니다.");
					adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.matchspinner, spinnerList);
					spinner.setAdapter(adapter);
					stadiumText.setText("-");
					inningText.setText("-");
					homeTeamNameText.setText("---");
					awayTeamNameText.setText("---");
					homeTeamPointText.setText("-");
					awayTeamPointText.setText("-");
				}
			}
		}.execute();
	}
	

	class TimerJob extends TimerTask {
		@Override
		public void run() {
			inning = pollingView.getInning();
			pollingView.updateView(matchId, inning);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		HttpLib.setTest(true);
		homeTeamNameText.setTextColor(Color.WHITE);
		awayTeamNameText.setTextColor(Color.WHITE);
		setData();
		timerTask = new TimerJob();
		timer = new Timer();
		timer.schedule(timerTask, delay, period);
	}

	@Override
	protected void onPause() {
		super.onPause();
		timerTask.cancel();
		timer.cancel();
		timerTask = null;
		timer = null;
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
		startActivity(intent);
		finish();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		float distance = 0;
//
//		switch(event.getAction()) {
//		case MotionEvent.ACTION_DOWN:
//			// 손가락을 touch 했을 떄 x 좌표값 저장
//			pressedX = event.getX();
//			break;
//		case MotionEvent.ACTION_UP:
//			// 손가락을 떼었을 때 저장해놓은 x좌표와의 거리 비교
//			distance = pressedX - event.getX();
//			break;
//		}
//
//		// 해당 거리가 100이 되지 않으면 이벤트 처리 하지 않는다.
//		if (Math.abs(distance) < 100) {
//			return false;	
//		}
//
//		Intent intent = new Intent(MainActivity.this, MainActivity.class);
//		if (distance > 0) {
//			// 손가락을 왼쪽으로 움직였으면 오른쪽 화면이 나타나야 한다.
//			intent.putExtra("selectMatch", (getSelectMatch+1)%matchTempListSize );
//		} else {
//			// 손가락을 오른쪽으로 움직였으면 왼쪽 화면이 나타나야 한다.
//			if (getSelectMatch == 0) {
//				intent.putExtra("selectMatch", matchTempListSize-1);
//			} else {
//				intent.putExtra("selectMatch", (getSelectMatch-1));
//			}
//		}
//		startActivity(intent);
//		finish(); // finish 해주지 않으면 activity가 계속 쌓인다. 

		return true;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		float distance = 0;
		
		switch(event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 손가락을 touch 했을 떄 x 좌표값 저장
			pressedX = event.getX();
			break;
		case MotionEvent.ACTION_UP:
			// 손가락을 떼었을 때 저장해놓은 x좌표와의 거리 비교
			distance = pressedX - event.getX();
			break;
		}

		// 해당 거리가 100이 되지 않으면 이벤트 처리 하지 않는다.
		if (Math.abs(distance) < 100) {
			return false;	
		}

		Intent intent = new Intent(MainActivity.this, MainActivity.class);
		if (distance > 0) {
			// 손가락을 왼쪽으로 움직였으면 오른쪽 화면이 나타나야 한다.
			intent.putExtra("selectMatch", (getSelectMatch+1)%getMatchListSize );
		} else {
			// 손가락을 오른쪽으로 움직였으면 왼쪽 화면이 나타나야 한다.
			if (getSelectMatch == 0) {
				intent.putExtra("selectMatch", getMatchListSize-1);
			} else {
				intent.putExtra("selectMatch", (getSelectMatch-1));
			}
		}
		startActivity(intent);
		finish(); // finish 해주지 않으면 activity가 계속 쌓인다. 

		return true;
	}
	
}