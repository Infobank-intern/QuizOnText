package kr.co.quizon;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

	}
	public void onClick(View view){
		Toast.makeText(MainActivity.this, "clickclick", Toast.LENGTH_SHORT).show();
		
	}
	
}
