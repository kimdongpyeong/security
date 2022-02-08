package kr.supporti.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.service.UserService;

@Configuration
public class UserDetailsServiceConfig {

    @Bean
    public UserDetailsService userDetailsService(UserService userService) {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                UserEntity user = userService.getUserByUsername(username);
                if (user != null) {
                    String password = user.getPassword();
                    return new User(username, password, AuthorityUtils.NO_AUTHORITIES);
                } else {
                    throw new UsernameNotFoundException(username);
                }
            }
        };
    }

}