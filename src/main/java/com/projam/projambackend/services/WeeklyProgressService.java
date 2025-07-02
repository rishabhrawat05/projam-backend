package com.projam.projambackend.services;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import com.projam.projambackend.dto.WeeklyProgressResponse;
import com.projam.projambackend.exceptions.MemberNotFoundException;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.models.Member;
import com.projam.projambackend.models.Project;
import com.projam.projambackend.models.WeeklyProgress;
import com.projam.projambackend.repositories.MemberRepository;
import com.projam.projambackend.repositories.ProjectRepository;
import com.projam.projambackend.repositories.TaskRepository;
import com.projam.projambackend.repositories.WeeklyProgressRepository;

import jakarta.transaction.Transactional;

@Service
public class WeeklyProgressService {

	private final WeeklyProgressRepository weeklyProgressRepository;
	
	private final ProjectRepository projectRepository;
	
	private final MemberRepository memberRepository;
	
	private final TaskRepository taskRepository;
	
	public WeeklyProgressService(WeeklyProgressRepository weeklyProgressRepository, ProjectRepository projectRepository, MemberRepository memberRepository, TaskRepository taskRepository) {
		this.weeklyProgressRepository = weeklyProgressRepository;
		this.projectRepository = projectRepository;
		this.memberRepository = memberRepository;
		this.taskRepository = taskRepository;
	}
	
	@Scheduled(cron = "0 0 0 * * MON") 
	@Transactional
	public void generateWeeklyProgressForAll() {
	    List<Project> projects = projectRepository.findAll();

	    for (Project project : projects) {
	        List<Member> members = memberRepository.findAllByProjectId(project.getProjectId());

	        for (Member member : members) {
	            addWeeklyProgress(project.getProjectId(), member.getMemberGmail());
	        }
	    }
	}

	@Transactional
	public void addWeeklyProgress(String projectId, String email) {
		Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectNotFoundException("Project Not Found"));
		Member member = memberRepository.findByMemberGmailAndProjectId(email, projectId).orElseThrow(() -> new MemberNotFoundException("Member Not Found"));
		
		boolean isAdmin = member.getMemberRole().stream().anyMatch(role -> role.getRoleName().equals("ADMIN"));
		
		int year = LocalDate.now().getYear();
		int month =LocalDate.now().getMonthValue();
		int weekOfMonth = LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfMonth());
		WeeklyProgress weeklyProgress = new WeeklyProgress();
		weeklyProgress.setProject(project);
		weeklyProgress.setMember(member);
		weeklyProgress.setYear(year);
		weeklyProgress.setMonth(month);
		weeklyProgress.setWeekOfMonth(weekOfMonth);
		weeklyProgress.setTotalWeeksOfMonth(getTotalWeeksOfTheMonth(year, month));
		if(isAdmin) {
			weeklyProgress.setWeeklyProgessPercent(getWeeklyProgressAdmin(projectId));
		}
		else {
			weeklyProgress.setWeeklyProgessPercent(getWeeklyProgressUser(projectId, email));
		}
		weeklyProgressRepository.save(weeklyProgress);
	}
	
	public int getWeeklyProgressUser(String projectId, String email) {
	    LocalDate today = LocalDate.now();
	    LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
	    LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

	    int completedTasks = taskRepository.countByProjectIdAndEmailAndCompletedAtBetween(projectId, email, startOfWeek.atStartOfDay() ,endOfWeek.atTime(23, 59, 59));

	    int totalTasks = taskRepository.countByProjectIdAndEmailAndAssignedAtBetween(projectId, email, startOfWeek.atStartOfDay() ,endOfWeek.atTime(23, 59, 59));

	    System.out.println("Completed Tasks: " + completedTasks);
	    System.out.println("Total Tasks: " + totalTasks);
	    
	    if (totalTasks == 0) return 0;

	    return (int) Math.round((completedTasks * 100.0) / totalTasks);
	}
	
	public int getWeeklyProgressAdmin(String projectId) {
	    LocalDate today = LocalDate.now();
	    LocalDate startOfWeek = today.with(DayOfWeek.MONDAY);
	    LocalDate endOfWeek = today.with(DayOfWeek.SUNDAY);

	    int completedTasks = taskRepository.countByProjectIdAndCompletedAtBetween(projectId, startOfWeek.atStartOfDay() ,endOfWeek.atTime(23, 59, 59));

	    int totalTasks = taskRepository.countByProjectIdAndAssignedAtBetween(projectId, startOfWeek.atStartOfDay() ,endOfWeek.atTime(23, 59, 59));

	    if (totalTasks == 0) return 0;

	    return (int) Math.round((completedTasks * 100.0) / totalTasks);
	}
	
	@Transactional
	public List<WeeklyProgressResponse> getWeeklyProgress(String projectId, String email) {
		Member member = memberRepository.findByMemberGmailAndProjectId(email, projectId).orElseThrow(() -> new MemberNotFoundException("Member Not Found"));
		boolean isAdmin = member.getMemberRole().stream().anyMatch(role -> role.getRoleName().equals("ADMIN"));
		if(isAdmin) {
			return weeklyProgressRepository.findByProjectId(projectId);
		}
		else {
			return weeklyProgressRepository.findByProjectIdAndEmail(projectId, email);
		}
	}
	
	public int getTotalWeeksOfTheMonth(int year, int month) {
		
		LocalDate firstDay = LocalDate.of(year, month, 1);
		
		LocalDate lastDay = LocalDate.of(year, month, firstDay.lengthOfMonth());
		
		WeekFields weekFields = WeekFields.of(Locale.getDefault());
		
		int lastWeek = lastDay.get(weekFields.weekOfMonth());
		
		return lastWeek;
	}

	
}
