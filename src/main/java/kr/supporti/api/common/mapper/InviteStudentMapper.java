package kr.supporti.api.common.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.dto.InviteStudentDto;
import kr.supporti.api.common.dto.InviteStudentParamDto;
import kr.supporti.api.common.dto.UserDto;
import kr.supporti.api.common.entity.InviteStudentEntity;
import kr.supporti.common.util.PageRequest;

@Repository
@Mapper
public interface InviteStudentMapper {

    public List<InviteStudentEntity> selectInviteStudentList(@Param(value = "inviteStudentParamDto") InviteStudentParamDto inviteStudentParamDto,
            @Param(value = "pageRequest") PageRequest pageRequest);

    public Integer selectInviteStudentListCount(@Param(value = "inviteStudentParamDto") InviteStudentParamDto inviteStudentParamDto);

    public InviteStudentEntity selectInviteStudent(@Param(value = "id") Long id);
    
    public Integer updateStudentInfo(@Param(value = "id") Long id, @Param(value = "inviteStudentParamDto") InviteStudentParamDto inviteStudentParamDto);

}