package server.impls;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import server.models.Reservation;

public class Database {
	
	public static void createTables() {
		
		try {
			// Cria a conexão com o banco de dados
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);

			// Roda o comando para criação da tabela de reservas
			statement.executeUpdate("CREATE TABLE IF NOT EXISTS reservations ("
					+ "userId STRING NOT NULL,"
					+ "roomId STRING NOT NULL,"
					+ "dt DATETIME NOT NULL,"
					+ "PRIMARY KEY(userId, roomId, dt))");
			
			//Fecha conexão com o banco
			connection.close();
			
		} catch (SQLException e) {
			
			System.err.println(e.getMessage());
		}
	}
	
	public static String insertReservation(String userId, String roomId, int year, int month, int day, int hour) {
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formattedDateTime = LocalDateTime.of(year, month, day, hour, 0).format(formatter);
		
		userId = userId.toLowerCase();
		roomId = roomId.toLowerCase();
		
		if (isRoomReserved(roomId, year, month, day, hour)) {
				
			return String.format("ERROR: Room [%s] ALREADY RESERVED at [%s]", roomId, formattedDateTime);
		}
		
		try {
			// Cria a conexão com o banco de dados
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);
			
			statement.executeUpdate(String.format("INSERT INTO reservations VALUES('%s', '%s', '%s')", userId, roomId,
					formattedDateTime));
			
			//Fecha conexão com o banco
			connection.close();
			
			return String.format("SUCCESS: Room [%s] was successfully RESERVED at [%s] for user [%s]", roomId, formattedDateTime, userId);
			
//			ResultSet rs = statement.executeQuery("SELECT * FROM reservations");
		} catch (SQLException e) {
			
			System.err.println(e.getMessage());
		}
		
		return String.format("ERROR: Unexpected error while inserting reservation", roomId, formattedDateTime);
	}

	public static String removeReservation(String userId, String roomId, int year, int month, int day, int hour) {
		
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.of(year, month, day, hour, 0).format(formatter);
        
		userId = userId.toLowerCase();
		roomId = roomId.toLowerCase();
        
        if (!isRoomReservedByUser(userId, roomId, year, month, day, hour)) {
        	
			return String.format("ERROR: Room [%s] is not reserved by user [%s] at [%s]", roomId, userId, formattedDateTime);
        }
        
		try {
			// Cria a conexão com o banco de dados
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);
			
			statement.executeUpdate(
					String.format("DELETE FROM reservations WHERE userId = '%s' AND roomId = '%s' AND dt = '%s'",
							userId, roomId, formattedDateTime));
				
			//Fecha conexão com o banco
			connection.close();
			
			return String.format("SUCCESS: Room [%s] reservation at [%s] for user [%s] was successfully DELETED", roomId, formattedDateTime, userId);
			
		} catch (SQLException e) {

			System.err.println(e.getMessage());
		}
		
		return String.format("ERROR: Unexpected error while deleting reservation", roomId, formattedDateTime);
	}
	
	public static List<Reservation> getReservationByRoom(String roomId){
		
		List<Reservation> reservations = new ArrayList<>();
		Reservation reservation;
		
		roomId = roomId.toLowerCase();
		
		try {
			// Cria a conexão com o banco de dados
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);

			ResultSet rs = statement
					.executeQuery(String.format("SELECT * FROM reservations WHERE roomId = '%s'", roomId));

			while(rs.next()) {

				reservation = new Reservation(rs.getString("roomId"), rs.getString("userId"), String.valueOf(rs.getTimestamp("dt")));
				reservations.add(reservation);
			}
			
			//Fecha conexão com o banco
			connection.close();
			
		} catch (SQLException e) {

			System.err.println(e.getMessage());
		}
		
		return reservations;
	}
	
	public static List<Reservation> getReservationByUser(String userId){
		
		List<Reservation> reservations = new ArrayList<>();
		Reservation reservation;
		
		userId = userId.toLowerCase();
		
		try {
			// Cria a conexão com o banco de dados
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);

			ResultSet rs = statement
					.executeQuery(String.format("SELECT * FROM reservations WHERE userId = '%s'", userId));

			while(rs.next()) {
				
				reservation = new Reservation(rs.getString("roomId"), rs.getString("userId"), rs.getTimestamp("dt").toString());
				reservations.add(reservation);
			}
			
			//Fecha conexão com o banco
			connection.close();
			
		} catch (SQLException e) {

			System.err.println(e.getMessage());
		}
		
		return reservations;
	}
	
	public static boolean isRoomReserved(String roomId, int year, int month, int day, int hour) {
		
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.of(year, month, day, hour, 0).format(formatter);
		boolean result = false;
        
		roomId = roomId.toLowerCase();
		
		try {
			// Cria a conexão com o banco de dados
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);

			ResultSet rs = statement.executeQuery(String
					.format("SELECT * FROM reservations WHERE roomId = '%s' AND dt = '%s'", roomId, formattedDateTime));

			//Fecha conexão com o banco
			result = rs.isBeforeFirst();
			connection.close();
			
		} catch (SQLException e) {

			System.err.println(e.getMessage());
		}
		
		return result;
	}
	
	public static boolean isRoomReservedByUser(String userId, String roomId, int year, int month, int day, int hour) {
		
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.of(year, month, day, hour, 0).format(formatter);
		boolean result = false;
        
		userId = userId.toLowerCase();
		roomId = roomId.toLowerCase();
		
		try {
			// Cria a conexão com o banco de dados
			Connection connection = DriverManager.getConnection("jdbc:sqlite:database.db");
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(10);
			
			ResultSet rs = statement.executeQuery(
					String.format("SELECT * FROM reservations WHERE userId = '%s' AND roomId = '%s' AND dt = '%s'",
							userId, roomId, formattedDateTime));
			
			result =  rs.isBeforeFirst();
			
			//Fecha conexão com o banco
			connection.close();
			
		} catch (SQLException e) {

			System.err.println(e.getMessage());
		}
		
		return result;
	}
}
