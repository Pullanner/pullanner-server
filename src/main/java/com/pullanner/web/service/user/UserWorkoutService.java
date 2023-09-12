package com.pullanner.web.service.user;

import com.pullanner.domain.user.User;
import com.pullanner.domain.user.UserRepository;
import com.pullanner.domain.user.UserWorkout;
import com.pullanner.domain.user.UserWorkoutRepository;
import com.pullanner.domain.workout.Workout;
import com.pullanner.domain.workout.WorkoutRepository;
import com.pullanner.exception.user.UserNotFoundedException;
import com.pullanner.web.controller.user.dto.UserWorkoutResponse;
import com.pullanner.web.controller.user.dto.UserWorkoutSaveOrUpdateRequest;
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

    @Transactional(readOnly = true)
    public UserWorkoutResponse findByUserId(Long userId) {
        User user = userRepository.findWithWorkoutsById(userId).orElseThrow(
                () -> new UserNotFoundedException("식별 번호가 " + userId + "에 해당되는 사용자가 없습니다.")
        );

        return UserWorkoutResponse.from(user);
    }

    @Transactional
    public void update(Long userId, UserWorkoutSaveOrUpdateRequest userWorkoutInfo) {
        User user = userRepository.findWithWorkoutsById(userId).orElseThrow(
                () -> new UserNotFoundedException("식별 번호가 " + userId + "에 해당되는 사용자가 없습니다.")
        );

        userWorkoutRepository.deleteAllInBatch(user.getUserWorkouts());

        List<UserWorkout> userWorkouts = getUserWorkouts(userWorkoutInfo, user);

        userWorkoutRepository.saveAll(userWorkouts);
    }

    private List<UserWorkout> getUserWorkouts(UserWorkoutSaveOrUpdateRequest userWorkoutInfo, User user) {
        List<Workout> workouts = workoutRepository.findAllByIdIn(userWorkoutInfo.getWorkouts());

        return workouts.stream()
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
    }
}
