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

	private boolean state_btn = true; //���� �� ������ true

	public Server(int port) {
		try {
			server_socket = new ServerSocket(port);

			// Ŭ���̾�Ʈ�� ������ �õ��ϴ� ������
			new Thread(() -> {
				while (true) {
					try {
						System.out.println("����� ���� ��ٸ�����...");
						Socket s = server_socket.accept();
						System.out.println("������ ������ �õ���");
						client_list.add(new ClientInfo(s));
					} catch (IOException e) { // ���� �������� ���� �߻��� ó��
						e.printStackTrace();
					}
				}
			}).start();

			// ������ ������ ��� Ŭ���̾�Ʈ���� ���� �ð����� ��ư�� Ȱ��ȭ��Ű�� �޼����� ������ ������
			new Thread(() -> {
				while (true) {
					try {
						Thread.sleep(r.nextInt(5000));
						if (state_btn == false) {
							System.out.println("��ư Ȱ��ȭ");
							broadcast("OnButton/ ");
							state_btn = true;
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}).start();

		} catch (IOException e) {// �̹� ������� ��Ʈ��� ���� ó��
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

			// ������ Ŭ���̾�Ʈ���� ��Ʈ�� ���� ����
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
				System.out.println("������ ��ư Ŭ����");
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
