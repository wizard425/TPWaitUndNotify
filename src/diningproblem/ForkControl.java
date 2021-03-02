package diningproblem;

public class ForkControl {

	Fork left, right = null;

	public ForkControl(Fork leftFork, Fork rightFOrk) {
		this.left = leftFork;
		this.right = rightFOrk;
	}

	public synchronized void get(Philosopher p) {
		if (right.isAvailable() && left.isAvailable()) {
			right.get(p);
			left.get(p);
		}
	}

	public synchronized void put(Philosopher p) {
		right.put(p);
		left.put(p);

	}

}