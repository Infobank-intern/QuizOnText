package net.ib.baseballtext.widget;


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
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import android.widget.Toast;



public class BaseballWidget extends AppWidgetProvider {

	private final String ACCESS_TOKEN = "b867b048-8f20-4a01-bfc4-53784e4b488e"; // Test
//	private final String ACCESS_TOKEN = "35fa9897-c723-44a7-a562-bcabd76b2fc0"; // release
    
	private static final String ACTION_CLICK = "CLICK";
	private static final String ACTION_REFRESH = "REFRESH";

	private static final String TAG = "HelloWidgetProvider";
	private static final int WIDGET_UPDATE_INTERVAL = 30000;
	private static PendingIntent mSender;
	private static AlarmManager mManager;

	private Timer timer;
	private TimerTask timerTask;// = new TimerJob();
	private int delay = 1000;
	private int period = 30000;
	
	// Data
    private static final String matchId = "2011731b-37b7-4af2-a771-43d56092a9c6";
   
    protected int inning;
    
    private RemoteViews views;
    private List<String> baseballTextList;
    
	public BaseballWidget() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BaseballWidget(List<String> baseballTextList) {
		this.baseballTextList = baseballTextList;
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}
	
	@Override
	public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
		
		HttpLib.setTest(true);

		for (int i=0; i<appWidgetIds.length; i++) {
			RemoteViews remoteView = new RemoteViews(context.getPackageName(), R.layout.baseballwidget);
			
			// add refreshIntent
			Intent refreshIntent = new Intent(context, BaseballWidget.class);
			refreshIntent.setAction(ACTION_REFRESH);
			refreshIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			PendingIntent refreshPendingIntent = PendingIntent.getBroadcast(context, appWidgetIds[i], refreshIntent, 0);
			remoteView.setOnClickPendingIntent(R.id.refreshbutton, refreshPendingIntent);
			
			// add clickItent
			Intent clickIntent = new Intent(context, BaseballWidget.class);
			clickIntent.setAction(ACTION_CLICK);
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, appWidgetIds[i], clickIntent, 0);
			remoteView.setOnClickPendingIntent(R.id.selectmatchbutton, clickPendingIntent);

			appWidgetManager.updateAppWidget(appWidgetIds[i], remoteView);
		}

		views = new RemoteViews(context.getPackageName(), R.layout.baseballwidget);
		
		Intent mainIntent = new Intent(context, MainActivity.class);
		PendingIntent mainPendingIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);
		views.setOnClickPendingIntent(R.id.title, mainPendingIntent);
		
//		Intent subIntent = new Intent(context, MainActivity.class);
//		PendingIntent subPendingIntent = PendingIntent.getActivity(context, 0, subIntent, 0);
//		views.setOnClickPendingIntent(R.id.selectmatchbutton, subPendingIntent);
		
		appWidgetManager.updateAppWidget(appWidgetIds, views);

		new SetData(views, appWidgetIds, appWidgetManager).execute();

		new AsyncTask<Void, Void, GetMatchBroadcastRes>() {
			@Override
			protected GetMatchBroadcastRes doInBackground(Void... params) {
				GetMatchBroadcastReq getMatchBroadcastReq = new GetMatchBroadcastReq();
				getMatchBroadcastReq.setMatchId(matchId);
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
						if (matchSummary.getMatchId().equals(matchId)) {
							views.setTextViewText(R.id.stadium, matchSummary.getMatchStadium());
							views.setTextViewText(R.id.inning, matchSummary.getMatchPresent());
							views.setTextViewText(R.id.hometeamname, matchSummary.getHomeTeamName());
							views.setTextViewText(R.id.awayteamname, matchSummary.getAwayTeamName());
							views.setTextViewText(R.id.hometeampoint, String.valueOf(matchSummary.getHomeTeamPoint()));
							views.setTextViewText(R.id.awayteampoint, String.valueOf(matchSummary.getAwayTeamPoint()));
							break;
						}
					}
					for (int i=0; i<appWidgetIds.length; i++) {
						RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.baseballwidget);

						Intent listviewIntent = new Intent(context, BaseballWidgetService.class);
						listviewIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
						listviewIntent.putExtra("text", sb.toString());

						rv.setRemoteAdapter(R.id.baseballtext, listviewIntent);
						appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
					}
				} else {
					views.setTextViewText(R.id.stadium, "---");
					for (int i=0; i<appWidgetIds.length; i++) {
						RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.baseballwidget);

						Intent listviewIntent = new Intent(context, BaseballWidgetService.class);
						listviewIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
						listviewIntent.putExtra("Data", "문자 중계 로딩 실패\n경기가 시작하였는지 확인해 주세요");

						rv.setRemoteAdapter(R.id.baseballtext, listviewIntent);
						appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
					}
				}
				appWidgetManager.updateAppWidget(appWidgetIds, views);
			}
		}.execute();
		super.onUpdate(context, appWidgetManager, appWidgetIds);
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Toast.makeText(context, action, Toast.LENGTH_SHORT).show();
		
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
		
		  // RENEW
		if (action != null && action.equals(ACTION_CLICK)) {
			Toast.makeText(context, "ACTION_CLICK", Toast.LENGTH_SHORT).show();
			return;
		} else if (action != null && action.equals(ACTION_REFRESH)) {
			Toast.makeText(context, "ACTION_REFRESH", Toast.LENGTH_SHORT).show();
			
			return;
		}
		super.onReceive(context, intent);
	}

//	public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId)
//	{
//		Date now = new Date();
//		RemoteViews updateViews = new RemoteViews(context.getPackageName(), R.layout.baseballwidget);
//		updateViews.setTextViewText(R.id.widgettext, "[" + String.valueOf(appWidgetId) + "]" + now.toLocaleString());
//		appWidgetManager.updateAppWidget(appWidgetId, updateViews);
//	}

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
	
	class SetData extends AsyncTask<Void, Void, List<Match>> {
		private RemoteViews views;
	    private int[] WidgetID;
	    private AppWidgetManager WidgetManager;
	    // Constructor
	    public SetData (RemoteViews views, int[] appWidgetIds, AppWidgetManager appWidgetManager){
	        this.views = views;
	        this.WidgetID = appWidgetIds;
	        this.WidgetManager = appWidgetManager;        
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
			if (Strings.isNotEmptyString(result.toString()) && result.size()>0) {
				views.setTextViewText(R.id.hometeamname, result.get(0).getHomeTeamName());
				views.setTextViewText(R.id.awayteamname, result.get(0).getAwayTeamName());
				views.setTextViewText(R.id.stadium, result.get(0).getMatchStadium());
				
				for (Match match : result) {
					match.getMatchStatus();
//					matchId = match.getMatchId();
					if (matchId != null) {
						break;
					}
				}
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
			WidgetManager.updateAppWidget(WidgetID, views);
		}
	}
	
	private void setMatchDisplayBoard(MatchDisplayBoard matchDisplayBoard) {
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
