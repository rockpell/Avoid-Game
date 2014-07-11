package aaa;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

class GameHelp {
	
	int x = 608, y = 510; // ������ ũ��
	
	GameHelp(){
		
	final JFrame h = new JFrame("Help");
	h.setSize(x, y); // ������ ũ�� ����
	
	h.addWindowListener(new WindowAdapter() {
		@SuppressWarnings("unused")
		public void WindowClosing(WindowEvent e) {
			h.setVisible(false);
			h.dispose();
		}
	});
	
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension screenSize = tk.getScreenSize(); // ����� ȭ�� ũ�� ����ִ� ��

	h.setBounds(screenSize.width / 2 - (x / 2), screenSize.height / 2
			- (y / 2), x, y);// ������ ��ġ
	
	
	ImageIcon info = new ImageIcon("img/gamehelp.png", "Record");
	JLabel lb = new JLabel(info);
	JButton ok = new JButton("Enjoy 'Avoid it'");
	
	h.add(lb, BorderLayout.CENTER);
	h.add(ok, BorderLayout.SOUTH);
	
	// ok ��ư ���� ��� â �ݱ�
	ok.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			h.setVisible(false);
			h.dispose();
			//System.exit(0);
		}
	});
	
	h.setVisible(true); // ������ ���̰� �����
	h.setResizable(false); // ������ ũ�� �������� ����
	
	
	}
	
}