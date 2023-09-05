package com.pullanner.domain.user;

import com.pullanner.domain.article.Article;
import com.pullanner.domain.BaseTimeEntity;
import com.pullanner.domain.plan.Plan;
import com.pullanner.domain.user.enums.UserExperiencePolicy;
import com.pullanner.domain.user.enums.UserLevelPolicy;
import com.pullanner.domain.user.enums.UserRole;
import com.pullanner.exception.user.NotSupportedLevelException;
import com.pullanner.web.controller.oauth2.dto.OAuth2Provider;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "UserWithUserWorkouts",
                attributeNodes = {
                        @NamedAttributeNode(value = "userWorkouts", subgraph = "userWorkouts")
                }
        )
})
@Table(name = "`user`", indexes = {
        @Index(name = "index_email_provider", columnList = "email, provider"),
        @Index(name = "index_nickname", columnList = "nickname", unique = true)
})
@Entity
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 15, name = "nickname")
    private String nickName;

    @Column(nullable = false)
    private String email;

    @Column(length = 500, name = "profile_image_url")
    private String profileImageUrl;

    private String profileImageFileName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OAuth2Provider provider;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole userRole;

    @Column(nullable = false)
    private Integer experiencePoint;

    @OneToMany(mappedBy = "writer")
    private List<Plan> plans = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserWorkout> userWorkouts = new ArrayList<>();

    @OneToMany(mappedBy = "author")
    private List<Article> articles = new ArrayList<>();

    @Builder
    public User(String name, String nickName, String email, String profileImageUrl,
                OAuth2Provider provider, UserRole userRole, Integer experiencePoint) {
        this.name = name;
        this.nickName = nickName;
        this.email = email;
        this.profileImageUrl = profileImageUrl;
        this.provider = provider;
        this.userRole = userRole;
        this.experiencePoint = experiencePoint;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public boolean hasProfileImageFileName() {
        return profileImageFileName != null;
    }

    public void updateProfileImage(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
        this.profileImageFileName = profileImageUrl.substring(profileImageUrl.lastIndexOf("/") + 1);
    }

    public String getRoleKey() {
        return userRole.getKey();
    }

    public void addPlan(Plan plan) {
        plans.add(plan);
    }

    public void addUserWorkout(UserWorkout userWorkout) {
        userWorkouts.add(userWorkout);
    }

    public void addArticle(Article article) {
        articles.add(article);
    }

    public List<Integer> getIdListOfPossibleWorkout() {
        return userWorkouts.stream()
                .map(UserWorkout::getIdOfWorkout)
                .collect(Collectors.toList());
    }

    public Set<Integer> getIdSetOfPossibleWorkout() {
        return userWorkouts.stream()
                .map(UserWorkout::getIdOfWorkout)
                .collect(Collectors.toSet());
    }

    public Set<Integer> getIdSetOfImpossibleWorkout() {
        Set<Integer> idsOfAllWorkouts = IntStream.rangeClosed(1, 8).boxed().collect(Collectors.toSet());
        idsOfAllWorkouts.removeAll(getIdSetOfPossibleWorkout());

        return idsOfAllWorkouts;
    }

    public void updateExperiencePoint(UserExperiencePolicy policy) {
        this.experiencePoint += policy.getPoint();
    }

    public int getLevel() {
        for (UserLevelPolicy userLevelPolicy : UserLevelPolicy.values()) {
            if (experiencePoint <= userLevelPolicy.getMaxExperiencePoint()) {
                return userLevelPolicy.getLevel();
            }
        }

        throw new NotSupportedLevelException();
    }
}
