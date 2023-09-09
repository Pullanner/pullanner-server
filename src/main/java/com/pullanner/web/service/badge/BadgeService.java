package com.pullanner.web.service.badge;

import com.pullanner.domain.badge.Badge;
import com.pullanner.domain.badge.BadgeIdEnum;
import com.pullanner.domain.badge.BadgeRepository;
import com.pullanner.domain.user.User;
import com.pullanner.domain.user.UserBadge;
import com.pullanner.domain.user.UserBadgeRepository;
import com.pullanner.exception.badge.BadgeNotFoundedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.pullanner.domain.badge.BadgeIdEnum.*;

@RequiredArgsConstructor
@Service
public class BadgeService {

    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    public void saveFirstPlanBadge(User user) {
        Badge firstPlanBadge = getBadgeByBadgeIdEnum(BADGE_FIRST_PLAN);

        UserBadge userBadge = createUserBadge(user, firstPlanBadge);

        userBadgeRepository.save(userBadge);
    }

    public void saveOneHundredPlanBadge(User user) {
        Badge oneHundredPlanBadge = getBadgeByBadgeIdEnum(BADGE_ONE_HUNDRED_PLAN);

        UserBadge userBadge = createUserBadge(user, oneHundredPlanBadge);

        userBadgeRepository.save(userBadge);
    }

    private static UserBadge createUserBadge(User user, Badge firstPlanBadge) {
        UserBadge userBadge = UserBadge.builder()
                .user(user)
                .badge(firstPlanBadge)
                .build();

        user.addUserBadge(userBadge);
        firstPlanBadge.addUserBadge(userBadge);

        return userBadge;
    }

    private Badge getBadgeByBadgeIdEnum(BadgeIdEnum badgeIdEnum) {
        return badgeRepository.findById(badgeIdEnum.getId())
                .orElseThrow(BadgeNotFoundedException::new);
    }
}
