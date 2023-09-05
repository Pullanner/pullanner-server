package com.pullanner.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserExperiencePolicy {

    JOIN(10),
    PLAN_COMPLETED(2),
    BADGE_ALL_ROUNDER(80),
    BADGE_MUSCLE_KING(30),
    BADGE_EXPERIENCE_KING(30),
    BADGE_PULL_UP_KING(100),
    BADGE_FIRST_PLAN(10),
    BADGE_ONE_HUNDRED_PLAN(100),
    BADGE_SEVEN_SEQUENCE_PLAN_COMPLETED(70),
    BADGE_COMMUNICATION_KING(10),
    BADGE_REACTION_KING(10);

    private final int point;
}
