package pgp.buildingblocks.assignment;

import java.util.Scanner;

public class ArrayFact {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n = sc.nextInt();
		int a[] = new int[n],b[] = new int[n];
		for(int i = 0 ; i <n ; i++) {
			a[i] = sc.nextInt();
		}
		for(int i = 0 ; i<n ; i++ ) {
			b[i] = 1;
			for(int  j = 0 ; j<n ; j++) {
				if(i == j) {
					continue;
				}
				b[i] *= a[j]; 
			}
		}
		for(int i=0;i<n;i++)
			a[i] = b[i];
		for(int i:a) {
			System.out.println(i);
		}
	}
}
