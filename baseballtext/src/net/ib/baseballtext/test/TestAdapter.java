package net.ib.baseballtext.test;

import java.util.ArrayList;
import java.util.List;

import net.ib.baseballtext.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class TestAdapter extends BaseAdapter {
	private List<String> stringList = new ArrayList<String>();
	private Context mContext;
	private LayoutInflater mInflater;
	
	public TestAdapter(Context context) {
		mContext = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		ViewHolder holder;
		if (convertView == null) {
			view = newView();
		} else {
			view = convertView;
		}
		holder = (ViewHolder) view.getTag();
		
		setView(view, holder, position);
		return view;
	}
	
	private View newView() {
		View view = mInflater.inflate(R.layout.activity_main, null);
		ViewHolder holder = new ViewHolder();
		holder.btn = (Button) view.findViewById(R.id.button);
		view.setTag(holder);
		return view;
	}
	
	private void setView(View view, ViewHolder holder, int position) {
		String item = getItem(position);
		if (item == null) {
			holder.btn.setText("item is null");
		} else {
			if (item.equals("samsung")) {
				holder.btn.setText("삼성");
			}
		}
	}

	@Override
	public int getCount() {
		if (stringList != null) {
			return stringList.size();
		}
		return 0;
	}

	@Override
	public String getItem(int position) {
		if (stringList != null && stringList.size() > position) {
			return stringList.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	class ViewHolder {
		Button btn;
	}
}