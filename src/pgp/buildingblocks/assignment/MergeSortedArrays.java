package pgp.buildingblocks.assignment;
import java.util.Scanner;

public class MergeSortedArrays {
	int c[];
	public static void main(String[] args) {
		Scanner sc= new Scanner(System.in);
		MergeSortedArrays merge = new MergeSortedArrays();
		int n,m,a[],b[],i;
		n = sc.nextInt();
		m = sc.nextInt();
		a = new int[n];
		b = new int[m];
		
		for(i = n-1; i >= 0; i--) {
			a[i] = sc.nextInt();
		}
		for(i = m-1; i >= 0; i--) {
			b[i] = sc.nextInt();
		}
		sc.close();
		merge.merge(a, b);
		merge.display();
		
	}
	
	public void display() {
		for(int l:c) {
			System.out.println(l);
		}
	}
	
	public void merge(int a[],int b[]) {
		int i,j,k,n,m;
		n = a.length;
		m = b.length;
		c = new int[m+n];
		i = j = k = 0;
		while(i < n && j < m) {
			if(a[i]>b[j]) {
				c[k++] = a[i++];
			}
			else {
				c[k++] = b[j++];
			}
		}
		while(i<n) {
			c[k++] = a[i++];
		}
		while(j<m) {
			c[k++] = b[j++];
		}
	}

}
