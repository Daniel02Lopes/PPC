package KnapsackGA;

public class Main {
	public static void main(String[] args) {
		KnapsackGAManager ga = new KnapsackGAManager();
		ga.getAddress().sendMessage(new BootstrapMessage());
	}
}
