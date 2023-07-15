package com.pullanner.web.service.user;

import static com.pullanner.web.ApiUtil.getResponseEntity;

import com.pullanner.web.controller.user.dto.UserResponse;
import com.pullanner.web.controller.user.dto.UserUpdateRequest;
import com.pullanner.domain.user.User;
import com.pullanner.exception.user.InvalidMailAuthorizationCodeException;
import com.pullanner.domain.user.MailAuthorizationCodeRepository;
import com.pullanner.domain.user.UserRepository;
import com.pullanner.web.ApiResponseCode;
import com.pullanner.web.ApiResponseMessage;
import com.pullanner.domain.token.RefreshTokenRepository;
import java.util.concurrent.ThreadLocalRandom;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    // 회원 탈퇴 처리를 위한 메일 발송 관련 의존성 주입
    private final JavaMailSender javaMailSender;
    private final MailAuthorizationCodeRepository mailAuthorizationCodeRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        return UserResponse.from(getUserById(id));
    }

    @Transactional(readOnly = true)
    public ResponseEntity<ApiResponseMessage> validateDuplicate(String nickName) {
        if (userRepository.findByNickName(nickName).isPresent()) {
            return getResponseEntity(ApiResponseCode.USER_DUPLICATE_NICKNAME);
        } else {
            return getResponseEntity(ApiResponseCode.USER_NOT_DUPLICATE_NICKNAME);
        }
    }

    @Transactional
    public UserResponse register(Long userId, UserUpdateRequest userInfo) {
        User user = getUserById(userId);

        user.updateNickName(userInfo.getNickname());

        return UserResponse.from(user);
    }

    // 메일 전송의 경우 Transactional 선언 X (Connection 리소스 절약)
    public void sendMail(Long userId) {
        User user = getUserById(userId);
        SimpleMailMessage mailMessage = createMailMessage(user.getEmail());
        javaMailSender.send(mailMessage);
    }

    private SimpleMailMessage createMailMessage(String email) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setTo(email);
        simpleMailMessage.setSubject("[Pullanner] 회원 탈퇴 인증 코드 발송");
        simpleMailMessage.setText("안녕하세요,\n\n귀하께서 요청하신 회원 탈퇴 처리를 위한 인증 코드를 다음과 같이 안내해드립니다.\n\n인증 코드 : " + createAuthorizationCode(email));

        return simpleMailMessage;
    }

   private int createAuthorizationCode(String email) {
       int authorizationCode = ThreadLocalRandom.current().nextInt(100_000, 1_000_000);
       mailAuthorizationCodeRepository.setCodeByEmail(email, authorizationCode);
       return authorizationCode;
    }

    @Transactional
    public void deleteUser(Long userId, String refreshTokenId, Integer code) {
        User user = getUserById(userId);
        if (mailAuthorizationCodeRepository.validateCode(user.getEmail(), code)) {
            userRepository.delete(user);
            refreshTokenRepository.deleteByKey(refreshTokenId);
        } else {
            throw new InvalidMailAuthorizationCodeException("회원 탈퇴 처리를 위한 인증 번호가 일치하지 않습니다.");
        }
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> {
                    throw new IllegalStateException("식별 번호가 " + id + "에 해당되는 사용자가 없습니다.");
                });
    }
}
