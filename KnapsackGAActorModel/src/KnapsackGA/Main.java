package KnapsackGA;

import library.SystemKillMessage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Phaser;

public class Main {
	public static void main(String[] args) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter("data/KnapsackGAActorModelExecutionTimes.csv"));
		StringBuilder sb = new StringBuilder("ExecutionNumber,KnapsackGAActorModelTime(ns)\n");
		writer.append(sb.toString());
		for (int i = 0; i < 30; i++) {
			sb = new StringBuilder();
			System.out.println("----------"+i+"----------");
			long startTime= System.nanoTime();
			KnapsackGAManager ga = new KnapsackGAManager();
			ga.getAddress().sendMessage(new BootstrapMessage());
			try {
				ga.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			long endTime= System.nanoTime();
			sb.append(i).append(",").append(endTime-startTime).append("\n");
			writer.write(sb.toString());

		}
		writer.close();
	}
}

