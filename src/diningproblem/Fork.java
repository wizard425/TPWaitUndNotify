package diningproblem;

public class Fork {
	private String name = null;;
	private boolean available = true;

	public Fork(String name) {
		this.name = name;
	}

	public synchronized void get(Philosopher p) {
		while (!available)
			try {
				wait();
			} catch (InterruptedException e) {
				;
			}
		available = false;
		System.out.println(p.getName() + " gets " + name);
	}

	public synchronized void put(Philosopher p) {
		available = true;
		System.out.println(p.getName() + " puts " + name);
		notifyAll();
	}
	
	public boolean isAvailable() {
		return available;
	}
}
