package com.pullanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableCaching // 캐시 기능 사용
@EnableJpaAuditing // JPA Auditing 활성화
@SpringBootApplication
public class PullannerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PullannerApplication.class, args);
	}

}
