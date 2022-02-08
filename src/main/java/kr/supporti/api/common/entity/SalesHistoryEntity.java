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
@Table(name = "`tb_sales_history`")
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalesHistoryEntity {

    @Null(groups = { CreateValidationGroup.class })
    @NotNull(groups = { ModifyValidationGroup.class })
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`", columnDefinition = "bigint(20)")
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "`sales_start_date`", columnDefinition = "date", nullable = false)
    private LocalDate salesStartDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "`sales_end_date`", columnDefinition = "date", nullable = false)
    private LocalDate salesEndDate;
    
    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`count`", columnDefinition = "int(11)", nullable = false)
    private Integer count;

    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`total_sales`", columnDefinition = "int(11)", nullable = false)
    private Integer totalSales;

    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`payment_method_cd`", columnDefinition = "varchar(4)", nullable = false)
    private String paymentMethodCd;

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
    private LocalDate salesDate;
    
    @Transient
    private Integer salesTotal;
    
    @Transient
    private Integer paymentTotal;
    
    @Transient
    private Integer dayTotal;
    
    @Transient
    private String month;
    
    @Transient
    private Integer monthTotal;
    
    @Transient
    private String year;
}