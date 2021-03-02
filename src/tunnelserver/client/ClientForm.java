package tunnelserver.client;

import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.text.BadLocationException;

/**
 * Diese Klasse erstellt die Benutzerschnittstelle und den GuidesMonitor zur
 * Verwaltung der Gruppenf�hrer pro Eingang. Sie enth�lt auch die
 * Ereignisbehandlungsmethoden f�r die beiden Kn�pfe. In diesen Methoden werden
 * die Objekte vom Typ ClientThread zur Behandlung der Clientanfragen angelegt
 * und die Threads gestartet.<br>
 * <br>
 * <b>Ereignisbehandlungsmethode Besichtigung anfordern</b><br>
 * Diese Methode kontrolliert zuerst, ob eine Besucherzahl ins Textfeld
 * eingegeben wurde und konvertiert den Inhalt in eine Zahl. Diese Zahl darf
 * nicht gr��er sein als das maximale Fassungsverm�gen des Tunnels. Dann wird
 * der ClientThread gestartet, dem diese Besucheranzahl und die Referenzen auf
 * das ClientForm sowie auf den GuidesMonitor �bergeben werden.<br>
 * <br>
 * <b>Ereignisbehandlungsmethode Besichtigung beenden</b><br>
 * Zuerst wird kontrolliert ob es �berhaupt Aktive Besichtigungen gibt, welche
 * von diesem Eingang aus den Tunnel betreten haben. Sind solche vorhanden, dann
 * wird kontrolliert, ob eine aktive Besichtigung ausgew�hlt wurde. Ist dies der
 * Fall so wird aus dem Text des ausgew�hlten JList-Eintrages die Anzahl der
 * Besucher ermittelt und in eine Zahl konvertiert. Dann wird der ClientThred
 * gestartet, dem diese negative (!) Anzahl und Referenzen auf ClientForm und
 * GuidesMonitor �bergeben werden
 */
public class ClientForm extends JFrame {

	/**
	 * Monitor durch welchen am Eingang ein F�hrer reserviert werden kann
	 */
	private GuidesMonitor guidesMonitor = null;
	/**
	 * Modell zur Verwaltung der Inhalte der JList
	 */
	protected DefaultListModel<String> mActiveVisits = null;

	private JLabel lbltitle, lblstatus, lblvisitors, lblavailablev;
	private JTextArea protocoll;
	private JButton btnfinish, btnrequestvisit;
	private JTextArea activevisitarea;
	private JTextField visitorsinput;

	private ClientForm t;

	private JLabel avavisitors, avaguiint;
	private Border border = BorderFactory.createTitledBorder(BorderFactory.createLoweredBevelBorder());

