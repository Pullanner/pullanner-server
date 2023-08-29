package com.pullanner.web.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PlanDateValidator implements ConstraintValidator<PlanDateValid, LocalDateTime> {

    @Override
    public void initialize(PlanDateValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDateTime planDateTime, ConstraintValidatorContext context) {
        LocalDate now = LocalDate.now();
        return planDateTime.toLocalDate().isAfter(now);
    }
}
