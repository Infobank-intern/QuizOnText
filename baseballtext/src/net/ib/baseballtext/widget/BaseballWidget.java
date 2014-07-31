package net.ib.baseballtext.widget;

import java.util.ArrayList;
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
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;



public class BaseballWidget extends AppWidgetProvider {

	private final String ACCESS_TOKEN = "b867b048-8f20-4a01-bfc4-53784e4b488e"; // Test
//	private final String ACCESS_TOKEN = "35fa9897-c723-44a7-a562-bcabd76b2fc0"; // release
    
//	static final ACTION_CLICK = "CLICK";
	
	private Timer timer;
	private TimerTask timerTask;// = new TimerJob();
	private int delay = 1000;
	private int period = 30000;
	
	// Data
    private String matchId;
    protected int inning;
    
    private RemoteViews views;
    private List<String> baseballTextList;
    
    private MatchSummary tempMatchSummary;
    private String tempBroadcast;
    private List<MatchBroadcast> temp;
    
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
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		
		Intent intent = new Intent(context, MainActivity.class);
		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
		
		for (int i=0; i<appWidgetIds.length; i++) {
			RemoteViews listviewViews = new RemoteViews(context.getPackageName(), R.layout.baseballwidget);

			Intent listviewIntent = new Intent(context, BaseballWidgetService.class);
			listviewIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			listviewViews.setRemoteAdapter(R.id.baseballtext, listviewIntent);
			appWidgetManager.updateAppWidget(appWidgetIds[i], listviewViews);

			RemoteViews clickViews = new RemoteViews(context.getPackageName(), R.layout.baseballwidget);
			Intent clickIntent = new Intent(context, BaseballWidget.class);
			clickIntent.setAction("CLICK");
			clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
			PendingIntent clickPendingIntent = PendingIntent.getBroadcast(context, appWidgetIds[i], clickIntent, 0);
			clickViews.setOnClickPendingIntent(R.id.selectmatchbutton, clickPendingIntent);
		}

		views = new RemoteViews(context.getPackageName(), R.layout.baseballwidget);
		views.setOnClickPendingIntent(R.id.title, pendingIntent);
		appWidgetManager.updateAppWidget(appWidgetIds, views);
		
		HttpLib.setTest(true);
		
		new SetData(views, appWidgetIds, appWidgetManager).execute();
		UpdateView updateView = new UpdateView(matchId, views, appWidgetIds, appWidgetManager);
		updateView.execute();
//		timerTask = new TimerJob(views, appWidgetIds, appWidgetManager);
//		timer = new Timer();
//		timer.schedule(timerTask, delay, period);
	}

//	@Override
//	public void onReceive(Context context, Intent intent) {
//		super.onReceive(context, intent);
//		String action = intent.getAction();
//		  // RENEW
//		  if (action != null && action.equals("CLICK")) {
//		   int id = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
////		   UpdateWidget(context, AppWidgetManager.getInstance(context), id);   // 버튼이 클릭되면 새로고침 수행
//		   
//		   Log.d("ExampleWidget", "onReceive: CLICK Button");
//		   return;
//		  }
//		  
//		  super.onReceive(context, intent);
//		 }
//	}

	////////static 지웠더니 겁나 오류남 왜?????
	public static class BaseballWidgetService extends RemoteViewsService {
		public BaseballWidgetService() {
			super();
			// TODO Auto-generated constructor stub
		}

		public RemoteViewsFactory onGetViewFactory(Intent intent) {
			return new BaseballWidgetFactory(this.getApplicationContext(), intent);
		}
	}
	
//	class TimerJob extends TimerTask {
//		private RemoteViews views;
//		private int[] appWidgetIds;
//		private AppWidgetManager appWidgetManager;
//		//Constructor
//		public TimerJob (RemoteViews views,  int[] appWidgetIds, AppWidgetManager appWidgetManager) {
//			this.views = views;
//			this.appWidgetIds = appWidgetIds;
//			this.appWidgetManager = appWidgetManager;
//		}
//		@Override
//		public void run() {
//			Log.i("여기0?", "여기0");
//			UpdateView updateView = new UpdateView(matchId, views, appWidgetIds, appWidgetManager);
//			updateView.execute();
//		}
//	}
	
	public class SetData extends AsyncTask<Void, Void, List<Match>> {
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
					matchId = match.getMatchId();
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
	
	public class UpdateView extends AsyncTask<Void, Void, GetMatchBroadcastRes> {
		private RemoteViews views;
	    private int[] WidgetID;
	    private AppWidgetManager WidgetManager;
	    private String matchId;
	    // Constructor
	    public UpdateView (final String matchId, RemoteViews views, int[] appWidgetIds, AppWidgetManager appWidgetManager){
	        this.matchId = matchId;
	    	this.views = views;
	        this.WidgetID = appWidgetIds;
	        this.WidgetManager = appWidgetManager;    
	    }
		
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
//				views.setTextViewText(R.id.baseballtext, "");
				StringBuilder sb = new StringBuilder();
				
				List<MatchBroadcast> broadcast = getMatchBroadcastRes.getBroadcast();
				for (MatchBroadcast matchBroadcast : broadcast) {
					if (matchBroadcast == null) {
						continue;
					}
					sb.append(matchBroadcast.getBroadcast());
				}
					
				MatchDisplayBoard matchDisplayBoard = getMatchBroadcastRes.getMatchDisplayBoard();///				setMatchDisplayBoard(matchDisplayBoard);
				
				List<MatchSummary> matchSummaryList = getMatchBroadcastRes.getMatchSummaryList();
				for (MatchSummary matchSummary : matchSummaryList) {
					if (matchSummary == null) {
						continue;
					}
					if (matchSummary.getMatchId().equals(matchId)) {
//						views.setTextViewText(R.id.stadium, matchSummary.getMatchStadium());
//						views.setTextViewText(R.id.inning, matchSummary.getMatchPresent());
//						views.setTextViewText(R.id.hometeamname, matchSummary.getHomeTeamName());
//						views.setTextViewText(R.id.awayteamname, matchSummary.getAwayTeamName());
//						views.setTextViewText(R.id.hometeampoint, String.valueOf(matchSummary.getHomeTeamPoint()));
//						views.setTextViewText(R.id.awayteampoint, String.valueOf(matchSummary.getAwayTeamPoint()));
						break;
					}
				}
//				views.setTextViewText(R.id.baseballtext, sb.toString());
			} else {
//				views.setTextViewText(R.id.baseballtext, "문자 중계 로딩 실패\n경기가 시작하였는지 확인해 주세요");
//				views.setTextViewText(R.id.stadium, "---");
//				baseballTextList.add("문자 중계 로딩 실패\n경기가 시작하였는지 확인해 주세요");
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
//	
//	public MatchSummary getMatchSummary() {
//		return tempMatchSummary;
//	}
//	
//	public List<MatchBroadcast> getTemp() {
//		return temp;
//	}
}
