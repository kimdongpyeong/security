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
@Table(name = "`tb_user`", uniqueConstraints = { @UniqueConstraint(columnNames = { "`username`" }) })
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity {

    @Null(groups = { CreateValidationGroup.class })
    @NotNull(groups = { ModifyValidationGroup.class })
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`", columnDefinition = "bigint(20)")
    private Long id;

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`username`", columnDefinition = "varchar(100)", nullable = false)
    private String username;

    @Column(name = "`password`", columnDefinition = "varchar(100)", nullable = true)
    private String password;

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`name`", columnDefinition = "varchar(30)", nullable = false)
    private String name;

    @Column(name = "`phone_num`", columnDefinition = "varchar(20)", nullable = true)
    private String phoneNum;

    @Column(name = "`profile_id`", columnDefinition = "bigint(20)", nullable = true)
    private Long profileId;

    @Column(name = "`bank_cd`", columnDefinition = "varchar(3)", nullable = true)
    private String bankCd;

    @Column(name = "`account_num`", columnDefinition = "varchar(50)", nullable = true)
    private String accountNum;

    @Column(name = "`payment_general`", columnDefinition = "enum('P','B')", nullable = true)
    private String paymentGeneral;
    
    @NotNull
    @Column(name = "`sign_up_way`", columnDefinition = "enum('N','G','K')", nullable = false)
    private String signUpWay;

    @NotNull
    @Column(name = "`status`", columnDefinition = "enum('T','D','F')", nullable = true)
    private String status;
    
    @Column(name = "`lecturer_approval_yn`", columnDefinition = "enum('Y','N','R')", nullable = true)
    private String lecturerApprovalYn;

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

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "`last_modified_password_date`", columnDefinition = "date", nullable = true)
    private LocalDate lastModifiedPasswordDate;

    @Transient
    private Long roleId;

    @Transient
    private String roleName;

    @Transient
    private String bankName;

    @Transient
    private String saveFileNm;
}