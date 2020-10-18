package pgp.buildingblocks.assignment;
import java.util.*;

public class QuickSort {
    public static void main(String[] args) {
        
    	Scanner sc = new Scanner(System.in);
        int a;
        a = sc.nextInt();
        int arr[] = new int[a];
        for(int i = 0; i < a ; i++){
        	arr[i] = sc.nextInt();
        }
        int target = sc.nextInt();
        QuickSort s = new QuickSort();
        s.sort(arr, 0, a-1);
        int res = s.binSearch(arr,target);
        System.out.println(res == -1?"Element not found":res);
        sc.close();
   }
    
    private int binSearch(int[] arr, int target) {
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

	int partition(int arr[], int low, int high) 
    { 
        int pivot = arr[high]; 
        int temp;
        int i = (low-1);
        for (int j=low; j<high; j++) 
        { 
            if (arr[j] <= pivot) 
            { 
                i++; 
                temp = arr[i]; 
                arr[i] = arr[j]; 
                arr[j] = temp; 
            } 
        } 
  
        temp = arr[i+1]; 
        arr[i+1] = arr[high]; 
        arr[high] = temp; 
  
        return i+1; 
    } 
  
    void sort(int arr[], int low, int high) 
    { 
        if (low < high) 
        { 
            int pi = partition(arr, low, high); 
            sort(arr, low, pi-1); 
            sort(arr, pi+1, high); 
        } 
    } 
}
