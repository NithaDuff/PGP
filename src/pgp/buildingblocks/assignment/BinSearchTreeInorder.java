package pgp.buildingblocks.assignment;


import java.util.Scanner;

public class BinSearchTreeInorder {
	Tree1 root = null;
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		BinSearchTreeInorder bs = new BinSearchTreeInorder();
		int n = in.nextInt();
		for(int i = 0; i<n ; i++) {
			bs.insert(in.nextInt());
		}
		bs.sort();
		in.close();
	}
	
	public void sort() {
		inorder(root);
	}

	private void inorder(Tree1 cur) {
		if(cur.left != null)
			inorder(cur.left);
		System.out.print(cur.value+" ");
		if(cur.right !=null)
			inorder(cur.right);
	}

	public void insert(int value) {
		root = insertNode(root,value);
	}

	private Tree1 insertNode(Tree1 cur,int value) {
		if(cur == null) {
			cur = new Tree1(value);
			return cur;
		}
		if(value < cur.value) {
			cur.left = insertNode(cur.left, value);
		}
		else if(value > cur.value) {
			cur.right = insertNode(cur.right, value);
		}
		return cur;
	}
}

	

class Tree {
	int value;
	Tree1 right;
	Tree1 left;
	
	public Tree(int value) {
		this.value = value;
		this.right = null;
		this.left = null;
	}

}

