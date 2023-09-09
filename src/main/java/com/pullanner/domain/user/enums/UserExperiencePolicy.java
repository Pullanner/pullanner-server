package com.pullanner.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserExperiencePolicy {

    EXPERIENCE_JOIN(10),
    EXPERIENCE_PLAN_COMPLETED(2),
    EXPERIENCE_BADGE_ALL_ROUNDER(80),
    EXPERIENCE_BADGE_MUSCLE_KING(30),
    EXPERIENCE_BADGE_EXPERIENCE_KING(30),
    EXPERIENCE_BADGE_PULL_UP_KING(100),
    EXPERIENCE_BADGE_FIRST_PLAN(10),
    EXPERIENCE_BADGE_ONE_HUNDRED_PLAN(100),
    EXPERIENCE_BADGE_SEVEN_SEQUENCE_PLAN_COMPLETED(70),
    EXPERIENCE_BADGE_COMMUNICATION_KING(10),
    EXPERIENCE_BADGE_REACTION_KING(10);

    private final int point;
}
