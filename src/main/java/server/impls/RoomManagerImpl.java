package server.impls;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import server.interfaces.RoomManager;
import server.models.Reservation;

public class RoomManagerImpl extends UnicastRemoteObject implements RoomManager {

	private static final long serialVersionUID = 1L;
	
	public RoomManagerImpl() throws RemoteException {
		
		super();
	}

	@Override
	public String createReservation(String userId, String roomId, int year, int month, int day, int hour)
			throws RemoteException {
		
		return Database.insertReservation(userId, roomId, year, month, day, hour);
	}

	@Override
	public String removeReservation(String userId, String roomId, int year, int month, int day, int hour)
			throws RemoteException {
		
		return Database.removeReservation(userId, roomId, year, month, day, hour);
	}

	@Override
	public String checkReservation(String roomId, int year, int month, int day, int hour) throws RemoteException {
		
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.of(year, month, day, hour, 0).format(formatter);
		
		if (Database.isRoomReserved(roomId, year, month, day, hour)) {
			
			return String.format("Room [%s] is RESERVED at [%s]", roomId, formattedDateTime);
		}
		
		return String.format("Room [%s] is FREE at [%s]", roomId, formattedDateTime);
	}

	@Override
	public List<Reservation> checkRoomReservations(String roomId) throws RemoteException {
		
		return Database.getReservationByRoom(roomId);
	}

	@Override
	public List<Reservation> checkUserReservations(String userId) throws RemoteException {
		
		return Database.getReservationByUser(userId);
	}
}
