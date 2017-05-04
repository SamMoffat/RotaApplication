package com.sammoffat.servlets;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;

import javax.annotation.PostConstruct;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("users/{user_id}/CheckEdit/")
public class CheckUserEdit extends DatabaseConnect {

	@PostConstruct
	protected void initialise() {
		super.initialise();
	}
	
	@GET
	public String getRequest(@PathParam("user_id") String id) {
		Connection conn = null;
		try {
			conn = getConnection();
			String sql = "SELECT employees.name FROM employees WHERE employees.db_access = 1 AND employees.employee_id = " + id + ";";
			ResultSet rs;
			Statement st = conn.createStatement();
			rs = st.executeQuery(sql);
			if (rs.next()) {
				return "{\"data\" : \"Success\"}";
			}
		} catch (SQLException e) {e.printStackTrace();}
		return "{\"data\" : \"Fail\"}";
	}

//	public String doCheck(String id) {
//		try {
//			Connection conn = getConnection();
//			String sql = "SELECT employees.name FROM employees WHERE employees.db_access = 1 AND employees.user_id = " + id + ";";
//			ResultSet rs;
//			Statement st = conn.createStatement();
//			rs = st.executeQuery(sql);
//			if (rs.next()) {
//				return "{\"data\" : \"Success\"}";
//			}
//		} catch (SQLException e) {e.printStackTrace();}
//		return "{\"data\" : \"Fail\"}";
//	}
}
