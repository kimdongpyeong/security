package kr.supporti.api.common.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
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
@Table(name = "`tb_lecturer_student`")
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentEntity {

    @Null(groups = { CreateValidationGroup.class })
    @NotNull(groups = { ModifyValidationGroup.class })
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`", columnDefinition = "bigint(20)")
    private Long id;
    
    @Column(name = "`student_id`", columnDefinition = "bigint(20)", nullable = true)
    private Long studentId;

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`name`", columnDefinition = "varchar(50)", nullable = false)
    private String name;

    @Column(name = "`phone_num`", columnDefinition = "varchar(11)", nullable = true)
    private String phoneNum;
    
    @Column(name = "`email`", columnDefinition = "varchar(50)", nullable = true)
    private String email;
    
    @Column(name = "`gender`", columnDefinition = "enum('M','W')", nullable = true)
    private String gender;

    @Column(name = "`education_cd`", columnDefinition = "varchar(5)", nullable = true)
    private String educationCd;

    @Column(name = "`lecture_type`", columnDefinition = "enum('O','F')", nullable = true)
    private String lectureType;

    @Column(name = "`lecture_name`", columnDefinition = "varchar(50)", nullable = true)
    private String lectureName;

    @Column(name = "`start_date`", columnDefinition = "date", nullable = true)
    private LocalDate startDate;

    @Column(name = "`end_date`", columnDefinition = "date", nullable = true)
    private LocalDate endDate;
    
    @Column(name = "`lecture_num`", columnDefinition = "INT(11)", nullable = true)
    private Integer lectureNum;
    
    @Column(name = "`created_by`", columnDefinition = "bigint(20)", nullable = true)
    private Long createdBy;
    
    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`created_date`", columnDefinition = "datetime", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`last_modified_date`", columnDefinition = "datetime", nullable = false)
    private LocalDateTime lastModifiedDate;
    
    @Transient
    private String linkYn;
    
    @Transient
    private String studentCode;
    
    @Transient
    private String week;
}