package com.sammoffat.servlets;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

@Path("/")
public class ApiUriLinks {
	
	@GET
	public String getRequest(@Context UriInfo uri) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		
		return "{\"_links\" : ["
					+ "{ \"rel\" : \"self\", \"href\" : \""+uri.getAbsolutePath()+"\" },"
					+ "{ \"rel\" : \"login\", \"href\" : \"/users/login\" },"
					+ "{ \"rel\" : \"week\", \"href\" : \"/table/GetWeek?dt="+dateFormat.format(cal.getTime())+"\" },"
					+ "{ \"rel\" : \"createday\", \"href\" : \"table/CreateDay/\" },"
					+ "{ \"rel\" : \"createshift\", \"href\" : \"table/CreateShift/\" }"
				+ "]}";
	}
	
}
