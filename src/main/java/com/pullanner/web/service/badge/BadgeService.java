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
        saveBadge(user, BADGE_FIRST_PLAN);
    }

    public void saveOneHundredPlanBadge(User user) {
        saveBadge(user, BADGE_ONE_HUNDRED_PLAN);
    }

    public void saveAllRounderBadge(User user) {
        saveBadge(user, BADGE_ALL_ROUNDER);
    }

    public void saveMuscleKingBadge(User user) {
        saveBadge(user, BADGE_MUSCLE_KING);
    }

    public void saveExperienceKingBadge(User user) {
        saveBadge(user, BADGE_EXPERIENCE_KING);
    }

    public void savePullUpKingBadge(User user) {
        saveBadge(user, BADGE_PULL_UP_KING);
    }

    public void saveSevenSequencePlanCompletionBadge(User user) {
        saveBadge(user, BADGE_SEVEN_SEQUENCE_PLAN_COMPLETED);
    }

    private void saveBadge(User user, BadgeIdEnum badgeIdEnum) {
        Badge experienceKingBadge = getBadgeByBadgeIdEnum(badgeIdEnum);

        UserBadge userBadge = createUserBadge(user, experienceKingBadge);

        userBadgeRepository.save(userBadge);
    }

    private Badge getBadgeByBadgeIdEnum(BadgeIdEnum badgeIdEnum) {
        return badgeRepository.findById(badgeIdEnum.getId())
                .orElseThrow(BadgeNotFoundedException::new);
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
}
