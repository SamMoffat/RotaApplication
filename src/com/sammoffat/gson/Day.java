package com.sammoffat.gson;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Day {

@SerializedName("date")  @Expose private Date date;
@SerializedName("shift") @Expose private List<Shift> shift = new ArrayList<Shift>();

	public String getDate() {
		return date.toString();
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public List<Shift> getShift() {
		return shift;
	}

	public void setShift(List<Shift> shift) {
		this.shift = shift;
	}

}