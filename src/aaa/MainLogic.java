package aaa;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;

//import sun.audio.AudioPlayer;
//import sun.audio.AudioStream;

import javax.swing.JFrame;

class Body extends JFrame implements KeyListener, Runnable, MouseListener {
	private static final long serialVersionUID = 1L;

	int a = 608, b = 510; // ������ ũ�� ����
	static int block = 30; // ����ũ��
	int blimit = 5; // ��ź ���� ���� �ִ�ġ 
	int keyP = 0; // ���� ���� ǥ���ϴ� �Լ� (0:���� ���� ��, 1:���� ���� ��, 2:���� ���� �� )
	int x = 274, y = 236; // ĳ���� �ʱ� ���� ��ġ
	int c = 0; // �ð� = ����
	int score = 0; // ����
	int db_over = 0; // �ߺ�Ȯ��
	int sound_over = 0; // �Ҹ� �ߺ�Ȯ��
	int lux_over = 0;
	int waveCheck =0; // ���̺� üũ
	
	int mx=0, my=0; // ���콺 ��ǥ
	
	int[] lx = new int[10]; // ���� ������ ������ǥ ���庯��
	int[] ly = new int[10];
	int[] lx2 = new int[10]; // ���� ������ ������ǥ ���庯��
	int[] ly2 = new int[10];

	int[] bobx = new int[5];
	int[] boby = new int[5];
	
	int[] itemx = new int[3];
	int[] itemy = new int[3];

	public static int lasert = 40; // ������ �����ð�
	int laserbt = 0; // �������� ����
	int lasBB = 4; // ������ ����
	int bombs = 1; // ��ź ����
	int bombbt = 0; // ��ź ���� ����
	int qw = block/2; // ������ ���̵� ���� �ø��� ����
	int itemg = 1; // ������ ����
	int shieldItem =0; // 0: �ƹ��͵� ����, 1:�� ���̺굿�� ��������, 2:���尡 ����
	
	public static String playerName = null; // ����� �̸�
	
	Image buffImage; // ������۸�
	Graphics buffg; // ������۸�
	Image title; // avoid it;
	Image player; // �÷��̾� �̹���
	Image cannon; // ������ �������� �̹���
	Image light1; // �� ���� �̹���
	Image light2; 
	Image bomb; // ��ź �̹���
	Image fire; // ��ź ������ �� �� �̹��� 
	Image start; // ���� ���� �̹���
	Image setting; // ���� ���̵� ���� �� ���� ����
	Image rank; // ��ũ
	Image help; // ���� �̹��� �� ������ 
	Image exit; // ���� �̹���
	Image gameover; // ���� ���� �̹���
	Image cv; // �ΰ� �� ���� �Ұ�
	Image outstanding, excellent, best, good, soso, bad; // ���� ��
	Image shield; // ���� ������ �̹���

	private volatile boolean terminated = false;
	boolean KeyUp = false; // Ű���� �Է� ó���� ���� ����
	boolean KeyDown = false;
	boolean KeyLeft = false;
	boolean KeyRight = false;
	boolean KeyEnter = false;
	boolean KeyShift = false;
	
	boolean contining = false; // ���� ������� ����
	
	boolean ranking = false; // ��ŷ ���� ����
	boolean gameSetting = false; // ���� ���� ����
	boolean gameHelp = false; // ���� ���� ����
	boolean soundSwitch = false;
	boolean laserPDtime = true; // ������ ���� �׸��� Ÿ�̹�
	
