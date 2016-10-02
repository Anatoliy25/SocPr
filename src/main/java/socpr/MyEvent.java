package socpr;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MyEvent implements Serializable {

	public MyEvent(long currentTime, int stationID, int userID, boolean isLeftAutoban) {
		this.setStationID(stationID);
		this.setCurrentTime(currentTime);
		this.setUserID(userID);
		this.setisLeftAutoban(isLeftAutoban);
	}

	private static final long serialVersionUID = 1L;
	private long currentTime;
	private int stationID;
	private int userID;
	private boolean isLeftAutoban;

	public String toString() {
		return getCurrentTime() + " " + new Integer(stationID).toString() + " " + new Integer(userID).toString() + " "
				+ new Boolean(isLeftAutoban).toString();
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String getCurrentTime() {

		Date now = new Date(currentTime);
		DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
		String s = formatter.format(now);
		return s;

	}

	public void setCurrentTime(long currentTime) {
		this.currentTime = currentTime;
	}

	public int getStationID() {
		return stationID;
	}

	public void setStationID(int stationID) {
		this.stationID = stationID;
	}

	public boolean isLeftAutoban() {
		return isLeftAutoban;
	}

	public void setisLeftAutoban(boolean isLeftautoban) {
		this.isLeftAutoban = isLeftautoban;
	}

}