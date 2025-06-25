package com.projam.projambackend.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import com.projam.projambackend.services.GithubInstallationService;

@RestController
@RequestMapping("/projam/github")
public class GithubInstallationController {


    private final GithubInstallationService githubInstallationService;

    public GithubInstallationController(GithubInstallationService githubInstallationService) {
        this.githubInstallationService = githubInstallationService;
    }

    @GetMapping("/callback")
    public RedirectView githubCallback(@RequestParam("installation_id") String installationId, @RequestParam("state") String state) throws Exception {
        

        String[] parts = state.split("_");
        Long projectId = Long.parseLong(parts[0]);
        Long workspaceId = Long.parseLong(parts[1]);
        
        githubInstallationService.saveInstallation(installationId, workspaceId);

        String redirectUrl = "http://localhost:5173/workspace/" + workspaceId + "/project/" + projectId + "/github";
        return new RedirectView(redirectUrl);
    }

    @GetMapping("/repos")
    public ResponseEntity<?> getAuthorizedRepos(@RequestParam Long workspaceId) throws Exception {
        return ResponseEntity.ok(githubInstallationService.getAuthorizedRepos(workspaceId));
    }


    @PostMapping("/connect-repo")
    public ResponseEntity<?> connectRepoToProject(
            @RequestParam Long projectId,
            @RequestParam String repoName,
            @RequestParam String repoOwner
    ) {
        githubInstallationService.connectRepoToProject(projectId, repoName, repoOwner);
        return ResponseEntity.ok("Repo connected to project successfully");
    }

    @GetMapping("/is-connected")
    public ResponseEntity<?> isProjectConnected(@RequestParam Long projectId) {
        return ResponseEntity.ok(githubInstallationService.isProjectConnected(projectId));
    }

}
