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

	int a = 608, b = 510; // 프레임 크기 변수
	static int block = 30; // 간격크기
	int blimit = 5; // 폭탄 개수 제한 최대치 
	int keyP = 0; // 게임 상태 표시하는 함수 (0:게임 시작 전, 1:게임 시작 후, 2:게임 종료 후 )
	int x = 274, y = 236; // 캐릭터 초기 생성 위치
	int c = 0; // 시간 = 점수
	int score = 0; // 점수
	int db_over = 0; // 중복확인
	int sound_over = 0; // 소리 중복확인
	int lux_over = 0;
	int waveCheck =0; // 웨이브 체크
	
	int mx=0, my=0; // 마우스 좌표
	
	int[] lx = new int[10]; // 가로 레이저 생성좌표 저장변수
	int[] ly = new int[10];
	int[] lx2 = new int[10]; // 세로 레이저 생성좌표 저장변수
	int[] ly2 = new int[10];

	int[] bobx = new int[5];
	int[] boby = new int[5];
	
	int[] itemx = new int[3];
	int[] itemy = new int[3];

	public static int lasert = 40; // 레이저 생성시간
	int laserbt = 0; // 레이져빔 변수
	int lasBB = 4; // 레이저 갯수
	int bombs = 1; // 폭탄 갯수
	int bombbt = 0; // 폭탄 생성 변수
	int qw = block/2; // 레이저 가이드 라인 늘리는 변수
	int itemg = 1; // 아이템 갯수
	int shieldItem =0; // 0: 아무것도 없음, 1:한 웨이브동안 무적상태, 2:쉴드가 존재
	
	public static String playerName = null; // 사용자 이름
	
	Image buffImage; // 더블버퍼링
	Graphics buffg; // 더블버퍼링
	Image title; // avoid it;
	Image player; // 플레이어 이미지
	Image cannon; // 레이져 예측지점 이미지
	Image light1; // 빛 공격 이미지
	Image light2; 
	Image bomb; // 폭탄 이미지
	Image fire; // 폭탄 터지고 난 후 이미지 
	Image start; // 게임 시작 이미지
	Image setting; // 게임 난이도 조절 및 세부 설정
	Image rank; // 랭크
	Image help; // 도움말 이미지 및 개발자 
	Image exit; // 종료 이미지
	Image gameover; // 게임 종료 이미지
	Image cv; // 로고 및 버전 소개
	Image outstanding, excellent, best, good, soso, bad; // 점수 평가
	Image shield; // 쉴드 아이템 이미지

	private volatile boolean terminated = false;
	boolean KeyUp = false; // 키보드 입력 처리를 위한 변수
	boolean KeyDown = false;
	boolean KeyLeft = false;
	boolean KeyRight = false;
	boolean KeyEnter = false;
	boolean KeyShift = false;
	
	boolean contining = false; // 게임 진행관련 변수
	
	boolean ranking = false; // 랭킹 관련 변수
	boolean gameSetting = false; // 세팅 관련 변수
	boolean gameHelp = false; // 도움말 관련 변수
	boolean soundSwitch = false;
	boolean laserPDtime = true; // 레이저 예측 그리는 타이밍
	
	Body() {
		setSize(a, b); // 프레임 크기 지정
		setTitle("Avoid");

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 프로그램 종료

		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize(); // 모니터 화면 크기 잡아주는 것

		setBounds(screenSize.width / 2 - (a / 2), screenSize.height / 2
				- (b / 2), a, b);// 프레임 위치

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
		
		setVisible(true); // 프레임 보이게 만들기
		setResizable(false); // 프레임 크기 임의조절 차단
		addKeyListener(this); // 키보드 입력
		addMouseListener(this); // 마우스 입력
	}

	public void relastart() { // 게임 시작 부분과 게임 내부 부분 나누는 메소드
		
		if (keyP == 0) {
			creat();
			while (keyP == 0) {
				repaint();
				
				if (keyP == 1 && contining == false){
					start();
					contining = true; // 쓰레드 생성 메소드와 게임 진행 쓰레드 구분 변수
				}
				else if(keyP == 1 && contining == true){
					run();
				}
			}
		}
	}

	public void paint(Graphics g) {
		buffImage = createImage(a, b); // 더블버퍼링 크기 프레임크기와 같게 설정
		buffg = buffImage.getGraphics(); // 더블버퍼링 객체 얻기
		update(g);
	}

	public void update(Graphics g) {
		
		if (keyP == 0){
			
			if(mx>90 && mx<218 && my>180 && my<285){ // 마우스 게임 시작
				keyP=1;
				mx=0; // 마우스 좌표 초기화
				my=0;
			}
			
			else if(mx>230 && mx<358 && my>180 && my<285){ // 마우스 랭킹
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
			
			else if(mx>320 && mx<438 && my>310 && my<415){ // 마우스 게임 종료
				System.exit(0);
			}
			
			TrueDraw2();// 실제로 그려진 그림을 가져온다
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
			System.out.println("점수");
		}
		
		if(gameSetting == true) {
			gameSetting = false;
			System.out.println("설정");
			GameSetting GS = new GameSetting();
		}
		
		if(gameHelp == true) {
			gameHelp = false;
			System.out.println("도움");
			GameHelp GH = new GameHelp();
		}

		g.drawImage(buffImage, 0, 0, this); // 프레임에 버퍼에 그린 그림을 가져와 그리기
	
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

	void LaserProcess(int lasBB) { // 레이저 예측지점 저장 메서드
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

	void LaserDraw(int lasBB) { // 레이저 예측지점 그리는 메서드;
		for (int z = 0; z < lasBB / 2; z++) {
			buffg.drawImage(cannon, lx[z], ly[z], this);
			buffg.drawImage(cannon, lx2[z], ly2[z], this);
		}
		LaserBDraw(lasBB);
	}

	void LaserPDraw(int lasBB) { // 레이저 가이드라인
		if(qw > 0){ // 점점 두꺼워지게 만들기
			// 세로 레이저
			for (int i = 1; i < 16; i++) {
				for (int z = 0; z < lasBB / 2; z++) {
					buffg.setColor(Color.yellow);
					buffg.fillRect(lx[z]+qw, ly[z] + (i * block), block-qw*2, block);
				}
			}
		}
		if(qw > 0) {
		// 가로 레이저
			for(int q=1; q < 20; q++){
				for (int z = 0; z < lasBB / 2; z++) {
					buffg.setColor(Color.yellow);
					buffg.fillRect(lx2[z] + (q * block), ly2[z]+qw, block, block-qw*2);
				}
			}
		}
		qw -= 2;
	}

	void LaserBDraw(int lasBB) { // 레이저 그리는 메서드
		if ((c - laserbt) > 10) {
			
			laserPDtime = false; // 레이저 예측 체크
			
			buffg.setColor(Color.yellow);
			
			// 세로 레이저!
			for (int i = 1; i < 16; i++) {
				for (int g = 0; g < lasBB / 2; g++) {
					buffg.drawImage(light1, lx[g], ly[g] + (i * block), this);
				}
			}
			// 가로 레이저!
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
	
	void TrueDraw() { // 게임 로직 관련 메소드
		if(sound_over == 0 && GameSetting.settingSound == true) {
			sound_over = 1;
			//playSound();
		}
		buffg.clearRect(0, 0, a, b); // 화면 지우기
			
        // 격자무늬 만들기 프레임 x의 최소는 4, y의 최소는 26
		for (int i = 56; i < b; i += block) {
			buffg.setColor(Color.gray);
			buffg.drawLine(4, i, a, i);
		}
		for (int i = 34; i < a; i += block) {
			buffg.setColor(Color.gray);
			buffg.drawLine(i, 26+block, i, b);
		}

		buffg.drawString("time : " + c, a-70, 40); // 게임진행상황
		buffg.drawString("shieldItem : "+shieldItem, a-200, 40);
		buffg.drawString("wave : "+waveCheck, a-300, 40);

		if (c % (lasert*3) == 0 && lasBB < 10) {
			lasBB += 2;
		} // 레이져 숫자 증가
		
		if(c% (lasert*5) == 0 && bombs <4){ // 폭탄 숫자 증가
			bombs ++;
			if(bombs>3) blimit++;
		}
		
		if (c % lasert == 0) {
			LaserProcess(lasBB); // 레이저 예측지점 생성
			laserbt = c; // / 레이저 예측지점과 레이저 생성시간의 간격을 맞출때 쓰는 변수
			qw = block/2;
			if(shieldItem==0)itemProcess();
			waveCheck++;
			if(shieldItem==1)shieldItem=0;
		}

		if (c % lasert == 0) {
			bombProcess(bombs); // 폭탄 생성
			bombbt = c;
		}

		if (c > lasert){  // lasert 만큼 이후에 그리기 시작
			if(c%lasert  > 5){
			if(laserPDtime==true)LaserPDraw(lasBB);
			if((c-bombbt)<10){
				bombDraw(bombs);
			}
			}
			if(c%lasert < lasert-10 ){ // 레이저 그리기 시간 제한
			bombBdraw(bombs);
			LaserDraw(lasBB);
			if(shieldItem==0)itemDraw();
			}
		}
		buffg.drawImage(player, x, y, this); // 플레이어 이미지 출력
	}
    
	void TrueDraw2() { // 게임 시작 전 관련 메소드
		setBackground(Color.white);
		title = Toolkit.getDefaultToolkit().getImage("img/title.png");
		buffg.drawImage(title, 0, 30, this);
		
		// 상단 메뉴
		start = Toolkit.getDefaultToolkit().getImage("img/play.png");
		buffg.drawImage(start, 90, 180, this);
		rank = Toolkit.getDefaultToolkit().getImage("img/rank.png");
		buffg.drawImage(rank, 230, 180, this);
		help = Toolkit.getDefaultToolkit().getImage("img/help.png");
		buffg.drawImage(help, 380, 180, this);
		// 하단 메뉴
		setting = Toolkit.getDefaultToolkit().getImage("img/setting.png");
		buffg.drawImage(setting, 150, 310, this);
		exit = Toolkit.getDefaultToolkit().getImage("img/exit.png");
		buffg.drawImage(exit, 320, 310, this);
		
		buffg.drawString("Avoid 1.0", 15, 500);
	}
    

	void TrueDraw3() { // 게임 종료 후 관련 메소드
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
		System.out.println("초기화");
	}

	public void start() {
		Thread th = new Thread(this);
		System.out.println("time : "+ c);
		th.start();
	}

	public void run() { // 스레드
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

	public void KeyProcess() { // 플레이어 이동 메소드

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