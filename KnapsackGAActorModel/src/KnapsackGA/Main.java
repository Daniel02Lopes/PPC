package KnapsackGA;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class Main {
	public static void main(String[] args){
			KnapsackGAManager ga = new KnapsackGAManager();
			ga.getAddress().sendMessage(new BootstrapMessage());
		}
	}

