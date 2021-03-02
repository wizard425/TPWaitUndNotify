package diningproblem;

public class Philosopher extends Thread {
	public static final int MAX_THINK_TIME = 2000;
	public static final int MAX_EAT_TIME = 1000;
	private Fork left, right = null;
	private ForkControl control = null;

	public Philosopher(String name, Fork left, Fork right) {
		setName(name);
		this.left = left;
		this.right = right;
	}

	@Override
	public void run() {
		control = new ForkControl(left,right);
		
		while (true) {
			try {
				sleep((int) (Math.random() * MAX_THINK_TIME));
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());;
			}
			control.get(this);
			
			try {
				sleep((int) (Math.random() * MAX_EAT_TIME));
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());;
			}

			control.put(this);
			
		}
	}
}
