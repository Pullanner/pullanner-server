package com.pullanner.domain.plan;

import com.pullanner.domain.BaseTimeEntity;
import com.pullanner.domain.workout.Workout;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "plan_workout")
@Entity
public class PlanWorkout extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_workout_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_workout_plan_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Plan plan;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_workout_workout_id", nullable = false)
    private Workout workout;

    @Column(nullable = false)
    private int countPerSet;

    @Column(nullable = false)
    private int setCount;

    @Column(nullable = false)
    private Boolean done;

    @Builder
    public PlanWorkout(Plan plan, Workout workout, int countPerSet, int set, boolean done) {
        this.plan = plan;
        this.workout = workout;
        this.countPerSet = countPerSet;
        this.setCount = set;
        this.done = done;
    }

    public void updateStatus(boolean done) {
        this.done = done;
    }

    public int getStepOfWorkout() {
        return workout.getId();
    }
}
