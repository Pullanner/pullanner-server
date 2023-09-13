package com.pullanner.domain.user.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserLevelPolicy {

    LEVEL_ONE(1, 99),
    LEVEL_TWO(2, 299),
    LEVEL_THREE(3, 699),
    LEVEL_FOUR(4, 1499),
    LEVEL_FIVE(5, Integer.MAX_VALUE);

    private final int level;
    private final int maxExperiencePoint;
}
