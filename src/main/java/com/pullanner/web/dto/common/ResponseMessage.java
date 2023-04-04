package com.pullanner.web.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResponseMessage(
    @JsonProperty("code") String code,
    @JsonProperty("message") String message
) {

}
