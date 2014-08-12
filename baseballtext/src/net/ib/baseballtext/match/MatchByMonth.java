package net.ib.baseballtext.match;

import java.util.Date;
import java.util.List;

import kr.co.quizon.network.link.match.GetMatchByMonthLink;
import net.ib.quizon.api.match.GetMatchByMonthReq;
import net.ib.quizon.api.match.GetMatchByMonthRes;
import net.ib.quizon.domain.match.Match;
import android.os.AsyncTask;
import android.util.Log;

public class MatchByMonth {
	private int currentMonth;
	private List<Match> matchList;
	
	public MatchByMonth (int currentMonth) {
		this.currentMonth = currentMonth;
		setDate();
	}
	
	private void setDate() {
		new AsyncTask<Void, Void, List<Match>>() {

			@Override
			protected List<Match> doInBackground(Void... params) {
				// network process
				GetMatchByMonthReq getMatchByMonthReq = new GetMatchByMonthReq();
				getMatchByMonthReq.setMonth(currentMonth + 1);
				GetMatchByMonthLink getMatchByMonthLink = new GetMatchByMonthLink(getMatchByMonthReq);
				GetMatchByMonthRes matchByMonthRes = getMatchByMonthLink.linkage();

				if (matchByMonthRes != null) {
					matchList = matchByMonthRes.getMatchList();
					return matchByMonthRes.getMatchList();
				}
				return null;
			}

			@Override
			protected void onPostExecute(List<Match> result) {
				if (result != null) {
					matchList = result;
				} else {
					matchList = null;
				}
			}
		}.execute();
	}
	
	public List<Match> getMatchList() {
		return matchList;
	}
}
