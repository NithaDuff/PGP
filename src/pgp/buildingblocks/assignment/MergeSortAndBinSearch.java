package pgp.buildingblocks.assignment;

import java.util.Scanner;

public class MergeSortAndBinSearch {
	 public static void main(String[] args) {
	        
	    	Scanner sc = new Scanner(System.in);
	        int a;
	        
	        a = sc.nextInt();
	        int arr[] = new int[a];
	        for(int i = 0; i < a ; i++){
	        	arr[i] = sc.nextInt();
	        }
	        MergeSort s = new MergeSort();
	        s.sort(arr, 0, a-1);
	        int target = sc.nextInt();
	        int res = s.binSearch(arr,target);
	        System.out.println(res == -1?"Element not found":res);
	        sc.close();
	   }
}
	 
 class MergeSort { 
	 
	 public int binSearch(int[] arr, int target) {
	    	int low = 0, high = arr.length-1, mid;
			while(low<high) {
				mid = (low+high)/2;
				if(target < arr[mid]) {
					high = mid-1;
				}
				else if(target > arr[mid]) {
					low = mid+1;
				}
				else {
					return mid;
				}
			}
			return -1;
		}
	 
	    void merge(int arr[], int l, int m, int r) 
	    { 
	        int n1 = m - l + 1; 
	        int n2 = r - m; 
	  
	        int L[] = new int[n1]; 
	        int R[] = new int[n2]; 
	  
	        for (int i = 0; i < n1; ++i) 
	            L[i] = arr[l + i]; 
	        for (int j = 0; j < n2; ++j) 
	            R[j] = arr[m + 1 + j]; 
	  
	        int i = 0, j = 0; 
	  
	        int k = l; 
	        while (i < n1 && j < n2) { 
	            if (L[i] <= R[j]) { 
	                arr[k] = L[i]; 
	                i++; 
	            } 
	            else { 
	                arr[k] = R[j]; 
	                j++; 
	            } 
	            k++; 
	        } 
	  
	        while (i < n1) { 
	            arr[k] = L[i]; 
	            i++; 
	            k++; 
	        } 
	  
	        while (j < n2) { 
	            arr[k] = R[j]; 
	            j++; 
	            k++; 
	        } 
	    } 
	  
	    void sort(int arr[], int l, int r) 
	    { 
	        if (l < r) { 
	            int m = (l + r) / 2; 
	  
	            sort(arr, l, m); 
	            sort(arr, m + 1, r); 
	            merge(arr, l, m, r); 
	        } 
	    } 
 }
