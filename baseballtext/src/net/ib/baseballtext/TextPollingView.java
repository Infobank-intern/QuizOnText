package net.ib.baseballtext;

import java.util.List;

import net.ib.baseballtext.network.TextPollingTask;
import net.ib.quizon.api.match.GetMatchBroadcastRes;
import net.ib.quizon.domain.match.MatchBroadcast;
import net.ib.quizon.domain.match.MatchDisplayBoard;
import net.ib.quizon.domain.match.MatchPlayers;
import net.ib.quizon.domain.match.MatchSummary;
import android.graphics.Color;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class TextPollingView extends TextPollingTask implements OnClickListener {
    private TextView baseballText;
    private TextView homeTeamNameText;
    private TextView awayTeamNameText;
    private TextView stadiumText;
    private TextView inningText;
    private TextView homeTeamPointText;
    private TextView awayTeamPointText;
    private TextView ballText;
    private TextView strikeText;
    private TextView outText;
    private Button firstButton;
    private Button secondButton;
    private Button thirdButton;
    private Button fourthButton;
    private Button fifthButton;
    private Button sixthButton;
    private Button seventhButton;
    private Button eighthButton;
    private Button ninthButton;
    private ImageView baseImageView;
    private ImageView base1ImageView;
    private ImageView base2ImageView;
    private ImageView base3ImageView;
    
	public TextPollingView(View titleView, View mainView) {
        homeTeamNameText = (TextView) titleView.findViewById(R.id.hometeamname);
        awayTeamNameText = (TextView) titleView.findViewById(R.id.awayteamname);
        stadiumText = (TextView) titleView.findViewById(R.id.stadium);
        inningText = (TextView) titleView.findViewById(R.id.inning);
        homeTeamPointText = (TextView) titleView.findViewById(R.id.hometeampoint);
        awayTeamPointText = (TextView) titleView.findViewById(R.id.awayteampoint);
        strikeText = (TextView) titleView.findViewById(R.id.strike);
        ballText = (TextView) titleView.findViewById(R.id.ball);
        outText = (TextView) titleView.findViewById(R.id.out);
        
        baseballText = (TextView) mainView.findViewById(R.id.baseballtext);
        baseballText.setMovementMethod(new ScrollingMovementMethod());
        
        firstButton = (Button) mainView.findViewById(R.id.first);
        firstButton.setOnClickListener(this);
        secondButton = (Button) mainView.findViewById(R.id.second);
        secondButton.setOnClickListener(this);
        thirdButton = (Button) mainView.findViewById(R.id.third);
        thirdButton.setOnClickListener(this);
        fourthButton = (Button) mainView.findViewById(R.id.fourth);
        fourthButton.setOnClickListener(this);
        fifthButton = (Button) mainView.findViewById(R.id.fifth);
        fifthButton.setOnClickListener(this);
        sixthButton = (Button) mainView.findViewById(R.id.sixth);
        sixthButton.setOnClickListener(this);
        seventhButton = (Button) mainView.findViewById(R.id.seventh);
        seventhButton.setOnClickListener(this);
        eighthButton = (Button) mainView.findViewById(R.id.eighth);
        eighthButton.setOnClickListener(this);
        ninthButton = (Button) mainView.findViewById(R.id.ninth);
        ninthButton.setOnClickListener(this);
        
        baseImageView = (ImageView) mainView.findViewById(R.id.base);
        base1ImageView = (ImageView) mainView.findViewById(R.id.base1);
        base2ImageView = (ImageView) mainView.findViewById(R.id.base2);
        base3ImageView = (ImageView) mainView.findViewById(R.id.base3);
	}

	@Override
	protected void onPostExecute(GetMatchBroadcastRes getMatchBroadcastRes) {
		if (getMatchBroadcastRes != null) {
			baseballText.setText("");
			StringBuilder sb = new StringBuilder();
			
			List<MatchBroadcast> broadcast = getMatchBroadcastRes.getBroadcast();
			for (MatchBroadcast matchBroadcast : broadcast) {
				if (matchBroadcast == null) {
					continue;
				}
				sb.append(matchBroadcast.getBroadcast());
			}
			
			MatchDisplayBoard matchDisplayBoard = getMatchBroadcastRes.getMatchDisplayBoard();
			setMatchDisplayBoard(matchDisplayBoard);
			
			MatchPlayers matchPlayers = getMatchBroadcastRes.getMatchPlayers();
			setMatchPlayers(matchPlayers);
			Log.i("matchPlayers", matchPlayers.toString());
			
			List<MatchSummary> matchSummaryList = getMatchBroadcastRes.getMatchSummaryList();
			Log.i("matchSummaryList", matchSummaryList.toString());
			for (MatchSummary matchSummary : matchSummaryList) {
				if (matchSummary == null) {
					continue;
				}
				if (matchSummary.getMatchId().equals(matchId)) {
					stadiumText.setText(matchSummary.getMatchStadium());
					inningText.setText(matchSummary.getMatchPresent());
					homeTeamNameText.setText(matchSummary.getHomeTeamName());
					awayTeamNameText.setText(matchSummary.getAwayTeamName());
					homeTeamPointText.setText(Integer.toString(matchSummary.getHomeTeamPoint()));
					awayTeamPointText.setText(Integer.toString(matchSummary.getAwayTeamPoint()));
				}
			}
			baseballText.setText(sb.toString());
		} else {
			baseballText.setText("문자 중계 로딩 실패");
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button:
			if (matchId != null) {
				inning = 0;
			}
			break;
		case R.id.first:
			if (matchId != null) {
				inning = 1;
				setButtonColor(firstButton);
				execute();
			}
			break;
		case R.id.second:
			if (matchId != null) {
				inning = 2;
				setButtonColor(secondButton);
				execute();
			}
			break;
		case R.id.third:
			if (matchId != null) {
				inning = 3;
				setButtonColor(thirdButton);
				execute();
			}
			break;
		case R.id.fourth:
			if (matchId != null) {
				inning = 4;
				setButtonColor(fourthButton);
				execute();
			}
			break;
		case R.id.fifth:
			if (matchId != null) {
				inning = 5;
				setButtonColor(fifthButton);
			}
			break;
		case R.id.sixth:
			if (matchId != null) {
				inning = 6;
				setButtonColor(sixthButton);
			}
			break;
		case R.id.seventh:
			if (matchId != null) {
				inning = 7;
				setButtonColor(seventhButton);
			}
			break;
		case R.id.eighth:
			if (matchId != null) {
				inning = 8;
				setButtonColor(eighthButton);
			}
			break;
		case R.id.ninth:
			if (matchId != null) {
				inning = 9;
				setButtonColor(ninthButton);
			}
			break;
		default:
			break;
		}
	}
	
	private void setMatchDisplayBoard(MatchDisplayBoard matchDisplayBoard) {
		if (matchDisplayBoard != null) {
			String awayTeamName = matchDisplayBoard.getAwayTeamName();
			if (awayTeamName != null) {				
//				awayTeamNameText.setText(awayTeamName);
			}
			List<String> awayTeamPoint = matchDisplayBoard.getAwayTeamPoint();
//			awayTeamPointText.setText(awayTeamPoint.get(awayTeamPoint.size()-1));
//			awayTeamPointText.setText(awayTeamPoint.get(INNING-1));
			
			String homeTeamName = matchDisplayBoard.getHomeTeamName();
			if (homeTeamName != null) {				
//				homeTeamNameText.setText(homeTeamName);
			}
			List<String> homeTeamPoint = matchDisplayBoard.getHomeTeamPoint();
			
//			homeTeamPointText.setText(homeTeamPoint.get(homeTeamPoint.size()-1));
//			homeTeamPointText.setText(homeTeamPoint.get(INNING-1));
			
			
//			뭔가이상해..
			String ball = matchDisplayBoard.getBall();
			ballText.setText(ball);
			String strike = matchDisplayBoard.getStrike();
			strikeText.setText(strike);
			String out = matchDisplayBoard.getOut();
			outText.setText(out);
		}
	}
	
	private void setMatchPlayers(MatchPlayers matchPlayers) {
		if (matchPlayers != null) {						
			String batter = matchPlayers.getBatter();
			String pitcher = matchPlayers.getPitcher();
			Log.i("matchPlayers111", "Batter " + batter);
			Log.i("matchPlayers111", "Pitcher " + pitcher);
			
			String firstBatter = matchPlayers.getFirstBatter();
			Log.i("test", firstBatter.toString());
			String secondBatter = matchPlayers.getSecondBatter();
			String thirdBatter = matchPlayers.getThirdBatter();
			
			if (Strings.isEmptyString(firstBatter)) {
				base1ImageView.setVisibility(View.VISIBLE);
			} else {
				base1ImageView.setVisibility(View.INVISIBLE);
			}
			if (Strings.isEmptyString(secondBatter)) {
				base2ImageView.setVisibility(View.VISIBLE);
			} else {
				base2ImageView.setVisibility(View.INVISIBLE);
			}
			if (Strings.isEmptyString(thirdBatter)) {
				base3ImageView.setVisibility(View.VISIBLE);
			} else {
				base3ImageView.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	private void setButtonColor(Button button) {
		initButtonColor();
		button.setTextColor(Color.RED);
	}

	private void initButtonColor() {
		firstButton.setTextColor(Color.WHITE);
		secondButton.setTextColor(Color.WHITE);
		thirdButton.setTextColor(Color.WHITE);
		fourthButton.setTextColor(Color.WHITE);
		fifthButton.setTextColor(Color.WHITE);
		sixthButton.setTextColor(Color.WHITE);
		seventhButton.setTextColor(Color.WHITE);
		eighthButton.setTextColor(Color.WHITE);
		ninthButton.setTextColor(Color.WHITE);
	}
}