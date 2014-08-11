package net.ib.baseballtext.util;

import android.content.Context;
import android.graphics.Typeface;
import android.util.DisplayMetrics;

public class ViewUtil {
	private final static float defaultDpi = 2.0f;
	private final static int defaultWidth = 1280;
	private final static int defaultHeight = 720;

	private static Integer screenWidth;
	private static Integer screenHeight;
	private static Float dpi;
	private static Float dpiRate;
	private static Typeface godoM;
	private static Typeface godoB;

	/**
	 * 화면 전체 너비 가져오기
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		if (screenWidth == null) {
			initWindowData(context);
		}
		return screenWidth;
	}

	/**
	 * 화면 전체 높이 가져오기
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		if (screenHeight == null) {
			initWindowData(context);
		}
		return screenHeight;
	}

	/**
	 * Device 의 Dpi 값을 가져온다.
	 * 
	 * @param activity
	 * @return
	 */
	public static float getDpi(Context context) {
		if (dpi == null) {
			initWindowData(context);
		}
		return dpi;
	}

	public static float getDpiRate(Context context) {
		if (dpiRate == null) {
			dpiRate = defaultDpi / getDpi(context);
		}
		return dpiRate;
	}

	public static int getPixels(Context context, int dip) {
		return (int) (dip * getDpi(context) + 0.5f);
	}

	public static Typeface getTypefaceGodoM(Context context) {
		if (godoM == null) {
			godoM = Typeface.createFromAsset(context.getAssets(),"fonts/GodoM.ttf");
		}
		return godoM;
	}

	public static Typeface getTypefaceGodoB(Context context) {
		if (godoB == null) {
			godoB = Typeface.createFromAsset(context.getAssets(), "fonts/GodoB.ttf");
		}
		return godoB;
	}
	
	public static int getWidth(Context context, int dip) {
		return getWidthSize(context, getPixels(context, dip));
	}

	public static int getWidthSize(Context context, int width) {
		return getWidthSize(context, width, getScreenWidth(context));
	}

	public static int getWidthSize(Context context, int width, int screenWidth) {
		return getWidthSize(getDpiRate(context), width, screenWidth);
	}

	public static int getWidthSize(float dpiRate, int width, int screenWidth) {
		return Math.round(dpiRate * width * screenWidth / defaultWidth);
	}

	public static int getHeight(Context context, int dip) {
		return getHeightSize(context, getPixels(context, dip));
	}
	
	public static int getHeightSize(Context context, int height) {
		return getHeightSize(context, height, getScreenHeight(context));
	}

	public static int getHeightSize(Context context, int height, int screenHeight) {
		return getHeightSize(getDpiRate(context), height, screenHeight);
	}

	public static int getHeightSize(float dpiRate, int height, int screenHeight) {
		return Math.round(dpiRate * height * screenHeight / defaultHeight);
	}

	private synchronized static void initWindowData(Context context) {
		if (screenHeight == null && screenWidth == null && dpi == null) {
			// final WindowManager windowManager = (WindowManager)
			// context.getSystemService(Context.WINDOW_SERVICE);
			// final Display display = windowManager.getDefaultDisplay();
			// screenHeight = display.getHeight();
			// screenWidth = display.getWidth();
			// final DisplayMetrics displayMetrics = new DisplayMetrics();
			// display.getMetrics(displayMetrics);
			// dpi = displayMetrics.density;

			final DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
			dpi = displayMetrics.density;
			screenWidth = displayMetrics.widthPixels;
			screenHeight = displayMetrics.heightPixels;
		}
	}
}