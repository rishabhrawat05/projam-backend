package com.projam.projambackend.dto;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.projam.projambackend.models.Activity;
import com.projam.projambackend.models.Task;

public class DashboardResponse {

    private List<Task> todayTask;

    private List<ActivityResponse> activity;

    private Integer projectProgress;

    private int totalTask;

    private List<Map<String, Object>> taskStatusBreakdown; 

    private List<TaskAssignmentSummaryDto> recentTasks;

    private List<String> userRoles; 

    private TeamStats teamStats; 

    public List<Task> getTodayTask() {
        return todayTask;
    }

    public void setTodayTask(List<Task> todayTask) {
        this.todayTask = todayTask;
    }
    
    public List<ActivityResponse> getActivity() {
		return activity;
	}

	public void setActivity(List<ActivityResponse> activity) {
		this.activity = activity;
	}

	public void setUserRoles(List<String> userRoles) {
		this.userRoles = userRoles;
	}

	public Integer getProjectProgress() {
        return projectProgress;
    }

    public void setProjectProgress(Integer projectProgress) {
        this.projectProgress = projectProgress;
    }

    public int getTotalTask() {
        return totalTask;
    }

    public void setTotalTask(int totalTask) {
        this.totalTask = totalTask;
    }

    public List<Map<String, Object>> getTaskStatusBreakdown() {
        return taskStatusBreakdown;
    }

    public void setTaskStatusBreakdown(List<Map<String, Object>> taskStatusBreakdown) {
        this.taskStatusBreakdown = taskStatusBreakdown;
    }

    public List<TaskAssignmentSummaryDto> getRecentTasks() {
        return recentTasks;
    }

    public void setRecentTasks(List<TaskAssignmentSummaryDto> recentTasks) {
        this.recentTasks = recentTasks;
    }

    public TeamStats getTeamStats() {
        return teamStats;
    }

    public void setTeamStats(TeamStats teamStats) {
        this.teamStats = teamStats;
    }
}
