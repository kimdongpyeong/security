package kr.supporti.common.config;

import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import kr.supporti.api.common.entity.UserEntity;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {

    @Bean
    public AuditorAware<Long> auditorAware() {
        return () -> {
            try {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder
                        .getContext().getAuthentication();
                if (usernamePasswordAuthenticationToken == null) {
                    return null;
                }
                @SuppressWarnings("unchecked")
                Map<String, Object> map = (Map<String, Object>) usernamePasswordAuthenticationToken.getDetails();
                UserEntity entity = (UserEntity) map.get("user");
                return Optional.ofNullable(entity.getId());
            } catch (ClassCastException e) {
                return null;
            }
        };
    }

}