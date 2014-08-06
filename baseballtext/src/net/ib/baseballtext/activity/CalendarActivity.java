package net.ib.baseballtext.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.quizon.network.link.match.GetMatchByMonthLink;
import net.ib.baseballtext.R;
import net.ib.quizon.api.match.GetMatchByMonthReq;
import net.ib.quizon.api.match.GetMatchByMonthRes;
import net.ib.quizon.domain.match.Match;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;




public class CalendarActivity extends Activity implements OnClickListener {
	private CalendarView calendar;
	
	private TextView firstHomeTeamNameCalendarText;
	private TextView firstAwayTeamNameCalendarText;
	private TextView firstMatchTimeText;
	private TextView firstMatchStadiumText;
	private TextView secondHomeTeamNameCalendarText;
	private TextView secondAwayTeamNameCalendarText;
	private TextView secondMatchTimeText;
	private TextView secondMatchStadiumText;
	private TextView thirdHomeTeamNameCalendarText;
	private TextView thirdAwayTeamNameCalendarText;
	private TextView thirdMatchTimeText;
	private TextView thirdMatchStadiumText;
	private TextView fourthHomeTeamNameCalendarText;
	private TextView fourthAwayTeamNameCalendarText;
	private TextView fourthMatchTimeText;
	private TextView fourthMatchStadiumText;
	
	private Button firstMatchButton;
	private Button secondMatchButton;
	private Button thirdMatchButton;
	private Button fourthMatchButton;
	
	private long currentTime;
	private int currentMonth;
	private int currentDate;
	
	private Intent intent;
	
	private List<Match> matchList;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar);

		firstHomeTeamNameCalendarText = (TextView) findViewById(R.id.firsthometeamnamecalendar);
		firstAwayTeamNameCalendarText = (TextView) findViewById(R.id.firstawayteamnamecalendar);
		firstMatchTimeText = (TextView) findViewById(R.id.firstmatchTimecalendar);
		firstMatchStadiumText = (TextView) findViewById(R.id.firstmatchStadiumcalendar);
		
		secondHomeTeamNameCalendarText = (TextView) findViewById(R.id.secondhometeamnamecalendar);
		secondAwayTeamNameCalendarText = (TextView) findViewById(R.id.secondawayteamnamecalendar);
		secondMatchTimeText = (TextView) findViewById(R.id.secondmatchTimecalendar);
		secondMatchStadiumText = (TextView) findViewById(R.id.secondmatchStadiumcalendar);
		
		thirdHomeTeamNameCalendarText = (TextView) findViewById(R.id.thirdhometeamnamecalendar);
		thirdAwayTeamNameCalendarText = (TextView) findViewById(R.id.thirdawayteamnamecalendar);
		thirdMatchTimeText = (TextView) findViewById(R.id.thirdmatchTimecalendar);
		thirdMatchStadiumText = (TextView) findViewById(R.id.thirdmatchStadiumcalendar);
		
		fourthHomeTeamNameCalendarText = (TextView) findViewById(R.id.fourthhometeamnamecalendar);
		fourthAwayTeamNameCalendarText = (TextView) findViewById(R.id.fourthawayteamnamecalendar);
		fourthMatchTimeText = (TextView) findViewById(R.id.fourthmatchTimecalendar);
		fourthMatchStadiumText = (TextView) findViewById(R.id.fourthmatchStadiumcalendar);
		
		firstMatchButton = (Button) findViewById(R.id.firstmatchbutton);
		firstMatchButton.setOnClickListener(this);
		secondMatchButton = (Button) findViewById(R.id.secondmatchbutton);
		secondMatchButton.setOnClickListener(this);
		thirdMatchButton = (Button) findViewById(R.id.thirdmatchbutton);
		thirdMatchButton.setOnClickListener(this);
		fourthMatchButton = (Button) findViewById(R.id.fourthmatchbutton);
		fourthMatchButton.setOnClickListener(this);
		
		calendar = (CalendarView)findViewById(R.id.calendar);
		matchList = new ArrayList<Match>();
		
