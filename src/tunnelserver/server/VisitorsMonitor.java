package tunnelserver.server;

/**
 * Diese Klasse verwaltet die verf�gbaren Besucher, welche eingelassen werden
 * k�nnen
 */
public class VisitorsMonitor {
	/**
	 * Maximalanzahl der im Tunnel vorhanden Besucher
	 */
	protected final int MAX_VISITORS = 50;
	/**
	 * Anzahl der Besucher die in den Tunnel noch eingelassen werden k�nnen
	 */
	protected int availableVisitors = MAX_VISITORS;

	/**
	 * Fordert count Besucher an und gibt Statusmeldungen an der Serverkonsole aus
	 * 
	 * @param count
	 */
	public synchronized void request(int count) {
		boolean bekommen = false;
		while (!bekommen) {
			System.out.println("-> " + count + " Visitors requested.");
			// wenn noch genügend PLatze frei sind
			if (availableVisitors - count >= 0) {
				System.out.println(
						"-> " + count + " Visitors received. " + (availableVisitors - count) + " Visitors available");
				availableVisitors -= count;
				bekommen = true;
				//notify();
			} else {
				try {
					System.out.println("Serverthread wartet auf " + count + " Besucher");
					this.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Gibt count Besucher an den VisitorsMonitor zur�ck und gibt Statusmeldungen an
	 * der Serverkonsole aus
	 * 
	 * @param count
	 */
	public synchronized void release(int count) {

		if (availableVisitors + count <= 50) {

			System.out.println("-> Visit with" + count + " Visitors finished.");
			System.out.println(
					"-> " + count + " Visitors came back. " + (availableVisitors + count) + " Visitors available");
			availableVisitors += count;
			notify();
		}

	}

	/**
	 * Liefert die Anzahl der momentan noch verf�gbaren Besucher zur�ck, die in den
	 * Tunnel eingelassen werden k�nnen
	 * 
	 * @return Anzahl der noch in den Tunnel einlassbaren Besucher
	 */
	public synchronized int getAvailableVisitors() {
		return availableVisitors;
	}

}
