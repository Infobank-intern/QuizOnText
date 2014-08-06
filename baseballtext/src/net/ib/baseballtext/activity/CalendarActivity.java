package net.ib.baseballtext.activity;

import java.util.Date;
import java.util.List;

import kr.co.quizon.network.link.match.GetMatchByMonthLink;
import kr.co.quizon.network.link.match.GetMatchListLink;
import net.ib.baseballtext.R;
import net.ib.quizon.api.match.GetMatchByMonthReq;
import net.ib.quizon.api.match.GetMatchByMonthRes;
import net.ib.quizon.api.match.GetMatchListReq;
import net.ib.quizon.api.match.GetMatchListRes;
import net.ib.quizon.domain.match.Match;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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
		
		long getDate = calendar.getDate();
		Log.i("getDate", String.valueOf(getDate));
		
		Date d = new Date(getDate);
		
		Log.i("date", d + "");
		
		int getDateTextAppearance = calendar.getDateTextAppearance();
		Log.i("getDateTextAppearance", String.valueOf(getDateTextAppearance));
		
		
		setData();
		
		calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
			public void onSelectedDayChange(CalendarView view, int year,
					int month, int dayOfMonth) {
				Toast.makeText(CalendarActivity.this, "" + year + "/" + (month + 1) + "/" + dayOfMonth, Toast.LENGTH_SHORT).show();
			}
		});
	}
	
	private void setData() {
		new AsyncTask<Void, Void, List<Match>>() {
			
			@Override
			protected List<Match> doInBackground(Void... params) {
				Log.i("11", "111");
				// network process
				GetMatchByMonthReq getMatchByMonthReq = new GetMatchByMonthReq();
				GetMatchByMonthLink getMatchByMonthLink = new GetMatchByMonthLink(getMatchByMonthReq);
				GetMatchByMonthRes matchByMonthRes = getMatchByMonthLink.linkage();
				
				if (matchByMonthRes != null) {
					Log.i("matchByMonthRes.getMatchList()", matchByMonthRes.getMatchList() + "");
					return matchByMonthRes.getMatchList();
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<Match> result) {
				Log.i("22", "222");
				// ui process
				if (result != null) {
					Log.i("result", result + "");
				} else {
					
				}
			}
		}.execute();
	}
}
