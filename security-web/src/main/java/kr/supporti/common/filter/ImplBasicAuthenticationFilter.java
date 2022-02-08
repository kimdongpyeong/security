package kr.supporti.common.filter;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.WeakHashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import kr.supporti.api.common.entity.MenuEntity;
import kr.supporti.api.common.entity.RoleEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.common.config.WebSecurityConfig;
import kr.supporti.common.keyvalue.entity.JwtKeyValueEntity;
import kr.supporti.common.keyvalue.service.JwtKeyValueService;
import kr.supporti.common.property.SupportiProperty;
import kr.supporti.common.util.menu.dto.TreeMenuDto;

public class ImplBasicAuthenticationFilter extends BasicAuthenticationFilter {

    public ImplBasicAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils
                .getWebApplicationContext(servletContext);
        if (webApplicationContext != null) {
            SupportiProperty property = webApplicationContext.getBean(SupportiProperty.class);
            JwtKeyValueService jwtKeyValueService = webApplicationContext.getBean(JwtKeyValueService.class);
            String secretKey = property.getJwt().getSecretKey();
            String authorization = request.getHeader("Authorization");
            if (authorization != null) {
                String token = authorization.replace("Bearer ", "");
                JwtKeyValueEntity jwtKeyValueEntity = jwtKeyValueService.getJwtKeyValue(token);
                if (jwtKeyValueEntity != null) {
                    try {
                        JWSVerifier jwsVerifier = new MACVerifier(secretKey);
                        SignedJWT signedJwt = SignedJWT.parse(token);
                        request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                                .getRequest();
                        HttpSession session = request.getSession();
                        if (signedJwt.verify(jwsVerifier)) {
                            JWTClaimsSet jwtClaimsSet = signedJwt.getJWTClaimsSet();
                            if(jwtClaimsSet.getExpirationTime().before(new Date())) {
                                jwtKeyValueService.removeJwtKeyValue(token);
                                return;
                            }
                            ObjectMapper objectMapper = new ObjectMapper();
                            objectMapper.registerModule(new JavaTimeModule());
                            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                            List<RoleEntity> roleEntityList = objectMapper.convertValue(
                                    jwtClaimsSet.getClaim("roleList"), new TypeReference<List<RoleEntity>>() {
                                    });
                            UserEntity userEntity = objectMapper.convertValue(jwtClaimsSet.getClaim("user"),
                                    UserEntity.class);
                            List<MenuEntity> menuEntityList = objectMapper.convertValue(
                                    session.getAttribute("MENU_LIST"), new TypeReference<List<MenuEntity>>() {
                                    });
                            List<TreeMenuDto> treeMenuDtoList = objectMapper.convertValue(
                                    session.getAttribute("TREE_MENU_LIST"), new TypeReference<List<TreeMenuDto>>() {
                                    });
                            List<String> authorityList = new ArrayList<>();
                            for (int i = 0; i < roleEntityList.size(); i++) {
                                RoleEntity roleEntity = roleEntityList.get(i);
                                String value = roleEntity.getValue();
                                authorityList.add(value);
                            }
                            String username = userEntity.getUsername();
                            WeakHashMap<String, Object> details = new WeakHashMap<>();
                            details.put("user", userEntity);
                            details.put("roleList", roleEntityList);
                            details.put("menuList", menuEntityList);
                            details.put("treeMenuList", treeMenuDtoList);
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                                    username, token,
                                    AuthorityUtils.createAuthorityList(authorityList.toArray(new String[0])));
                            usernamePasswordAuthenticationToken.setDetails(details);
                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                        }
                    } catch (JOSEException | ParseException e) {
                    }
                }
            }
        }
        chain.doFilter(request, response);
    }
}