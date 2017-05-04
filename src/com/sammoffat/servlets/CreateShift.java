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

@Path("table/CreateShift/")
public class CreateShift extends DatabaseConnect {

	private Connection conn = null;
	private Statement st;
	
	@PostConstruct
	protected void initialise() {
		super.initialise();
	}
	
	@POST
	public String postRequest(@QueryParam("id") String id, 
							@QueryParam("dt") String date, 
							@QueryParam("t") String shiftType, 
							@QueryParam("emp") String emp) {
		String emps[] = getEmps(emp);
		
		String idProd = doCreate(id, date, shiftType, emps);
		if (idProd != null) {
			return "{ \"data\" : \"" + idProd + "\" }";
		}
		return "{ \"data\" : \"Failure\" }" ;
	}

	public String[] getEmps(String emp) { //seperates out employee IDs
		char[] emps = emp.toCharArray();
		int i=0, j=0;
		String res[] = new String[3];
		res[0] = "";
		res[1] = "";
		res[2] = "";
		for (i=0; i<emps.length; i++) {
			if (emps[i] != 'E') {
				res[j] += emps[i];
			} else if (j != 2) {
				j++;
			}
		}
		for (i=0; i<res.length;i++) {
			if (res[i] == "nn") {
				res[i] = "NULL";
			}
		}
		return res;
	}

	public String doCreate(String id, String date, String type, String[] emp) {
		String sql = "INSERT INTO shiftlists VALUES ("+emp[0]+", "+emp[1]+", "+emp[2]+", '"+type+"', NULL)";
		int res = doSqlUpdate(sql);
		try {
			if (res == 1) {
				String sqlTest = ("SELECT shiftlists.shiftlist_id FROM shiftlists"
							 + " WHERE shiftlists.on_shift1_id = "+emp[0]
							 + " AND shiftlists.on_shift2_id = "+emp[1]
							 + " AND shiftlists.on_shift3_id = "+emp[2]
							 + " ORDER BY shiftlists.shiftlist_id DESC;");
				ResultSet rs = doSqlQuery(sqlTest);
				if (rs.next()) {
					return rs.getString("shiftlist_id");
				}
			} else {
				return null;
			}
		} catch (SQLException e) {}
		return null;
	}
	
	private ResultSet doSqlQuery(String sql) {
		try {
			conn = getConnection();
			st = conn.createStatement();
			return st.executeQuery(sql);
		} catch (SQLException e) { e.printStackTrace(); }
		return null;
	}

	private int doSqlUpdate(String sql) {
		try {
			conn = getConnection();
			st = conn.createStatement();
			return st.executeUpdate(sql);
		} catch (SQLException e) { e.printStackTrace(); }
		return 0;
	}
}
