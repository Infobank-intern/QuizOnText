package net.ib.baseballtext.util;

import android.app.Activity;
import android.content.Context;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ViewHelper {
	private final Context context;
	private final WindowManager windowManager;
	private Display defaultDisplay;
	
	private final float defaultWidth;
	private final float defaultHeight;
	
	public ViewHelper(Context context) {
		this.context = context;
		this.defaultWidth = ViewUtil.getDefaultWidth();
		this.defaultHeight = ViewUtil.getDefaultHeight();
		this.windowManager =  (WindowManager) context.getSystemService(Activity.WINDOW_SERVICE);
		if (this.windowManager != null) {			
			this.defaultDisplay = windowManager.getDefaultDisplay();
		}
	}
	
	public void setGlobalSize(ViewGroup root) {
		boolean isHorizontal = getOrientation();
		if (isHorizontal) {
			setGlobalSize(root, ViewUtil.getScreenHorizontalHeight(context), ViewUtil.getScreenHorizontalWidth(context));
		} else {
			setGlobalSize(root, ViewUtil.getScreenVerticalWidth(context), ViewUtil.getScreenVerticalHeight(context));
		}
	}

	private boolean getOrientation() {
		if (defaultDisplay != null) {
			int rotation = defaultDisplay.getRotation();
			switch (rotation) {
			case Surface.ROTATION_0:
			case Surface.ROTATION_180:
				return false;
			case Surface.ROTATION_90:
			case Surface.ROTATION_270:
				return true;
			default:
				return false;
			}
		}
		return false;
	}

	private void setGlobalSize(ViewGroup root, int width, int height){
		for (int i = 0; i < root.getChildCount(); i++) {
	        final View child = root.getChildAt(i);
	        setViewGroupSize(root, child, width, height);
	        if (child instanceof TextView){
	        }else if(child instanceof ImageView){
	        }else if(child instanceof Button){
	        }else if(child instanceof EditText){
	        }else if (child instanceof ViewGroup){
	            setGlobalSize((ViewGroup)child, width, height);
	        }
	    }
	}
	
	public void setViewGroupSize(ViewGroup root, View child, int width, int height){
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
			
			params.width = Math.round(dpiRate*params.width*width/defaultWidth);
			params.height = Math.round(dpiRate*params.height*height/defaultHeight);
			params.bottomMargin = Math.round(dpiRate*params.bottomMargin*height/defaultHeight);
			params.topMargin = Math.round(dpiRate*params.topMargin*height/defaultHeight);
			params.leftMargin = Math.round(dpiRate*params.leftMargin*width/defaultWidth);
			params.rightMargin = Math.round(dpiRate*params.rightMargin*width/defaultWidth);
			
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