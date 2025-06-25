package com.projam.projambackend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projam.projambackend.services.GithubWebhookService;

@RestController
@RequestMapping("/projam/github")
public class GithubWebhookController {
	
	private final GithubWebhookService githubWebhookService;
	
	public GithubWebhookController(GithubWebhookService githubWebhookService) {
		this.githubWebhookService = githubWebhookService;
	}

    @PostMapping("/webhook")
    public ResponseEntity<String> handleGithubWebhook(@RequestBody String payload,
                                                      @RequestHeader(value = "X-GitHub-Event", required = false) String eventType,
                                                      @RequestHeader(value = "X-Hub-Signature-256", required = false) String signature) {
        System.out.println("Received event: " + eventType);
        System.out.println("Payload: " + payload);

        githubWebhookService.processWebhookEvent(eventType, payload);

        return ResponseEntity.ok("Received");
    }
}
