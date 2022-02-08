package kr.supporti.common.util.category.dto;

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
public class TreeCategoryParamDto {

    private String publishYn;

    private String menuExposureYn;

    private Long userId;

    private Boolean isDefault;

}