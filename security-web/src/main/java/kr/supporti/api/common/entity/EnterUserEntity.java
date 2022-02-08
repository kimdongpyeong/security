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
@Table(name = "`tb_enter_user`")
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EnterUserEntity {

    @Null(groups = { CreateValidationGroup.class })
    @NotNull(groups = { ModifyValidationGroup.class })
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`", columnDefinition = "bigint(20)")
    private Long id;

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`classroom_id`", columnDefinition = "bigint(20)", nullable = false)
    private Long classroomId;
    
    @NotNull
    @Column(name = "`type`", columnDefinition = "enum('S','P')", nullable = false)
    private String type;
    
    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`name`", columnDefinition = "varchar(20)", nullable = false)
    private String name;

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`grade`", columnDefinition = "varchar(10)", nullable = false)
    private String grade;

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`email`", columnDefinition = "varchar(100)", nullable = false)
    private String email;

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`phone_num`", columnDefinition = "varchar(20)", nullable = false)
    private String phoneNum;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`created_date`", columnDefinition = "datetime", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Transient
    private String guestCode;
    
    @Transient
    private String compareCode;
    
    @Transient
    private String profile;

}