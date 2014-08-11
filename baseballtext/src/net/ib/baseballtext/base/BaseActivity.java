package net.ib.baseballtext.base;


import net.ib.baseballtext.util.GlobalUtil;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
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

public class BaseActivity extends Activity {
	/**
	 * context는 BaseActivity에 있는놈을 그냥 쓴다.
	 */
	public Context context;
	/**handler를 상속받아 쓴다*/
	public Handler handler;
	/**intent는 전역변수로*/
	public Intent intent;

	public static Typeface godoM;
	public static Typeface godoB;

	public int soundCrowd;
	public int soundIdHomer;
	public int soundBtnClick1;
	public int soundBtnClick2;
	public int soundClock;
	public int soundQuiz;
	public int soundRandomItem;

	public int soundCrowdPlay;
	public int soundIdHomerPlay;
	public int soundBtnClick1Play;
	public int soundBtnClick2Play;
	public int soundClockPlay;
	public int soundQuizPlay;
	public int soundRandomItemPlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//context는 걍 디스 이즈 디스
		context=this;
		handler=new Handler();


		getBundleValue();
		initActivity(savedInstanceState);
		setGatewayLink();
		setActionBar();
		setAdapter();
	}

	/**
	 * intent에 추가된 Bundle 값을 여기서 처리한다.
	 */
	public void getBundleValue(){

	}

	public void initActivity(Bundle savedInstanceState){
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initLayout();
		showLayout();
		setListener();
		doAction();
	}

	public void setListener() {

	}

	public void initLayout() {

	}

	public void showLayout() {

	}
	/**
	 * 무언가 화면전환이 필요할때 사용하자
	 */
	public void doAction(){

	}
	
	/**
	 * api 연동
	 */
	public void setGatewayLink(){
		
	}
	
	/**
	 * ActionBar 세팅
	 */
	public void setActionBar(){
	}
	/**
	 * adapter 세팅</br> 
	 * 선언된 dynamic view에 adapter를 달아준다.</br> 
	 */
	public void setAdapter(){}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    //No call for super(). Bug on API Level > 11.
	}
	
	
	
	
	@Override
	public void setContentView(int layoutResID){
		super.setContentView(layoutResID);
		//글씨체 변경(godo font)
		if(BaseActivity.godoM==null)
			BaseActivity.godoM=Typeface.createFromAsset(getAssets(),"fonts/GodoM.ttf");
		
		ViewGroup rootView=(ViewGroup)findViewById(android.R.id.content);
		setGlobalFont(rootView);
		setGlobalSize(rootView,GlobalUtil.getScreenWidth(context),GlobalUtil.getScreenHeight(context));
//		setGlobalSize(rootView,480,800);
		
	}
	

	/**
	 * view group의 child가 가질 수 있는 text를 모두 변경하는 method 
	 * @param root
	 */
	public void setGlobalFont(ViewGroup root) {
	    for (int i = 0; i < root.getChildCount(); i++) {
	        View child = root.getChildAt(i);
	        if (child instanceof TextView){
	            ((TextView)child).setTypeface(godoM);
//	            DisplayMetrics displayMetrics = new DisplayMetrics();
//	            WindowManager windowManager = (WindowManager) BaseActivity.this.getSystemService(Context.WINDOW_SERVICE);
//	            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
//	    		
//	            float rateDpi=defaultDpi/displayMetrics.density;
	            
	            
//				((TextView)child).setTextSize((((TextView)child).getTextSize()*GlobalUtil.getScreenWidth(context))/1280);
		       
	        }
	        else if (child instanceof ViewGroup)
	            setGlobalFont((ViewGroup)child);
	    }
	}
	
	int defaultWidth=1280;
	int defaultHeight=720;
	float defaultDpi=2.0f;
	public void setGlobalSize(ViewGroup root,int width,int height){
		
		for (int i = 0; i < root.getChildCount(); i++) {
	        View child = root.getChildAt(i);
	        
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
	public float getRateDpi(){
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) BaseActivity.this.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		
        float rateDpi=defaultDpi/displayMetrics.density;
		
		return rateDpi;
	}
	public void setViewGroupSize(ViewGroup root,View child,int width,int height){
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) BaseActivity.this.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
		
        float rateDpi=defaultDpi/displayMetrics.density;
        
		if(root instanceof LinearLayout){
			LinearLayout.LayoutParams params=(LinearLayout.LayoutParams) child.getLayoutParams();
			params.width=Math.round(rateDpi*params.width*width/defaultWidth);
			params.height=Math.round(rateDpi*params.height*height/defaultHeight);
			params.bottomMargin=Math.round(rateDpi*params.bottomMargin*height/defaultHeight);
			params.topMargin=Math.round(rateDpi*params.topMargin*height/defaultHeight);
			params.leftMargin=Math.round(rateDpi*params.leftMargin*width/defaultWidth);
			params.rightMargin=Math.round(rateDpi*params.rightMargin*width/defaultWidth);
			
			child.setLayoutParams(params);
		}else if(root instanceof RelativeLayout){
			RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams) child.getLayoutParams();
			
			params.width=Math.round(rateDpi*params.width*width/defaultWidth);
			params.height=Math.round(rateDpi*params.height*height/defaultHeight);
			params.bottomMargin=Math.round(rateDpi*params.bottomMargin*height/defaultHeight);
			params.topMargin=Math.round(rateDpi*params.topMargin*height/defaultHeight);
			params.leftMargin=Math.round(rateDpi*params.leftMargin*width/defaultWidth);
			params.rightMargin=Math.round(rateDpi*params.rightMargin*width/defaultWidth);
			
			child.setLayoutParams(params);
		}else if(root instanceof FrameLayout){
			FrameLayout.LayoutParams params=(FrameLayout.LayoutParams) child.getLayoutParams();
			
			params.width=Math.round(rateDpi*params.width*width/defaultWidth);
			params.height=Math.round(rateDpi*params.height*height/defaultHeight);
			params.bottomMargin=Math.round(rateDpi*params.bottomMargin*height/defaultHeight);
			params.topMargin=Math.round(rateDpi*params.topMargin*height/defaultHeight);
			params.leftMargin=Math.round(rateDpi*params.leftMargin*width/defaultWidth);
			params.rightMargin=Math.round(rateDpi*params.rightMargin*width/defaultWidth);
			
			child.setLayoutParams(params);
		}
	}
}
