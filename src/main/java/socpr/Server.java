package socpr;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import com.mongodb.MongoClient;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;


public class Server {
	
	public static final int PORT_WORK = 3000;
	private static ServerSocket server;
	
	// ������ ��� �������� ����(���������) �����(������� ����� ��������). ������� ������� - ������� �����
	private static double m [][] = {
			
			{0,1.1,0,0,0,0,0,0,0,0},
			{0,0,1.2,0,0,0,0,0,0,0},
			{0,0,0,1.3,0,0,0,0,0,0},
			{0,0,0,0,1.4,0,0,0,0,0},
			{0,0,0,0,0,1.5,0,0,0,0},
			{0,0,0,0,0,0,1.6,0,0,0},
			{0,0,0,0,0,0,0,1.7,0,0},
			{0,0,0,0,0,0,0,0,1.8,0},
			{0,0,0,0,0,0,0,0,0,1.9},
			{2.0,0,0,0,0,0,0,0,0,0}
			
	};
	
	// ��������� ������ ��� �������� ������ �� ��
	static List<String> templist = new ArrayList<>();

	public static void main(String[] args) throws IOException, ClassNotFoundException {

		server = new ServerSocket(PORT_WORK);

		// ��������� ������ ������ ��� ������ ����������
		while (true) {
			Socket accept1 = server.accept();

			new Thread(() -> {

				try (Socket accept = accept1) {

					serve(accept);

				} catch (Exception ex) {

				}
			}).start();
		}
	}

	private static void serve(Socket accept) throws IOException, ClassNotFoundException {
		InputStream inputStream = accept.getInputStream();

		ObjectInputStream ois = new ObjectInputStream(inputStream);
		while (true) {

			MyEvent readObject = (MyEvent) ois.readObject();

			if (readObject.isLeftAutoban() == false) {

				System.out.print("���� - " + readObject.getCurrentTime());
				System.out.print("    ����� ������ �������� - " + readObject.getStationID());
				System.out.print("    ID ������������ - " + readObject.getUserID());
				System.out.print("    ������������ ������� �������? - " + readObject.isLeftAutoban());
				System.out.println("  ������ � ��");

				try {

					// To connect to mongodb server
					MongoClient mongoClient = new MongoClient("localhost", 27017);

					// Now connect to your databases
					DB db = mongoClient.getDB("admin");
					System.out.println("Connect to database successfully");

					DBCollection coll = db.createCollection("mycol", null);
					BasicDBObject doc = new BasicDBObject();
					doc.put(new Integer(readObject.getUserID()).toString(), readObject.toString());
					coll.insert(doc);
					List<String> list = new ArrayList(doc.values());
					templist.add(list.get(0));

					mongoClient.close();
					System.out.println("Collection created successfully");
				} catch (Exception e) {
					System.err.println(e.getClass().getName() + ": " + e.getMessage());
				}

			} else if (readObject.isLeftAutoban() == true) {

				System.out.print("���� - " + readObject.getCurrentTime());
				System.out.print("    ����� ������ �������� - " + readObject.getStationID());
				System.out.print("    ID ������������ - " + readObject.getUserID());
				System.out.print("    ������������ ������� �������? - " + readObject.isLeftAutoban());
				System.out.println("   ������ � �� ");

				try {

					// To connect to mongodb server
					MongoClient mongoClient = new MongoClient("localhost", 27017);

					// Now connect to your databases
					DB db = mongoClient.getDB("admin");
					System.out.println("Connect to database successfully");

					DBCollection coll = db.createCollection("mycol", null);
					BasicDBObject doc = new BasicDBObject();
					doc.put(new Integer(readObject.getUserID()).toString(), readObject.toString());

					coll.insert(doc);
					System.out.println("Collection created successfully");

					DBCursor cur = coll.find();
					List<String> list = new ArrayList(doc.values());

					//������ ��������� ������ �� �� � ������
					templist.add(list.get(0));

					coll.drop();
//					System.out.println("Collection droped successfully");
					mongoClient.close();

				} catch (Exception e) {
					System.err.println(e.getClass().getName() + ": " + e.getMessage());
				}

				System.out.println("��������� ������� " + costCalculate() + " �.�");

				System.out.println("�������� ���������� ������ � �������: \n");
				sendEmail();

			}

		}
	}

	//����� �������� ��������� �������
	private static double costCalculate() {
		int i = 0;
		double cost = 0;
		int currentStationID = 0;
		int previousStationID = 0;

		ListIterator<String> litr = null;
		litr = templist.listIterator();
		while (litr.hasNext()) {

			String[] s = templist.get(i).toString().split(" ");
			currentStationID = Integer.parseInt(s[2]);

			cost = cost + m[previousStationID][currentStationID];
			previousStationID = currentStationID;
			i++;

			litr.next();

		}

		return cost;
	}

	//����� ������� ����� ������ � ������� 
	private static String sendEmail() {

		int i = 0;
		int currentStationID = 0;
		String date = null;
		int userID = 0;

		ListIterator<String> litr = null;
		litr = templist.listIterator();

		String[] s = templist.get(i).toString().split(" ");
		userID = Integer.parseInt(s[3]);
		System.out.println("��������� �������� � ID - " + userID + "!");
		System.out.println(
				"�� ��������� �� �������� �������� � ������� ���������� ������� ����� ��������� ������ ��������:");

		while (litr.hasNext()) {

			s = templist.get(i).toString().split(" ");
			date = s[1] + "  " + s[0];
			currentStationID = Integer.parseInt(s[2]);
			System.out.println(date + "   ID ������:" + currentStationID);

			i++;

			litr.next();

		}

		System.out.println("����� ������� ����������: " + costCalculate() + " �.�");
		System.out.println("������� ��� ��������������� ������ ��������!");
		return "test";
	}

}
