package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

public class Server {

	private ServerSocket server_socket;
	private Vector<ClientInfo> client_list = new Vector<ClientInfo>();
	private Random r = new Random();

	private boolean state_btn = true; //누를 수 있으면 true

	public Server(int port) {
		try {
			server_socket = new ServerSocket(port);

			// 클라이언트와 연결을 시도하는 스레드
			new Thread(() -> {
				while (true) {
					try {
						System.out.println("사용자 접속 기다리는중...");
						Socket s = server_socket.accept();
						System.out.println("누군가 접속을 시도함");
						client_list.add(new ClientInfo(s));
					} catch (IOException e) { // 연결 과정에서 오류 발생시 처리
						e.printStackTrace();
					}
				}
			}).start();

			// 서버에 접속한 모든 클라이언트에게 일정 시간마다 버튼을 활성화시키는 메세지를 보내는 스레드
			new Thread(() -> {
				while (true) {
					try {
						Thread.sleep(r.nextInt(5000));
						if (state_btn == false) {
							System.out.println("버튼 활성화");
							broadcast("OnButton/ ");
							state_btn = true;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();

		} catch (IOException e) {// 이미 사용중인 포트라면 오류 처리
			e.printStackTrace();
		}
	}

	public void broadcast(String str) {
		for (ClientInfo client : client_list) {
			client.sendMessage(str);
		}
	}

	private class ClientInfo {
		private Socket socket;
		private InputStream is;
		private DataInputStream dis;
		private OutputStream os;
		private DataOutputStream dos;

		public ClientInfo(Socket socket) {
			this.socket = socket;

			// 서버와 클라이언트간의 스트림 연결 과정
			try {
				is = socket.getInputStream();
				dis = new DataInputStream(is);
				os = socket.getOutputStream();
				dos = new DataOutputStream(os);

				new Thread(() -> {
					while (true) {
						try {
							String msg = dis.readUTF();
							inMessage(msg);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}).start();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void inMessage(String str) {
			StringTokenizer st = new StringTokenizer(str, "/");

			String protocol = st.nextToken();

			if (protocol.equals("ClickButton") || state_btn == true) {
				System.out.println("누군가 버튼 클릭함");
				state_btn = false;
				broadcast("OffButton/ ");
			}
		}

		public void sendMessage(String str) {
			try {
				dos.writeUTF(str);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new Server(9999);
	}

}
