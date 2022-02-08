package kr.supporti.api.common.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
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
@Table(name = "`tb_code`", uniqueConstraints = @UniqueConstraint(columnNames = { "parent_id", "name" }))
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CodeEntity {

    @Null(groups = { CreateValidationGroup.class })
    @NotNull(groups = { ModifyValidationGroup.class })
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`", columnDefinition = "bigint(20)", nullable = false)
    private Long id;

    @Column(name = "`parent_id`", columnDefinition = "bigint(20)", unique = true)
    private Long parentId;

    @Column(name = "`name`", columnDefinition = "varchar(50)", unique = true)
    private String name;

    @Column(name = "`description`", columnDefinition = "varchar(200)")
    private String description;

    @Column(name = "`value`", columnDefinition = "varchar(50)")
    private String value;

    @Column(name = "`order_no`", columnDefinition = "int(11)")
    private Integer orderNo;

    @Column(name = "`status`", columnDefinition = "enum('T','F')")
    private String status;

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

}
