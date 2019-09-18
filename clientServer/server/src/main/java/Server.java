
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Server {
	Map<String, DataOutputStream> clients;
	private Veritabani vt = new Veritabani();;

	Server() {
		clients = Collections.synchronizedMap(new HashMap<String, DataOutputStream>());
		for (Musteri musteri : vt.getMusteriListesi()) {
			System.out.println(musteri);
		}
	}

	public void start() {
		ServerSocket serverSocket = null;
		Socket socket = null;
		try {
			serverSocket = new ServerSocket(12345);
			System.out.println("Server başladı");
			// ----------------------------------
			while (true) {
				socket = serverSocket.accept();
				System.out.println("[" + socket.getInetAddress() + ":" + socket.getPort() + "]" + " bağlandı");

				ServerReceiver serverReceiver = new ServerReceiver(socket);
				ServerReceiver thread = serverReceiver;
				thread.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Conncetion Failed");
		} finally {
			SocketUtil.close(serverSocket);
		}
	}// start()

	public void broadCast(String msg) {
		Iterator<String> it = clients.keySet().iterator();

		while (it.hasNext()) {
			try {
				String name = it.next();
				DataOutputStream out = clients.get(name);
				out.writeUTF(msg);
			} catch (IOException e) {
				System.out.println("Map Failed");
			}
		} // while()
	} // broadCast

	public static void main(String[] args) {
		new Server().start();
	}

	/*-----------------------------------------------------------------------------------*/
	/**
	 * Her bağlanan kullanıcı için bu objeden oluşturulur
	 *
	 */
	class ServerReceiver extends Thread {
		Socket socket;
		DataInputStream dis;
		DataOutputStream dos;

		ServerReceiver(Socket _socket) {
			this.socket = _socket;

			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());
			} catch (IOException e) {
				System.out.println("Stream Failed");
			}
		}// constructor

		@Override
		public void run() {
			String name = "Unknown";
			try {
				name = dis.readUTF();
				broadCast(name + " : Connected");
				clients.put(name, dos);
				System.out.println("Bağlı Kullanıcı sayısı: " + clients.size());

				while (dis != null) {
					String gelenKomut = dis.readUTF();
					System.out.println("gelen komut: " + gelenKomut);
					// broadCast(name + " : " + dis.readUTF());

					if (gelenKomut.startsWith("hesapAc")) {
						String komut = gelenKomut.replaceAll("hesapAc", "");
						String[] parcalanmisKomut = komut.split("\\|");

						int sonuc = vt.kullaniciEkle(parcalanmisKomut[0], parcalanmisKomut[1], parcalanmisKomut[2],
								parcalanmisKomut[3]);
						if (sonuc > 0) {
							System.out.println("hesap açma başarılı");
							dos.writeUTF("Hesap Açma Başarılı");
						}
					} else if (gelenKomut.startsWith("girisYap")) {
						String komut = gelenKomut.replaceAll("girisYap", "");
						String[] parcalanmisKomut = komut.split("\\|");

						String tc = parcalanmisKomut[0];
						String parola = parcalanmisKomut[1];
						/**
						 * müşteriyi bulur ve bilgilerini döner
						 */
						List<Musteri> musteriler = vt.getMusteriListesi();
						for (Musteri musteri : musteriler) {
							if (musteri.getTc().equals(tc) && musteri.getParola().equals(parola)) {
								dos.writeUTF("girisBasarili" + musteri.getHesapNumarasi() + "|" + musteri.getTc() + "|"
										+ musteri.getAdSoyad() + "|" + musteri.getHesapTuru() + "|"
										+ musteri.getPara());
								break;
							}
						}
					}

				}
			} catch (IOException e) {
				System.out.println("Read Failed");
			} finally {
				/*
				 * Client disconnect oldu
				 */
				// broadCast(name + " : Disconnected");
				clients.remove(name);
				System.out.println("[" + socket.getInetAddress() //
						+ ":" + socket.getPort() + "]" + " ayrıldı");

			}
		}
	}
}