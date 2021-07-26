package pgp.buildingblocks.assignment;

import java.util.HashMap;
import java.util.Scanner;

public class Billing {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int n = Integer.parseInt(sc.nextLine());
		String in;
		HashMap<String, Integer> catalog = new HashMap<String, Integer>();
		int total = 0;
		for(int i = 0 ;i<n; i++) {
			in = sc.nextLine();
			catalog.put(in.split(" ")[0].trim(), Integer.parseInt(in.split(" ")[1]));
		}
		int m = Integer.parseInt(sc.nextLine());
		for(int i = 0; i<m; i++) {
			total += catalog.get(sc.nextLine().trim());
		}
		System.out.println(total);
		sc.close();
	}
}
