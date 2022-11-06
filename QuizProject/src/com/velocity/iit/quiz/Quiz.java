package com.velocity.iit.quiz;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import com.velocity.iit.quiz.dbconnection.DataConnection;
import com.velocity.iit.student.Student;

public class Quiz {
	static Connection connection = DataConnection.connect();
	static PreparedStatement preparedStatement;
	static String sqlQuery="select q_number,question from tbl_questions order by rand()";
	
	public static void launchApplication() {
		String isContinue = "";
		do {
			System.out.println("**********Welcome to Quiz PlayStation**********");
			System.out.println("(You will have 10 MCQs on JAVA Programming)");
			System.out.println("(Your score will be displayed at the end)");
			System.out.println();
			System.out.println("*****MENU*****");
			System.out.println("1.Start Quiz Now");
			System.out.println("2.Show My Score");
			System.out.println("3.Show All Records");
			System.out.println("4.Exit");
			System.out.println("Please enter you choice: ");
			Scanner scan = new Scanner(System.in);
			
			int menuChoice = scan.nextInt();
			switch(menuChoice) {
				case 1:
					playQuiz();
					break;
				case 2:
					System.out.println("Enter your id: ");
					int id = scan.nextInt();
					getRecord(id);
					break;
				case 3:
					showAllRecords();
					break;
				case 4:
					System.out.println("Bye..See you again");
					System.exit(0);
				default:
					System.out.println("Please enter choice between 1 to 3");
			}
			System.out.println("Do you want to continue(y/n): ");
			scan.nextLine();
			isContinue = scan.nextLine();
			if(!isContinue.equalsIgnoreCase("y")) {
				System.out.println("Bye Bye");
				System.exit(1);
			}
		}while(isContinue.equalsIgnoreCase("y"));	
	}
	
	static void playQuiz() {				
	//	Student student = new Student();
		Map<Integer,String> student = Student.acceptStudentData();
		try {
				PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
				ResultSet resultSet = preparedStatement.executeQuery();
				int qNum = 1;
				int correctCounter = 0;
				while(resultSet.next()) {
					
					System.out.println("Q."+qNum+") "+resultSet.getString(2));
					int questionNumber = resultSet.getInt(1);
					String query = "select op1,op2,op3,op4 from tbl_options where tbl_options.q_number=?";
					PreparedStatement preparedStatement1 = connection.prepareStatement(query);
					preparedStatement1.setInt(1, questionNumber);
					ResultSet resultSet1 = preparedStatement1.executeQuery();
					resultSet1.next();
					System.out.println("Options: ");
					int opNum = 65;
					for(int i=1; i<=4; i++) {
						
						System.out.print((char)(opNum++)+") "+resultSet1.getString(i)+" ");
					}
					System.out.println();
					qNum++;
					String studentAnswer = chooseAnswer();
					if(isCorrect(questionNumber,studentAnswer)) { 
						System.out.println("Yay...Correct Answer");
						correctCounter++;
					}
					else {
						System.out.println("Oops...it's wrong");
					}
				}
				int id = 0;
				String name = "";
				for(Map.Entry<Integer,String> stud : student.entrySet()) {
					id = stud.getKey();
					name = stud.getValue();
					System.out.println("Your ID: "+stud.getKey());
					System.out.println("Your Name: "+stud.getValue());
				}
				System.out.println("Your score: "+correctCounter+"/10");
				String grade = calculateGrade(correctCounter);
				System.out.println("Your grade: "+grade);
				insertIntoDatabase(id,name,correctCounter,grade);
			}catch(SQLException e) {
				e.printStackTrace();
		}				
	}
	
	static String chooseAnswer() {
		Scanner scanner = new Scanner(System.in);	
		System.out.println("Please type your answer: ");
		String studentAnswer = scanner.nextLine();
		return studentAnswer;
	}
	
	static boolean isCorrect(int questionNumber,String studentAnswer) {
		String correctAnswer = "";
		try {
			String query = "select correct_option from tbl_questions where tbl_questions.q_number=?";
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, questionNumber);
			ResultSet resultSet = preparedStatement.executeQuery();
			resultSet.next();
			correctAnswer = resultSet.getString(1);
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return studentAnswer.equalsIgnoreCase(correctAnswer);
	}
	
	static String calculateGrade(int correctCounter) {
		if(correctCounter >= 8)
			return "A";
		else if(correctCounter <=7 && correctCounter>=6)
			return "B";
		else if(correctCounter == 5)
			return "C";
		else
			return "fail";
	}
	
	static int insertIntoDatabase(int id,String name,int marks,String grade) {
		String insertQuery = "insert into tbl_studentrecord values (?,?,?,?)";
		int rowsAffected = 0;
		try {
		PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
		preparedStatement.setInt(1, id);
		preparedStatement.setString(2, name);
		preparedStatement.setInt(3, marks);
		preparedStatement.setString(4, grade);
		rowsAffected = preparedStatement.executeUpdate();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return rowsAffected;
	}
	
	static void showAllRecords() {
		String insertQuery = "select * from tbl_studentrecord order by marks;";
		try {
		PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
		ResultSet resultSet = preparedStatement.executeQuery();
		System.out.println("*****All Students Records*****");
		while(resultSet.next()) {
			System.out.println("ID:"+resultSet.getInt(1)+" | Name:"+resultSet.getString(2)+
								" | Marks:"+resultSet.getInt(3)+" | Grade:"+resultSet.getString(4));
		}
		System.out.println();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
	static void getRecord(int id) {
		String insertQuery = "select * from tbl_studentrecord where student_id=?;";
		try {
		PreparedStatement preparedStatement = connection.prepareStatement(insertQuery);
		preparedStatement.setInt(1, id);
		ResultSet resultSet = preparedStatement.executeQuery();
		while(resultSet.next()) {
			System.out.println("ID:"+resultSet.getInt(1)+" | Name:"+resultSet.getString(2)+
								" | Marks:"+resultSet.getInt(3)+" | Grade:"+resultSet.getString(4));
		}
		System.out.println();
		}catch(SQLException e) {
			e.printStackTrace();
		}
	}
}
