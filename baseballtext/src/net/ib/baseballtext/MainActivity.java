package net.ib.baseballtext;

import java.util.ArrayList;
import java.util.List;

import kr.co.quizon.network.HttpLib;
import kr.co.quizon.network.link.match.GetMatchBroadcastLink;
import kr.co.quizon.network.link.match.GetMatchListLink;
import net.ib.quizon.api.match.GetMatchBroadcastReq;
import net.ib.quizon.api.match.GetMatchBroadcastRes;
import net.ib.quizon.api.match.GetMatchListReq;
import net.ib.quizon.api.match.GetMatchListRes;
import net.ib.quizon.domain.match.Match;
import net.ib.quizon.domain.match.MatchBroadcast;
import net.ib.quizon.domain.match.MatchDisplayBoard;
import net.ib.quizon.domain.match.MatchPlayers;
import net.ib.quizon.domain.match.MatchStatus;
import net.ib.quizon.domain.match.MatchSummary;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	private final String ACCESS_TOKEN = "b867b048-8f20-4a01-bfc4-53784e4b488e";
	private Integer INNING; // ?????????????????????/  null이면????? 
	ArrayList<String> spinnerList;
	ArrayList<String> matchIdList;
	Spinner spinner;
	
//	List<Match> matchList;
	
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
    
    private Button button;
    
    private String matchId;// = "1f3f46ec-e01a-47e2-a54e-b1371d0d4806";

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        baseballText = (TextView) findViewById(R.id.baseballtext);
        baseballText.setMovementMethod(new ScrollingMovementMethod());
        
        homeTeamNameText = (TextView) findViewById(R.id.hometeamname);
        awayTeamNameText = (TextView) findViewById(R.id.awayteamname);
        stadiumText = (TextView) findViewById(R.id.stadium);
        inningText = (TextView) findViewById(R.id.inning);
        homeTeamPointText = (TextView) findViewById(R.id.hometeampoint);
        awayTeamPointText = (TextView) findViewById(R.id.awayteampoint);
        ballText = (TextView) findViewById(R.id.ball);
        strikeText = (TextView) findViewById(R.id.strike);
        outText = (TextView) findViewById(R.id.out);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(this);
        
        matchIdList = new ArrayList<String>();
        
        spinner = (Spinner) findViewById(R.id.selectmatchspinner);
        spinner.setPrompt("원하는 경기를 선택하세요");
        spinnerList = new ArrayList<String>();
    }

	@Override
	protected void onResume() {
		super.onResume();
		HttpLib.setTest(true);
		setData();
	}

	private void setData() {
		new AsyncTask<Void, Void, List<Match>>() {
			@Override
			protected List<Match> doInBackground(Void... params) {
				// network process
				GetMatchListReq getMatchListReq = new GetMatchListReq();
				getMatchListReq.setAccessToken(ACCESS_TOKEN);
				GetMatchListLink getMatchListLink = new GetMatchListLink(getMatchListReq);
				GetMatchListRes matchListRes = getMatchListLink.linkage();
				if (matchListRes != null) {
//					matchList = matchListRes.getMatchInfoList();
					return matchListRes.getMatchInfoList();
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<Match> result) {
				// ui process
				if (result != null) {
					for (int i=0; i<result.size(); i++) {
						matchIdList.add(result.get(i).getMatchId());
						spinnerList.add(result.get(i).getHomeTeamName() + " vs " + result.get(i).getAwayTeamName());
					}
					
					
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.matchspinner, spinnerList);
					spinner.setAdapter(adapter);
					
					spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> parent,
								View view, int position, long id) {
							// TODO Auto-generated method stub
							Log.i("test", parent.getItemIdAtPosition(position) + "");
							Log.i("test1", position + "");
							switch (position) {
							case 0:
								getBroadcast(matchIdList.get(0), String.valueOf(4));
								break;
							case 1:
								getBroadcast(matchIdList.get(1), String.valueOf(4));
								break;
							case 2:
								getBroadcast(matchIdList.get(2), String.valueOf(4));
								break;
							case 3:
								getBroadcast(matchIdList.get(3), String.valueOf(4));
								break;
							default:
								break;
							}
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
							// TODO Auto-generated method stub
							
						}
					});
					
					for (Match match : result) {
						match.getMatchStatus();
						matchId = match.getMatchId();
						if (matchId != null) {

							break;
						}
					}
				} else {
					baseballText.setText("매치 정보 로딩 실패");
				}
			}
		}.execute();
	}
	
	private void getBroadcast(String matchId, final String inning) {
		new AsyncTask<String, Void, GetMatchBroadcastRes>() {
			@Override
			protected GetMatchBroadcastRes doInBackground(String... params) {
				GetMatchBroadcastReq getMatchBroadcastReq = new GetMatchBroadcastReq();
				getMatchBroadcastReq.setMatchId(params[0]);
				if (inning != null) {
					INNING = Integer.parseInt(inning);
					Log.i("inning", INNING + "");
					getMatchBroadcastReq.setInning(Integer.valueOf(inning));
				}
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
					baseballText.setText("");
//					Log.i("test", result.toString());
					StringBuilder sb = new StringBuilder();
//					sb.append("매치 정보");
//					sb.append("\n");
//					sb.append("\n");
					
					List<MatchBroadcast> broadcast = result.getBroadcast();
					for (MatchBroadcast matchBroadcast : broadcast) {
						if (matchBroadcast == null) {
							continue;
						}
						String broadcast2 = matchBroadcast.getBroadcast();
						Integer inning2 = matchBroadcast.getInning();
						if (inning2 != null && inning2 == 1) {							
							sb.append(matchBroadcast.getBroadcast());
						}
//						inning = matchBroadcast.getInning();
					}
//					result.getMatchSummaryList().
//					MatchDetail.this.getMatch().getMatchStadium();
					
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
						
						stadiumText.setText(matchStadium);
						inningText.setText(matchPresent);
					}
//					MatchSummary matchSummary = null;
//					String matchStadium = matchSummary.getMatchStadium();s
//					stadiumText.setText(m)
					
					
					
					baseballText.setText(sb.toString());
				} else {
					baseballText.setText("문자 중계 로딩 실패");
				}
			}
		}.execute(matchId);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button:
			if (matchId != null) {				
				getBroadcast(matchId, null);
			}
			break;
		case R.id.first:
			if (matchId != null) {				
				getBroadcast(matchId, String.valueOf(1));
			}
			break;
		case R.id.second:
			if (matchId != null) {				
				getBroadcast(matchId, String.valueOf(2));
			}
			break;
		case R.id.third:
			if (matchId != null) {				
				getBroadcast(matchId, String.valueOf(3));
			}
			break;
		case R.id.fourth:
			if (matchId != null) {				
				getBroadcast(matchId, String.valueOf(4));
			}
			break;
		case R.id.fifth:
			if (matchId != null) {				
				getBroadcast(matchId, String.valueOf(5));
			}
			break;
		case R.id.sixth:
			if (matchId != null) {				
				getBroadcast(matchId, String.valueOf(6));
			}
			break;
		case R.id.seventh:
			if (matchId != null) {				
				getBroadcast(matchId, String.valueOf(7));
			}
			break;
		case R.id.eighth:
			if (matchId != null) {				
				getBroadcast(matchId, String.valueOf(8));
			}
			break;
		case R.id.ninth:
			if (matchId != null) {
				Log.i("ninth", matchId);
				getBroadcast(matchId, String.valueOf(9));
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
				awayTeamNameText.setText(awayTeamName);
			}
			List<String> awayTeamPoint = matchDisplayBoard.getAwayTeamPoint();
			awayTeamPointText.setText(awayTeamPoint.get(INNING-1));
			
			String homeTeamName = matchDisplayBoard.getHomeTeamName();
			if (homeTeamName != null) {				
				homeTeamNameText.setText(homeTeamName);
			}
			List<String> homeTeamPoint = matchDisplayBoard.getHomeTeamPoint();
			
//			homeTeamPointText.setText(homeTeamPoint.get(homeTeamPoint.size()-1));
			homeTeamPointText.setText(homeTeamPoint.get(INNING-1));
			
			
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
			
			String firstBatter = matchPlayers.getFirstBatter();
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
