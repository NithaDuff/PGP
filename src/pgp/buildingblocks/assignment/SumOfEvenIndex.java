package pgp.buildingblocks.assignment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;


public class SumOfEvenIndex {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		List<Integer> a = new ArrayList<>();
		int n = sc.nextInt();
		int sum = 0;
		for(int i = 0;i<n;i++) {
			a.add(sc.nextInt());
		}
		sc.close();
		Iterator it = a.iterator();
		while(it.hasNext()) {
			sum += (int)it.next();
			if(it.hasNext()) {
				it.next();
			}
		}
		System.out.println(sum);
	}

}
