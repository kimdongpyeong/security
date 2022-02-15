package kr.supporti.common.filter;

import java.io.IOException;
import java.io.Writer;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import javax.servlet.FilterChain;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import kr.supporti.api.common.dto.MenuParamDto;
import kr.supporti.api.common.dto.RoleParamDto;
import kr.supporti.api.common.dto.UserLoginHistoryDto;
import kr.supporti.api.common.entity.ApiEntity;
import kr.supporti.api.common.entity.InviteStudentEntity;
import kr.supporti.api.common.entity.MenuEntity;
import kr.supporti.api.common.entity.RoleEntity;
import kr.supporti.api.common.entity.UserEntity;
import kr.supporti.api.common.entity.UserLoginHistoryEntity;
import kr.supporti.api.common.mapper.UserMapper;
import kr.supporti.api.common.repository.UserRepository;
import kr.supporti.api.common.service.MenuService;
import kr.supporti.api.common.service.RoleService;
import kr.supporti.api.common.service.UserService;
import kr.supporti.common.exception.IsNotPemitException;
import kr.supporti.common.keyvalue.entity.JwtKeyValueEntity;
import kr.supporti.common.keyvalue.service.JwtKeyValueService;
import kr.supporti.common.property.SupportiProperty;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.menu.dto.TreeMenuDto;
import kr.supporti.common.util.menu.dto.TreeMenuParamDto;
import kr.supporti.common.util.menu.service.ApiUtilMenuService;

