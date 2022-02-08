package kr.supporti.api.common.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TermsParamDto {

    private Long id;

    private int adminYn = 2;
}