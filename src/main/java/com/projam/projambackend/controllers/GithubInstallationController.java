package com.projam.projambackend.controllers;

import java.security.Principal;
import java.util.Base64;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.servlet.view.RedirectView;

import com.projam.projambackend.dto.GithubLinkRequest;
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

    	String redirectUrl = "http://localhost:5173/github/callback?installation_id=" + installationId + "&state=" + state;
        return new RedirectView(redirectUrl);
    }
    
    @PutMapping("/link-installation")
    public ResponseEntity<?> linkInstallation(@RequestBody GithubLinkRequest request, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).body("Not logged in");
        }

        String userEmail = principal.getName(); 
        try {
			githubInstallationService.saveInstallation(request.getInstallationId(), request.getWorkspaceId(), request.getProjectId(), userEmail);
		} catch (Exception e) {
			e.printStackTrace();
		}

        return ResponseEntity.ok("GitHub installation linked successfully");
    }


    @GetMapping("/repos")
    public ResponseEntity<?> getAuthorizedRepos(@RequestParam String workspaceId) throws Exception {
        return ResponseEntity.ok(githubInstallationService.getAuthorizedRepos(workspaceId));
    }


    @PostMapping("/connect-repo")
    public ResponseEntity<?> connectRepoToProject(
            @RequestParam String projectId,
            @RequestParam String repoName,
            @RequestParam String repoOwner
    ) {
        githubInstallationService.connectRepoToProject(projectId, repoName, repoOwner);
        return ResponseEntity.ok("Repo connected to project successfully");
    }

    @GetMapping("/is-connected")
    public ResponseEntity<?> isProjectConnected(@RequestParam String projectId) {
        return ResponseEntity.ok(githubInstallationService.isProjectConnected(projectId));
    }
    
    @PutMapping("/disconnect-repo")
    public ResponseEntity<?> disconnectRepo(@RequestParam String projectId, Principal principal) {
    	System.out.println(principal.getName());
        githubInstallationService.disconnectRepoFromProject(projectId, principal.getName());
        return ResponseEntity.ok().build();
    }


}
