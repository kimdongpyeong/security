package kr.supporti.api.common.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "`tb_classroom`")
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomEntity {

    @Null(groups = { CreateValidationGroup.class })
    @NotNull(groups = { ModifyValidationGroup.class })
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`", columnDefinition = "bigint(20)")
    private Long id;

    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`type`", columnDefinition = "enum('R','N')", nullable = false)
    private String type;

    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`title`", columnDefinition = "varchar(50)", nullable = false)
    private String title;

    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`member_num`", columnDefinition = "int(11)", nullable = false)
    private int memberNum;
    
    @Column(name = "`category_id`", columnDefinition = "bigint(20)", nullable = true)
    private int categoryId;
    
    @Column(name = "`etc`", columnDefinition = "varchar(50)", nullable = true)
    private String etc;

    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`open_type`", columnDefinition = "enum('O','C')", nullable = false)
    private String openType;

    @Column(name = "`enter_code`", columnDefinition = "varchar(4)", nullable = true)
    private String enterCode;

    @Column(name = "`origin_file_nm`", columnDefinition = "varchar(200)", nullable = true)
    private String originFileNm;

    @Column(name = "`save_file_nm`", columnDefinition = "varchar(200)", nullable = true)
    private String saveFileNm;

    @Column(name = "`file_size`", columnDefinition = "varchar(50)", nullable = true)
    private String fileSize;

    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`delete_yn`", columnDefinition = "enum('Y','N')", nullable = false)
    private String deleteYn;

    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`created_by`", columnDefinition = "bigint(20)", nullable = false)
    private Long createdBy;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`created_date`", columnDefinition = "datetime", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Null(groups = { CreateValidationGroup.class })
    @NotNull(groups = { ModifyValidationGroup.class })
    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`last_modified_date`", columnDefinition = "datetime", nullable = false)
    private LocalDateTime lastModifiedDate;

    @Transient
    private Long userId;

    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime classEndTime;
    
    @Transient
    private String createdByNm;
    
    @Transient
    private String lecturerCategory;
    
    @Transient
    private Integer lectureDay;
    
    @Transient
    private Integer lectureCnt;

}