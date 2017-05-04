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

@Path("table/CreateDay/")
public class CreateDay extends DatabaseConnect {

	@PostConstruct
	protected void initialise() {
		super.initialise();
	}
	
	@GET
	public String postRequest(@QueryParam("id") String id, 
								@QueryParam("dt") String date, 
								@QueryParam("m_emp") String mEmp, 
								@QueryParam("a_emp") String aEmp,
								@QueryParam("e_emp") String eEmp) {
		String idM="Failed",idA="Failed",idE="Failed",dayAdded="Failed";
		CheckUserEdit checkUser = new CheckUserEdit();
		checkUser.initialise();
		if (checkUser.getRequest(id).equals("{\"data\" : \"Success\"}")) {
			try {
				Connection conn = getConnection();
				String sql = "INSERT INTO shifts VALUES (NULL, NULL, NULL, '" + date + "');";
				Statement st = conn.createStatement();
				int pass = st.executeUpdate(sql);
				if (pass == 1) {
					CreateShift createShift = new CreateShift();
					createShift.initialise();
					idM = createShift.doCreate(id, date, "M", createShift.getEmps(mEmp));
					idA = createShift.doCreate(id, date, "A", createShift.getEmps(aEmp));
					idE = createShift.doCreate(id, date, "E", createShift.getEmps(eEmp));
					
					String sqlM="", sqlA="", sqlE="";
					if (idM != null) { sqlM = " day_shift_id = " 		+ idM + ", "; } else { sqlM = " day_shift_id = NULL," ; 		}
					if (idA != null) { sqlA = " afternoon_shift_id = "	+ idA + ", "; } else { sqlA = " afternoon_shift_id = NULL,"; }
					if (idE != null) { sqlE = " evening_shift_id = " 	+ idE + " "; } else { sqlE = " evening_shift_id = NULL "; 	}
					String sqlUpdate = "UPDATE shifts SET" + sqlM + sqlA + sqlE + 
														 "WHERE shifts.date = '" + date + "';";
					int rsAddDays = st.executeUpdate(sqlUpdate);
					if (rsAddDays == 1) { dayAdded="Success"; }
				}
			} catch (SQLException e) {e.printStackTrace();}
		}
		
		return "{\"M\" : \"" + idM + "\", \"A\" : \"" + idA + "\", \"E\" : \"" + idE +"\", \"added_day\" : \""+ dayAdded +"\" }";
	}
}
