package net.ib.baseballtext.widget;

import java.util.ArrayList;
import java.util.List;

import net.ib.baseballtext.R;
import net.ib.baseballtext.widget.BaseballWidget.SetData;
import net.ib.quizon.domain.match.MatchBroadcast;
import net.ib.quizon.domain.match.MatchSummary;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;


public class BaseballWidgetFactory implements RemoteViewsFactory {
	
	private BaseballWidget baseballWidget;
	private Context mContext;
	private int mAppWidgetId;
	private List<String> baseballTextList = new ArrayList<String>();
	private MatchSummary matchSummary;
	private List<MatchBroadcast> broadcast;
	
	
	
	public BaseballWidgetFactory(Context context, Intent intent) {
		mContext = context;
		mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
	}
	
	@Override
	public void onCreate() {
//		matchSummary = baseballWidget.getMatchSummary();
		
//		baseballWidget = new BaseballWidget(baseballTextList);
//
//		broadcast = baseballWidget.getTemp();
//		matchSummary = baseballWidget.getMatchSummary();
		
		baseballTextList.add("1111");
		baseballTextList.add("2222");
		baseballTextList.add("3333");
	}
	
	@Override
	public int getCount() {
		return baseballTextList.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.textlist);
		views.setTextViewText(R.id.text, baseballTextList.get(position));
		return views;
	}
	
	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public void onDataSetChanged() {
	}

	@Override
	public void onDestroy() {
	}

}