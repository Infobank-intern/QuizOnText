package net.ib.baseballtext.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import kr.co.quizon.network.link.match.GetMatchByMonthLink;
import net.ib.baseballtext.R;
import net.ib.baseballtext.match.MatchByMonth;
import net.ib.baseballtext.util.BackPressCloseHandler;
import net.ib.baseballtext.util.ViewHelper;
import net.ib.quizon.api.match.GetMatchByMonthReq;
import net.ib.quizon.api.match.GetMatchByMonthRes;
import net.ib.quizon.domain.match.Match;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;


public class CalendarActivity extends Activity implements OnClickListener {
	
	private ViewHelper mViewHelper;
	
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
	private int selectedMonth;
	private int selectedDate;

	private Intent intent;

	private List<Match> matchList;
	
	private BackPressCloseHandler backPressCloseHandler;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_calendar);
		mViewHelper = new ViewHelper(this);
		View calendarLayout = findViewById(R.id.calendar_layout);
		mViewHelper.setGlobalSize((ViewGroup) calendarLayout);

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

		currentTime = System.currentTimeMillis();
		Date date = new Date(currentTime);
		currentMonth = date.getMonth();
		currentDate = date.getDate();
		selectedDate = currentDate;
		selectedMonth = currentMonth;
		
		backPressCloseHandler = new BackPressCloseHandler(this);
		
		final SharedPreferences pref = getSharedPreferences("Pref1", 0);
		final Editor prefEdit = pref.edit();
		
		final CharSequence[] teamName = {"SAMSUNG LIONS", "DOOSAN BEARS", "LG TWINS", "NEXEN HEROES",
										 "LOTTE GIANTS", "SK WYVERNS", "NC DINOS", "KIA TIGERS", "HANWHA EAGLES", "없음"};
		
		String name;
		int temp = pref.getInt("whitchTeam", 10);
		if (temp < 9) {
			name = (String) teamName[temp];
		} else {
			name = "선호 구단 선택하기";
		}
		
		setAlertDialog(pref, prefEdit, teamName, name);
		MatchByMonth matchByMonth = new MatchByMonth(currentMonth);
		matchList = matchByMonth.getMatchList();
		
		initCalendarText();
		int count = 0;
		if (matchList != null) {
			for (int i=0; i<matchList.size(); i++) {
				Date matchDate = new Date(matchList.get(i).getMatchDate());
				if (matchDate.getMonth() == currentMonth && matchDate.getDate() == currentDate) {
					count ++ ;
					getCalendarText(count, i);
				}
			}
			
			calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
				public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
					int count = 0;
					selectedDate = dayOfMonth;
					selectedMonth = month;
					initCalendarText();
					if (month == currentMonth && dayOfMonth <= currentDate) {
						for (int i=0; i<matchList.size(); i++) {
							Date matchDate = new Date(matchList.get(i).getMatchDate());
							if (matchDate.getMonth() == month && matchDate.getDate() == dayOfMonth) {
								count ++ ;
								getCalendarText(count, i);
							} 
						}
					} else {
						initCalendarText();
					}
				}
			});
		} else {
			Toast.makeText(CalendarActivity.this, "경기정보를 불러오는데 실패하였습니다.", Toast.LENGTH_SHORT).show();
		}
	}

	private void setAlertDialog(final SharedPreferences pref, final Editor prefEdit, final CharSequence[] teamName, String name) {
		AlertDialog.Builder ab = new AlertDialog.Builder(CalendarActivity.this);
		ab.setTitle("    ※ 안내 ※").setMessage("1. 달력을 통하여 경기일정을   확인하세요." +
				"\n\n2. 날짜를 선택하면 그날의 문자 중계를 확인 할   수 있습니다." +
				"\n\n3. 문자 중계 화면에서도 팀 선택을 할 수 있어요." +
				"\n\n4. 문자 중계는 10초마다 자동 업데이트   됩니다." +
				"\n\n5. 위젯을 통하여 스코어, 문자 중계를   볼 수 있습니다." +
				"\n\n6. 위젯은 새로고침 버튼을 눌러   업데이트 된 기록을 받아 주세요." + 
				"\n\n7. '선호 구단'을 선택하면, 다음 앱 실행시에 자동으로 문자 중계화면으로 이동합니다." +
				"\n\n★ 매치정보가 안나오면 앱을 껐다 켜 주세요~^^");
		ab.setPositiveButton(name, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int whitchButton) {
				AlertDialog.Builder ab2 = new AlertDialog.Builder(CalendarActivity.this);
				ab2.setTitle("선호 구단을 선택해 주세요");
				ab2.setSingleChoiceItems(teamName, 0, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int whitchItem) {
						prefEdit.putInt("whitchTeam", whitchItem);
						prefEdit.commit();
						Log.i("whitchItem", whitchItem + "");
					}
				});
				ab2.setPositiveButton("선택하기", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						int name = pref.getInt("whitchTeam", 10);
						Toast.makeText(CalendarActivity.this, teamName[name] + "  팀을 선호구단으로 선택 하였습니다.", Toast.LENGTH_SHORT).show();
					}
				});
				ab2.show();
			}
		});
		ab.setNegativeButton("닫기", null);
		ab.show();
	}
	
	@Override
	public void onBackPressed() {
		backPressCloseHandler.onBackPressed();
	}

	private void getCalendarText(int count, int i) {
		if (count == 1) {
			firstHomeTeamNameCalendarText.setText(matchList.get(i).getHomeTeamName());
			firstAwayTeamNameCalendarText.setText(matchList.get(i).getAwayTeamName());
			firstMatchTimeText.setText(matchList.get(i).getMatchDescription().substring(4, 10));
			firstMatchStadiumText.setText(matchList.get(i).getMatchStadium());
		} else if (count == 2) {
			secondHomeTeamNameCalendarText.setText(matchList.get(i).getHomeTeamName());
			secondAwayTeamNameCalendarText.setText(matchList.get(i).getAwayTeamName());
			secondMatchTimeText.setText(matchList.get(i).getMatchDescription().substring(4, 10));
			secondMatchStadiumText.setText(matchList.get(i).getMatchStadium());
		} else if (count == 3) {
			thirdHomeTeamNameCalendarText.setText(matchList.get(i).getHomeTeamName());
			thirdAwayTeamNameCalendarText.setText(matchList.get(i).getAwayTeamName());
			thirdMatchTimeText.setText(matchList.get(i).getMatchDescription().substring(4, 10));
			thirdMatchStadiumText.setText(matchList.get(i).getMatchStadium());
		} else if (count == 4) {
			fourthHomeTeamNameCalendarText.setText(matchList.get(i).getHomeTeamName());
			fourthAwayTeamNameCalendarText.setText(matchList.get(i).getAwayTeamName());
			fourthMatchTimeText.setText(matchList.get(i).getMatchDescription().substring(4, 10));
			fourthMatchStadiumText.setText(matchList.get(i).getMatchStadium());
		}
	}
	
	private void initCalendarText() {
		firstHomeTeamNameCalendarText.setText("---");
		firstAwayTeamNameCalendarText.setText("---");
		firstMatchTimeText.setText("--");
		firstMatchStadiumText.setText("--");
		secondHomeTeamNameCalendarText.setText("---");
		secondAwayTeamNameCalendarText.setText("---");
		secondMatchTimeText.setText("--");
		secondMatchStadiumText.setText("--");
		thirdHomeTeamNameCalendarText.setText("---");
		thirdAwayTeamNameCalendarText.setText("---");
		thirdMatchTimeText.setText("--");
		thirdMatchStadiumText.setText("--");
		fourthHomeTeamNameCalendarText.setText("---");
		fourthAwayTeamNameCalendarText.setText("---");
		fourthMatchTimeText.setText("--");
		fourthMatchStadiumText.setText("--");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.firstmatchbutton:
			if (selectedDate == currentDate && selectedMonth == currentMonth) {
				intent = new Intent(CalendarActivity.this, MainActivity.class);
				intent.putExtra("selectMatch", 0);
				startActivity(intent);
				finish();
			} else if ((selectedDate < currentDate && selectedMonth == currentMonth) || (selectedMonth < currentMonth)) {
				Toast.makeText(CalendarActivity.this, "지난 중계기록 보기는 준비중입니다.", Toast.LENGTH_SHORT).show();
			} else if ((selectedDate > currentDate && selectedMonth == currentMonth) || (selectedMonth > currentMonth)) {
				Toast.makeText(CalendarActivity.this, "열리지 않은 경기 입니다.", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.secondmatchbutton:
			if (selectedDate == currentDate && selectedMonth == currentMonth) {
				intent = new Intent(CalendarActivity.this, MainActivity.class);
				intent.putExtra("selectMatch", 1);
				startActivity(intent);
				finish();
			} else if ((selectedDate < currentDate && selectedMonth == currentMonth) || (selectedMonth < currentMonth)) {
				Toast.makeText(CalendarActivity.this, "지난 중계기록 보기는 준비중입니다.", Toast.LENGTH_SHORT).show();
			} else if ((selectedDate > currentDate && selectedMonth == currentMonth) || (selectedMonth > currentMonth)) {
				Toast.makeText(CalendarActivity.this, "열리지 않은 경기 입니다.", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.thirdmatchbutton:
			if (selectedDate == currentDate && selectedMonth == currentMonth) {
				intent = new Intent(CalendarActivity.this, MainActivity.class);
				intent.putExtra("selectMatch", 2);
				startActivity(intent);
				finish();
			} else if ((selectedDate < currentDate && selectedMonth == currentMonth) || (selectedMonth < currentMonth)) {
				Toast.makeText(CalendarActivity.this, "지난 중계기록 보기는 준비중입니다.", Toast.LENGTH_SHORT).show();
			} else if ((selectedDate > currentDate && selectedMonth == currentMonth) || (selectedMonth > currentMonth)) {
				Toast.makeText(CalendarActivity.this, "열리지 않은 경기 입니다.", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.fourthmatchbutton:
			if (selectedDate == currentDate && selectedMonth == currentMonth) {
				intent = new Intent(CalendarActivity.this, MainActivity.class);
				intent.putExtra("selectMatch", 3);
				startActivity(intent);
				finish();
			} else if ((selectedDate < currentDate && selectedMonth == currentMonth) || (selectedMonth < currentMonth)) {
				Toast.makeText(CalendarActivity.this, "지난 중계기록 보기는 준비중입니다.", Toast.LENGTH_SHORT).show();
			} else if ((selectedDate > currentDate && selectedMonth == currentMonth) || (selectedMonth > currentMonth)) {
				Toast.makeText(CalendarActivity.this, "열리지 않은 경기 입니다.", Toast.LENGTH_SHORT).show();
			}
			break;
		default:
			break;
		}
	}
}