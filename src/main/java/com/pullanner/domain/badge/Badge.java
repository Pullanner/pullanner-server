package com.pullanner.domain.badge;

import com.pullanner.domain.BaseTimeEntity;
import com.pullanner.domain.user.UserBadge;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "badge")
@Entity
public class Badge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_id")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "user")
    private List<UserBadge> userBadges = new ArrayList<>();

    /*
           Relation methods : start
    */

    public void addUserBadge(UserBadge userBadge) {
        userBadges.add(userBadge);
    }

    /*
           Relation methods : end
    */
}
