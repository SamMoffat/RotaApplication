package com.sammoffat.servlets;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.springframework.web.servlet.mvc.method.RequestMappingInfoHandlerMethodMappingNamingStrategy;
import org.springframework.web.servlet.mvc.method.annotation.CallableMethodReturnValueHandler;

import com.google.gson.Gson;
import com.sammoffat.gson.Day;
import com.sammoffat.gson.Shift;

import java.sql.Connection;

@Path("table/GetWeek/")
public class GetWeek extends DatabaseConnect {
	
	@PostConstruct
	protected void initialise() {
		super.initialise();
	}
	
	public enum ShiftType {
		M, A, E 
	}
	
	@GET
	public String getRequest(@QueryParam("dt") String dt, @Context UriInfo uri) {
		Connection conn = null;
		Date lsMon = new Date(0);
		Date weMon = new Date(0);
		java.util.Date sdfp = new Date(0);
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			sdfp = sdf.parse(dt);
			lsMon = getLsMon(new Date(sdfp.getTime()));
			weMon = getWeMon(lsMon);
		}
		catch (ParseException e) {
		}
		
		try {
			conn = getConnection();
			String sql = "SELECT shifts.date, employees.name, shiftlists.shift_type FROM employees, shifts, shiftlists "+ 
						 "WHERE (shifts.day_shift_id=shiftlists.shiftlist_id OR shifts.afternoon_shift_id=shiftlists.shiftlist_id OR shifts.evening_shift_id=shiftlists.shiftlist_id) "+ 
						 "AND (on_shift1_id=employees.employee_id OR on_shift2_id=employees.employee_id OR on_shift3_id=employees.employee_id) "+ 
						 "AND (shifts.date BETWEEN '"+ lsMon +"' AND '"+ weMon +"') "+
						 "ORDER BY shifts.date, shift_type ASC;";
	    	ResultSet rs;
	    	Statement st = conn.createStatement();
	    	rs = st.executeQuery(sql);
	    	
	    	Date curDate = null;
	    	ShiftType curShift=null;
	    	List<Day> days = new ArrayList<Day>();
	    	Day day = new Day();
	    	Shift shift = new Shift();
	    	while (rs.next()) { //moves to correct new day/shift if requires, states date when new day is first used, states
	    						//shift type when shift first used inserts employee after this has been done.
	    		if (curShift == null) {
	    			curShift = setEnum(rs.getString("shift_type"));
	    			shift.setType(curShift.toString());
	    		} else if ((!curShift.toString().equals(rs.getString("shift_type"))) || rs.isLast()) {
	    			day.getShift().add(shift);
	    			shift= new Shift();
	    			shift.setType(rs.getString("shift_type"));
	    			curShift = setEnum(rs.getString("shift_type"));
	    		}
	    		if ((curDate != null && !(rs.getObject("date").toString()).equals(curDate.toString())) || rs.isLast()) {
	    			day.setDate(curDate);
	    			days.add(day);
	    			day = new Day();
	    			curDate = null;
	    		} else if (curDate == null) {
	    			curDate = new Date(((Date) rs.getObject("date")).getTime());
	    			day.setDate(curDate);
	    		}
	    		if (rs.getString("name") != null) {
	    			List<String> temp = shift.getOnShift();
	    			temp.add(rs.getString("name"));
	    			shift.setOnShift(temp);
	    		}
	    	}
	    	    	
	    	Gson gson = new Gson();
	    	Iterator<Day> daysIt = days.iterator();
	    	String retStr = "{"; 
	    	while (daysIt.hasNext()) {
	    		retStr += gson.toJson(daysIt.next(), Day.class); //JSON serialising 
	    		if (daysIt.hasNext()) {
	    			retStr += ", ";
	    		}
	    	}
	    	return retStr + "} { \"_links\": [{ \"rel\" : \"self\", \"href\": \""+uri.getPath()+"?dt="+dt+"\" },"
	    			+ "						  { \"rel\" : \"next\", \"href\" : \""+uri.getPath()+"?dt="+getWeMon(new Date(sdfp.getTime()))+"\"]}";
	    	
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			return e.getStackTrace().toString();
		}
	}

	private ShiftType setEnum(String string) {
		if 		(string.equals("M")) 	{ return ShiftType.M; }
		else if (string.equals("A")) 	{ return ShiftType.A; }
		else if (string.equals("E"))	{ return ShiftType.E; } 
		else 							{ return null; 		  }
	}

	private Date getWeMon(Date dt) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        calendar.add(Calendar.DAY_OF_YEAR, 7);
		return new Date(calendar.getTimeInMillis());
	}

	private Date getLsMon(Date dt) {
		Calendar calendar = Calendar.getInstance();
        calendar.setTime(dt);
        int dif = calendar.DAY_OF_WEEK;
        if (dif != 1) {
        	calendar.add(Calendar.DAY_OF_YEAR, -1 * (dif - 1) );
        }

		return new Date(calendar.getTimeInMillis());
   
	}
}
