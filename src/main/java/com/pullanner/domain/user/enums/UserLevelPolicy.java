package com.pullanner.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserLevelPolicy {

    LEVEL_ONE(99),
    LEVEL_TWO(299),
    LEVEL_THREE(699),
    LEVEL_FOUR(1499);

    private final int maxExperiencePoint;
}
