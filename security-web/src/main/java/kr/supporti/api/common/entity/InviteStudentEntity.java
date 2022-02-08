package kr.supporti.api.common.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
@Table(name = "`tb_invite_student`", uniqueConstraints = { @UniqueConstraint(columnNames = { "`student_id`" }) })
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InviteStudentEntity {

    @Null(groups = { CreateValidationGroup.class })
    @NotNull(groups = { ModifyValidationGroup.class })
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`", columnDefinition = "bigint(20)")
    private Long id;

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`lecturer_id`", columnDefinition = "bigint(20)", nullable = false)
    private Long lecturerId;
    
    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`student_id`", columnDefinition = "bigint(20)", nullable = false)
    private Long studentId;
    
    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`classroom_id`", columnDefinition = "bigint(20)", nullable = false)
    private Long classroomId;

    @NotNull
    @Column(name = "`link_yn`", columnDefinition = "enum('Y','N')", nullable = false)
    private String linkYn;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`created_date`", columnDefinition = "datetime", nullable = false, updatable = false)
    private LocalDateTime createdDate;
    
    @Transient
    private String userName;
    
    @Transient
    private String userPhone;
    
    @Transient
    private String userEmail;
    
    @Transient
    private String classTitle;
}