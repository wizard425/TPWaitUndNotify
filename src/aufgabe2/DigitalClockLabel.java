package aufgabe2;

import java.util.Date;

import javax.swing.JLabel;

public class DigitalClockLabel extends JLabel implements Runnable {

	private boolean stop = false;
	private Date datum;
	private JLabel output = null;
	private Thread t;

	public DigitalClockLabel() {
		t = new Thread(this, "Clock");
		t.start();
	}

	@Override
	public void run() {

		while (true) {
			if (!stop) {
				datum = new Date();
				setText(String.valueOf(datum.toString().substring(11, 19)));
			}else {
				try {
					wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public boolean isStop() {
		return stop;
	}



	public void setStop(boolean stop) {
		this.stop = stop;
	}

}
