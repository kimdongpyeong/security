package kr.supporti.api.common.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CategoryParamDto {

    private Long id;

    private List<Long> idList;

    private String publishYn;

}