//		Date temp = new Date(1407317400000L);	// 8/6
//		Log.i("temp", temp +"");
		
		currentTime = System.currentTimeMillis();
		
		Date date = new Date(currentTime);
		currentMonth = date.getMonth();
		currentDate = date.getDate();
		
		
		
		setDate();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		int count = 0;
		for (int i=0; i<matchList.size(); i++) {
			Date matchDate = new Date(matchList.get(i).getMatchDate());

			if (matchDate.getMonth() == currentMonth && matchDate.getDate() == currentDate) {
				count ++ ;
				if (count == 1) {
					firstHomeTeamNameCalendarText.setText(matchList.get(i).getHomeTeamName());
					firstAwayTeamNameCalendarText.setText(matchList.get(i).getAwayTeamName());
					firstMatchTimeText.setText(matchList.get(i).getMatchDescription().substring(4, 9));
					firstMatchStadiumText.setText(matchList.get(i).getMatchStadium());
				} else if (count == 2) {
					secondHomeTeamNameCalendarText.setText(matchList.get(i).getHomeTeamName());
					secondAwayTeamNameCalendarText.setText(matchList.get(i).getAwayTeamName());
					secondMatchTimeText.setText(matchList.get(i).getMatchDescription().substring(4, 9));
					secondMatchStadiumText.setText(matchList.get(i).getMatchStadium());
				} else if (count == 3) {
					thirdHomeTeamNameCalendarText.setText(matchList.get(i).getHomeTeamName());
					thirdAwayTeamNameCalendarText.setText(matchList.get(i).getAwayTeamName());
					thirdMatchTimeText.setText(matchList.get(i).getMatchDescription().substring(4, 9));
					thirdMatchStadiumText.setText(matchList.get(i).getMatchStadium());
				} else if (count == 4) {
					fourthHomeTeamNameCalendarText.setText(matchList.get(i).getHomeTeamName());
					fourthAwayTeamNameCalendarText.setText(matchList.get(i).getAwayTeamName());
					fourthMatchTimeText.setText(matchList.get(i).getMatchDescription().substring(4, 9));
					fourthMatchStadiumText.setText(matchList.get(i).getMatchStadium());
				}
			}
		}
		
		calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
			public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
				int count = 0;
				for (int i=0; i<matchList.size(); i++) {
					Date matchDate = new Date(matchList.get(i).getMatchDate());
					Log.i("matchDate.getMonth()", String.valueOf(matchDate.getMonth()));
					Log.i("month",month+ "");
					Log.i("matchDate.getDate()", String.valueOf(matchDate.getDate() ));
					Log.i("dayOfMonth", dayOfMonth + "");
					if (matchDate.getMonth() == month && matchDate.getDate() == dayOfMonth) {
						count ++ ;
						Log.i("dayOfMonth",dayOfMonth+"" );
						if (count == 1) {
							firstHomeTeamNameCalendarText.setText("");
							firstHomeTeamNameCalendarText.setText(matchList.get(i).getHomeTeamName());
							firstAwayTeamNameCalendarText.setText(matchList.get(i).getAwayTeamName());
							firstMatchTimeText.setText(matchList.get(i).getMatchDescription().substring(4, 9));
							firstMatchStadiumText.setText(matchList.get(i).getMatchStadium());
						} else if (count == 2) {
							secondHomeTeamNameCalendarText.setText(matchList.get(i).getHomeTeamName());
							secondAwayTeamNameCalendarText.setText(matchList.get(i).getAwayTeamName());
							secondMatchTimeText.setText(matchList.get(i).getMatchDescription().substring(4, 9));
							secondMatchStadiumText.setText(matchList.get(i).getMatchStadium());
						} else if (count == 3) {
							thirdHomeTeamNameCalendarText.setText(matchList.get(i).getHomeTeamName());
							thirdAwayTeamNameCalendarText.setText(matchList.get(i).getAwayTeamName());
							thirdMatchTimeText.setText(matchList.get(i).getMatchDescription().substring(4, 9));
							thirdMatchStadiumText.setText(matchList.get(i).getMatchStadium());
						} else if (count == 4) {
							fourthHomeTeamNameCalendarText.setText(matchList.get(i).getHomeTeamName());
							fourthAwayTeamNameCalendarText.setText(matchList.get(i).getAwayTeamName());
							fourthMatchTimeText.setText(matchList.get(i).getMatchDescription().substring(4, 9));
							fourthMatchStadiumText.setText(matchList.get(i).getMatchStadium());
						}
					}
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.firstmatchbutton:
			
			break;
		case R.id.secondmatchbutton:

			break;
		case R.id.thirdmatchbutton:

			break;
		case R.id.fourthmatchbutton:

			break;

		default:
			break;
		}
	}
	
	private void setDate() {
		new AsyncTask<Void, Void, List<Match>>() {
			
			@Override
			protected List<Match> doInBackground(Void... params) {
				// network process
				GetMatchByMonthReq getMatchByMonthReq = new GetMatchByMonthReq();
				getMatchByMonthReq.setMonth(currentMonth + 1);
				GetMatchByMonthLink getMatchByMonthLink = new GetMatchByMonthLink(getMatchByMonthReq);
				GetMatchByMonthRes matchByMonthRes = getMatchByMonthLink.linkage();
				
				if (matchByMonthRes != null) {
					matchList = matchByMonthRes.getMatchList();
					Log.i("matchList", matchList + "");
					return matchByMonthRes.getMatchList();
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<Match> result) {
//				Log.i("result", result + "");
				// ui process
				if (result != null) {
//					Log.i("result", result + "");
//					Date temp = new Date(result.get(0).getMatchDate());
//					Log.i("getMonth", String.valueOf(temp.getMonth()));
//					matchList = result;
					
				} else {
					matchList = null;
				}
			}
		}.execute();
	}
}
