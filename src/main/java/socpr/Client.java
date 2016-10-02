package socpr;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static final int PORT_WORK = 3000;

	public static void main(String[] args) throws IOException, InterruptedException {

		long currentTime = 0;
		int stationID = 0;
		int userID = 0;
		boolean isLeftAutoban = false;

		// —имул€ци€ работы пунктов пропуска
		Scanner in = new Scanner(System.in);
		System.out.print("¬ведите ID тестового пользовател€: ");
		userID = in.nextInt();
		System.out.print("¬ведите начальный пункт движени€ (0-6): ");
		int first = in.nextInt();
		System.out.print("¬ведите количество проехавших пунктов(0-3) : ");
		int count = in.nextInt();
		in.close();

		int i = 0;
		while (i < count) {
			currentTime = System.currentTimeMillis();
			stationID = first;
			doEvent(currentTime, stationID, userID, isLeftAutoban);
			first++;
			count--;
			stationID++;
			Thread.sleep(1000);
		}

		currentTime = System.currentTimeMillis();

		doEvent(currentTime, stationID, userID, true);
	}

	private static void doEvent(long currentTime, int stationID, int userID, boolean isLeftAutoban) throws IOException {
		Socket socket = new Socket("localhost", PORT_WORK);
		ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

		oos.writeObject(new MyEvent(currentTime, stationID, userID, isLeftAutoban));

		oos.close();
		socket.close();
	}

}
