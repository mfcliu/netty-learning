package org.liujingyu.test.multithread;

public class MyThreadRunnable implements Runnable {

	private int ticketCount = 10;

	@Override
	public void run() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 20; i++) {

			if (this.ticketCount > 0) {
				System.out.println(Thread.currentThread().getName() + " 卖票：ticket" + this.ticketCount--);
			}
		}
	}

}
