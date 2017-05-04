package com.sammoffat.servlets;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;

import javax.annotation.PostConstruct;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.*;

@Path("/users/login")
public class LogIn extends DatabaseConnect {

	@PostConstruct
	protected void initialise() {
		super.initialise();
	}
	
	@POST
	public String postRequest(@QueryParam("user_id") String id, @QueryParam("email") String email, @QueryParam("password") String pw) {
		Connection conn = null;
		try {
			conn = getConnection();
			String sql = "SELECT employee_id FROM employees WHERE email='"+email+"' AND password='"+pw+"';";
			Statement st = conn.createStatement();
			ResultSet rs = st.executeQuery(sql);
			if (rs.next()) {
				return "{\"data\" : \""+rs.getString(0)+"\"}";
			} 
		} catch (SQLException e) {e.printStackTrace();}
		
		return "{\"data\" : \"Failure\"}";
	}
}
