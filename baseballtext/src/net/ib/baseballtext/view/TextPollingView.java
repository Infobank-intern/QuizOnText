package net.ib.baseballtext.view;

import java.util.ArrayList;
import java.util.List;

import kr.co.quizon.network.link.match.GetMatchBroadcastLink;
import kr.co.quizon.network.link.match.GetMatchInfoLink;
import net.ib.baseballtext.R;
import net.ib.baseballtext.activity.MainActivity;
import net.ib.baseballtext.activity.MatchLineUpActivity;
import net.ib.baseballtext.util.Strings;
import net.ib.quizon.api.match.GetMatchBroadcastReq;
import net.ib.quizon.api.match.GetMatchBroadcastRes;
import net.ib.quizon.api.match.GetMatchInfoReq;
import net.ib.quizon.api.match.GetMatchInfoRes;
import net.ib.quizon.domain.match.Match;
import net.ib.quizon.domain.match.MatchBroadcast;
import net.ib.quizon.domain.match.MatchDisplayBoard;
import net.ib.quizon.domain.match.MatchPlayers;
import net.ib.quizon.domain.match.MatchSummary;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


public class TextPollingView implements OnClickListener {
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
    private Button allButton;
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
    
    private static List<Match> matchTempList = new ArrayList<Match>();
    private static int inning;
    private static int matchNumber;
    
    private String matchId;
    private int presentInning;
    
    private Context mainContext;
    private Intent intent;
    
    public TextPollingView() {
    }
    
	public TextPollingView(View titleView, View mainView, Context mContext) {
        homeTeamNameText = (TextView) titleView.findViewById(R.id.hometeamname);
        homeTeamNameText.setOnClickListener(this);
        awayTeamNameText = (TextView) titleView.findViewById(R.id.awayteamname);
        awayTeamNameText.setOnClickListener(this);
        stadiumText = (TextView) titleView.findViewById(R.id.stadium);
        inningText = (TextView) titleView.findViewById(R.id.inning);
        homeTeamPointText = (TextView) titleView.findViewById(R.id.hometeampoint);
        awayTeamPointText = (TextView) titleView.findViewById(R.id.awayteampoint);
        strikeText = (TextView) titleView.findViewById(R.id.strike);
        ballText = (TextView) titleView.findViewById(R.id.ball);
        outText = (TextView) titleView.findViewById(R.id.out);
        
        baseballText = (TextView) mainView.findViewById(R.id.baseballtext);
        allButton = (Button) mainView.findViewById(R.id.button);
        allButton.setOnClickListener(this);
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
        
        mainContext = mContext;
	}
	
	public void getPresentInning(final String matchId, final List<Match> matchTempList, final int matchNumber) {
		this.matchId = matchId;
		this.matchTempList = matchTempList;
		this.matchNumber = matchNumber;
		
		new AsyncTask<Void, Void, GetMatchInfoRes>() {
			@Override
			protected GetMatchInfoRes doInBackground(Void... params) {
				GetMatchInfoReq getMatchInfoReq = new GetMatchInfoReq();
				getMatchInfoReq.setMatchId(matchId);
				
				GetMatchInfoLink getMatchInfoLink = new GetMatchInfoLink(getMatchInfoReq);
				return getMatchInfoLink.linkage();
			}
			@Override
			protected void onPostExecute(GetMatchInfoRes result) {
				if (result != null && result.getMatchInfo() != null) {
					Log.i("getPresentInning_GetMatchInfoReq_result", result + "");
					presentInning = result.getMatchInfo().getInning();
					if (presentInning == 1) {
						setButtonColor(firstButton);
					} else if (presentInning == 2) {
						setButtonColor(secondButton);
					} else if (presentInning == 3) {
						setButtonColor(thirdButton);
					} else if (presentInning == 4) {
						setButtonColor(fourthButton);
					} else if (presentInning == 5) {
						setButtonColor(fifthButton);
					} else if (presentInning == 6) {
						setButtonColor(sixthButton);
					} else if (presentInning == 7) {
						setButtonColor(seventhButton);
					} else if (presentInning == 8) {
						setButtonColor(eighthButton);
					} else if (presentInning == 9) {
						setButtonColor(ninthButton);
					}
					inning = presentInning;
					updateView(matchId, presentInning);
				}
			}
		}.execute();
	}
	