	Body() {
		setSize(a, b); // ������ ũ�� ����
		setTitle("Avoid");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // ���α׷� ����

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize(); // ����� ȭ�� ũ�� ����ִ� ��

		setBounds(screenSize.width / 2 - (a / 2), screenSize.height / 2
				- (b / 2), a, b);// ������ ��ġ

		player = Toolkit.getDefaultToolkit().getImage("img/player.png");
		cannon = Toolkit.getDefaultToolkit().getImage("img/cannon.png");
        bomb = Toolkit.getDefaultToolkit().getImage("img/bomb.png");
		fire = Toolkit.getDefaultToolkit().getImage("img/fire.png");
		light1 = Toolkit.getDefaultToolkit().getImage("img/light1.png");
		light2 = Toolkit.getDefaultToolkit().getImage("img/light2.png");
		outstanding = Toolkit.getDefaultToolkit().getImage("img/outstanding.png");
		excellent = Toolkit.getDefaultToolkit().getImage("img/excellent.png");
		best = Toolkit.getDefaultToolkit().getImage("img/best.png");
		good = Toolkit.getDefaultToolkit().getImage("img/good.png");
		soso = Toolkit.getDefaultToolkit().getImage("img/soso.png");
		bad = Toolkit.getDefaultToolkit().getImage("img/bad.png");
		gameover = Toolkit.getDefaultToolkit().getImage("img/gameover.png");
		shield = Toolkit.getDefaultToolkit().getImage("img/shield.png");
		
		setVisible(true); // ������ ���̰� �����
		setResizable(false); // ������ ũ�� �������� ����
		addKeyListener(this); // Ű���� �Է�
		addMouseListener(this); // ���콺 �Է�
	}

	public void relastart() { // ���� ���� �κа� ���� ���� �κ� ������ �޼ҵ�
		
		if (keyP == 0) {
			creat();
			while (keyP == 0) {
				repaint();
				
				if (keyP == 1 && contining == false){
					start();
					contining = true; // ������ ���� �޼ҵ�� ���� ���� ������ ���� ����
				}
				else if(keyP == 1 && contining == true){
					run();
				}
			}
		}
	}

	public void paint(Graphics g) {
		buffImage = createImage(a, b); // ������۸� ũ�� ������ũ��� ���� ����
		buffg = buffImage.getGraphics(); // ������۸� ��ü ���
		update(g);
	}

	public void update(Graphics g) {
		
		if (keyP == 0){
			
			if(mx>90 && mx<218 && my>180 && my<285){ // ���콺 ���� ����
				keyP=1;
				mx=0; // ���콺 ��ǥ �ʱ�ȭ
				my=0;
			}
			
			else if(mx>230 && mx<358 && my>180 && my<285){ // ���콺 ��ŷ
				ranking = true;
				mx=0;
				my=0;
			}
			
			else if(mx>150 && mx<278 && my>310 && my<415){
				gameSetting = true;
				mx=0;
				my=0;
			}
			
			else if(mx>380 && mx<510 && my>185 && my<310){
				gameHelp = true;
				mx=0;
				my=0;
			}
			
			else if(mx>320 && mx<438 && my>310 && my<415){ // ���콺 ���� ����
				System.exit(0);
			}
			
			TrueDraw2();// ������ �׷��� �׸��� �����´�
		}
		else if (keyP == 1) {
			TrueDraw();
			score = c;
		}
		else if (keyP == 2) {
			TrueDraw3();
			relastart();
		}
	
		if(ranking == true) {
			ranking = false;
			System.out.println("����");
		}
		
		if(gameSetting == true) {
			gameSetting = false;
			System.out.println("����");
			GameSetting GS = new GameSetting();
		}
		
		if(gameHelp == true) {
			gameHelp = false;
			System.out.println("����");
			GameHelp GH = new GameHelp();
		}

		g.drawImage(buffImage, 0, 0, this); // �����ӿ� ���ۿ� �׸� �׸��� ������ �׸���
	
	}
	/*
	public void playSound() {
		try {
			File con_s = new File("wav/connect.wav");
			FileInputStream fis = new FileInputStream(con_s); 
		   // AudioStream as = new AudioStream(fis);
		//	AudioPlayer.player.start(as);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("sound exception");
		}
	}
	
	public void endSound() {
		try {
			File end_s = new File("wav/end.wav");
			FileInputStream fis = new FileInputStream(end_s); 
			//AudioStream as = new AudioStream(fis);
			//AudioPlayer.player.start(as);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("sound exception");
		}
	}

	public void luxSound() {
		try {
			File lux_s = new File("wav/lux.wav");
			FileInputStream fis = new FileInputStream(lux_s); 
		 	//AudioStream as = new AudioStream(fis);
			//AudioPlayer.player.start(as);
		} catch(Exception e) {
			e.printStackTrace();
			System.out.println("sound exception");
		}
	}
	*/

