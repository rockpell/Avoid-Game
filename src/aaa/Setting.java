package aaa;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JOptionPane;

class GameSetting implements ItemListener {

	int ac = 300, he = 250; // ������ ũ��

	static String getUserName = null;

	public static boolean settingContinig = false;
	public static boolean settingSound = true;

	JLabel yourName = new JLabel("����� ���̵� :");
	JLabel difficulty = new JLabel("���̵� ���� :");
	JLabel setSound = new JLabel("�Ҹ� ���� :");

	JTextField PN = new JTextField(8);

	JButton JBO = new JButton("Ȯ��");

	JPanel JPO = new JPanel();
	JPanel JPO2 = new JPanel();
	JPanel JPO3 = new JPanel();
	
	JButton ok = new JButton("���� �Ϸ�");
	

	JRadioButton radioBtn1 = new JRadioButton("��");
	JRadioButton radioBtn2 = new JRadioButton("��");
	JRadioButton radioBtn3 = new JRadioButton("OFF");
	JRadioButton radioBtn4 = new JRadioButton("ON");

	GameSetting() {
		final JFrame sfs = new JFrame("Setting");
		sfs.setSize(ac, he); // ������ ũ�� ����

		sfs.addWindowListener(new WindowAdapter() {
			@SuppressWarnings("unused")
			public void WindowClosing(WindowEvent e) {

				sfs.setVisible(false);
				sfs.dispose();
			}
		});
		
		settingContinig = false;
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize(); // ����� ȭ�� ũ�� ����ִ� ��

		sfs.setBounds(screenSize.width / 2 - (ac / 2), screenSize.height / 2
				- (he / 2), ac, he);// ������ ��ġ

		sfs.setVisible(true); // ������ ���̰� �����
		sfs.setResizable(false); // ������ ũ�� �������� ����

		Dialog info = new Dialog(sfs, "Information", true);
		info.setSize(140, 90);
		info.setLocation(screenSize.width / 2 - (140 / 2), screenSize.height
				/ 2 - (90 / 2));
		info.setLayout(new FlowLayout());

		sfs.setLayout(new GridLayout(4, 1, 5, 20));
		JPO.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPO2.setLayout(new FlowLayout(FlowLayout.CENTER));
		JPO3.setLayout(new FlowLayout(FlowLayout.CENTER));

		PN.setText("");

		JPO.add(yourName);
		JPO.add(PN);
		JPO.add(JBO);

		ButtonGroup bg = new ButtonGroup();
		ButtonGroup bg2 = new ButtonGroup();

		JPO2.add(difficulty);

		bg.add(radioBtn1);
		bg.add(radioBtn2);

		bg2.add(radioBtn3);
		bg2.add(radioBtn4);

		JPO2.add(radioBtn1);
		JPO2.add(radioBtn2);

		radioBtn1.addItemListener(this); // ������ư�� üũ���� Ȯ��
		radioBtn2.addItemListener(this);
		radioBtn3.addItemListener(this);
		radioBtn4.addItemListener(this);
		
		if(Body.lasert == 40){ // ���� ��ư ��ġ
		radioBtn1.setSelected(true);
		}
		else if(Body.lasert == 30){
		radioBtn2.setSelected(true);
		}

		JPO3.add(setSound);
		JPO3.add(radioBtn3);
		JPO3.add(radioBtn4);
		
		
		if(settingSound == false){
		radioBtn3.setSelected(true); 
		}
		else if(settingSound == true){
			radioBtn4.setSelected(true);
		}

		sfs.add(JPO);
		sfs.add(JPO2);
		sfs.add(JPO3);
		sfs.add(ok);
		
		// ok ��ư ���� ��� â �ݱ�
		ok.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sfs.setVisible(false);
				sfs.dispose();
				//System.exit(0);
			}
		});

		JBO.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getUserName = PN.getText();
				if (getUserName.equals("")) {
					JOptionPane.showMessageDialog(null, "���̵� �Է����ּ���");
				} else {
					JOptionPane.showMessageDialog(null, "���̵� �ԷµǾ����ϴ�");
					System.out.println(getUserName);
					settingContinig = true;
					Body.playerName = getUserName;
				}
			}
		});

	}

	static String setterName() {
		System.out.println("setterName : "+getUserName);
		return getUserName;
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		Object o = e.getSource();
		int type = e.getStateChange();

		if (o == radioBtn1) {
			if (type == ItemEvent.SELECTED) {
				System.out.println("���̵� ��");
				Body.lasert = 40;
			} else {
				System.out.println("���̵� ��");
				Body.lasert = 20;
			}
		}
		
		else if(o == radioBtn3){
			if(type == ItemEvent.SELECTED){
				System.out.println("OFF");
				settingSound = false;
			}else{
				System.out.println("ON");
				settingSound = true;
			}
			
		}

	}
}