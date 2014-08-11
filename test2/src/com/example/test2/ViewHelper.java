package com.example.test2;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewHelper {
	private final Context context;
	
	private final int defaultWidth;
	private final int defaultHeight;
	
	public ViewHelper(Context context) {
		this.context = context;
		this.defaultWidth = ViewUtil.getScreenWidth(context);
		this.defaultHeight = ViewUtil.getScreenHeight(context);
	}
	
	public void setGlobalFont(ViewGroup root) {
		for (int i = 0; i < root.getChildCount(); i++) {
	        View child = root.getChildAt(i);
	        if (child instanceof TextView){
	            ((TextView)child).setTypeface(ViewUtil.getTypefaceGodoM(context));
	        }
	        else if (child instanceof ViewGroup)
	            setGlobalFont((ViewGroup)child);
	    }
	}
	
	public void setGlobalSize(ViewGroup root) {
		setGlobalSize(root, ViewUtil.getScreenWidth(context), ViewUtil.getScreenHeight(context));
	}

	private void setGlobalSize(ViewGroup root, int width, int height){
		for (int i = 0; i < root.getChildCount(); i++) {
	        final View child = root.getChildAt(i);
	        setViewGroupSize(root,child,width,height);
	        if (child instanceof TextView){
	        }else if(child instanceof ImageView){
	        }else if(child instanceof Button){
	        }else if(child instanceof EditText){
	        }else if (child instanceof ViewGroup){
	            setGlobalSize((ViewGroup)child,width,height);
	        }
	    }
	}
	
	public void setViewGroupSize(ViewGroup root,View child,int width,int height){
        final float dpiRate = ViewUtil.getDpiRate(context);
        
		if (root instanceof LinearLayout) {
			final LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) child.getLayoutParams();
			
			params.width = Math.round(dpiRate*params.width*width/defaultWidth);
			params.height = Math.round(dpiRate*params.height*height/defaultHeight);
			params.bottomMargin = Math.round(dpiRate*params.bottomMargin*height/defaultHeight);
			params.topMargin = Math.round(dpiRate*params.topMargin*height/defaultHeight);
			params.leftMargin = Math.round(dpiRate*params.leftMargin*width/defaultWidth);
			params.rightMargin = Math.round(dpiRate*params.rightMargin*width/defaultWidth);
			
			child.setLayoutParams(params);
		} else if(root instanceof RelativeLayout) {
			final RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) child.getLayoutParams();
			
			Log.i("size", "dpiRate : " + dpiRate);
			Log.i("size", "device width : " + width);
			Log.i("size", "device height : " + height);
			Log.i("size", "before params.width : " + params.width);
			Log.i("size", "before params.height : " + params.height);
			params.width = Math.round(dpiRate*params.width*width/defaultWidth);
			params.height = Math.round(dpiRate*params.height*height/defaultHeight);
			params.bottomMargin = Math.round(dpiRate*params.bottomMargin*height/defaultHeight);
			params.topMargin = Math.round(dpiRate*params.topMargin*height/defaultHeight);
			params.leftMargin = Math.round(dpiRate*params.leftMargin*width/defaultWidth);
			params.rightMargin = Math.round(dpiRate*params.rightMargin*width/defaultWidth);
			Log.i("size", "after params.width : " + params.width);
			Log.i("size", "after params.height : " + params.height);
			
			child.setLayoutParams(params);
		} else if(root instanceof FrameLayout) {
			final FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) child.getLayoutParams();
			
			params.width = Math.round(dpiRate*params.width*width/defaultWidth);
			params.height = Math.round(dpiRate*params.height*height/defaultHeight);
			params.bottomMargin = Math.round(dpiRate*params.bottomMargin*height/defaultHeight);
			params.topMargin = Math.round(dpiRate*params.topMargin*height/defaultHeight);
			params.leftMargin = Math.round(dpiRate*params.leftMargin*width/defaultWidth);
			params.rightMargin = Math.round(dpiRate*params.rightMargin*width/defaultWidth);
			
			child.setLayoutParams(params);
		}
	}
}