	void LaserProcess(int lasBB) { // ������ �������� ���� �޼���
		Laser las = new Laser(block, lasBB);
		laserPDtime = true;
		if(GameSetting.settingSound == true) {
			//luxSound();
		}
		for (int z = 0; z < lasBB / 2; z++) {
			lx[z] = las.las[z].x;
			ly[z] = las.las[z].y;
			lx2[z] = las.las2[z].x;
			ly2[z] = las.las2[z].y;
		}
	}

	void LaserDraw(int lasBB) { // ������ �������� �׸��� �޼���;
		for (int z = 0; z < lasBB / 2; z++) {
			buffg.drawImage(cannon, lx[z], ly[z], this);
			buffg.drawImage(cannon, lx2[z], ly2[z], this);
		}
		LaserBDraw(lasBB);
	}

	void LaserPDraw(int lasBB) { // ������ ���̵����
		if(qw > 0){ // ���� �β������� �����
			// ���� ������
			for (int i = 1; i < 16; i++) {
				for (int z = 0; z < lasBB / 2; z++) {
					buffg.setColor(Color.yellow);
					buffg.fillRect(lx[z]+qw, ly[z] + (i * block), block-qw*2, block);
				}
			}
		}
		if(qw > 0) {
		// ���� ������
			for(int q=1; q < 20; q++){
				for (int z = 0; z < lasBB / 2; z++) {
					buffg.setColor(Color.yellow);
					buffg.fillRect(lx2[z] + (q * block), ly2[z]+qw, block, block-qw*2);
				}
			}
		}
		qw -= 2;
	}

	void LaserBDraw(int lasBB) { // ������ �׸��� �޼���
		if ((c - laserbt) > 10) {
			
			laserPDtime = false; // ������ ���� üũ
			
			buffg.setColor(Color.yellow);
			
			// ���� ������!
			for (int i = 1; i < 16; i++) {
				for (int g = 0; g < lasBB / 2; g++) {
					buffg.drawImage(light1, lx[g], ly[g] + (i * block), this);
				}
			}
			// ���� ������!
			for (int i = 1; i < 20; i++) {
				for (int g = 0; g < lasBB / 2; g++) {
					buffg.drawImage(light2, lx2[g] + (i * block), ly2[g], this);
				}
			}
			
			for (int i = 0; i < lasBB / 2; i++) {
			if(shieldItem==2 && x == lx[i] || y == ly2[i]){
				shieldItem=1;
				break;
			}
				else if (shieldItem !=1 && x == lx[i] || y == ly2[i]) {
					keyP = 2;
					break;
				}
			}
		}
	}

	void bombProcess(int bombs) {
		Bomb bombMake = new Bomb(x, y, blimit, block, bombs, a, b);

		for (int z = 0; z < bombs; z++) {
			bobx[z] = bombMake.bob1[z].x;
			boby[z] = bombMake.bob1[z].y;
		}
	}

	void bombDraw(int bombs) {
		for (int z = 0; z < bombs; z++) {
			buffg.drawImage(bomb, bobx[z], boby[z], this);
		}
	}

	void bombBdraw(int bombs) {
		if ((c - bombbt) > 10) {
			for (int g = 0; g < bombs; g++) {
				for(int aa=-1; aa<2; aa++){
					for(int bb=-1; bb<2; bb++){
						buffg.drawImage(fire, bobx[g]+block*aa, boby[g]+block*bb, this);
					}
				}
			}

			for (int g = 0; g < bombs; g++) {
				for (int ab = -1; ab < 2; ab++) {
					for (int ac = -1; ac < 2; ac++) {
						
						if(shieldItem==2 && x == (bobx[g] + block * ab) && y == (boby[g] + block * ac)){
							shieldItem=1;
							break;
						}
						else if (shieldItem !=1 && x == (bobx[g] + block * ab) && y == (boby[g] + block * ac)) {
							keyP = 2;
							break;
						}
					}

				}
			}
		}
	}
	
	void itemProcess(){
		Item itemMake = new Item(a, b, block, itemg);
		
		for(int z=0; z<itemg; z++){
			itemx[z]=itemMake.itemLocation[z].x;
			itemy[z]=itemMake.itemLocation[z].y;
		}
	}
	
	void itemDraw(){
		for (int z = 0; z < itemg; z++) {
			buffg.setColor(Color.orange);
			buffg.drawImage(shield, itemx[z], itemy[z], this);
			if(itemx[z]==x && itemy[z]==y){
				shieldItem=2;
			}
		}
	}
	
