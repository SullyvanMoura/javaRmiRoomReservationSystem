package server.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import server.models.Reservation;

public interface RoomManager extends Remote {
	
	/** Create a reservation
	 * @return a string message informing if reservation was successfully created or if reservation was not created
	 */
	String createReservation(String userId, String roomId, int year, int month, int day, int hour) throws RemoteException;
	
	/** Remove a reservation
	 * @return a string message informing if reservation was successfully removed or if reservation was not removed
	 */
	String removeReservation(String userId, String roomId, int year, int month, int day, int hour) throws RemoteException;
	
	/** Verify is a reservation exists
	 * @return a string message informing if reservation exists or if reservation not exists
	 */
	String checkReservation(String roomId, int year, int month, int day, int hour) throws RemoteException;
	
	/** List a room's reservations
	 * @return room's reservations
	 */
	List<Reservation> checkRoomReservations(String roomId) throws RemoteException;
	
	/** List a user's reservations
	 * @return users's reservations
	 */
	List<Reservation> checkUserReservations(String userId) throws RemoteException;
}
