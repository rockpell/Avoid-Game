package aaa;

import java.awt.Point;

class Laser { // 레이저 생성 클래스
	Point[] las;
	Point[] las2;

	Laser(int block, int lasBB) {
		las = new Point[lasBB];
		las2 = new Point[lasBB];

		int[] a = new int[lasBB / 2];
		int[] b = new int[lasBB / 2];

		for (int z = 0; z < lasBB / 2; z++) {
			randomMake(a, z, 18, lasBB);
			randomMake(b, z, 14, lasBB);

			las[z] = new Point(4 + block * a[z], 26+block);
			las2[z] = new Point(4, 26 + block * b[z]+block);
		}
	}

	void randomMake(int a[], int z, int limit, int lasBB) { // 레이저를 중복되지 않는 랜덤으로 생성
		int stemp = 0;
		for (int b = 1; b < 2; b++) {
			stemp = (int) ((Math.random()) * (limit) + 1);

			for (int i = 0; i < (lasBB/2)-1; i++) {
				if (a[i] == stemp) {
					--b;
				}
			}
		}
		a[z] = stemp;
	}
}

class Bomb {
	Point[] bob1;

	Bomb(int x, int y, int limit, int block, int bombs, int ax, int by) { // 플레이어 좌표, 프레임크기
		bob1 = new Point[bombs];

		int a[] = new int[bombs];
		int b[] = new int[bombs];

		for (int i = 0; i < bombs; i++) {
			if(i<1){
			bombRandomMake(a, b, i, x, y, ax, by, bombs, limit, block);
			bob1[i] = new Point(x + block * a[i], y + block * b[i]);
			}
			
			else if(i>=1){
				bombRandomMake2(a, b, i, 16, 13, bombs);
				bob1[i] = new Point(64 + block*a[i], 56 + block*b[i]);
			}
			
			System.out.println("x : "+(x + block * a[i]) + " y : " + (y + block * b[i]) + " bombs : "+bombs);
		}
	}

	void bombRandomMake(int za[], int zb[], int i, int x, int y, int a, int b, int bombs,
			int limit, int block) {
		// 폭탄값 저장 배열, 폭탄 저장배열 번수, 플레이어 좌표, 프레임 크기, 폭탄 갯수
		int stemp = 0, stemp2 = 0; // 폭탄 x,y 좌표값
		int stemp3 = 0, stemp4 = 0; // 폭탄 좌표 +- 결정하는 변수
	
		if(x < a - (block * limit) && y < b - (block * (limit+1)) && x > block * (limit + 1) && y > block * (limit + 1)){
			for (int bbc = 1; bbc < 2; bbc++) {
				stemp = (int) ((Math.random()) * (limit) + 1);
				stemp2 = (int) ((Math.random()) * (limit) + 1);
				stemp3 = (int) ((Math.random()) * 2);
				stemp4 = (int) ((Math.random()) * 2);

				for (int q = 0; q < bombs-1; q++) {
					if (stemp3 == 1) {
						stemp = (stemp) * (-1);
					}
					if(stemp4 == 1){
						stemp2 = (stemp)*(-1);
					}
					if (za[q] == stemp && zb[q] == stemp2) {
						--bbc;
					}
				}
			}
			za[i] = stemp;
			zb[i] = stemp2;
		}
		else{
			za[i] = stemp;
			zb[i] = stemp2;
		}
	}
	
	void bombRandomMake2(int za[], int zb[], int i, int limit , int limit2, int bombs){
		int stemp = 0, stemp2 = 0;
		
		for (int bcc = 1; bcc < 2; bcc++) {
			stemp = (int) (Math.random()*limit+ 1);
			stemp2 = (int) (Math.random()*limit2 + 1);

			for (int q = 0; q < bombs-1; q++) {
				
				if (za[q] == stemp && zb[q] == stemp2) {
					--bcc;
				}
			}
		}
		za[i]=stemp;
		zb[i]=stemp2;
		
	}
}

class Item{
	Point[] itemLocation;
	
	Item(int xx, int yy, int block, int itemLimit){
		itemLocation = new Point[itemLimit];
		
		int a[] = new int[itemLimit];
		int b[] = new int[itemLimit];
		
		for(int i=0; i<itemLimit; i++){
		ItemMake1(a,b, itemLimit, i, xx/block-2, yy/block-3);
		itemLocation[i] = new Point(a[i]*block + 64, b[i]*block + 56);
		}
	}
	
	void ItemMake1(int a[], int b[], int items, int i, int limit1, int limit2){
		int stemp = 0, stemp2 =0;
		
		stemp = (int)(Math.random()*limit1+1);
		stemp2 = (int)(Math.random()*limit2+1);
		
		for (int bcc = 1; bcc < 2; bcc++) {
			stemp = (int) (Math.random()*limit1+ 1);
			stemp2 = (int) (Math.random()*limit2 + 1);

			for (int q = 0; q < items-1; q++) {
				
				if (a[q] == stemp && b[q] == stemp2) {
					--bcc;
				}
			}
		}
		a[i]=stemp;
		b[i]=stemp2;
	}
	
}