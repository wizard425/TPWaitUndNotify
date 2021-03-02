package tunnelserver.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Der Thread liest vom Socket die Anzahl, und dabei werden die drei F�lle �
 * gr��er 0, kleiner 0 oder gleich 0 � unterschieden und entsprechen am
 * VisitorsMonitor die Anfragen gestellt. Das Ergebnis wird an den Client zur�ck
 * geschickt. der ServerThread erh�lt den Socket des Clients und eine Referenz
 * auf VisitorsMonitor
 */
public class ServerThread extends Thread {
	/**
	 * Der Clientsocket von welchem die Besucheranzahl gelesen werden kann
	 */
	protected Socket client = null;
	/**
	 * VisitorsMonitor an dem die Anfrage nach Besuchern bzw. die R�ckgabe der
	 * Besucher nach Beendigung einer Besichtigung gestellt werden kann
	 */
	protected VisitorsMonitor visitorsMonitor = null;

	/**
	 * Konstruktor erh�lt den Clientsocket und den VisitorsMonitor als Referenz. Als
	 * Threadname wird die IP-Adresse des Clients gesetzt. Die IP-Adresse kann �ber
	 * den Clientsocket durch die Methode getInetAdress() erfragt werden
	 * 
	 * @param client
	 * @param visitorsMonitor
	 */
	public ServerThread(Socket client, VisitorsMonitor visitorsMonitor) {

		this.client = client;
		this.visitorsMonitor = visitorsMonitor;

	}

	/**
	 * Diese Methode liest zuerst vom Clientsocket die Anzahl. Je nach dem welche
	 * Werte in anzahl stehen, werden folgende Aufgaben erledigt:<br>
	 * <br>
	 * <b>anzahl == 0</b><br>
	 * Es wird die Anzahl der am VisitorsMonitor momentan verf�gbaren Benutzer
	 * abgefragt und an den Client zur�ck geschickt<br>
	 * <br>
	 * <b>anzahl > 0</b><br>
	 * Es werden am VisitorsMonitor die Benutzer angefordert<br>
	 * <br>
	 * <b>anzahl < 0</b><br>
	 * Es werden dem VisitorsMonitor die Anzahl an Benutzer zur�ck gegeben
	 */
	public void run() {
		try {
			DataInputStream socketin = new DataInputStream(client.getInputStream());
			DataOutputStream socketout = new DataOutputStream(client.getOutputStream());

			int anzahl = socketin.readInt();

			if (anzahl > 0) {
				visitorsMonitor.request(anzahl);
				System.out.println("nach request");
				// schreibt zurück, sobald die besucher eingelassen werden dürfen
				socketout.writeInt(1);
			} else if (anzahl < 0) {
				System.out.println("besucher releasen");
				visitorsMonitor.release(anzahl * -1);
				socketout.writeInt(1);
			} else {
				// schreibt die verfügbaren Besucher zurück zum CLienthread
				socketout.writeInt(visitorsMonitor.getAvailableVisitors());
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
