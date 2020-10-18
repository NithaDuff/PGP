package pgp.buildingblocks.assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SubordinateManager {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Map<String,Manager> managers = new HashMap<>();
		List<Manager> managerList = new ArrayList<>();
		int s,m;
		String in,str;
		Manager manager;
		in = sc.nextLine();
		m = Integer.parseInt(in.split(" ")[0]);
		s = Integer.parseInt(in.split(" ")[1]);
		
		for(int i = 0; i < m;i++) {
			manager = new Manager(sc.nextLine());
			managerList.add(manager);
			managers.put(manager.name,manager);
		}
		for(int i = 0; i < s;i++) {
			str = sc.nextLine();
			manager = managers.get(str.split(" ")[0]);
			manager.sub.add(new Subordinate(str.split(" ")[1], Integer.parseInt(str.split(" ")[2])));
		}
		sc.close();
		Collections.sort(managerList);
		for(Manager mn:managerList) {
			System.out.println(mn.name);
			Collections.sort(mn.sub);
			for(Subordinate sb:mn.sub) {
				System.out.println(sb.name+" "+sb.age);
			}
		}
	}
}

class Manager implements Comparable<Manager>{
	String name;
	List<Subordinate> sub;
	
	public Manager(String name) {
		this.name = name;
		this.sub = new ArrayList<>();
	}

	@Override
	public int compareTo(Manager manager) {
		return this.name.compareTo(manager.name);
	}
}

class Subordinate implements Comparable<Subordinate>{
	String name;
	int age;
	
	public Subordinate(String name,int age) {
		this.name = name;
		this.age = age;
	}

	@Override
	public int compareTo(Subordinate sub) {
		return this.age - sub.age;
	}
}
