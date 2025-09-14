package com.mj.dev.spring_ai_playground.web.controller;

import com.mj.dev.spring_ai_playground.domain.entity.ModerateRequest;
import com.mj.dev.spring_ai_playground.domain.service.TextInputModerationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/moderation")
@RequiredArgsConstructor
public class InputModerationRestController {

    private final TextInputModerationService textInputModerationService;

    @PostMapping
    public ResponseEntity<String> moderateUserInput(@RequestBody final ModerateRequest moderateRequest) {

        log.info("User input: {}", moderateRequest.getUserInput());

        final String moderationResult = this.textInputModerationService.moderateInput(moderateRequest.getUserInput());

        log.info("Moderation result: {}", moderationResult);
        return new ResponseEntity<>(moderationResult, HttpStatus.OK);
    }
}
