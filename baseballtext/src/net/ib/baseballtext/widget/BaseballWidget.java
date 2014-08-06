package net.ib.baseballtext.widget;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import kr.co.quizon.network.HttpLib;
import kr.co.quizon.network.link.match.GetMatchBroadcastLink;
import kr.co.quizon.network.link.match.GetMatchListLink;
import net.ib.baseballtext.R;
import net.ib.baseballtext.activity.MainActivity;
import net.ib.baseballtext.util.Strings;
import net.ib.quizon.api.match.GetMatchBroadcastReq;
import net.ib.quizon.api.match.GetMatchBroadcastRes;
import net.ib.quizon.api.match.GetMatchListReq;
import net.ib.quizon.api.match.GetMatchListRes;
import net.ib.quizon.domain.match.Match;
import net.ib.quizon.domain.match.MatchBroadcast;
import net.ib.quizon.domain.match.MatchDisplayBoard;
import net.ib.quizon.domain.match.MatchSummary;
import net.ib.quizon.domain.payment.AppStoreProduct;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;



public class BaseballWidget extends AppWidgetProvider {

	private final static String ACCESS_TOKEN = "b867b048-8f20-4a01-bfc4-53784e4b488e"; // Test
//	private final static String ACCESS_TOKEN = "35fa9897-c723-44a7-a562-bcabd76b2fc0"; // release
    
	private static final String ACTION_CLICK = "CLICK";
	private static final String ACTION_REFRESH = "REFRESH";
	
	private final String ACTION_THIRD = "THIRDGAME";
	
	private static final String PREF = "BaseballWidgetSelect";

	private static final String TAG = "HelloWidgetProvider";
	private static final int WIDGET_UPDATE_INTERVAL = 30000;
	private static PendingIntent mSender;
	private static AlarmManager mManager;

	private Timer timer;
	private TimerTask timerTask;// = new TimerJob();
	private int delay = 1000;
	private int period = 30000;
	
	// Data
    private static String matchId;
    private static String MATCHID = "2011731b-37b7-4af2-a771-43d56092a9c6";
    
    protected static int inning;
    private static int selectId = 0;
    private static int mId;
    
    private static RemoteViews views;
    private static RemoteViews selectViews;
    private static List<Match> matchTempList = new ArrayList<Match>();
    
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}
	
	@Override
	public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
		
		HttpLib.setTest(true);

		for(int i=0; i<appWidgetIds.length; i++) {
Log.i("OnUpdate", "호출");
			UpdateWidget(context, appWidgetManager, appWidgetIds[i]);
		}
	}

	static void UpdateWidget(final Context context, final AppWidgetManager appWidgetManager, final int appWidgetIds) {
		RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.baseballwidget);

		// add refreshIntent
		Intent refreshIntent = new Intent(context, BaseballWidget.class);
		refreshIntent.setAction(ACTION_REFRESH);
		refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
		PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, appWidgetIds, refreshIntent, 0);
		remoteView.setOnClickPendingIntent(R.id.refreshbutton, refreshPendingIntent);

		// add clickItent
		Intent clickIntent = new Intent(context, BaseballWidget.class);
		clickIntent.setAction(ACTION_CLICK);
		clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
		PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, appWidgetIds, clickIntent, 0);
		remoteView.setOnClickPendingIntent(R.id.selectmatchbutton, clickPendingIntent);
		
		//updateAppWidget
		appWidgetManager.updateAppWidget(appWidgetIds, remoteView);

		views = new RemoteViews(context.getPackageName(), R.layout.baseballwidget);
		
		//상단레이아웃 MainActivity로 전환
		Intent mainIntent = new Intent(context, MainActivity.class);
		PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
		views.setOnClickPendingIntent(R.id.title, mainPendingIntent);
		
		//하단레이아웃 MainActivity로 전환
		Intent subIntent = new Intent(context, BaseballWidgetSelect.class);
		PendingIntent subPendingIntent = PendingIntent.getActivity(context, 0, subIntent, 0);
		views.setOnClickPendingIntent(R.id.selectmatchbutton, subPendingIntent);
		
		//updateAppWidget
		appWidgetManager.updateAppWidget(appWidgetIds, views);
		
