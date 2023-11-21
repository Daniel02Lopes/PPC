package KnapsackGA;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Phaser;

public class Main {
	public static void main(String[] args) {
		final Phaser phaser = new Phaser(1);
		for (int i = 0; i < 30; i++) {
			//phaser.register();
			System.out.println(i);
			KnapsackGAManager ga = new KnapsackGAManager();
			ga.getAddress().sendMessage(new BootstrapMessage());
			//phaser.arriveAndAwaitAdvance();
			try {
				ga.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

