package kr.supporti.common.filter;

import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class ImplLogoutFilter extends LogoutFilter {

    public ImplLogoutFilter(LogoutSuccessHandler logoutSuccessHandler, LogoutHandler[] handlers) {
        super(logoutSuccessHandler, handlers);
    }

}