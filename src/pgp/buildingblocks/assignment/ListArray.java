package pgp.buildingblocks.assignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ListArray {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		List<Integer> a = new ArrayList<>();
		for(int i =0 ;i<6;i++) {
			a.add(sc.nextInt());
		}
		sc.close();
		System.out.print(a.get(2) +" ");
		a.set(4, 8);
		System.out.print(a.get(4) +" ");
		System.out.print(a.indexOf(56) +" ");
		a.add(2, 44);
		System.out.print(a.get(4) +" ");
		a.remove(1);
		System.out.print(a.indexOf(9) +" ");
	}

}
