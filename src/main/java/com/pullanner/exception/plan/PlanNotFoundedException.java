package com.pullanner.exception.plan;

import com.pullanner.web.ApiResponseCode;

public class PlanNotFoundedException extends RuntimeException {

    public PlanNotFoundedException(Long planId) {
        super("식별 번호가 " + planId + "에 해당되는 계획이 없습니다.");
    }
}
