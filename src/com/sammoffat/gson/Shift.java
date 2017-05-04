package com.sammoffat.gson;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Shift {

	@SerializedName("type") @Expose private String type; 
	@SerializedName("on_shift") @Expose private List<String> onShift = new ArrayList<String>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getOnShift() {
		return onShift;
	}

	public void setOnShift(List<String> onShift) {
		this.onShift = onShift;
	}

}