	public void updateView(final String matchId, final int inning) {
		this.matchId = matchId; 
		new AsyncTask<Void, Void, GetMatchBroadcastRes>() {
			@Override
			protected GetMatchBroadcastRes doInBackground(Void... param) {
				GetMatchBroadcastReq getMatchBroadcastReq = new GetMatchBroadcastReq();
				getMatchBroadcastReq.setMatchId(matchId);
				if (inning != 0) {
					getMatchBroadcastReq.setInning(Integer.valueOf(inning));
				}
				GetMatchBroadcastLink getMatchBroadcastLink = new GetMatchBroadcastLink(getMatchBroadcastReq);
				return getMatchBroadcastLink.linkage();
			}
			
			@Override
			protected void onPostExecute(GetMatchBroadcastRes getMatchBroadcastRes) {
				if (getMatchBroadcastRes != null && getMatchBroadcastRes.getBroadcast().size() > 0) {
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
					
					List<MatchSummary> matchSummaryList = getMatchBroadcastRes.getMatchSummaryList();
					Log.i("updateView_ getMatchBroadcastRes.getMatchPlayers()", matchSummaryList + "");
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
							break;
						}
					}
					baseballText.setText(sb.toString());
				} else {
					baseballText.setText("경기 시작 전 입니다. 경기 시간을 확인해 주세요");
				}
			}
		}.execute();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button:
			setButtonColor(allButton);
			inning = 0;
			updateView(matchId, 0);
			break;
		case R.id.first:
			setButtonColor(firstButton);
			inning = 1;
			updateView(matchId, 1);
			break;
		case R.id.second:
			setButtonColor(secondButton);
			inning = 2;
			updateView(matchId, 2);
			break;
		case R.id.third:
			setButtonColor(thirdButton);
			inning = 3;
			updateView(matchId, 3);
			break;
		case R.id.fourth:
			setButtonColor(fourthButton);
			inning = 4;
			updateView(matchId, 4);
			break;
		case R.id.fifth:
			setButtonColor(fifthButton);
			inning = 5;
			updateView(matchId, 5);
			break;
		case R.id.sixth:
			setButtonColor(sixthButton);
			inning = 6;
			updateView(matchId, 6);
			break;
		case R.id.seventh:
			setButtonColor(seventhButton);
			inning = 7;
			updateView(matchId, 7);
			break;
		case R.id.eighth:
			setButtonColor(eighthButton);
			inning = 8;
			updateView(matchId, 8);
			break;
		case R.id.ninth:
			setButtonColor(ninthButton);
			inning = 9;
			updateView(matchId, 9);
			break;
		case R.id.hometeamname:
			homeTeamNameText.setTextColor(Color.YELLOW);
			awayTeamNameText.setTextColor(Color.YELLOW);
			intent = new Intent(mainContext, MatchLineUpActivity.class);
			mainContext.startActivity(intent);
			break;
		case R.id.awayteamname:
			homeTeamNameText.setTextColor(Color.YELLOW);
			awayTeamNameText.setTextColor(Color.YELLOW);
			intent = new Intent(mainContext, MatchLineUpActivity.class);
			mainContext.startActivity(intent);
			break;
		default:
			break;
		}
	}
	
	private void setMatchDisplayBoard(MatchDisplayBoard matchDisplayBoard) {
		if (matchDisplayBoard != null) {
			String ball = matchDisplayBoard.getBall();
//			Log.i("ball", ball);
			strikeText.setText(ball);
			String strike = matchDisplayBoard.getStrike();
//			Log.i("strike", strike);
			ballText.setText(strike);
			String out = matchDisplayBoard.getOut();
			outText.setText(out);
		}
	}
	
	private void setMatchPlayers(MatchPlayers matchPlayers) {
		if (matchPlayers != null) {						
			String firstBatter = matchPlayers.getFirstBatter();
//			Log.i("test", firstBatter.toString());
			String secondBatter = matchPlayers.getSecondBatter();
			String thirdBatter = matchPlayers.getThirdBatter();
			
			if (Strings.isNotEmptyString(firstBatter)) {
				base1ImageView.setVisibility(View.VISIBLE);
			} else {
				base1ImageView.setVisibility(View.INVISIBLE);
			}
			if (Strings.isNotEmptyString(secondBatter)) {
				base2ImageView.setVisibility(View.VISIBLE);
			} else {
				base2ImageView.setVisibility(View.INVISIBLE);
			}
			if (Strings.isNotEmptyString(thirdBatter)) {
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
		allButton.setTextColor(Color.WHITE);
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
	
	public int getInning() {
		return inning;
	}
	
	public List<Match> getMatchTempList() {
		Log.i("pollingview_matchTempList", matchTempList + "");
		return matchTempList;
	}
	
	public int getMatchNumber() {
		return matchNumber;
	}
}