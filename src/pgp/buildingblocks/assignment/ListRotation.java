package pgp.buildingblocks.assignment;

import java.util.Scanner;

public class ListRotation {
	Node head;
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		ListRotation l = new ListRotation();
		int n = Integer.parseInt(in.nextLine());
		
		for(int i=0;i< n;i++) {
			l.insert(Integer.parseInt(in.nextLine().trim()));
		}
		l.reverse(l.head);
		l.display();
		in.close();
	}
	
	public void reverse(Node cur) {
		
	}
	public void rotate() {
		Node last = head;
		Node prev = last;
		while(last.next != null) {
			prev = last;
			last = last.next;
		}
		prev.next = null;
		last.next = head;
		head = last;		
	}
	
	public void display() {
		Node curNode = head;
		while(curNode != null) {
			System.out.print(curNode.value+" ");
			curNode = curNode.next;
		}
	}
	public void insert(int data) {
		Node newNode = new Node(data);
		newNode.next = null;
		if(head == null) {
			head = newNode;
		}
		else {
			Node last = head;
			while(last.next !=null) {
				last = last.next;
			}
			last.next = newNode;
		}			
	}
}
