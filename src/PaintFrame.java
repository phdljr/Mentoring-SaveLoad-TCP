import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class PaintFrame extends JFrame {

	private PaintPanel paintPanel;

	public PaintFrame() {
		paintPanel = new PaintPanel();
		init();
	}

	private void init() {
		setTitle("그림판");
		setSize(500, 500);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setContentPane(paintPanel);
		setResizable(false);
		setLayout(new BorderLayout());

		JPanel btn_panel = new JPanel();
		btn_panel.setLayout(new GridLayout(1, 3));
		add(btn_panel, BorderLayout.SOUTH);

		JButton save_btn = new JButton("저장");
		save_btn.addActionListener((e) -> {
			try {
				new ObjectOutputStream(new FileOutputStream("mypoint.vector")).writeObject(paintPanel.dot_list);
				JOptionPane.showMessageDialog(null, "정상적으로 파일을 저장하였습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
			} catch(Exception e1) {
				e1.printStackTrace();
			}
		});
		btn_panel.add(save_btn);

		JButton load_btn = new JButton("불러오기");
		load_btn.addActionListener((e) -> {
			try {
				paintPanel.dot_list = (Vector<Point>) (new ObjectInputStream(new FileInputStream("mypoint.vector")).readObject());
				paintPanel.repaint();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				JOptionPane.showMessageDialog(null, "mypoint.vector 파일이 존재하지 않습니다.", "오류", JOptionPane.ERROR_MESSAGE);
				//e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		btn_panel.add(load_btn);

		JButton clear_btn = new JButton("지우기");
		clear_btn.addActionListener((e) -> {
			paintPanel.clear();
		});
		btn_panel.add(clear_btn);

		setVisible(true);
	}

	private class PaintPanel extends JPanel {

		private Vector<Point> dot_list = new Vector<Point>();

		public PaintPanel() {
			setBackground(Color.WHITE);
			addMouseMotionListener(new MouseMotionListener() {
				@Override
				public void mouseMoved(MouseEvent e) {
				}

				@Override
				public void mouseDragged(MouseEvent e) {
					dot_list.add(e.getPoint());
					repaint();
				}
			});
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			myDraw(g);
		}

		public void myDraw(Graphics g) {
			g.setColor(Color.BLACK);
			for (Point p : dot_list) {
				g.drawOval(p.x, p.y, 3, 3);
			}
		}
		
		public void clear() {
			dot_list.clear();
			repaint();
		}
	}

	public static void main(String[] args) {
		new PaintFrame();
	}

}
