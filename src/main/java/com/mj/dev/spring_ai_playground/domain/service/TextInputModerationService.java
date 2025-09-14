package com.mj.dev.spring_ai_playground.domain.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.moderation.*;
import org.springframework.ai.openai.OpenAiModerationModel;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class TextInputModerationService {

    private final OpenAiModerationModel openAiModerationModel;

    public String moderateInput(final String input) {

        final ModerationPrompt moderationPrompt = new ModerationPrompt(input);
        final ModerationResponse moderationResponse = this.openAiModerationModel.call(moderationPrompt);
        final Moderation moderation = moderationResponse.getResult().getOutput();

        return moderation.getResults().stream().map(this::buildModerationResponse).collect(Collectors.joining("\n"));
    }

    private String buildModerationResponse(final ModerationResult moderationResult) {

        Categories categories = moderationResult.getCategories();

        String violations = Stream.of(
                        Map.entry("Sexual", categories.isSexual()),
                        Map.entry("Hate", categories.isHate()),
                        Map.entry("Harassment", categories.isHarassment()),
                        Map.entry("Self-Harm", categories.isSelfHarm()),
                        Map.entry("Sexual/Minors", categories.isSexualMinors()),
                        Map.entry("Hate/Threatening", categories.isHateThreatening()),
                        Map.entry("Violence/Graphic", categories.isViolenceGraphic()),
                        Map.entry("Self-Harm/Intent", categories.isSelfHarmIntent()),
                        Map.entry("Self-Harm/Instructions", categories.isSelfHarmInstructions()),
                        Map.entry("Harassment/Threatening", categories.isHarassmentThreatening()),
                        Map.entry("Violence", categories.isViolence()))
                .filter(entry -> Boolean.TRUE.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .collect(Collectors.joining(", "));

        return violations.isEmpty()
                ? "No category violations detected."
                : "Violated categories: " + violations;
    }
}
