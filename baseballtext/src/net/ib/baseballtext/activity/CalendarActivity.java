package net.ib.baseballtext.activity;

import java.util.List;

import kr.co.quizon.network.link.match.GetMatchListLink;
import net.ib.baseballtext.R;
import net.ib.quizon.api.match.GetMatchListReq;
import net.ib.quizon.api.match.GetMatchListRes;
import net.ib.quizon.domain.match.Match;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;



public class CalendarActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		CalendarView calendar = (CalendarView)findViewById(R.id.calendar);
		
		calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				Toast.makeText(CalendarActivity.this, "" + year + "/" + (month + 1) + "/" + dayOfMonth, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
//	private void setData() {
//		new AsyncTask<Void, Void, List<Match>>() {
//			@Override
//			protected List<Match> doInBackground(Void... params) {
//				// network process
//				GetMatchListReq getMatchListReq = new GetMatchListReq();
//				getMatchListReq.setAccessToken(ACCESS_TOKEN);
//				GetMatchListLink getMatchListLink = new GetMatchListLink(getMatchListReq);
//				GetMatchListRes matchListRes = getMatchListLink.linkage();
//				
//				if (matchListRes != null) {
//					return matchListRes.getMatchInfoList();
//				}
//				return null;
//			}
//
//			@Override
//			protected void onPostExecute(List<Match> result) {
//				// ui process
//				if (result != null) {
//					homeTeamNameText.setText(result.get(0).getHomeTeamName());
//					awayTeamNameText.setText(result.get(0).getAwayTeamName());
//					stadiumText.setText(result.get(0).getMatchStadium());
//					
//					
//					
//							pollingView.updateView(matchId);
//
//					
//					for (Match match : result) {
//						match.getMatchStatus();
//						matchId = match.getMatchId();
//						if (matchId != null) {
//							break;
//						}
//					}
//				} else {
//					stadiumText.setText("-");
//					homeTeamNameText.setText("---");
//					awayTeamNameText.setText("---");
//					homeTeamPointText.setText("-");
//					awayTeamPointText.setText("-");
//				}
//			}
//		}.execute();
//	}
}
