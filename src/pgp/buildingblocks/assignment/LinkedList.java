package pgp.buildingblocks.assignment;

import java.util.Scanner;

public class LinkedList {
	Node head;
	Node headDup;
	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		LinkedList l = new LinkedList();
		int n = Integer.parseInt(in.nextLine());
		
		for(int i=0;i< n;i++) {
			l.insert(in.nextLine());
		}
		l.reverse();
		l.display();
		in.close();
	}
	
	public void reverse() {
		Node dupCur = headDup;
		while(head.next !=null) {
		Node cur = head;
		Node prev = cur;
		while(cur.next != null) {
			prev = cur;
			cur = cur.next;
		}
		if(dupCur == null) {
			dupCur = cur;
			if(headDup == null) {
				headDup = dupCur;
			}
		}
		else {
			dupCur.next = cur;
			dupCur = dupCur.next;
		}
		prev.next = null;
		}
		dupCur.next = head;
		head = headDup;
	}
	
	/*
	 * public void removeDuplicates() { Node newHead = new Node(head.data); Node cur
	 * = head; Node t = newHead; newHead.next = null; while(cur != null) {
	 * if(hasElement(newHead,cur.data) == null) { t.next = new Node(cur.data); t =
	 * t.next; } cur = cur.next; } head = newHead; }
	 * 
	 * public Node hasElement(Node n,String data) { Node curNode = n; while(curNode
	 * != null) { if(data.equals(curNode.data)) { return curNode; } curNode =
	 * curNode.next; } return null; }
	 */
	
	public void display() {
		Node curNode = head;
		while(curNode != null) {
			System.out.print(curNode.data + " ");
			curNode = curNode.next;
		}
	}
	public void insert(String data) {
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

class Node{
	String data;
	int value;
	Node next;
	public Node(String d) {
		this.data = d;
		this.next = null;
	}
	
	public Node(int d) {
		this.value = d;
		this.next = null;
	}
}
