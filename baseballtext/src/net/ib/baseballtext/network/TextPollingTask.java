package net.ib.baseballtext.network;

import java.util.concurrent.TimeUnit;

import kr.co.quizon.network.link.match.GetMatchBroadcastLink;
import net.ib.quizon.api.match.GetMatchBroadcastReq;
import net.ib.quizon.api.match.GetMatchBroadcastRes;
import android.os.AsyncTask;
import android.util.Log;

public class TextPollingTask extends AsyncTask<Void, Void, GetMatchBroadcastRes> implements Runnable {
	private int WAITING_TIME = 30;
	
	private boolean isRunning;
	protected String matchId;
	protected int inning = 0;
	
	@Override
	public void run() {
		while (isRunning) {
			execute();
			try {
				TimeUnit.SECONDS.sleep(WAITING_TIME);
			} catch (InterruptedException e) {}
		}
	}
	
	@Override
	protected GetMatchBroadcastRes doInBackground(Void... params) {
		GetMatchBroadcastRes getMatchBroadcastRes = updateData();
		if (getMatchBroadcastRes != null) {
			return getMatchBroadcastRes;
		}
		return null;
	}
	
	private GetMatchBroadcastRes updateData() {
		GetMatchBroadcastReq getMatchBroadcastReq = new GetMatchBroadcastReq();
		getMatchBroadcastReq.setMatchId(matchId);
		if (inning != 0) {
			getMatchBroadcastReq.setInning(Integer.valueOf(inning));
		}
		GetMatchBroadcastLink getMatchBroadcastLink = new GetMatchBroadcastLink(getMatchBroadcastReq);
		Log.i("task", "updateData");
		return getMatchBroadcastLink.linkage();
	}
	
	public boolean isRunning() {
		return isRunning;
	}

	public void setRunning(boolean isRunning) {
		this.isRunning = isRunning;
	}

	public String getMatchId() {
		return matchId;
	}

	public void setMatchId(String matchId) {
		this.matchId = matchId;
	}

	public int getInning() {
		return inning;
	}

	public void setInning(int inning) {
		this.inning = inning;
	}
}