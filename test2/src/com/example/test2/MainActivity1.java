//package com.example.test2;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.content.Context;
//import android.graphics.Typeface;
//import android.util.DisplayMetrics;
//import android.util.Log;
//
//
//public class MainActivity1 extends Activity {
//	private MainActivity1 activity;
//	
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		
//		setContentView(R.layout.activity_main);
//		
//		ViewUtil.getDpi(activity);
//		ViewUtil.getDpiRate(activity);
//	}
//}
//
//class ViewUtil {
//
//	private final static float defaultDpi = 2.0f;
//	private final static int defaultWidth = 1280;
//	private final static int defaultHeight = 720;
//
//	private static Integer screenWidth;
//	private static Integer screenHeight;
//	private static Float dpi;
//	private static Float dpiRate;
//	private static Typeface godoM;
//	private static Typeface godoB;
//
//	/**
//	 * 화면 전체 너비 가져오기
//	 * 
//	 * @param context
//	 * @return
//	 */
//	public static int getScreenWidth(Context context) {
//		if (screenWidth == null) {
//			initWindowData(context);
//		}
//		Log.i("screenWidth", screenWidth + "");
//		return screenWidth;
//	}
//
//	/**
//	 * 화면 전체 높이 가져오기
//	 * 
//	 * @param context
//	 * @return
//	 */
//	public static int getScreenHeight(Context context) {
//		if (screenHeight == null) {
//			initWindowData(context);
//		}
//		Log.i("screenHeight", screenHeight + "");
//		return screenHeight;
//	}
//
//	/**
//	 * Device 의 Dpi 값을 가져온다.
//	 * 
//	 * @param activity
//	 * @return
//	 */
//	public static float getDpi(Context context) {
//		if (dpi == null) {
//			initWindowData(context);
//		}
//		Log.i("dpi", dpi + "");
//		return dpi;
//	}
//
//	public static float getDpiRate(Context context) {
//		if (dpiRate == null) {
//			dpiRate = defaultDpi / getDpi(context);
//		}
//		Log.i("dpiRate", dpiRate + "");
//		return dpiRate;
//	}
//
//	public static int getPixels(Context context, int dip) {
//		Log.i("(int) (dip * getDpi(context) + 0.5f)", (int) (dip * getDpi(context) + 0.5f) + "");
//		return (int) (dip * getDpi(context) + 0.5f);
//	}
//
//	public static Typeface getTypefaceGodoM(Context context) {
//		if (godoM == null) {
//			godoM = Typeface.createFromAsset(context.getAssets(),"fonts/GodoM.ttf");
//		}
//		Log.i("godoM", godoM + "");
//		return godoM;
//	}
//
//	public static Typeface getTypefaceGodoB(Context context) {
//		if (godoB == null) {
//			godoB = Typeface.createFromAsset(context.getAssets(), "fonts/GodoB.ttf");
//		}
//		Log.i("godoB", godoB + "");
//		return godoB;
//	}
//
//	public static int getWidth(Context context, int dip) {
//		Log.i("getWidthSize(context, getPixels(context, dip))", getWidthSize(context, getPixels(context, dip)) + "");
//		return getWidthSize(context, getPixels(context, dip));
//	}
//
//	public static int getWidthSize(Context context, int width) {
//		Log.i("getWidthSize(context, width, getScreenWidth(context))", getWidthSize(context, width, getScreenWidth(context)) + "");
//		return getWidthSize(context, width, getScreenWidth(context));
//	}
//
//	public static int getWidthSize(Context context, int width, int screenWidth) {
//		Log.i("getWidthSize(getDpiRate(context), width, screenWidth)", getWidthSize(getDpiRate(context), width, screenWidth) + "");
//		return getWidthSize(getDpiRate(context), width, screenWidth);
//	}
//
//	public static int getWidthSize(float dpiRate, int width, int screenWidth) {
//		Log.i("Math.round(dpiRate * width * screenWidth / defaultWidth)", Math.round(dpiRate * width * screenWidth / defaultWidth) + "");
//		return Math.round(dpiRate * width * screenWidth / defaultWidth);
//	}
//
//	public static int getHeight(Context context, int dip) {
//		Log.i("getHeightSize(context, getPixels(context, dip))", getHeightSize(context, getPixels(context, dip)) + "");
//		return getHeightSize(context, getPixels(context, dip));
//	}
//
//	public static int getHeightSize(Context context, int height) {
//		Log.i("getHeightSize(context, height, getScreenHeight(context))", getHeightSize(context, height, getScreenHeight(context)) + "");
//		return getHeightSize(context, height, getScreenHeight(context));
//	}
//
//	public static int getHeightSize(Context context, int height, int screenHeight) {
//		Log.i("getHeightSize(getDpiRate(context), height, screenHeight)", getHeightSize(getDpiRate(context), height, screenHeight) + "");
//		return getHeightSize(getDpiRate(context), height, screenHeight);
//	}
//
//	public static int getHeightSize(float dpiRate, int height, int screenHeight) {
//		Log.i("Math.round(dpiRate * height * screenHeight / defaultHeight)", Math.round(dpiRate * height * screenHeight / defaultHeight) + "");
//		return Math.round(dpiRate * height * screenHeight / defaultHeight);
//	}
//
//	private synchronized static void initWindowData(Context context) {
//		if (screenHeight == null && screenWidth == null && dpi == null) {
//			// final WindowManager windowManager = (WindowManager)
//			// context.getSystemService(Context.WINDOW_SERVICE);
//			// final Display display = windowManager.getDefaultDisplay();
//			// screenHeight = display.getHeight();
//			// screenWidth = display.getWidth();
//			// final DisplayMetrics displayMetrics = new DisplayMetrics();
//			// display.getMetrics(displayMetrics);
//			// dpi = displayMetrics.density;
//
//			final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//			dpi = displayMetrics.density;
//			screenWidth = displayMetrics.widthPixels;
//			screenHeight = displayMetrics.heightPixels;
//			Log.i("initWindowData_dpi", dpi + "");
//			Log.i("initWindowData_screenWidth", screenWidth + "");
//			Log.i("initWindowData_screenHeight", screenHeight + "");
//		}
//	}
//}