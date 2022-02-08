package kr.supporti.api.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClassRoomParamDto {

    private Long id;

    private Long userId;

    private String enterCode;

    private String deleteYn;

    private String fileFlag;

    private Long createdBy;
    
    private String title;

}