//		selectViews = new RemoteViews(context.getPackageName(), R.layout.baseballwidget_select);
//		
//		Intent firstGameIntent = new Intent(context, BaseballWidget.class);
//		PendingIntent firstGamePendingIntent = PendingIntent.getActivity(context, 0, firstGameIntent, 0);
//		selectViews.setOnClickPendingIntent(R.id.firstgame, firstGamePendingIntent);
//		
//		Intent secondGameIntent = new Intent(context, BaseballWidget.class);
//		PendingIntent secondGamePendingIntent = PendingIntent.getActivity(context, 0, secondGameIntent, 0);
//		selectViews.setOnClickPendingIntent(R.id.secondgame, secondGamePendingIntent);
//		
//		Intent thirdGameIntent = new Intent(context, BaseballWidget.class);
//		PendingIntent thirdGamePendingIntent = PendingIntent.getActivity(context, 0, thirdGameIntent, 0);
//		selectViews.setOnClickPendingIntent(R.id.thirdgame, thirdGamePendingIntent);
//		
//		Intent fourthGameIntent = new Intent(context, BaseballWidget.class);
//		PendingIntent fourthGamePendingIntent = PendingIntent.getActivity(context, 0, fourthGameIntent, 0);
//		selectViews.setOnClickPendingIntent(R.id.fourthgame, fourthGamePendingIntent);
//		
//		appWidgetManager.updateAppWidget(appWidgetIds, selectViews);
		
		new SetData(views, context, appWidgetIds, appWidgetManager).execute();
//		UpdateView(matchId, context, appWidgetManager, appWidgetIds);
		
