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
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import org.springframework.data.annotation.CreatedDate;
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
@Table(name = "`tb_personal_room`")
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonalRoomEntity {

    @Null(groups = { CreateValidationGroup.class })
    @NotNull(groups = { ModifyValidationGroup.class })
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`", columnDefinition = "bigint(20)")
    private Long id;

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`created_user`", columnDefinition = "bigint(20)", nullable = false)
    private Long createdUser;

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`invited_user`", columnDefinition = "bigint(20)", nullable = false)
    private Long invitedUser;

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`created_out_yn`", columnDefinition = "enum('Y','N')", nullable = false)
    private String createdOutYn;

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`invited_out_yn`", columnDefinition = "enum('Y','N')", nullable = false)
    private String invitedOutYn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`created_enter_date`", columnDefinition = "datetime", nullable = false, updatable = false)
    private LocalDateTime createdEnterDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`invited_enter_date`", columnDefinition = "datetime", nullable = false, updatable = false)
    private LocalDateTime invitedEnterDate;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`created_date`", columnDefinition = "datetime", nullable = false, updatable = false)
    private LocalDateTime createdDate;

    @Transient
    private String createdNm;

    @Transient
    private String invitedNm;

    @Transient
    private String createdProfile;

    @Transient
    private String invitedProfile;
}