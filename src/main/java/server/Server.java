package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.impls.Database;
import server.impls.RoomManagerImpl;
import server.interfaces.RoomManager;

public class Server {
	
    public static void main(String[] args) {

    	Database.createTables();
    	//System.out.println(Database.insertReservation("20200007457", "lab306", 2024, 07, 10, 10));
//    	System.out.println(Database.removeReservation("20200007457", "lab306", 2024, 07, 10, 10));
//    	boolean searchReservation = Database.isRoomReserved("lab306", 2024, 07, 10, 10);
//    	
//    	System.out.println(searchReservation);
    	
//    	for (Reservation r : Database.getReservationByRoom("lab306")) {
//    		System.out.println(String.format("RoomId: %s| UserId: %s| Date: %s", r.getRoomId(), r.getUserId(), r.getDateTime()));
//    	}

    	//EXPÕE ONLINE
        try {
            // Cria uma instância da implementação do Gerenciador de salas
        	RoomManager roomManager = new RoomManagerImpl();

            // Cria e inicia um registro RMI na porta 1099
            Registry registry = LocateRegistry.createRegistry(1099);

            // Liga a instância do gerenciador de salas ao registro criado
            registry.rebind("RoomManager", roomManager);

            System.out.println("Server is running...");
            
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
        //System.exit(0);
    }
}
