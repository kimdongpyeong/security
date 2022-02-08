package kr.supporti.common.util.menu.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TreeMenuDto {

    private Long id;

    private Long parentId;

    private String name;

    private String description;

    private String path;

    private Integer ranking;

    private String show;

    private String sideShow;

    private String publicyStatus;

    private String icon;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lastModifiedDate;

    private Integer depth;

    private String namePath;

    private String rankingPath;

    private Integer childrenCount;

}