	/**
	 * Legt das Formular an und macht es sichtbar. Beim Anlegen des Forumulas wird
	 * auch der GuidesMonitor angelegt. Nachdem das Formular angelegt wurde, werden
	 * in Abst�nden von einer Sekunde Serveranfragen geschickt zur Ermittlung der
	 * verf�gbaren Besucher, d. h. der Server antwortet und liefert die Anzahl je
	 * Besucheranzahl zur�ck die noch in den Tunnel eingelassen werden kann
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		ClientForm c = new ClientForm();
		c.setVisible(true);
	}

	public ClientForm() {
		setSize(620, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("Entrance 1");
		setLocationRelativeTo(null);
		setLayout(null);
		setResizable(false);

		t = this;
		Container con = getContentPane();

		guidesMonitor = new GuidesMonitor(this);
		mActiveVisits = new DefaultListModel<String>();
		// GUI
		// titel Entrance 1
		lbltitle = new JLabel("Entrance 1");
		lbltitle.setBounds(20, 10, 200, 35);
		lbltitle.setFont(lbltitle.getFont().deriveFont(Font.BOLD, 25));
		con.add(lbltitle);

		// label status
		lblstatus = new JLabel("Status:");
		lblstatus.setBounds(250, 50, 200, 20);
		con.add(lblstatus);

		// Protokollausgabe
		protocoll = new JTextArea();
		protocoll.setBounds(250, 70, 340, 480);
		protocoll.setBorder(BorderFactory.createCompoundBorder(border, BorderFactory.createEmptyBorder(0, 0, 0, 0)));
		protocoll.setEditable(false);
		con.add(protocoll);

		// JPanel requestVisitors
		JPanel request = new JPanel();
		request.setLayout(null);
		request.setBounds(20, 70, 210, 150);
		request.setBorder(border);
		// request.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel avagui = new JLabel("Available guides:");
		avagui.setBounds(10, 10, 150, 20);
		// avagui.setFont(avagui.getFont().deriveFont(Font.BOLD, 16));
		request.add(avagui);

		avaguiint = new JLabel("4");
		avaguiint.setBounds(170, 10, 100, 20);
		// avaguiint.setFont(avaguiint.getFont().deriveFont(Font.PLAIN, 16));
		request.add(avaguiint);

		JLabel visit = new JLabel("Visitors: ");
		visit.setBounds(10, 40, 200, 20);
		// visit.setFont(visit.getFont().deriveFont(Font.BOLD, 16));
		request.add(visit);

		visitorsinput = new JTextField();
		visitorsinput.setBounds(160, 40, 35, 25);
		visitorsinput.setHorizontalAlignment(SwingConstants.CENTER);
		// visitorsinput.setFont(visitorsinput.getFont().deriveFont(Font.PLAIN, 16));
		request.add(visitorsinput);

		btnrequestvisit = new JButton("Request visit");
		btnrequestvisit.setBounds(30, 160, 190, 35);
		con.add(btnrequestvisit);

		btnrequestvisit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					int besucherangefordert = Integer.parseInt(visitorsinput.getText());
					// startet den clientthread
					new ClientThread(besucherangefordert, t, guidesMonitor).start();
					visitorsinput.setText("");
				} catch (Exception e) {

				}
			}
		});
		con.add(request);

		// Active visits panel
		JPanel active = new JPanel();
		active.setLayout(null);
		active.setBounds(20, 230, 210, 230);
		active.setBorder(border);

		JLabel act = new JLabel("Active visits:");
		act.setBounds(10, 10, 100, 20);
		active.add(act);

		activevisitarea = new JTextArea();
		activevisitarea.setBounds(10, 40, 190, 130);
		activevisitarea.setEditable(false);
		active.add(activevisitarea);

		btnfinish = new JButton("Finish visit");
		btnfinish.setBounds(10, 180, 190, 40);
		btnfinish.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String[] items = mActiveVisits.get(0).split(" ");
				int anzahlBesucherZuruck = Integer.parseInt(items[0]);

				new ClientThread(anzahlBesucherZuruck * -1, t, guidesMonitor).start();
			}
		});
		active.add(btnfinish);

		con.add(active);

		avavisitors = new JLabel("Available Visitors: ");
		avavisitors.setBounds(20, 480, 150, 20);
		con.add(avavisitors);
		
		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		Thread control = new ClientThread(0, t, guidesMonitor);
		
		service.scheduleAtFixedRate(control, 1,1, TimeUnit.SECONDS);
		
	}

	// Fügt dem Status/Protokoll eine Meldung hinzu
	public void addStatus(String message) {
		if (protocoll.getText().length() > 0) {
			protocoll.setText(protocoll.getText() + "\n" + message);
		} else {
			protocoll.setText(message);

		}
	}

	public void addVisit(int besucher) {
		mActiveVisits.add(mActiveVisits.size(), besucher + " visitors");
		if (activevisitarea.getText().length() > 0) {
			activevisitarea.setText(activevisitarea.getText() + "\n" + mActiveVisits.get(mActiveVisits.size() - 1));
		} else {
			activevisitarea.setText(mActiveVisits.get(0));
		}
	}

	public void deleteLastVisit() {
		mActiveVisits.remove(0);
		activevisitarea.setText("");
		for (int i = 0; i < mActiveVisits.size(); i++) {
			if (activevisitarea.getText().length() > 0) {
				activevisitarea.setText(activevisitarea.getText() + "\n" + mActiveVisits.get(mActiveVisits.size() - 1));
			} else {
				activevisitarea.setText(mActiveVisits.get(0));
			}

		}

	}

	// Aktualisiert die aktuell verfügbaren Guides links oben
	public void refreshGuides(int availableGuides) {
		avaguiint.setText(String.valueOf(availableGuides));
	}
	
	public void refreshVisitors(int availableVisitors) {
		avavisitors.setText("Available Visitors: " + availableVisitors);
	}

}