	void TrueDraw() { // ���� ���� ���� �޼ҵ�
		if(sound_over == 0 && GameSetting.settingSound == true) {
			sound_over = 1;
			//playSound();
		}
		buffg.clearRect(0, 0, a, b); // ȭ�� �����
			
        // ���ڹ��� ����� ������ x�� �ּҴ� 4, y�� �ּҴ� 26
		for (int i = 56; i < b; i += block) {
			buffg.setColor(Color.gray);
			buffg.drawLine(4, i, a, i);
		}
		for (int i = 34; i < a; i += block) {
			buffg.setColor(Color.gray);
			buffg.drawLine(i, 26+block, i, b);
		}

		buffg.drawString("time : " + c, a-70, 40); // ���������Ȳ
		buffg.drawString("shieldItem : "+shieldItem, a-200, 40);
		buffg.drawString("wave : "+waveCheck, a-300, 40);

		if (c % (lasert*3) == 0 && lasBB < 10) {
			lasBB += 2;
		} // ������ ���� ����
		
		if(c% (lasert*5) == 0 && bombs <4){ // ��ź ���� ����
			bombs ++;
			if(bombs>3) blimit++;
		}
		
		if (c % lasert == 0) {
			LaserProcess(lasBB); // ������ �������� ����
			laserbt = c; // / ������ ���������� ������ �����ð��� ������ ���⶧ ���� ����
			qw = block/2;
			if(shieldItem==0)itemProcess();
			waveCheck++;
			if(shieldItem==1)shieldItem=0;
		}

		if (c % lasert == 0) {
			bombProcess(bombs); // ��ź ����
			bombbt = c;
		}

		if (c > lasert){  // lasert ��ŭ ���Ŀ� �׸��� ����
			if(c%lasert  > 5){
			if(laserPDtime==true)LaserPDraw(lasBB);
			if((c-bombbt)<10){
				bombDraw(bombs);
			}
			}
			if(c%lasert < lasert-10 ){ // ������ �׸��� �ð� ����
			bombBdraw(bombs);
			LaserDraw(lasBB);
			if(shieldItem==0)itemDraw();
			}
		}
		buffg.drawImage(player, x, y, this); // �÷��̾� �̹��� ���
	}
    
	void TrueDraw2() { // ���� ���� �� ���� �޼ҵ�
		setBackground(Color.white);
		title = Toolkit.getDefaultToolkit().getImage("img/title.png");
		buffg.drawImage(title, 0, 30, this);
		
		// ��� �޴�
		start = Toolkit.getDefaultToolkit().getImage("img/play.png");
		buffg.drawImage(start, 90, 180, this);
		rank = Toolkit.getDefaultToolkit().getImage("img/rank.png");
		buffg.drawImage(rank, 230, 180, this);
		help = Toolkit.getDefaultToolkit().getImage("img/help.png");
		buffg.drawImage(help, 380, 180, this);
		// �ϴ� �޴�
		setting = Toolkit.getDefaultToolkit().getImage("img/setting.png");
		buffg.drawImage(setting, 150, 310, this);
		exit = Toolkit.getDefaultToolkit().getImage("img/exit.png");
		buffg.drawImage(exit, 320, 310, this);
		
		buffg.drawString("Avoid 1.0", 15, 500);
	}
    

