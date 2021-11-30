package client;

import java.awt.Color;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Client extends JFrame{
	
	private JButton btn;
	
	private Socket socket;
	private InputStream is;
	private DataInputStream dis;
	private OutputStream os;
	private DataOutputStream dos;
	
	public Client() {
		connect("127.0.0.1", 9999);
		
		if(socket != null)
			initGUI();
	}
	
	private void connect(String ip, int port) {
		try {
			socket = new Socket(ip, port);
			is = socket.getInputStream();
			dis = new DataInputStream(is);
			os = socket.getOutputStream();
			dos = new DataOutputStream(os);
			
			new Thread(()->{
				while(true) {
					try {
						String msg = dis.readUTF();
						System.out.println(msg);
						inMessage(msg);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void inMessage(String str) {
		System.out.println("클라이언트: 메세지 받음");
		StringTokenizer st = new StringTokenizer(str, "/");
		String protocol = st.nextToken();
		
		if(protocol.equals("OffButton")) {
			System.out.println("클라이언트 : 버튼 끄기");
			btn.setEnabled(false);
		}
		else if(protocol.equals("OnButton")) {
			System.out.println("클라이언트 : 버튼 켜기");
			btn.setEnabled(true);
		}
	}
	
	private void sendMessage(String str) {
		try {
			dos.writeUTF(str);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void initGUI() {
		setTitle("버튼 누르기 게임");
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setLayout(null);
		getContentPane().setBackground(Color.pink);
		
		btn = new JButton("클릭!");
		btn.setBounds(200, 200, 100, 100);
		btn.addActionListener((e)->{
			sendMessage("ClickButton/ ");
		});
		add(btn);
		
		setVisible(true);
	}

	public static void main(String[] args) {
		new Client();
	}

}
