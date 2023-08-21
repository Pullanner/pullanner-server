package com.pullanner.domain.workout;

import com.pullanner.domain.user.UserWorkout;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "workout")
@Entity
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "workout_id")
    private Integer id;

    private String name;

    @OneToMany(mappedBy = "workout")
    private List<UserWorkout> userWorkouts = new ArrayList<>();
}
