package server;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import server.impls.Database;
import server.impls.RoomManagerImpl;
import server.interfaces.RoomManager;

public class Server {
	
    public static void main(String[] args) {

    	Database.createTables();

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
    }
}
