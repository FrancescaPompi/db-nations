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
			
			String queryStats = "select max(cs.`year`) as Latest_year, population, gdp \r\n"
					+ "from country_stats cs\r\n"
					+ "where country_id = ?;";
			
			try(PreparedStatement ps = con.prepareStatement(queryStats)) {
				ps.setInt(1, countryId);
				
				try(ResultSet rs = ps.executeQuery()) {
					System.out.println("");
					System.out.println("Most recent stats");
					while(rs.next()) {
						System.out.println("Year: " + rs.getString(1));
						System.out.println("Population: " + rs.getString(2));
						System.out.println("GDP: " + rs.getString(3));
					}
				}
			}
			
			scan.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
}