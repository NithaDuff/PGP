package pgp.buildingblocks.assignment;

import java.util.HashSet;
import java.util.Scanner;

public class TwoSum {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		HashSet<Integer> h;
		HashSet<HashSet<Integer>> set = new HashSet<HashSet<Integer>>();
		int n = sc.nextInt(),target;
		int a[] = new int[n];
		for(int i = 0; i < n; i++) {
			a[i] = sc.nextInt();
		}
		target = sc.nextInt(); 
		for(int i = 0; i<n; i++) {
			for(int j=0; j<n; j++) {
				if(i == j) {
					continue;
				}
				if(a[i]+a[j] == target) {
					h = new HashSet<Integer>();
					h.add(a[i]);
					h.add(a[j]);
					set.add(h);
				}
			}
		}
		System.out.println(set.size());
		sc.close();
	}
}
