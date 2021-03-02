package tunnelserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * In dieser Konsolenanwendung wird zuerst ein VisitorsMonitor angelegt, und
 * dann wartet das Programm in einer Endlosschleife auf Clientanfragen. Erreicht
 * ihm eine solche, so wird diese in einem Thread vom Typ ServerThread
 * abgearbeitet. Dadurch dass jede Anfrage in einem eigenen Thread abgearbeitet
 * wird, k�nnen mehrere Anfragen gleichzeitig bearbeitet werden
 */
public class ServerMain {
	/**
	 * Port an welchem der Server arbeitet
	 */
	protected static final int PORT = 65535;

	private static VisitorsMonitor visitorMonitor = null;
	private static ServerSocket server = null;
	/**
	 * Besuchermonitor wird angelegt, und in einer Endlosschleife wird auf
	 * Clientanfragen gewartet, welche alle �ber einzelne ServerThreads abgearbeitet
	 * werden. Dadurch dass jede Anfrage in einem eigenen Thread abgearbeitet wird,
	 * k�nnen mehrere Anfragen gleichzeitig bearbeitet werden
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		visitorMonitor = new VisitorsMonitor();
		try {
			server = new ServerSocket(PORT);
			System.out.println("Server\n---------------");

			while (true) {
				Socket cl = server.accept();
				//Serverthread wird gestartet
				new ServerThread(cl,visitorMonitor).start();
			}

		} catch (IOException e) {
			System.out.println("Servermeldung: Fehler aufgetreten");
			e.printStackTrace();
		}catch(Exception e) {
			try {
				server.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}catch(NullPointerException e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * Methode zur Exceptionbehandlung
	 * 
	 * @param e
	 */
	public static void behandleException(Exception e) {
	}
}
