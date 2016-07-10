package org.liujingyu.test.multithread.producerandconsumer;

import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumerInJava {
	public static void main(String[] args) {
		System.out.println("How to use wait and notify method in Java");
		System.out.println("Solving Producer Consumper Problem");
		Queue<Integer> buffer = new LinkedList<Integer>();
		int maxSize = 10;
		Thread producer = new Producer(buffer, maxSize, "PRODUCER");
		Thread consumer = new Consumer(buffer, maxSize, "CONSUMER");
		consumer.start();
		producer.start();
		//consumer.start();
	}
}
