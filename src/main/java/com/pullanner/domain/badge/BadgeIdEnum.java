package com.pullanner.domain.badge;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BadgeIdEnum {

    BADGE_ALL_ROUNDER(1),
    BADGE_MUSCLE_KING(2),
    BADGE_EXPERIENCE_KING(3),
    BADGE_PULL_UP_KING(4),
    BADGE_FIRST_PLAN(5),
    BADGE_ONE_HUNDRED_PLAN(6),
    BADGE_SEVEN_SEQUENCE_PLAN_COMPLETED(7),
    BADGE_COMMUNICATION_KING(8),
    BADGE_REACTION_KING(9);

    private final int id;
}
