package com.projam.projambackend.models;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "weekly_progress")
public class WeeklyProgress {

	@Id
	private String weeklyProgressId;
	
	
	@Column(name = "year")
	private int year;
	
	@ManyToOne
	@JoinColumn(name = "project_id")
	private Project project;
	
	@Column(name = "weekly_progress_percent")
	private int weeklyProgressPercent;
	
	@ManyToOne
	@JoinColumn(name = "member_id")
	private Member member;
	
	@Column(name = "month")
	private int month;
	
	@Column(name = "week_of_month")
	private int weekOfMonth;
	
	@Column(name = "total_weeks_of_month")
	private int totalWeeksOfMonth;

	public String getWeeklyProgressId() {
		return weeklyProgressId;
	}

	public void setWeeklyProgressId(String weeklyProgressId) {
		this.weeklyProgressId = weeklyProgressId;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public int getWeeklyProgessPercent() {
		return weeklyProgressPercent;
	}

	public void setWeeklyProgessPercent(int weeklyProgressPercent) {
		this.weeklyProgressPercent = weeklyProgressPercent;
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
	
	public WeeklyProgress() {
		this.weeklyProgressId = NanoIdUtils.randomNanoId();
	}
	
}
