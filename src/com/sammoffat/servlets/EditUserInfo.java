package com.sammoffat.servlets;

import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;

import javax.annotation.PostConstruct;

import javax.ws.rs.GET;
import javax.ws.rs.QueryParam;
import javax.ws.rs.*;

@Path("/users/{id}/edit")
public class EditUserInfo extends DatabaseConnect {

	@PostConstruct
	protected void initialise() {
		super.initialise();
	}
	
	@GET
	public String getRequest(@PathParam("id") String id) {
		Connection conn = null;
		try {
			conn = getConnection();
			String sql = "";
			ResultSet rs;
			Statement st = conn.createStatement();
			rs = st.executeQuery(sql);
		} catch (SQLException e) {e.printStackTrace();}
		
		return "";
	}
}
