package com.pullanner.docs.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.pullanner.docs.RestDocsSupport;
import com.pullanner.domain.user.controller.UserController;
import com.pullanner.domain.user.dto.UserResponseDto;
import com.pullanner.domain.user.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("User API 문서 생성을 위한 테스트")
public class UserControllerDocsTest extends RestDocsSupport {

    private final UserService userService = mock(UserService.class);

    @Override
    protected Object initController() {
        return new UserController(userService);
    }

    @DisplayName("사용자는 자신의 회원 정보를 조회할 수 있다.")
    @Test
    void getUserInfo() throws Exception {
        // given
        given(userService.findById(any(Long.class)))
            .willReturn(
                UserResponseDto.builder()
                    .userId(1L)
                    .name("조명익")
                    .nickName("ikjo")
                    .email("ikjo@naver.com")
                    .profileImage("https://ikjo-picture.com")
                    .oauthProvider("NAVER")
                    .build()
            );

        // when // then
        mockMvc.perform(
            get("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andDo(print())
            .andExpect(status().isOk());
            //.andDo(document("user-get", ))
    }
}
