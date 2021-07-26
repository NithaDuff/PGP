package pgp.buildingblocks.assignment;

import java.util.Scanner;

public class QuickSortAndBinSearch {

    public static void main(String[] args) {
        
    	Scanner sc = new Scanner(System.in);
        int a;
        
        a = sc.nextInt();
        int arr[] = new int[a];
        for(int i = 0; i < a ; i++){
        	arr[i] = sc.nextInt();
        }
        QuickSort s = new QuickSort();
        s.sort(arr, 0, a-1);
        
        for(int i:arr) {
        	System.out.println(i);
        }
        sc.close();
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

