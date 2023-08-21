package com.pullanner.web.service.user;

import com.pullanner.domain.user.User;
import com.pullanner.domain.user.UserRepository;
import com.pullanner.domain.user.UserWorkout;
import com.pullanner.domain.user.UserWorkoutRepository;
import com.pullanner.domain.workout.Workout;
import com.pullanner.domain.workout.WorkoutRepository;
import com.pullanner.web.controller.user.dto.UserWorkoutSaveRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserWorkoutService {

    private final UserRepository userRepository;
    private final WorkoutRepository workoutRepository;
    private final UserWorkoutRepository userWorkoutRepository;

    @Transactional
    public void save(Long userId, UserWorkoutSaveRequest userWorkoutInfo) {
        User user = getUserById(userId);
        List<Workout> workouts = workoutRepository.findAllByIdIn(userWorkoutInfo.getWorkouts());

        List<UserWorkout> userWorkouts = workouts.stream()
                .map(workout -> {
                    UserWorkout userWorkout = UserWorkout.builder()
                            .user(user)
                            .workout(workout)
                            .build();

                    user.addUserWorkout(userWorkout);
                    workout.addUserWorkout(userWorkout);

                    return userWorkout;
                })
                .toList();

        userWorkoutRepository.saveAll(userWorkouts);
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new IllegalStateException("식별 번호가 " + id + "에 해당되는 사용자가 없습니다.")
        );
    }
}
