package net.ib.baseballtext.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.quizon.network.HttpLib;
import kr.co.quizon.network.link.match.GetMatchListLink;
import net.ib.baseballtext.R;
import net.ib.baseballtext.R.id;
import net.ib.baseballtext.R.layout;
import net.ib.baseballtext.util.Strings;
import net.ib.baseballtext.view.TextPollingView;
import net.ib.quizon.api.match.GetMatchListReq;
import net.ib.quizon.api.match.GetMatchListRes;
import net.ib.quizon.domain.match.Match;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
//	private final String ACCESS_TOKEN = "b867b048-8f20-4a01-bfc4-53784e4b488e"; // Test
	private final String ACCESS_TOKEN = "35fa9897-c723-44a7-a562-bcabd76b2fc0"; // release
    
	private Timer timer;
	private TimerTask timerTask = new TimerJob();
	private int delay = 1000;
	private int period = 5000;
	
	// Data
    private String matchId;
	private List<String> spinnerList = new ArrayList<String>();
	private List<String> matchIdList = new ArrayList<String>();
	private ArrayAdapter<String> adapter;

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
    
    private View titleView;
    private View mainView;
    
    private Button button;
    
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        titleView = findViewById(R.id.title);
        mainView = findViewById(R.id.main);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        
        baseballText = (TextView) findViewById(R.id.baseballtext);
        homeTeamNameText = (TextView) findViewById(R.id.hometeamname);
        awayTeamNameText = (TextView) findViewById(R.id.awayteamname);
        stadiumText = (TextView) findViewById(R.id.stadium);
        inningText = (TextView) findViewById(R.id.inning);
        homeTeamPointText = (TextView) findViewById(R.id.hometeampoint);
        awayTeamPointText = (TextView) findViewById(R.id.awayteampoint);
        
        matchIdList = new ArrayList<String>();
        spinner = (Spinner) findViewById(R.id.selectmatchspinner);
        spinner.setPrompt("원하는 경기를 선택하세요");
        spinnerList = new ArrayList<String>();
        
        pollingView = new TextPollingView(titleView, mainView);
    }

	@Override
	protected void onResume() {
		super.onResume();
//		HttpLib.setTest(true);
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
	

	private void setData() {
		new AsyncTask<Void, Void, List<Match>>() {
			@Override
			protected List<Match> doInBackground(Void... params) {
				// network process
				GetMatchListReq getMatchListReq = new GetMatchListReq();
				getMatchListReq.setAccessToken(ACCESS_TOKEN);
				GetMatchListLink getMatchListLink = new GetMatchListLink(getMatchListReq);
				GetMatchListRes matchListRes = getMatchListLink.linkage();
				
				
				Log.i("matchListRes", matchListRes + "");
				if (matchListRes != null) {
					return matchListRes.getMatchInfoList();
				}
				Log.i("dsdf","adsf");
				return null;
			}

			@Override
			protected void onPostExecute(List<Match> result) {
				// ui process
				Log.i("result", result.toString());
				if (Strings.isNotEmptyString(result.toString()) && result.size() > 0) {
					homeTeamNameText.setText(result.get(0).getHomeTeamName());
					awayTeamNameText.setText(result.get(0).getAwayTeamName());
					stadiumText.setText(result.get(0).getMatchStadium());
					
					spinnerList.clear();
					for (int i=0; i<result.size(); i++) {
						matchIdList.add(result.get(i).getMatchId());
						spinnerList.add(result.get(i).getHomeTeamName() + " vs " + result.get(i).getAwayTeamName());
					}
					
					adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.matchspinner, spinnerList);
					spinner.setAdapter(adapter);
					
					spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
//							Log.i("test", parent.getItemIdAtPosition(position) + "");
//							Log.i("test1", position + "");
							switch (position) {
							case 0:
								matchId = matchIdList.get(0);
								break;
							case 1:
								matchId = matchIdList.get(1);
								break;
							case 2:
								matchId = matchIdList.get(2);
								break;
							case 3:
								matchId = matchIdList.get(3);
								break;
							default:
								break;
							}
							pollingView.updateView(matchId);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
						}
					});
					
					for (Match match : result) {
						match.getMatchStatus();
						matchId = match.getMatchId();
						if (matchId != null) {
							break;
						}
					}
				} else {
					baseballText.setText("매치 정보 로딩 실패\n아직 경기가 열리지 않았습니다.");
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

	@Override
	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.button:
//			if (matchId != null) {
//				pollingView.updateView(matchId);
//			}
//			break;
//		default:
//			break;
//		}
	}
	
	class TimerJob extends TimerTask {
		@Override
		public void run() {
//			pollingView.getInning();
			pollingView.updateView(matchId);
		}
	}
}