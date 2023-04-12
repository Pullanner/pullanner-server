package com.pullanner.global;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ApiResponseMessage(
    @JsonProperty("code") String code,
    @JsonProperty("message") String message
) {

}
