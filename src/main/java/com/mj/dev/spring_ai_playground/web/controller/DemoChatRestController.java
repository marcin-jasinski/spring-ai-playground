package com.mj.dev.spring_ai_playground.web.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoChatRestController {

    private final ChatClient chatClient;

    public DemoChatRestController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/demo-message")
    public String getDemoMessage() {

        final PromptTemplate promptTemplate = new PromptTemplate("""
                You are a funny stand-up comedian. Tell ma good joke!
                """);

        return this.chatClient.prompt(promptTemplate.create()).call().content();
    }
}
