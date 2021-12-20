package it.nations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Bonus {

	private final static String URL = "jdbc:mysql://localhost:3306/nations";
	private final static String USER = "root";
	private final static String PASSWORD = "g2jxepx7fp9hxg2";
	
	public static void main(String[] args) {
		
		try(Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
				Scanner scan = new Scanner(System.in);) {
			
			
			// Bonus
			System.out.print("Insert a country id: ");
			int countryId = Integer.parseInt(scan.nextLine());
			
			String queryCountryId = "select c.name as Country\r\n"
					+ "from countries c \r\n"
					+ "where c.country_id = ?;";
			
			try(PreparedStatement ps = con.prepareStatement(queryCountryId)) {
				ps.setInt(1, countryId);
				
				try(ResultSet rs = ps.executeQuery()) {
					while(rs.next()) {
						System.out.println("Details for country: " + rs.getString(1));
					}
				}
			}
			
			String queryLanguages = "select l.`language` \r\n"
					+ "from country_languages cl \r\n"
					+ "join languages l on cl.language_id = l.language_id \r\n"
					+ "where cl.country_id = ?;";
			
			try(PreparedStatement ps = con.prepareStatement(queryLanguages)) {
				ps.setInt(1, countryId);
				
				try(ResultSet rs = ps.executeQuery()) {
					System.out.print("Languages: ");
					while(rs.next()) {
						System.out.print(rs.getString(1) + ", ");
					}
				}
			}
			
			scan.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}