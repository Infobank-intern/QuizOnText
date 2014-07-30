package net.ib.baseballtext.widget;

import java.util.ArrayList;
import java.util.List;

import net.ib.baseballtext.R;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;



class BaseballWidgetFactory implements RemoteViewsFactory {
	Context mContext;
	int mAppWidgetId;
	List<String> baseballtextList = new ArrayList<String>();
	
	public BaseballWidgetFactory(Context context, Intent intent) {
		mContext = context;
		mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
	}
	
	
	@Override
	public int getCount() {
		return baseballtextList.size();
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public RemoteViews getLoadingView() {
		return null;
	}

	@Override
	public RemoteViews getViewAt(int position) {
		RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.textlist);
		views.setTextViewText(R.id.text, baseballtextList.get(position));
		return views;
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
	
	@Override
	public void onCreate() {
		
	}
}