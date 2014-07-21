package net.ib.baseballtext;

import java.util.List;

import kr.co.quizon.network.HttpLib;
import kr.co.quizon.network.link.match.GetMatchBroadcastLink;
import net.ib.quizon.api.match.GetMatchBroadcastReq;
import net.ib.quizon.api.match.GetMatchBroadcastRes;
import net.ib.quizon.domain.match.MatchBroadcast;
import net.ib.quizon.domain.match.MatchDisplayBoard;
import net.ib.quizon.domain.match.MatchPlayers;
import net.ib.quizon.domain.match.MatchSummary;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {
	private final String ACCESS_TOKEN = "b867b048-8f20-4a01-bfc4-53784e4b488e";
    private TextView baseballText;
    private TextView teamName1;
    private TextView teamName2;
    private Button button;
    
    private String matchId = "7580b1a4-ac96-4369-a93e-f462be19df88";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        baseballText = (TextView) findViewById(R.id.baseballtext);
        baseballText.setMovementMethod(new ScrollingMovementMethod());
        
        teamName1 = (TextView) findViewById(R.id.teamname1);
        teamName2 = (TextView) findViewById(R.id.teamname2);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
    }

	@Override
	protected void onResume() {
		super.onResume();
		HttpLib.setTest(true);
//		setData();
	}
	
//	private void setData() {
//		new AsyncTask<Void, Void, List<Match>>() {
//			@Override
//			protected List<Match> doInBackground(Void... params) {
//				// network process
//				GetMatchListReq getMatchListReq = new GetMatchListReq();
//				getMatchListReq.setAccessToken(accessToken);
//				GetMatchListLink getMatchListLink = new GetMatchListLink(getMatchListReq);
//				GetMatchListRes matchListRes = getMatchListLink.linkage();
//				if (matchListRes != null) {
//					return matchListRes.getMatchInfoList();
//				}
//				return null;
//			}
//
//			@Override
//			protected void onPostExecute(List<Match> result) {
//				// ui process
//				if (result != null) {
//					for (Match match : result) {
////						matchId = match.getMatchId();
//						if (matchId != null) {							
//							textView.setText("매치 ID : " + matchId);
//							break;
//						}
//					}
//				} else {
//					textView.setText("매치 정보 로딩 실패");
//				}
//			}
//		}.execute();
//	}
	
	private void getBroadcast(String matchId, String inning) {
		new AsyncTask<String, Void, GetMatchBroadcastRes>() {
			@Override
			protected GetMatchBroadcastRes doInBackground(String... params) {
				GetMatchBroadcastReq getMatchBroadcastReq = new GetMatchBroadcastReq();
				getMatchBroadcastReq.setMatchId(params[0]);
				getMatchBroadcastReq.setInning(Integer.valueOf(params[1]));
				GetMatchBroadcastLink getMatchBroadcastLink = new GetMatchBroadcastLink(getMatchBroadcastReq);
				GetMatchBroadcastRes getMatchBroadcastRes = getMatchBroadcastLink.linkage();
				if (getMatchBroadcastRes != null) {
					return getMatchBroadcastRes;
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(GetMatchBroadcastRes result) {
				if (result != null) {
//					StringBuilder sb = new StringBuilder();
//					sb.append("매치 정보");
//					sb.append("\n");
//					sb.append(result);
//					sb.append("\n");
					
					List<MatchBroadcast> broadcast = result.getBroadcast();
					for (MatchBroadcast matchBroadcast : broadcast) {
						if (matchBroadcast == null) {
							continue;
						}
						String broadcast2 = matchBroadcast.getBroadcast();
						Integer inning2 = matchBroadcast.getInning();
					}
					MatchDisplayBoard matchDisplayBoard = result.getMatchDisplayBoard();
					setMatchDisplayBoard(matchDisplayBoard);
					
					MatchPlayers matchPlayers = result.getMatchPlayers();
					setMatchPlayers(matchPlayers);
					
					List<MatchSummary> matchSummaryList = result.getMatchSummaryList();
					for (MatchSummary matchSummary : matchSummaryList) {
						if (matchSummary == null) {
							continue;
						}
						String awayTeamName2 = matchSummary.getAwayTeamName();
						Integer awayTeamPoint2 = matchSummary.getAwayTeamPoint();
						String homeTeamName2 = matchSummary.getHomeTeamName();
						Integer homeTeamPoint2 = matchSummary.getHomeTeamPoint();
						Integer inning2 = matchSummary.getInning();
						Integer isBottom = matchSummary.getIsBottom();
						String matchId2 = matchSummary.getMatchId();
						String matchPresent = matchSummary.getMatchPresent();
						String matchStadium = matchSummary.getMatchStadium();
						Integer matchStatus = matchSummary.getMatchStatus();
					}
					
//					baseballText.setText(sb.toString());
				} else {
					baseballText.setText("문자 중계 로딩 실패");
				}
			}
		}.execute(matchId, inning);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button:
			if (matchId != null) {				
				getBroadcast(matchId, String.valueOf(1));
			}
			break;
		default:
			break;
		}
	}
	
	private void setMatchDisplayBoard(MatchDisplayBoard matchDisplayBoard) {
		if (matchDisplayBoard != null) {
			String awayTeamName = matchDisplayBoard.getAwayTeamName();
//			sb.append("AwayTeamName");
//			sb.append("\t");
//			sb.append(awayTeamName);
//			sb.append("\n");
			List<String> awayTeamPoint = matchDisplayBoard.getAwayTeamPoint();
			String ball = matchDisplayBoard.getBall();
			String homeTeamName = matchDisplayBoard.getHomeTeamName();
//			sb.append("HomeTeamName");
//			sb.append("\t");
//			sb.append(homeTeamName);
//			sb.append("\n");
			List<String> homeTeamPoint = matchDisplayBoard.getHomeTeamPoint();
			String out = matchDisplayBoard.getOut();
			String strike = matchDisplayBoard.getStrike();
		}
	}
	
	private void setMatchPlayers(MatchPlayers matchPlayers) {
		if (matchPlayers != null) {						
			String batter = matchPlayers.getBatter();
			String firstBatter = matchPlayers.getFirstBatter();
			String pitcher = matchPlayers.getPitcher();
			String secondBatter = matchPlayers.getSecondBatter();
			String thirdBatter = matchPlayers.getThirdBatter();
			
//			sb.append("타자");
//			sb.append("\t");
//			sb.append(batter);
//			sb.append("\n");
//			sb.append("투수");
//			sb.append("\t");
//			sb.append(pitcher);
//			sb.append("\n");
		}
	}
}
