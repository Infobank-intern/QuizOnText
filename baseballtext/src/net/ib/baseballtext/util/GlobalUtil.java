package net.ib.baseballtext.util;

import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.ib.baseballtext.R;
import net.ib.quizon.domain.battle.BattleUserInfo;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class GlobalUtil {
	
	public static String getTopActivity(Context context){
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> Info = am.getRunningTasks(1);
		ComponentName topActivity = Info.get(0).topActivity;
		
		return topActivity.getShortClassName();
	}
	
	
	public static String getCurrentAppVersion(Context context){
		String version="";
		try {
			PackageInfo i = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			version = i.versionName;
		} catch(NameNotFoundException e) { }
		return version;
	}
	/**
	 * 화먄 전체 가로 길이 가져오기
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenWidth(Context context) {
		int screenWidth = 0;

		Display display;
		display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

		screenWidth = display.getWidth();

		return screenWidth;
	}

	/**
	 * 화먄 전체 높이 가져오기
	 * 
	 * @param context
	 * @return
	 */
	public static int getScreenHeight(Context context) {
		int screenHeight = 0;

		Display display;
		display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

		screenHeight = display.getHeight();

		return screenHeight;
	}

	public static float getDpi(Activity activity) {
		WindowManager wm = activity.getWindowManager(); // getWindowManager() 는
														// Activity 의 메소드
		Display dp = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		dp.getMetrics(metrics);

		float dpi = metrics.density;

		return dpi;
	}

	public static boolean isStorage() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return true;
		}
		return false;

	}
}