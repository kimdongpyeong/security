package kr.supporti.common.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Component
@ConfigurationProperties(prefix = "supporti")
@Getter
@Setter
@ToString
public class SupportiProperty {

    private Jwt jwt;

    @Getter
    @Setter
    @ToString
    public static class Jwt {

        private String secretKey;

        private String subject;

        private Long expirationTimeMillis;

    }
}