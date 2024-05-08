package client;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import server.interfaces.RoomManager;
import server.models.Reservation;

public class Client {
	
    public static void main(String[] args) throws RemoteException {
    	
    	RoomManager roomManager = null;
        Scanner scanner = new Scanner(System.in);
        
        try {
            // Obtém uma referência para o registro RMI
            Registry registry = LocateRegistry.getRegistry("localhost");

            // Procura o gerenciador de salas no registro pelo nome
            roomManager = (RoomManager) registry.lookup("RoomManager");
            
        } catch (Exception e) {
        	
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
            System.exit(0);
        }
        
        System.out.println("Bem vindo ao sistema de reserva de salas!");
        System.out.println("As salas são reservadas em períodos de 1 hora!");
        
        String options = "-----------------------------------------------------\n"
				+ "1. Cadastrar nova reserva\n"
				+ "2. Desfazer uma reserva\n"
    			+ "3. Consultar se a sala está livre no horário desejado\n"
    			+ "4. Listar reservas de uma sala\n"
    			+ "5. Listar reservas de um usuário\n"
    			+ "6. Terminar o cliente\n"
    			+ "0. Mostrar opções novamente\n"
    			+ "-----------------------------------------------------\n";
        
		System.out.print(options);
        
        while(true) {
        	
        	System.out.print("Selecione uma das opções disponíveis (0 para mostrar opções novamente): ");
        	
        	int key;
        	
        	try {
        		
        		key = scanner.nextInt();
        	} catch (InputMismatchException e) {
        		
				System.out.print("Por favor, digite o número da opção desejada: ");
				scanner.next();
				continue;
			}
	        
	        switch (key) {
		        
	        	//Cadastrar nova reserva
				case 1: {
					
					System.out.println("Criando reserva...");
					
					System.out.print("Digite o ID do usuário: ");
					String userId = scanner.next();
					System.out.print("Digite o ID da sala: ");
					String roomId = scanner.next();
	
					String dateString = scanAndValidateDateString(scanner);
	
					try (Scanner dateScanner = new Scanner(dateString).useDelimiter("[^0-9]+")) {
	
						int hour = dateScanner.nextInt();
						int day = dateScanner.nextInt();
						int month = dateScanner.nextInt();
						int year = dateScanner.nextInt();
	
						System.out.println("Resposta do servidor: "
								+ roomManager.createReservation(userId, roomId, year, month, day, hour));
					}
					continue;
				}
				//Desfazer uma reserva
				case 2: {
					
					System.out.println("Desfazendo reserva...");
					
					System.out.print("Digite o ID do usuário: ");
					String userId = scanner.next();
					System.out.print("Digite o ID da sala: ");
					String roomId = scanner.next();
	
					String dateString = scanAndValidateDateString(scanner);

					try (Scanner dateScanner = new Scanner(dateString).useDelimiter("[^0-9]+")) {
	
						int hour = dateScanner.nextInt();
						int day = dateScanner.nextInt();
						int month = dateScanner.nextInt();
						int year = dateScanner.nextInt();
	
						System.out.println("Resposta do servidor: "
								+ roomManager.removeReservation(userId, roomId, year, month, day, hour));
					}
					continue;
				}
				//Consultar se a sala está livre no horário desejado
				case 3: {
					System.out.println("Consultando uma sala...");
					
					System.out.print("Digite o ID da sala: ");
					String roomId = scanner.next();
					
					String dateString = scanAndValidateDateString(scanner);
					
					try (Scanner dateScanner = new Scanner(dateString).useDelimiter("[^0-9]+")) {
						
						int hour = dateScanner.nextInt();
						int day = dateScanner.nextInt();
						int month = dateScanner.nextInt();
						int year = dateScanner.nextInt();
	
						System.out.println("Resposta do servidor: "
								+ roomManager.checkReservation(roomId, year, month, day, hour));
					}
					continue;
				}
				
				//Listar reservas de uma sala
				case 4: {
					
					System.out.println("Listando reservas de uma sala...");
					
					System.out.print("Digite o ID da sala: ");
					String roomId = scanner.next();
					
					System.out.println("Reservas da sala " + roomId + ": ");
					
			    	List<Reservation> reservations = roomManager.checkRoomReservations(roomId);
			    	
			    	if (reservations.isEmpty()) {
			    		
			    		System.out.println("Nâo há reservas para a sala!");
			    		continue;
			    	}
			    	
			        String format = "|%1$-20s|%2$-20s|%3$-25s|\n";
			        System.out.format(format, "ID da sala", "ID do Usuário", "Data e hora da reserva");
			    	
					for (Reservation r : reservations) {
		    		
				        System.out.format(format,  r.getRoomId(), r.getUserId(), r.getDateTime());
			    	}
					
					continue;
				}
				
				//Listar reservas de um usuário
				case 5: {
					
					System.out.println("Listando reservas de um usuário...");
					
					System.out.print("Digite o ID do usuário: ");
					String userId = scanner.next();
					
					System.out.println("Reservas do usuário " + userId + ": ");
					
			    	List<Reservation> reservations = roomManager.checkUserReservations(userId);
			    	
			    	if (reservations.isEmpty()) {
			    		
			    		System.out.println("Nâo há reservas desse usuário!");
			    		continue;
			    	}
			    	
			        String format = "|%1$-20s|%2$-20s|%3$-25s|\n";
			        System.out.format(format, "ID da sala", "ID do Usuário", "Data e hora da reserva");
			    	
					for (Reservation r : reservations) {
		    		
				        System.out.format(format,  r.getRoomId(), r.getUserId(), r.getDateTime());
			    	}
					
					continue;
		
				}
				case 6: {
					System.out.println("Finalizando cliente...");
					break;
				}
				case 0: {
					System.out.print(options);
					continue;
				}
				default:
					System.out.println("Digite um valor válido");
					continue;
				}
	        break;
        }
        
        scanner.close();
    }

	private static String scanAndValidateDateString(Scanner scanner) {
		
        Pattern pattern = Pattern.compile("(\\d{2}h \\d{2}/\\d{2}/\\d{4})", Pattern.CASE_INSENSITIVE);
		boolean matchFound = false;
		String dateString = "";

		do {
			scanner.useDelimiter(Pattern.compile("[\\r\\n;]+"));
			System.out.println("Insira a data da reserva  (Siga o formato: 16h 30/06/2024): ");
			dateString = scanner.next();
			Matcher matcher = pattern.matcher(dateString);
			matchFound = matcher.find();
		} while (!matchFound);
		
		return dateString;
	}
}
