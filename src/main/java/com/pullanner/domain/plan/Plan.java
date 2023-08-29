package com.pullanner.domain.plan;

import com.pullanner.domain.BaseTimeEntity;
import com.pullanner.domain.user.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "plan")
@Entity
public class Plan extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanType planType;

    @Column(nullable = false)
    private String name;

    @Column(length = 300)
    private String note;

    @Column(nullable = false)
    private LocalDateTime planDate;

    private String mainColor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User writer;

    @OneToMany(mappedBy = "plan")
    private List<PlanWorkout> planWorkouts = new ArrayList<>();

    @Builder
    public Plan(User writer, PlanType planType, String name, String note,
                LocalDateTime planDate, String mainColor) {
        this.writer = writer;
        this.planType = planType;
        this.name = name;
        this.note = note;
        this.planDate = planDate;
        this.mainColor = mainColor;
    }

    public void addPlanWorkout(PlanWorkout planWorkout) {
        planWorkouts.add(planWorkout);
    }

}
