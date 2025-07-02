package com.projam.projambackend.dto;

public class WeeklyProgressResponse {

	private int month;  
	
	private int weekOfMonth;
	
	private int year;
	
	private int weeklyProgressPercent;
	
	private int totalWeeksOfMonth;


	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getWeeklyProgressPercent() {
		return weeklyProgressPercent;
	}

	public void setWeeklyProgressPercent(int weeklyProgressPercent) {
		this.weeklyProgressPercent = weeklyProgressPercent;
	}

	
	
	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getWeekOfMonth() {
		return weekOfMonth;
	}

	public void setWeekOfMonth(int weekOfMonth) {
		this.weekOfMonth = weekOfMonth;
	}

	
	
	public int getTotalWeeksOfMonth() {
		return totalWeeksOfMonth;
	}

	public void setTotalWeeksOfMonth(int totalWeeksOfMonth) {
		this.totalWeeksOfMonth = totalWeeksOfMonth;
	}

	/**
	 * @param weekNumber
	 * @param year
	 * @param weeklyProgressPercent
	 */
	public WeeklyProgressResponse(int month, int weekOfMonth, int year, int weeklyProgressPercent, int totalWeeksOfMonth) {
		this.month = month;
		this.weekOfMonth = weekOfMonth;
		this.year = year;
		this.weeklyProgressPercent = weeklyProgressPercent;
		this.totalWeeksOfMonth = totalWeeksOfMonth;
	}
	
	
	
}
