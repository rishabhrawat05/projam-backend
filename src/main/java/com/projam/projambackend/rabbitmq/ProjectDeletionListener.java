package com.projam.projambackend.rabbitmq;

import java.util.ArrayList;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.projam.projambackend.config.RabbitMQConfig;
import com.projam.projambackend.exceptions.ProjectNotFoundException;
import com.projam.projambackend.models.*;
import com.projam.projambackend.repositories.JoinProjectTokenRepository;
import com.projam.projambackend.repositories.MemberRepository;
import com.projam.projambackend.repositories.MemberRoleRepository;
import com.projam.projambackend.repositories.ProjectDeletionStatusRepository;
import com.projam.projambackend.repositories.ProjectRepository;
import com.projam.projambackend.repositories.TagRepository;

import jakarta.transaction.Transactional;

@Service
public class ProjectDeletionListener {

    private final ProjectRepository projectRepository;
    private final ProjectDeletionStatusRepository projectDeletionStatusRepository;
    private final AmqpTemplate amqpTemplate;
    private final MemberRepository memberRepository;
    private final TagRepository tagRepository;
    private final MemberRoleRepository memberRoleRepository;
    private final JoinProjectTokenRepository joinProjectTokenRepository;

    public ProjectDeletionListener(ProjectRepository projectRepository,
                                   ProjectDeletionStatusRepository projectDeletionStatusRepository,
                                   AmqpTemplate amqpTemplate,
                                   TagRepository tagRepository,
                                   MemberRepository memberRepository,
                                   MemberRoleRepository memberRoleRepository,
                                   JoinProjectTokenRepository joinProjectTokenRepository) {
        this.projectRepository = projectRepository;
        this.projectDeletionStatusRepository = projectDeletionStatusRepository;
        this.amqpTemplate = amqpTemplate;
        this.memberRepository = memberRepository;
        this.tagRepository = tagRepository;
        this.memberRoleRepository = memberRoleRepository;
        this.joinProjectTokenRepository = joinProjectTokenRepository;
    }

    @Transactional
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void handleDelete(String projectId) {
        System.out.println("🚀 Received delete message for projectId: " + projectId);

        Project project = projectRepository.findById(projectId)
                .orElse(null);
        
        if (project == null) {
            System.out.println("Project already deleted or does not exist: " + projectId);
            return; 
        }


        // 1. Unlink project from Members (ManyToMany)
        for (Member member : new ArrayList<>(project.getMembers())) {
            member.getProjects().remove(project);
        }
        project.getMembers().clear();

        // 2. Handle MemberRoles — delete after unlinking from tags/members
        for (MemberRole role : new ArrayList<>(project.getMemberRoles())) {
            for (Member member : new ArrayList<>(role.getMembers())) {
                member.getMemberRoles().remove(role);
            }
            role.getMembers().clear();

            for (Tag tag : new ArrayList<>(role.getTags())) {
                tag.getMemberRole().remove(role);
            }
            role.getTags().clear();

            memberRoleRepository.delete(role);
        }

        // 3. Tags (ManyToMany) — remove from tag and delete if unused
        project.getTags().clear();

        // 4. Just clear task collection (orphanRemoval handles full cleanup)
        project.getTasks().clear();

        // 5. Just clear taskColumns (orphanRemoval handles cleanup)
        project.getTaskColumns().clear();

        // 6. Just clear JoinProjectRequest
        project.getJoinRequests().clear();

        // 7. Just clear JoinProjectTokens
        project.getJoinToken().clear();

        // 8. Just clear WeeklyProgress
        project.getWeeklyProgress().clear();

        // 9. Just clear ChatMessages
        project.getChatMessage().clear();

        // 10. Just clear Activities
        project.getActivities().clear();

        // 11. Just clear GitHubAutomations
        project.getEdges().clear();
        
        project.getChatMessage().clear();

        // 12. Unlink Workspace
        if (project.getWorkspace() != null) {
            project.getWorkspace().getProjects().remove(project);
            project.setWorkspace(null);
        }

        // 13. Unlink GitHubInstallation
        project.setGithubInstallation(null);

        // Optional: flush before delete (if needed for FK cleanup)
        projectRepository.flush();

        // 🔚 Delete Project
        projectRepository.delete(project);

        System.out.println("Project deletion completed for: " + projectId);
    }
}