//		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}

	
	
	public void onReceive(Context context, Intent intent, AppWidgetManager appWidgetManager) {
		String action = intent.getAction();
		mId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
//		Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
		
		  // RENEW
		if (action != null && action.equals(ACTION_CLICK)) {
//			Toast.makeText(context, "ACTION_CLICK", Toast.LENGTH_SHORT).show();
			return;
		} else if (action != null && action.equals(ACTION_REFRESH)) {
			Toast.makeText(context, "준비중 입니다.", Toast.LENGTH_SHORT).show();
			
			return;
		}
		
		if (action != null && action.equals(ACTION_THIRD)) {
			Log.i("onReceive", "ACTION_THIRD");
			selectId = intent.getIntExtra("selectGame", 0);
			new SetData(views, context, mId, appWidgetManager).execute();
		}
		
		if(action.equals("android.appwidget.action.APPWIDGET_UPDATE"))
	    {
	      Log.w(TAG, "android.appwidget.action.APPWIDGET_UPDATE");
	      removePreviousAlarm();
	 
	      long firstTime = System.currentTimeMillis() + WIDGET_UPDATE_INTERVAL;
	      mSender = PendingIntent.getBroadcast(context, 0, intent, 0);
	      mManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	      mManager.set(AlarmManager.RTC, firstTime, mSender);
	    }
	    // 위젯 제거 인텐트를 수신했을 때
	    else if(action.equals("android.appwidget.action.APPWIDGET_DISABLED"))
	    {
	      Log.w(TAG, "android.appwidget.action.APPWIDGET_DISABLED");
	      removePreviousAlarm();
	    }
		
		super.onReceive(context, intent);
	}

	public void removePreviousAlarm() {
		if(mManager != null && mSender != null)
		{
	      mSender.cancel();
	      mManager.cancel(mSender);
	    }
	}

	public static class BaseballWidgetService extends RemoteViewsService {
		public RemoteViewsFactory onGetViewFactory(Intent intent) {
			return new BaseballWidgetFactory(this.getApplicationContext(), intent);
		}
	}
	
	static class SetData extends AsyncTask<Void, Void, List<Match>> {
		private RemoteViews views;
		private Context context;
	    private int appWidgetIds;
	    private AppWidgetManager appWidgetManager;
	    
	    // Constructor
	    public SetData (RemoteViews views, Context context, int appWidgetIds, AppWidgetManager appWidgetManager){
	        this.views = views;
	        this.context = context;
	        this.appWidgetIds = appWidgetIds;
	        this.appWidgetManager = appWidgetManager;      
Log.i("setData생성자", "setData 호출");
	    }
		
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
Log.i("setData_postExecute_result.this", result.toString());


//			SharedPreferences prefs = context.getSharedPreferences(PREF, 0);
//			selectId = prefs.getInt("select_" + appWidgetIds, 0);
Log.i("setData_postExecute_이거야_selectId", String.valueOf(selectId));

			if (Strings.isNotEmptyString(result.toString()) && result.size()>0) {
				for (int i=0; i<result.size(); i++) {
					matchTempList.add(result.get(i));
				}

					matchId = matchTempList.get(selectId).getMatchId();
Log.i("setData_postExecute_이거야_matchId", matchId);
				
				views.setTextViewText(R.id.hometeamname, result.get(selectId).getHomeTeamName());
				views.setTextViewText(R.id.awayteamname, result.get(selectId).getAwayTeamName());
				views.setTextViewText(R.id.stadium, result.get(selectId).getMatchStadium());

Log.i("setData_postExecute_UpdateView 호출", "setData의 UpdateView 호출");
				

				
				UpdateView(matchId, context, appWidgetManager, appWidgetIds);
//				UpdateView(MATCHID, context, appWidgetManager, appWidgetIds);
				
				
				
				
				
				for (Match match : result) {
					match.getMatchStatus();
					matchId = match.getMatchId();
					if (matchId != null) {
						break;
					}
				}
Log.i("setDate_postExecute_matchidthisthis", matchId);
			} else {
//				views.setTextViewText(R.id.baseballtext, "매치 정보 로딩 실패\n아직 경기가 열리지 않았습니다.");
//				baseballTextList.add("매치 정보 로딩 실패\n아직 경기가 열리지 않았습니다.");
				views.setTextViewText(R.id.stadium, "-");
				views.setTextViewText(R.id.inning, "-");
				views.setTextViewText(R.id.hometeamname, "-");
				views.setTextViewText(R.id.awayteamname, "-");
				views.setTextViewText(R.id.hometeampoint, "-");
				views.setTextViewText(R.id.awayteampoint, "-");
			}
			appWidgetManager.updateAppWidget(appWidgetIds, views);
		}
	}
	
	public static void UpdateView(final String matchId, final Context context, final AppWidgetManager appWidgetManager, final int appWidgetIds) {
Log.i("UpdateView", "UpdateView 호출시작");
		
		new AsyncTask<Void, Void, GetMatchBroadcastRes>() {
			@Override
			protected GetMatchBroadcastRes doInBackground(Void... params) {
				GetMatchBroadcastReq getMatchBroadcastReq = new GetMatchBroadcastReq();
				getMatchBroadcastReq.setMatchId(matchId);
Log.i("updateView_doinBackground_matchId", matchId);
				if (inning != 0) {
					getMatchBroadcastReq.setInning(Integer.valueOf(inning));
				}
				GetMatchBroadcastLink getMatchBroadcastLink = new GetMatchBroadcastLink(getMatchBroadcastReq);
				return getMatchBroadcastLink.linkage();
			}

			@Override
			protected void onPostExecute(GetMatchBroadcastRes getMatchBroadcastRes) {
				if (getMatchBroadcastRes != null) {
					StringBuffer sb = new StringBuffer();
					List<MatchBroadcast> broadcast = getMatchBroadcastRes.getBroadcast();
					for (MatchBroadcast matchBroadcast : broadcast) {
						if (matchBroadcast == null) {
							continue;
						}
						sb.append(matchBroadcast.getBroadcast());
					}

					MatchDisplayBoard matchDisplayBoard = getMatchBroadcastRes.getMatchDisplayBoard();
					setMatchDisplayBoard(matchDisplayBoard);

					List<MatchSummary> matchSummaryList = getMatchBroadcastRes.getMatchSummaryList();
					for (MatchSummary matchSummary : matchSummaryList) {
						if (matchSummary == null) {
							continue;
						}
Log.i("비교   matchSummary.getMatchId()", matchSummary.getMatchId());
Log.i("비교   matchId", matchId);
						if (matchSummary.getMatchId().equals(matchId)) {
							views.setTextViewText(R.id.stadium, matchSummary.getMatchStadium());
							views.setTextViewText(R.id.inning, matchSummary.getMatchPresent());
							views.setTextViewText(R.id.hometeamname, matchSummary.getHomeTeamName());
Log.i("나와야할팀이름", matchSummary.getHomeTeamName());
							views.setTextViewText(R.id.awayteamname, matchSummary.getAwayTeamName());
							views.setTextViewText(R.id.hometeampoint, String.valueOf(matchSummary.getHomeTeamPoint()));
							views.setTextViewText(R.id.awayteampoint, String.valueOf(matchSummary.getAwayTeamPoint()));
Log.i("updateView_postExecute", "views.setTextViewText");
							break;
						}
					}
						RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.baseballwidget);

						Intent listviewIntent = new Intent(context, BaseballWidgetService.class);
						listviewIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
						listviewIntent.putExtra("text", sb.toString());
Log.i("나와야할내용", sb.toString());

						rv.setRemoteAdapter(R.id.baseballtext, listviewIntent);
						appWidgetManager.updateAppWidget(appWidgetIds, rv);
				} else {
					views.setTextViewText(R.id.stadium, "---");
						RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.baseballwidget);

						Intent listviewIntent = new Intent(context, BaseballWidgetService.class);
						listviewIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds);
						listviewIntent.putExtra("Data", "문자 중계 로딩 실패\n경기가 시작하였는지 확인해 주세요");

						rv.setRemoteAdapter(R.id.baseballtext, listviewIntent);
//Log.i("그럼 이건 언제???", "그럼 이건 언제???");
						appWidgetManager.updateAppWidget(appWidgetIds, rv);
				}
Log.i("UpdateView", "UpdateView 호출끝끝끝끝");
				appWidgetManager.updateAppWidget(appWidgetIds, views);
			}
		}.execute();
	}
	
	private static void setMatchDisplayBoard(MatchDisplayBoard matchDisplayBoard) {
		if (matchDisplayBoard != null) {
			String ball = matchDisplayBoard.getBall();
			views.setTextViewText(R.id.strike, ball);
			String strike = matchDisplayBoard.getStrike();
			views.setTextViewText(R.id.ball, strike);
			String out = matchDisplayBoard.getOut();
			views.setTextViewText(R.id.out, out);
		}
	}
}
