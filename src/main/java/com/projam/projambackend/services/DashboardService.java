package com.projam.projambackend.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.projam.projambackend.dto.DashboardResponse;
import com.projam.projambackend.dto.TeamStats;
import com.projam.projambackend.exceptions.MemberNotFoundException;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.models.Member;
import com.projam.projambackend.models.MemberRole;
import com.projam.projambackend.models.Project;
import com.projam.projambackend.repositories.ActivityRepository;
import com.projam.projambackend.repositories.MemberRepository;
import com.projam.projambackend.repositories.ProjectRepository;
import com.projam.projambackend.repositories.TaskRepository;


import jakarta.transaction.Transactional;

@Service
public class DashboardService {


	private final ActivityRepository activityRepository;

	private final TaskRepository taskRepository;

	private final ProjectRepository projectRepository;
	
	private final MemberRepository memberRepository;

	public DashboardService(ActivityRepository activityRepository, TaskRepository taskRepository, ProjectRepository projectRepository, MemberRepository memberRepository) {
		this.activityRepository = activityRepository;
		this.taskRepository = taskRepository;
		this.projectRepository = projectRepository;
		this.memberRepository = memberRepository;
	}

	@Transactional
	public DashboardResponse getDashboard(String projectId, String email) {
		Member member = memberRepository.findByMemberGmailAndProjectId(email, projectId).orElseThrow(() -> new MemberNotFoundException("Member Not Found"));

		boolean isAdmin = member.getMemberRoles().stream().anyMatch(role -> role.getRoleName().equals("ADMIN"));

		DashboardResponse response = buildCommonDashboard(projectId, email);
		
		Project project = projectRepository.findById(projectId)
			    .orElseThrow(() -> new ProjectNotFoundException("Project not found"));

		LocalDateTime startOfDay = LocalDate.now().atStartOfDay();
		LocalDateTime endOfDay = startOfDay.plusDays(1);
		
		if (isAdmin) {
			TeamStats teamStats = new TeamStats();
			teamStats.setTotalMembers(projectRepository.countTotalMembersByProjectId(projectId));
			teamStats.setActiveMembers(taskRepository.countActiveMembersByProjectIdAndToday(projectId, startOfDay, endOfDay));
			teamStats.setActiveMembersList(taskRepository.getActiveMembersByToday(projectId, startOfDay, endOfDay));
			response.setTeamStats(teamStats);
			response.setRecentTasks(taskRepository.getTaskByProjectAndAdmin(projectId, email));
			response.setTaskStatusBreakdown(getTaskStatusBreakdown(projectId));
			response.setTotalTask(taskRepository.countByProject(project));
			response.setProjectProgress(calculateProjectProgressAdmin(projectId));

		} else {
			response.setRecentTasks(taskRepository.getTaskByProjectAndUser(projectId, email));
			response.setTaskStatusBreakdown(getTaskStatusBreakdownUser(projectId, email));
			response.setProjectProgress(calculateProjectProgressUser(projectId, email));
			response.setTotalTask(taskRepository.countByProjectAndAssignedTo_memberGmail(project, email));
		}

		response.setUserRoles(member.getMemberRoles().stream().map(MemberRole::getRoleName).collect(Collectors.toList()));

		return response;
	}

	public DashboardResponse buildCommonDashboard(String projectId, String email) {
		DashboardResponse response = new DashboardResponse();
		Project project = projectRepository.findById(projectId)
			    .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
		response.setActivity(activityRepository.findAllByProject_ProjectId(projectId).stream().map(i -> i)
				.collect(Collectors.toList()));
		return response;

	}

	public Integer calculateProjectProgressUser(String projectId, String email) {
		Project project = projectRepository.findById(projectId)
			    .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
		int totalTasks = taskRepository.countByProjectAndAssignedTo_memberGmail(project, email);

		if (totalTasks == 0) {
			return 0;
		}

		int completedTasks = taskRepository.countCompletedTasksByProjectAndUser(projectId, email);

		Integer projectProgress = (completedTasks * 100) / totalTasks;
		
		
		return projectProgress;
	}
	
	public Integer calculateProjectProgressAdmin(String projectId) {
		Project project = projectRepository.findById(projectId)
			    .orElseThrow(() -> new ProjectNotFoundException("Project not found"));
		int totalTasks = taskRepository.countByProject(project);

		if (totalTasks == 0) {
			return 0;
		}

		int completedTasks = taskRepository.countByProject_ProjectIdAndStatus(projectId, "COMPLETED");

		Integer projectProgress = (completedTasks * 100) / totalTasks;
		
		
		return projectProgress;
	}

	public List<Map<String, Object>> getTaskStatusBreakdown(String projectId) {
	    List<Map<String, Object>> breakdown = new ArrayList<>();
	    List<Object[]> statusCounts = taskRepository.getStatusCountsByProject(projectId);

	    for (Object[] row : statusCounts) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("status", row[0]);
	        map.put("color", row[1]);
	        map.put("count", ((Long) row[2]).intValue());
	        breakdown.add(map);
	    }
	    return breakdown;
	}

	public List<Map<String, Object>> getTaskStatusBreakdownUser(String projectId, String email) {
	    List<Map<String, Object>> breakdown = new ArrayList<>();
	    List<Object[]> statusCounts = taskRepository.getStatusCountsByProjectAndUser(projectId, email);

	    for (Object[] row : statusCounts) {
	        Map<String, Object> map = new HashMap<>();
	        map.put("status", row[0]);
	        map.put("color", row[1]);
	        map.put("count", ((Long) row[2]).intValue());
	        breakdown.add(map);
	    }
	    return breakdown;
	}


}
