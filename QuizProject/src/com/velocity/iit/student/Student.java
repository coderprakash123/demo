package com.velocity.iit.student;

import java.util.HashMap;
import java.util.Scanner;

public class Student {
	int id;
	String name;
	
	public void setId(int id) {
		this.id = id;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}
	
	public static HashMap acceptStudentData() {
		HashMap<Integer,String> student = new HashMap<>(); 
		Scanner scanner = new Scanner(System.in);
		System.out.println("Enter your id: ");
	//	setId(scanner.nextInt());
		int id = scanner.nextInt();
		scanner.nextLine();
		System.out.println("Enter your name: ");
	//	setName(scanner.nextLine());
		String name = scanner.nextLine();
		student.put(id, name);
		return student;
	}
	
}