	void TrueDraw3() { // ���� ���� �� ���� �޼ҵ�
		if(sound_over == 1 && GameSetting.settingSound == true) {
			sound_over = 2;
			//endSound();
		}
		setBackground(Color.black);
		
		buffg.drawImage(gameover, 0, 20, this);
	
		//buffg.setFont(new Font("Noteworthy", Font.BOLD, 60));
		//buffg.setColor(Color.red);
		//buffg.drawString("GAME OVER", 110, 120);
		
		buffg.setColor(Color.white);
		buffg.setFont(new Font("Noteworthy", Font.BOLD, 40));
		buffg.drawString("SCORE : " + score, 300, 210);
		
		buffg.setColor(Color.white);
		buffg.setFont(new Font("Noteworthy", Font.PLAIN, 30));
		buffg.drawString("Retry?", 80, 440);
		buffg.drawString("Press Enter Key", 300, 440);
		buffg.drawString("Main Menu?",80, 480);
		buffg.drawString("Press Shift Key", 300, 480);
		
		if(score>500) {
			
			buffg.drawImage(outstanding, 20, 150, this);
			
			buffg.setFont(new Font("Noteworthy", Font.BOLD, 35));
			buffg.setColor(Color.yellow);
			buffg.drawString("OUTSTANDING", 300, 300);
		}
		else if(score>400) {
			
			buffg.drawImage(excellent, 20, 150, this);
			
			buffg.setFont(new Font("Noteworthy", Font.BOLD, 40));
			buffg.setColor(Color.yellow);
			buffg.drawString("EXCELLENT", 330, 300);
		}
		else if(score>300) {
			
			buffg.drawImage(best, 20, 150, this);
			
			buffg.setFont(new Font("Noteworthy", Font.BOLD, 60));
			buffg.setColor(Color.yellow);
			buffg.drawString("BEST", 370, 300);
		}
		else if(score>200) {
			
			buffg.drawImage(good, 20, 150, this);
			
			buffg.setFont(new Font("Noteworthy", Font.BOLD, 60));
			buffg.setColor(Color.yellow);
			buffg.drawString("GOOD", 370, 300);
		}
		else if(score>100) {
			
			buffg.drawImage(bad, 20, 150, this);
			
			buffg.setFont(new Font("Noteworthy", Font.BOLD, 60));
			buffg.setColor(Color.yellow);
			buffg.drawString("SOSO", 370, 300);
		}
		else if(score>0) {
			
			buffg.drawImage(bad, 20, 150, this);
			
			buffg.setFont(new Font("Noteworthy", Font.BOLD, 60));
			buffg.setColor(Color.yellow);
			buffg.drawString("BAD", 370, 300);
		}
		else
			buffg.drawString("ERROR", 370, 300);
		
		if(db_over == 0) {
			db_over++;
		}
	}

	public void terminate() {
		terminated = true;
	}

	public void creat() {
		terminated = false;
	}

	public void init() {
		c = 0;
		keyP = 0;
		laserbt = 0;
		lasBB = 4;
		bombs = 1;
		itemg = 1;
		db_over = 0; 
		sound_over = 0;
		lux_over = 0;
		x = 274;
		y = 236;
		waveCheck=0;
		shieldItem = 0;
		setBackground(Color.white);
		System.out.println("�ʱ�ȭ");
	}

	public void start() {
		Thread th = new Thread(this);
		System.out.println("time : "+ c);
		th.start();
	}

	public void run() { // ������
		try {
			while (!terminated) {
				KeyProcess();
				repaint();
				c++;
				Thread.sleep(80);
			}

		} catch (Exception e) {
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {

		case KeyEvent.VK_UP:
			KeyUp = true;
			break;

		case KeyEvent.VK_DOWN:
			KeyDown = true;
			break;

		case KeyEvent.VK_LEFT:
			KeyLeft = true;
			break;

		case KeyEvent.VK_RIGHT:
			KeyRight = true;
			break;

		case KeyEvent.VK_ENTER:
			KeyEnter = true;
			break;
			
		case KeyEvent.VK_SHIFT:
			KeyShift = true;
			break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {

		case KeyEvent.VK_UP:
			KeyUp = false;
			break;

		case KeyEvent.VK_DOWN:
			KeyDown = false;
			break;

		case KeyEvent.VK_LEFT:
			KeyLeft = false;
			break;

		case KeyEvent.VK_RIGHT:
			KeyRight = false;
			break;

		case KeyEvent.VK_ENTER:
			KeyEnter = false;
			break;
			
		case KeyEvent.VK_SHIFT:
			KeyShift = false;
			break;
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void KeyProcess() { // �÷��̾� �̵� �޼ҵ�

		if (KeyUp == true && y > 56+block)
			y -= block;
		if (KeyDown == true && y < (b - 2 * block))
			y += block;
		if (KeyLeft == true && x > 34)
			x -= block;
		if (KeyRight == true && x < (a - 2 * block))
			x += block;
		
		if (KeyShift == true && keyP == 2) {
			terminate();
			init();
			relastart();
		}
		
		if (KeyEnter == true && keyP == 2){
			terminate();
			init();
			creat();
			keyP=1;
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(keyP==0){
			mx=e.getX();
			my=e.getY();
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}
}