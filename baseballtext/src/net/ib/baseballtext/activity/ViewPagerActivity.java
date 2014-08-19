package net.ib.baseballtext.activity;

import net.ib.baseballtext.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

public class ViewPagerActivity extends Activity {

	//ViewPager
	private final int COUNT=2;
	private ViewPager mPager; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		//ViewPgaer
		mPager = (ViewPager)findViewById(R.id.pager);
		mPager.setAdapter(new PagerAdapterClass(getApplicationContext()));
	}

	private class PagerAdapterClass extends PagerAdapter{

		private LayoutInflater mInflater;

		public PagerAdapterClass(Context context){
			super();
			mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return 3;
		}

		@Override
		public Object instantiateItem(View pager, int position) {
			View v = null;
			if (position == 0) {
				v = mInflater.inflate(R.layout.activity_main, null);
				//    			v.findViewById(R.id.iv_one);
				//    			v.findViewById(R.id.btn_click).setOnClickListener(mPagerListener);
			} else if (position == 1) {
				v = mInflater.inflate(R.layout.activity_splash, null);
				//    			v.findViewById(R.id.iv_two);
				//    			v.findViewById(R.id.btn_click_2).setOnClickListener(mPagerListener);
			} else if (position == 2) {
				v = mInflater.inflate(R.layout.activity_calendar, null);
				//    			v.findViewById(R.id.iv_three); 
				//    			v.findViewById(R.id.btn_click_3).setOnClickListener(mPagerListener);
			}

			((ViewPager)pager).addView(v, 0);

			return v; 
		}
		@Override
		public void destroyItem(View pager, int position, Object view) {	
			((ViewPager)pager).removeView((View)view);
		}
		
		@Override
		public boolean isViewFromObject(View pager, Object obj) {
			return pager == obj; 
		}

		@Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
		@Override public Parcelable saveState() { return null; }
		@Override public void startUpdate(View arg0) {}
		@Override public void finishUpdate(View arg0) {}
	}
}
