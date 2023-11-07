package com.pullanner.domain.plan;

import com.pullanner.domain.BaseTimeEntity;
import com.pullanner.domain.user.User;
import com.pullanner.exception.plan.PlanWorkoutNotFoundedException;
import com.pullanner.web.controller.plan.dto.PlanSaveOrUpdateRequest;
import com.pullanner.web.controller.plan.dto.PlanWorkoutResponse;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "PlanWithPlanWorkouts",
                attributeNodes = {
                        @NamedAttributeNode(value = "planWorkouts", subgraph = "planWorkouts")
                }
        )
})
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

    @Column(nullable = false, length = 20)
    private String name;

    @Column(length = 300)
    private String note;

    @Column(nullable = false)
    private LocalDateTime planDate;

    @Column(nullable = false)
    private Boolean completed;

    private LocalDateTime completedDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User writer;

    @Getter
    @OneToMany(mappedBy = "plan")
    private List<PlanWorkout> planWorkouts = new ArrayList<>();

    @Builder
    private Plan(User writer, PlanType planType, String name, String note,
                LocalDateTime planDate) {
        this.writer = writer;
        this.planType = planType;
        this.name = name;
        this.note = note;
        this.planDate = planDate;
    }

    /*
        Relation methods : start
     */

    public void addPlanWorkout(PlanWorkout planWorkout) {
        planWorkouts.add(planWorkout);
    }

    /*
        Relation methods : end
     */

    public void updatePlanInformation(PlanSaveOrUpdateRequest request) {
        this.name = request.getPlanName();
        this.planType = request.getPlanType();
    }

    public List<PlanWorkoutResponse> getPlanWorkoutResponses() {
        return planWorkouts.stream()
                .map(PlanWorkoutResponse::from)
                .collect(Collectors.toList());
    }

    public int getProgress() {
        return (int) planWorkouts.stream()
                .filter(PlanWorkout::getDone)
                .count() * 100 / planWorkouts.size();
    }

    public int getMainWorkoutStep() {
        int prevCount = 0, maxWorkoutStep = 0;
        for (PlanWorkout workout : planWorkouts) {
            int totalCount = workout.getCountPerSet() * workout.getSetCount();
            if (prevCount < totalCount) {
                prevCount = totalCount;
                maxWorkoutStep = workout.getIdOfWorkout();
            }
        }

        if (maxWorkoutStep == 0) {
            throw new PlanWorkoutNotFoundedException();
        }

        return maxWorkoutStep;
    }

    public void updateNote(String note) {
        this.note = note;
    }

    public LocalDate getPlanDateValue() {
        return planDate.toLocalDate();
    }

    public boolean isCompleted() {
        return completed;
    }

    public boolean checkCompletion() {
        for (PlanWorkout planWorkout : planWorkouts) {
            if (!planWorkout.getDone()) {
                return false;
            }
        }

        return true;
    }

    public void completePlan() {
        this.completed = true;
        this.completedDate = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    public boolean isMasterPlanType() {
        return planType.equals(PlanType.MASTER);
    }

    public boolean isStrengthPlanType() {
        return planType.equals(PlanType.STRENGTH);
    }

    public boolean checkCompletionDateForOneDayBefore(LocalDateTime date) {
        return completedDate.plusDays(1).isEqual(date);
    }

    public boolean checkCompletionDateForSameDate(LocalDateTime date) {
        return completedDate.isEqual(date);
    }

    public boolean matchGivenMonth(Month givenMonth) {
        return completedDate.getMonth().equals(givenMonth);
    }

    public String getPlanCompletedTime() {
        int hour = completedDate.getHour();
        if (5 < hour && hour < 12) {
            return PlanCompletedTimeEnum.MORNING.getTime();
        } else if (11 < hour && hour < 18) {
            return PlanCompletedTimeEnum.AFTER_NOON.getTime();
        } else if (17 < hour) {
            return PlanCompletedTimeEnum.EVENING.getTime();
        } else {
            return PlanCompletedTimeEnum.NIGHT.getTime();
        }
    }
}
