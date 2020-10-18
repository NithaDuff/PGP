package pgp.buildingblocks.assignment;

import java.util.Scanner;

public class Substring {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String s1,s2;
		int out = 0,min;
		s1 = sc.nextLine();
		s2 = sc.nextLine();
		sc.close();
		min = s1.length()<=s2.length()?s1.length():s2.length();
		for(int i = 0; i < min;i++) {
			out = s1.charAt(i)==s2.charAt(i)?out+1:out;
		}
		System.out.println(out);			
	}
}
