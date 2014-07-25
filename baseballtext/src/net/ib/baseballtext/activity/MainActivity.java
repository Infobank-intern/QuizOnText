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

	// JOB
	private TextPollingView pollingView;
	
	// UI
	private Spinner spinner;
    private TextView baseballText;
    
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
				if (result != null) {
					spinnerList.clear();
					for (int i=0; i<result.size(); i++) {
						matchIdList.add(result.get(i).getMatchId());
						spinnerList.add(result.get(i).getHomeTeamName() + " vs " + result.get(i).getAwayTeamName());
					}
					
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.matchspinner, spinnerList);
					spinner.setAdapter(adapter);
					
					spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							Log.i("test", parent.getItemIdAtPosition(position) + "");
							Log.i("test1", position + "");
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
					baseballText.setText("매치 정보 로딩 실패");
				}
			}
		}.execute();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button:
			if (matchId != null) {
				pollingView.updateView(matchId);
			}
			break;
		default:
			break;
		}
	}
	
	class TimerJob extends TimerTask {
		@Override
		public void run() {
			pollingView.updateView(matchId);
		}
	}
}