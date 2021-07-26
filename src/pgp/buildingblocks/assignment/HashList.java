package pgp.buildingblocks.assignment;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class HashList {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Factor f;
		Boolean flag = false;
		int n = sc.nextInt();
		int a[] = new int[n];
		for(int i=0;i<n;i++) {
			a[i] = sc.nextInt();
		}
		sc.close();
		Map<Integer,Factor> products = new HashMap<>();
		for(int i=0;i<n;i++) {
			for(int j=0;j<i;j++) {
				if(products.get(a[i]*a[j]) == null) {
					products.put(a[i]*a[j], new Factor(a[i],a[j]));
				}
				else {
					flag = true;
					f = products.get(a[i]*a[j]);
					System.out.println("("+a[j]+","+a[i]+") and ("+f.b+","+f.a+")");
				}
			}
		}
		if(flag == false) {
			System.out.println("No pairs found");
		}
	}

}
class Factor{
	int a;
	int b;
	public Factor(int a,int b) {
		this.a = a;
		this.b = b;
	}
}
