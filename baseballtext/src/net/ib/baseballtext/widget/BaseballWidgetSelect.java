package net.ib.baseballtext.widget;

import java.util.ArrayList;
import java.util.List;

import net.ib.baseballtext.widget.BaseballWidget;
import kr.co.quizon.network.HttpLib;
import kr.co.quizon.network.link.match.GetMatchListLink;
import net.ib.baseballtext.R;
import net.ib.baseballtext.activity.MainActivity;
import net.ib.baseballtext.util.Strings;
import net.ib.quizon.api.match.GetMatchListReq;
import net.ib.quizon.api.match.GetMatchListRes;
import net.ib.quizon.domain.match.Match;
import android.app.Activity;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
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
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class BaseballWidgetSelect extends Activity implements OnClickListener {
	private final String ACCESS_TOKEN = "b867b048-8f20-4a01-bfc4-53784e4b488e"; // Test
//	private final String ACCESS_TOKEN = "35fa9897-c723-44a7-a562-bcabd76b2fc0"; // release
	
	private final String ACTION_SELECT = "GAME";
	
	private TextView firstGameText;
	private TextView secondGameText;
	private TextView thirdGameText;
	private TextView fourthGameText;
	
	private String matchId;
	private List<String> matchIdList;
	
	private Context context;
	private AppWidgetManager appWidgetManager;
	private ComponentName provider;
	private int[] ids;
	private Intent gameIntent;
	
	
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
        
        firstGameText.setOnClickListener(this);
        secondGameText.setOnClickListener(this);
        thirdGameText.setOnClickListener(this);
        fourthGameText.setOnClickListener(this);
        
        context = this;
		appWidgetManager = AppWidgetManager.getInstance(context);
		provider = new ComponentName("net.ib.baseballtext.widget", "net.ib.baseballtext.widget.BaseballWidget");
		ids = appWidgetManager.getAppWidgetIds(provider);
        
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
			gameIntent = new Intent(this, BaseballWidget.class);
			gameIntent.setAction(ACTION_SELECT);
			gameIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
			gameIntent.putExtra("selectGame", 0);
			sendBroadcast(gameIntent);
			break;

		case R.id.secondgame:
			gameIntent = new Intent(this, BaseballWidget.class);
			gameIntent.setAction(ACTION_SELECT);
			gameIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
			gameIntent.putExtra("selectGame", 1);
			sendBroadcast(gameIntent);
			
			break;

		case R.id.thirdgame:

			break;

		case R.id.fourthgame:

			break;

		default:
			break;
		}
		finish();
	}
}
