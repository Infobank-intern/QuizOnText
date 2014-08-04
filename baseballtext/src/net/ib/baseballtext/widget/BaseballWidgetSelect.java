package net.ib.baseballtext.widget;

import java.util.ArrayList;
import java.util.List;

import kr.co.quizon.network.HttpLib;
import kr.co.quizon.network.link.match.GetMatchListLink;
import net.ib.baseballtext.R;
import net.ib.baseballtext.activity.MainActivity;
import net.ib.baseballtext.util.Strings;
import net.ib.quizon.api.match.GetMatchListReq;
import net.ib.quizon.api.match.GetMatchListRes;
import net.ib.quizon.domain.match.Match;
import android.app.Activity;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;


public class BaseballWidgetSelect extends Activity implements OnClickListener {
	private final String ACCESS_TOKEN = "b867b048-8f20-4a01-bfc4-53784e4b488e"; // Test
//	private final String ACCESS_TOKEN = "35fa9897-c723-44a7-a562-bcabd76b2fc0"; // release
	
	private TextView firstGameText;
	private TextView secondGameText;
	private TextView thirdGameText;
	private TextView fourthGameText;
	
	private String matchId;
	private List<String> matchIdList;
	
	final static String PREF = "BaseballWidgetSelect";
	private int mId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.baseballwidget_select);
        
        HttpLib.setTest(true);
        
        matchIdList = new ArrayList<String>();
        
        firstGameText = (TextView) findViewById(R.id.firstgame);
        secondGameText = (TextView) findViewById(R.id.secondgame);
        thirdGameText = (TextView) findViewById(R.id.thirdgame);
        fourthGameText = (TextView) findViewById(R.id.fourthgame);
        
        setResult(RESULT_CANCELED);
        
        Intent intent = getIntent();
        mId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        SharedPreferences prefs = getSharedPreferences(PREF, 0);
        
        //
        setData();
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
// 매치아이디 받았고..   뭐해야되지??   어떻게 넘겨줘?  음................................아아아
// 처음 바로 이화면 나오는데, 그것도 좀 이상해..  인텐트로는 못넘기나?
// 앱, 처음 이닝으로 안나와 왜지??????
			@Override
			protected void onPostExecute(List<Match> result) {
				// ui process
				if (Strings.isNotEmptyString(result.toString()) && result.size() > 0) {
					if (result.size() >= 1)
						firstGameText.setText(result.get(0).getHomeTeamName() + " vs " + result.get(0).getAwayTeamName());
					if (result.size() >= 2)
						secondGameText.setText(result.get(1).getHomeTeamName() + " vs " + result.get(1).getAwayTeamName());
					if (result.size() >= 3)
						thirdGameText.setText(result.get(2).getHomeTeamName() + " vs " + result.get(2).getAwayTeamName());
					if (result.size() >= 4)
						fourthGameText.setText(result.get(3).getHomeTeamName() + " vs " + result.get(3).getAwayTeamName());

					for (Match match : result) {
						match.getMatchStatus();
						matchId = match.getMatchId();
						matchIdList.add(matchId);
						if (matchId != null) {
							break;
						}
					}
				} else {
					firstGameText.setText("경기가 없습니다. 경기 시간을 확인해 주세요");
					secondGameText.setText("경기가 없습니다. 경기 시간을 확인해 주세요");
					thirdGameText.setText("경기가 없습니다. 경기 시간을 확인해 주세요");
					fourthGameText.setText("경기가 없습니다. 경기 시간을 확인해 주세요");
				}
			} 
		}.execute();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.firstgame:
			SharedPreferences prefs = getSharedPreferences(PREF, 0);
			SharedPreferences.Editor editor = prefs.edit();
			editor.putInt("select_" + mId, 0);
			editor.commit();
			
			Context con = BaseballWidgetSelect.this;
			
			
			break;

		case R.id.secondgame:

			break;

		case R.id.thirdgame:

			break;

		case R.id.fourthgame:

			break;

		default:
			break;
		}
	}
}
