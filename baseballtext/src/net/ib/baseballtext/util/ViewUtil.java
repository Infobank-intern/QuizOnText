package net.ib.baseballtext.util;

import android.content.Context;
import android.util.DisplayMetrics;

public class ViewUtil {
	private final static float defaultDpi = 2.0f;
	private final static int defaultWidth = 720;
	private final static int defaultHeight = 1280;

	private static Integer screenVerticalWidth;
	private static Integer screenVerticalHeight;
	private static Integer screenHorizontalWidth;
	private static Integer screenHorizontalHeight;
	private static Float dpi;
	private static Float dpiRate;

	public static int getDefaultWidth() {
		return defaultWidth;
	}
	
	public static int getDefaultHeight() {
		return defaultHeight;
	}
	
	/**
	 * 화면 전체 너비 가져오기
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenVerticalWidth(Context context) {
		if (screenVerticalWidth == null) {
			initVerticalWindowData(context);
		}
		return screenVerticalWidth;
	}

	/**
	 * 화면 전체 높이 가져오기
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenVerticalHeight(Context context) {
		if (screenVerticalHeight == null) {
			initVerticalWindowData(context);
		}
		return screenVerticalHeight;
	}

	/**
	 * 화면 전체 너비 가져오기
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHorizontalWidth(Context context) {
		if (screenHorizontalWidth == null) {
			initHorizontalWindowData(context);
		}
		return screenHorizontalWidth;
	}
	
	/**
	 * 화면 전체 높이 가져오기
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHorizontalHeight(Context context) {
		if (screenHorizontalHeight == null) {
			initHorizontalWindowData(context);
		}
		return screenHorizontalHeight;
	}

	/**
	 * Device 의 Dpi 값을 가져온다.
	 * 
	 * @param activity
	 * @return
	 */
	public static float getDpi(Context context) {
		if (dpi == null) {
			final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
			dpi = displayMetrics.density;
		}
		return dpi;
	}
	
	public static float getDpiRate(Context context) {
		if (dpiRate == null) {
			dpiRate = defaultDpi / getDpi(context);
		}
		return dpiRate;
	}
	
	public static float getDefalutDip() {
		return defaultDpi;
	}

	public static int getPixels(Context context, int dip) {
		return (int) (dip * getDpi(context) + 0.5f);
	}

	public static int getWidthSize(Context context, int width, int screenWidth) {
		return getWidthSize(getDpiRate(context), width, screenWidth);
	}

	public static int getWidthSize(float dpiRate, int width, int screenWidth) {
		return Math.round(dpiRate * width * screenWidth / defaultWidth);
	}

	public static int getHeightSize(Context context, int height, int screenHeight) {
		return getHeightSize(getDpiRate(context), height, screenHeight);
	}

	public static int getHeightSize(float dpiRate, int height, int screenHeight) {
		return Math.round(dpiRate * height * screenHeight / defaultHeight);
	}

	private synchronized static void initVerticalWindowData(Context context) {
		if (screenVerticalHeight == null && screenVerticalWidth == null) {
			final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
			screenVerticalWidth = displayMetrics.widthPixels;
			screenVerticalHeight = displayMetrics.heightPixels;
		}
	}
	private synchronized static void initHorizontalWindowData(Context context) {
		if (screenHorizontalHeight == null && screenHorizontalWidth == null) {
			final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
			screenHorizontalWidth = displayMetrics.widthPixels;
			screenHorizontalHeight = displayMetrics.heightPixels;
		}
	}
}