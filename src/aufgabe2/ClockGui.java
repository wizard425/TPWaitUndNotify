package aufgabe2;

import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ClockGui extends JFrame {

	JButton btnstop;
	JButton btncontinue;
	DigitalClockLabel clock;

	public static void main(String[] args) {

		ClockGui gui = new ClockGui();
		gui.setVisible(true);
	}

	public ClockGui() {
		setSize(400, 200);
		setLocationRelativeTo(null);
		setTitle("Digital Clock");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(null);

		Container con = getContentPane();

		clock = new DigitalClockLabel();
		clock.setBounds(0, 0, 400, 140);
		clock.setFont(clock.getFont().deriveFont(Font.BOLD, 45));
		clock.setHorizontalAlignment(SwingConstants.CENTER);
		con.add(clock);

		btnstop = new JButton("Stop");
		btnstop.setBounds(80, 120, 115, 25);
		btnstop.setVisible(true);
		con.add(btnstop);
		// Clock stoppen
		btnstop.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				clock.setStop(true);

			}
		});

		btncontinue = new JButton("Continue");
		btncontinue.setBounds(205, 120, 115, 25);
		btncontinue.setVisible(true);
		con.add(btncontinue);
		// CLock pausieren
		btncontinue.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				clock.setStop(false);
				notify();
			}
		});

	}

}
