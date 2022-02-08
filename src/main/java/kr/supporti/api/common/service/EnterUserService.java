package kr.supporti.api.common.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.WeakHashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.supporti.api.common.dto.ClassRoomDto;
import kr.supporti.api.common.dto.ClassRoomParamDto;
import kr.supporti.api.common.dto.EnterUserDto;
import kr.supporti.api.common.dto.EnterUserParamDto;
import kr.supporti.api.common.entity.ClassRoomEntity;
import kr.supporti.api.common.entity.EnterUserEntity;
import kr.supporti.api.common.mapper.ClassRoomMapper;
import kr.supporti.api.common.mapper.EnterUserMapper;
import kr.supporti.api.common.repository.ClassRoomRepository;
import kr.supporti.api.common.repository.EnterUserRepository;
import kr.supporti.common.util.PageRequest;
import kr.supporti.common.util.PageResponse;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import kr.supporti.common.validation.group.ReadValidationGroup;
import kr.supporti.common.validation.group.RemoveValidationGroup;

@Validated
@Service
public class EnterUserService {

    @Autowired
    private EnterUserRepository enterUserRepository;

    @Autowired
    private EnterUserMapper enterUserMapper;

    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public PageResponse<EnterUserEntity> getEnterUserList(@Valid EnterUserParamDto enterUserParamDto, PageRequest pageRequest) {
        Integer enterUserListCount = enterUserMapper.selectEnterUserListCount(enterUserParamDto);
        List<EnterUserEntity> enterUserList = enterUserMapper.selectEnterUserList(enterUserParamDto, pageRequest);
        PageResponse<EnterUserEntity> pageResponse = new PageResponse<>(pageRequest, enterUserListCount);
        pageResponse.setItems(enterUserList);
        return pageResponse;
    }


    @Validated(value = { ReadValidationGroup.class })
    @Transactional(readOnly = true)
    public EnterUserEntity getEnterUser(@Valid @NotNull(groups = { ReadValidationGroup.class }) Long id, HttpServletRequest request) {
        HttpSession session = request.getSession();
        Map<String, Object> map = new WeakHashMap<>();
        session.getAttribute("id");
        map.put("guestCode", session.getAttribute("id"));
        System.out.println("저장 가져오기");
        System.out.println("세션에 저장 되어 있는 변수:" + session.getAttribute("id"));
        System.out.println(map);
        return enterUserMapper.selectEnterUser(id, request);
    }
    
    @Validated(value = { CreateValidationGroup.class })
    @Transactional
    public EnterUserEntity createEnterUser(HttpServletRequest request, @Valid @NotNull(groups = { CreateValidationGroup.class }) EnterUserEntity enterUserEntity) {
        EnterUserEntity result =  enterUserRepository.save(enterUserEntity);
        Map<String, Object> map = new WeakHashMap<>();
        HttpSession session = request.getSession();
        session.setAttribute("id", result.getId());
        map.put("guestId", session.getAttribute("id"));
        System.out.println("저장");
        System.out.println(map);
        return result;
    }


    @Validated(value = { ModifyValidationGroup.class })
    @Transactional
    public EnterUserEntity modifyEnterUser(@Valid @NotNull(groups = { ModifyValidationGroup.class }) EnterUserEntity enterUserEntity) {
        return enterUserRepository.save(enterUserEntity);
    }
    //테스트
    public Map<String, Object> getSession() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest(); //추가
        HttpSession session = request.getSession(); //추가
        Map<String, Object> map = new WeakHashMap<>();
        map.put("guestId", session.getAttribute("id"));
        System.out.println(map);
        System.out.println("테스트");
        return map;
    }

    @Validated (value = {ReadValidationGroup.class})
    @Transactional (readOnly = true)
    public Integer getGuestExists(
            @Valid EnterUserParamDto enterUserParamDto) {
        return enterUserMapper.selectGuestExists(enterUserParamDto);
    }

}