package net.ib.baseballtext.widget;

import java.util.ArrayList;
import java.util.List;

import net.ib.baseballtext.R;
import net.ib.quizon.domain.match.Match;
import net.ib.quizon.domain.match.MatchBroadcast;
import net.ib.quizon.domain.match.MatchSummary;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;


public class BaseballWidgetFactory implements RemoteViewsFactory {
	
	private Context mContext;
	private int mAppWidgetId;
	private List<String> baseballTextList = new ArrayList<String>();
	private String text;
	
	public BaseballWidgetFactory(Context context, Intent intent) {
		Log.i("BaseballWidgetFactory", "생성자생성자");
		mContext = context;
		mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
		text = intent.getStringExtra("text");
		Log.i("BaseballWidgetFactory생성자", text);
	}
	
	@Override
	public void onCreate() {
		Log.i("BaseballWidgetFactory ONCREATE", text);
		baseballTextList.add(text);
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