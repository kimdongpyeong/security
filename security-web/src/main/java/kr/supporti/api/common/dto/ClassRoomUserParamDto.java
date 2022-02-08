package kr.supporti.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomUserParamDto {

    private Long classroomId;

    private Long userId;

    private String name;

    private String profile;

    private String title;

    private String saveFileNm;

    private String deleteYn;

}