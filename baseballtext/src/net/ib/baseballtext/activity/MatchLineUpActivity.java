package net.ib.baseballtext.activity;

import java.util.ArrayList;
import java.util.List;

import net.ib.baseballtext.R;
import net.ib.baseballtext.util.ViewHelper;
import net.ib.baseballtext.view.TextPollingView;
import net.ib.quizon.domain.match.Match;
import net.ib.quizon.domain.match.MatchLineup;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class MatchLineUpActivity extends Activity {
	private TextView homeTeamPitcherText;
	private TextView awayTeamPitcherText;
	private TextView homeTeambatterText;
	private TextView awayTeambatterText;

	private ViewHelper mViewHelper;
	
	private TextPollingView textPollingView;

	private List<Match> matchTempList;
	private MatchLineup homeTeamLineUpMatch;
	private MatchLineup awayTeamLineUpMatch;

	private int matchNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_matchlineup);

		homeTeamPitcherText = (TextView) findViewById(R.id.hometeampitchertext);
		awayTeamPitcherText = (TextView) findViewById(R.id.awayteampitchertext);
		homeTeambatterText = (TextView) findViewById(R.id.hometeambattertext);
		awayTeambatterText = (TextView) findViewById(R.id.awayteambattertext);

		textPollingView = new TextPollingView();
		matchTempList = new ArrayList<Match>();

		matchTempList = textPollingView.getMatchTempList();
		matchNumber = textPollingView.getMatchNumber();

		StringBuilder sb = new StringBuilder();
		StringBuilder sb1 = new StringBuilder();

		if (matchNumber < 4) {
			homeTeamLineUpMatch = matchTempList.get(matchNumber).getHomeTeamMatchLineup();
			awayTeamLineUpMatch = matchTempList.get(matchNumber).getAwayTeamMatchLineup();

			homeTeamPitcherText.setText(homeTeamLineUpMatch.getPitcherInfo().getPitcherName());
			awayTeamPitcherText.setText(awayTeamLineUpMatch.getPitcherInfo().getPitcherName());

			Log.i("homeTeamLineUpMatch.getBatterInfo()", homeTeamLineUpMatch.getBatterInfo() + "");
			Log.i("homeTeamLineUpMatch.getBatterInfo().size()", homeTeamLineUpMatch.getBatterInfo().size() + "");
			for (int i=0; i<homeTeamLineUpMatch.getBatterInfo().size(); i++) {
				sb.append(homeTeamLineUpMatch.getBatterInfo().get(i).getBatterOrder());
				sb.append(" . ");
				sb.append(homeTeamLineUpMatch.getBatterInfo().get(i).getBatterName());
				sb.append("\n");
				
				sb1.append(awayTeamLineUpMatch.getBatterInfo().get(i).getBatterOrder());
				sb1.append(" . ");
				sb1.append(awayTeamLineUpMatch.getBatterInfo().get(i).getBatterName());
				sb1.append("\n");
			}
			homeTeambatterText.setText(sb);
			awayTeambatterText.setText(sb1);
		}
		mViewHelper = new ViewHelper(this);
		View mainLayout = findViewById(R.id.main_layout);
		mViewHelper.setGlobalSize((ViewGroup) mainLayout);
	}
}
