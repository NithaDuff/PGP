package pgp.buildingblocks.assignment;

import java.util.Scanner;

public class BinSearch {
	TreeNode root = null;
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		BinSearch bs = new BinSearch();
		int n = Integer.parseInt(in.nextLine());
		int value = Integer.parseInt(in.nextLine());
		for(int i = 0; i<n ; i++) {
			bs.insert(Integer.parseInt(in.nextLine()));
		}
		bs.hasElement(value);
		in.close();
	}
	
	private void hasElement(int value) {
		TreeNode emp = search(root,value);
		if(emp!=null) {
			System.out.println(emp.manager);
		}
		else {
			System.out.println("Employee not found");
		}
	}
	
	private TreeNode search(TreeNode cur, int value) {
	    if (cur==null || cur.value==value) 
	        return cur; 
	  
	    if (cur.value > value) 
	        return search(cur.left, value); 
	  
	    return search(cur.right, value); 
	}

	public void insert(int value) {
		root = insertNode(root,value);
	}

	private TreeNode insertNode(TreeNode cur,int value) {
		if(cur == null) {
			cur = new TreeNode(value);
			return cur;
		}
		if(value < cur.value) {
			cur.left = insertNode(cur.left, value);
			cur.left.manager = cur;
		}
		else if(value > cur.value) {
			cur.right = insertNode(cur.right, value);
			cur.right.manager = cur;
		}
		return cur;
	}
}

	

class TreeNode {
	int value;
	TreeNode right;
	TreeNode left;
	TreeNode manager;
	
	public TreeNode(int value) {
		this.value = value;
		this.right = null;
		this.left = null;
		this.manager = null;
	}

	@Override
	public String toString() {
		return value+" ";
	}
	
}
