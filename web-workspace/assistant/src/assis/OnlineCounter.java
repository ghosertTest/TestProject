package assis;

public class OnlineCounter {

	private static long counter = 0;

	public static void raise() {

		counter++;
	}

	public static void reduce() {

		counter--;
	}

	public static long getCounter() {

		return counter;
	}
}