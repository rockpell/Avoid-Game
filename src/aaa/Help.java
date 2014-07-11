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
	
	int x = 608, y = 510; // 프레임 크기
	
	GameHelp(){
		
	final JFrame h = new JFrame("Help");
	h.setSize(x, y); // 프레임 크기 지정
	
	h.addWindowListener(new WindowAdapter() {
		@SuppressWarnings("unused")
		public void WindowClosing(WindowEvent e) {
			h.setVisible(false);
			h.dispose();
		}
	});
	
	Toolkit tk = Toolkit.getDefaultToolkit();
	Dimension screenSize = tk.getScreenSize(); // 모니터 화면 크기 잡아주는 것

	h.setBounds(screenSize.width / 2 - (x / 2), screenSize.height / 2
			- (y / 2), x, y);// 프레임 위치
	
	
	ImageIcon info = new ImageIcon("img/gamehelp.png", "Record");
	JLabel lb = new JLabel(info);
	JButton ok = new JButton("Enjoy 'Avoid it'");
	
	h.add(lb, BorderLayout.CENTER);
	h.add(ok, BorderLayout.SOUTH);
	
	// ok 버튼 누를 경우 창 닫기
	ok.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			h.setVisible(false);
			h.dispose();
			//System.exit(0);
		}
	});
	
	h.setVisible(true); // 프레임 보이게 만들기
	h.setResizable(false); // 프레임 크기 임의조절 차단
	
	
	}
	
}