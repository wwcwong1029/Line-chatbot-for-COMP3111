package com.example.bot.spring;

import lombok.extern.slf4j.Slf4j;
import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.*;
import java.net.URISyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;

@Slf4j
public class SQLDatabaseEngine extends DatabaseEngine {
	@Override
	String search(String text) throws Exception {
		String result = null;
		Connection connection = this.getConnection();
		PreparedStatement stmt = connection.prepareStatement("SELECT response FROM linechatbot WHERE ? LIKE CONCAT('%',keyword,'%')");
		stmt.setString(1, text.toLowerCase());
		ResultSet rs = stmt.executeQuery();
		if (rs.next())
			result = rs.getString(1);
		rs.close();
		stmt.close();
		connection.close();
		if (result != null)
			return result;
		throw new Exception("NOT FOUND");
	}
	
	private Connection getConnection() throws URISyntaxException, SQLException {

		Connection connection;
		//URI dbUri = new URI(System.getenv("DATABASE_URL"));
		URI dbUri = new URI("postgres://ouvrklftelnadt:dcea5b4b29d174d1d8a97ee3ae951b187ad7ea91c828e9cb193917ca663e4ce6@ec2-107-20-188-239.compute-1.amazonaws.com:5432/de3qvia0b54c6t");
		String username = dbUri.getUserInfo().split(":")[0];
		String password = dbUri.getUserInfo().split(":")[1];
		String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath() +  "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

		log.info("Username: {} Password: {}", username, password);
		log.info ("dbUrl: {}", dbUrl);

		connection = DriverManager.getConnection(dbUrl, username, password);

		return connection;
	}
}
