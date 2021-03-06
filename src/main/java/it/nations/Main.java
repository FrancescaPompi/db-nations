package it.nations;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

	private final static String URL = "jdbc:mysql://localhost:3306/nations";
	private final static String USER = "root";
	private final static String PASSWORD = "g2jxepx7fp9hxg2";
	
	public static void main(String[] args) {
		
		try(Connection con = DriverManager.getConnection(URL, USER, PASSWORD);
				Scanner scan = new Scanner(System.in);) {
			
			System.out.print("Insert a search word: ");
			String research = scan.nextLine();
			String queryResearch = "select c.name as Country, c.country_id as Id, r.name as Region, c2.name as Continent\r\n"
					+ "from countries c \r\n"
					+ "inner join regions r on r.region_id = c.region_id \r\n"
					+ "inner join continents c2 on c2.continent_id = r.continent_id \r\n"
					+ "where c.name like ?\r\n"
					+ "order by c.name;";

			try(PreparedStatement ps = con.prepareStatement(queryResearch)) {
				ps.setString(1, "%" + research + "%");
				
				try(ResultSet rs = ps.executeQuery()) {
					System.out.println("Country - Id - Region - Continent");
					while(rs.next()) {
						System.out.print(rs.getString(1) + " - ");
						System.out.print(rs.getInt(2) + " - ");
						System.out.print(rs.getString(3) + " - ");
						System.out.println(rs.getString(4));
					}
					
				}
			}
						
			scan.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

}
