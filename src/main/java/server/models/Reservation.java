package server.models;

import java.io.Serializable;

public class Reservation implements Serializable {

	private static final long serialVersionUID = 1L;
	
	String roomId;
	String userId;
	String dateTime;
	
	public Reservation(String roomId, String userId, String localDateTime) {
		
		this.roomId = roomId;
		this.userId = userId;
		this.dateTime = localDateTime;
	};
	
	public String getRoomId() {
		return roomId;
	}
	
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	
	public String getUserId() {
		return userId;
	}
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDateTime() {
		return dateTime;
	}

	public void setDateTime(String dateTime) {
		this.dateTime = dateTime;
	}
	
}
