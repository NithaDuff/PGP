package pgp.buildingblocks.assignment;

import java.util.HashSet;

public class Demo {
	   public static void main(String[] args) {
	        int[] arr = { 1, 3, 5, 7, 14, 18, 24, 26, 32 };
	        int low = 0, high = arr.length - 1;
	        int element = 26;
	        int result = BinarySearch(arr, low, high, element);
	       }

	   public static int BinarySearch(int[] arr, int low, int high, int element) {
	             while (low <= high) {
	                   int middle = (low + high) / 2;
	                   System.out.print(middle + " ");
	                   if (arr[middle] > element) {
	                          high = middle;
	                    } 
	                   else if (arr[middle] < element) {
	                          low = middle;
	                    } 
	                   else if (arr[middle] == element) {
	                          return 1;
	                    }
	               }
	             return 0;
	         } 
	 }