public class ImplUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private UserService userService;
    
    @Autowired
    private UserMapper userMapper;
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
        AuthenticationManager authenticationManager = this.getAuthenticationManager();
        Authentication authentication = null;
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = null;
        String username = null;
        String password = null;
        String signUpWay = null;
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            Map<String, Object> map = objectMapper.readValue(request.getInputStream(),
                    new TypeReference<Map<String, Object>>() {
                    });
            username = (String) map.get("username");
            password = (String) map.get("password");
            signUpWay = (String) map.get("signUpWay");
            usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(username, password,
                    AuthorityUtils.NO_AUTHORITIES);

            ServletContext servletContext = request.getServletContext();
            WebApplicationContext webApplicationContext = WebApplicationContextUtils
                    .getWebApplicationContext(servletContext);
            if (webApplicationContext != null) {

                if (this.beforeAuthenticationProcessLockedException(username, webApplicationContext))
                    throw new LockedException("");

                if (this.beforeAuthenticationProcessIsNotPemitException(username, webApplicationContext))
                    throw new IsNotPemitException("");
            }

            if (signUpWay.equals("N")) {
                authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            } else {
                authentication = new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.NO_AUTHORITIES);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                
            }
        } catch (IOException e) {

        } catch (LockedException le) {
            this.isLockedException(response);
        } catch (IsNotPemitException iNP) {
            this.isIsNotPemitException(response);
        } catch (AuthenticationException ae) {
            this.isAuthenticationException(request, response, username);
        }
        return authentication;
    }

    public boolean beforeAuthenticationProcessLockedException(String username,
            WebApplicationContext webApplicationContext) {

        boolean isThrowsExepction = false;
        UserService userService = webApplicationContext.getBean(UserService.class);
        UserEntity userEntity = userService.getUserByUsername(username != null ? username : "");

        if (userEntity != null && userEntity.getStatus() != null && !userEntity.getStatus().equals("")
                && userEntity.getStatus().equals("S")) {
            isThrowsExepction = true;
        }

        return isThrowsExepction;
    }

    public boolean beforeAuthenticationProcessIsNotPemitException(String username,
            WebApplicationContext webApplicationContext) {
        boolean isThrowsExepction = false;
        UserService userService = webApplicationContext.getBean(UserService.class);
        UserEntity userEntity = userService.getUserByUsername(username != null ? username : "");

        if (userEntity != null && userEntity.getStatus() != null && !userEntity.getStatus().equals("")
                && userEntity.getStatus().equals("F")) {
            isThrowsExepction = true;
        }

        return isThrowsExepction;
    }

    public void occurExceptionSetResponse(HttpServletResponse response) {
        response.setHeader("Content-Type", "application/json; charset=utf-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
    }

    public Map<String, Object> setExceptionMessage(String code, String message) {
        Map<String, Object> map = new WeakHashMap<>();
        map.put("code", code);
        map.put("message", message);
        return map;
    }

    public void isLockedException(HttpServletResponse response) {
        this.occurExceptionSetResponse(response);
        try (Writer writer = response.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            writer.write(
                    objectMapper.writeValueAsString(this.setExceptionMessage("LOCKED", "정지된 계정입니다.\n관리자에게 문의해주세요.")));
        } catch (IOException ioe) {
        }

    }

    public void isIsNotPemitException(HttpServletResponse response) {
        this.occurExceptionSetResponse(response);
        try (Writer writer = response.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            writer.write(objectMapper.writeValueAsString(this.setExceptionMessage("NotPermit", "회원탈퇴된 계정입니다.")));
        } catch (IOException ioe) {
        }
    }

    public void isAuthenticationException(HttpServletRequest request, HttpServletResponse response, String username) {
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils
                .getWebApplicationContext(servletContext);
        if (webApplicationContext != null) {
        }
        this.occurExceptionSetResponse(response);
        try (Writer writer = response.getWriter()) {
            ObjectMapper objectMapper = new ObjectMapper();
            writer.write(objectMapper.writeValueAsString(this.setExceptionMessage("FAIL", "아이디 혹은 비밀번호가 틀렸습니다.")));
        } catch (IOException ioe) {
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication authResult) throws IOException, ServletException {
        ServletContext servletContext = request.getServletContext();
        WebApplicationContext webApplicationContext = WebApplicationContextUtils
                .getWebApplicationContext(servletContext);
        if (webApplicationContext != null) {
            SupportiProperty property = webApplicationContext.getBean(SupportiProperty.class);
            JwtKeyValueService jwtKeyValueService = webApplicationContext.getBean(JwtKeyValueService.class);
            UserService userService = webApplicationContext.getBean(UserService.class);
            MenuService menuService = webApplicationContext.getBean(MenuService.class);
            RoleService roleService = webApplicationContext.getBean(RoleService.class);
            ApiUtilMenuService apiUtilMenuService = webApplicationContext.getBean(ApiUtilMenuService.class);
            String username = authResult.getName();
            JwtKeyValueEntity jwtKeyValueEntity = null;
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            PageRequest pageRequest = new PageRequest();
            pageRequest.setPage(1);
            pageRequest.setRowSize(100000000);
            PageRequest treeMenuDtoListPageRequest = new PageRequest();
            treeMenuDtoListPageRequest.setPage(1);
            treeMenuDtoListPageRequest.setRowSize(100000000);
            treeMenuDtoListPageRequest.setSort(Arrays.asList("rankingPath,asc"));
            UserEntity userEntity = userService.getUserByUsername(username);
            List<RoleEntity> roleEntityList = roleService
                    .getRoleList(RoleParamDto.builder().userId(userEntity.getId()).build(), pageRequest).getItems();
            List<MenuEntity> menuEntityList = menuService
                    .getMenuList(MenuParamDto.builder().userId(userEntity.getId()).build(), pageRequest).getItems();
            List<TreeMenuDto> treeMenuDtoList = apiUtilMenuService
                    .getTreeMenuList(TreeMenuParamDto.builder().userId(userEntity.getId()).build(),
                            treeMenuDtoListPageRequest)
                    .getItems();
            Map<String, Object> userMap = objectMapper.convertValue(userEntity,
                    new TypeReference<Map<String, Object>>() {
                    });
            List<Map<String, Object>> roleList = objectMapper.convertValue(roleEntityList,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
            List<Map<String, Object>> menuList = objectMapper.convertValue(menuEntityList,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
            List<Map<String, Object>> treeMenuList = objectMapper.convertValue(treeMenuDtoList,
                    new TypeReference<List<Map<String, Object>>>() {
                    });
            userMap.remove("password");
            String secretKey = property.getJwt().getSecretKey();
            String subject = property.getJwt().getSubject();
            Long currentTimeMillis = System.currentTimeMillis();
            Long expirationTimeMillis = property.getJwt().getExpirationTimeMillis();
            Date currentTime = new Date(currentTimeMillis);
            Date expirationTime = new Date(currentTimeMillis + 10800000);
            String token = null;
            if (roleList.isEmpty()) {
                throw new ServletException("권한 목록이 없습니다.");
            }
            try {
                JWSSigner jwsSigner = new MACSigner(secretKey);
                JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                        .subject(subject)
                        .issueTime(currentTime)
                        .expirationTime(expirationTime)
                        .notBeforeTime(currentTime)
                        .claim("user", userMap)
                        .claim("roleList", roleList)
//                        .claim("menuList", menuList)
//                        .claim("treeMenuList", treeMenuList)
                        .build();
                SignedJWT signedJwt = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), jwtClaimsSet);
                signedJwt.sign(jwsSigner);
                token = signedJwt.serialize();
                jwtKeyValueEntity = JwtKeyValueEntity.builder()
                        .token(token)
                        .username(username)
                        .build();
                jwtKeyValueService.createJwtKeyValue(jwtKeyValueEntity);
            } catch (JOSEException e) {
            }
            
            UserLoginHistoryDto userLoginHistoryDto = new UserLoginHistoryDto();
            UserEntity user = userService.getUserByUsername(username);
            
              try {
                  InetAddress ip = InetAddress.getLocalHost(); 
                  System.out.println("Host Name = [" + ip.getHostName() + "]"); 
                  System.out.println("Host Address = [" + ip.getHostAddress() + "]"); 
                  userLoginHistoryDto.setLoginIp(ip.getHostAddress());
              } catch (Exception e) { 
                  System.out.println(e); 
              }
    
            UserLoginHistoryEntity userLoginHistoryEntity = UserLoginHistoryEntity.builder() 
                .userId(user.getId())
                .loginIp(userLoginHistoryDto.getLoginIp())
                .loginDate(LocalDateTime.now())
                .build(); 
            userService.createUserIp(userLoginHistoryEntity);
            
            response.setHeader("Authorization", "Bearer " + token);

            HttpSession session = request.getSession();
            session.setAttribute("MENU_LIST", menuList);
            session.setAttribute("TREE_MENU_LIST", treeMenuList);
            session.setMaxInactiveInterval(-1);
        }
    }

}