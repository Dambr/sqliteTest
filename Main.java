package sqliteExample.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.sqlite.SQLiteDataSource;

public class Main {

	public static void main(String[] args) {
		SQLiteDataSource ds = new SQLiteDataSource();
		ds.setUrl("jdbc:sqlite:/Users/dambr/Downloads/sqlite-tools-osx-x86-3370200/users.db");
		try {
			Connection connection = ds.getConnection();
			System.out.println("Connected");
			
			createTable(connection);
			insertValues(connection);
			select(connection);
			dropTable(connection);
			
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private static void createTable(Connection connection) throws SQLException {
		String query = """
				CREATE TABLE users (
				id INTEGER PRIMARY KEY AUTOINCREMENT,
				name VARCHAR(50),
				phone TEXT);
				""";
		Statement statement = connection.createStatement();
		statement.execute(query);
		statement.close();
	}
	
	private static void insertValues(Connection connection) throws SQLException {
		String query1 = "INSERT INTO users (name, phone) VALUES ('Mary', '8-777-777-77-77');";
		String query2 = "INSERT INTO users (name, phone) VALUES ('Ann', '8-999-999-99-99');";
		String query3 = "INSERT INTO users (name, phone) VALUES ('Deny', '8-888-888-88-88');";
		Statement statement = connection.createStatement();
		for (String query : List.of(query1, query2, query3)) {
			statement.execute(query);
		}
		statement.close();
	}
	
	private static void select(Connection connection) throws SQLException {
		String query = "SELECT * FROM users;";
		Statement statement = connection.createStatement();
		
		ResultSet result = statement.executeQuery(query);
		ResultSetMetaData metaData = result.getMetaData();
		
		List<String> columnLabels = new ArrayList<>();
		for (int i = 1; i <= metaData.getColumnCount(); i++) {
			String columnLabel = metaData.getColumnName(i);
			columnLabels.add(columnLabel);
		}
		
		System.out.println(columnLabels);
		
		while (result.next()) {
			List<String> values = new ArrayList<>(); 
			for (String columnLabel : columnLabels) {
				String value = result.getString(columnLabel);
				values.add(value);
			}
			System.out.println(values);
		}
	}

	private static void dropTable(Connection connection) throws SQLException {
		String query = "DROP TABLE users;";
		Statement statement = connection.createStatement();
		statement.execute(query);
		statement.close();
	}
	
}
