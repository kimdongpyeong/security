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
@Table(name = "`tb_payment_history`")
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentHistoryEntity {

    @Null(groups = { CreateValidationGroup.class })
    @NotNull(groups = { ModifyValidationGroup.class })
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`", columnDefinition = "bigint(20)")
    private Long id;

    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`request_lecturer_id`", columnDefinition = "bigint(20)", nullable = false)
    private Long requestLecturerId;

    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`name`", columnDefinition = "varchar(50)", nullable = false)
    private String name;

    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`phone_num`", columnDefinition = "varchar(11)", nullable = false)
    private String phoneNum;

    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`payment_target_cd`", columnDefinition = "varchar(4)", nullable = false)
    private String paymentTargetCd;

    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`amount`", columnDefinition = "int(11)", nullable = false)
    private Integer amount;

    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`payment_type`", columnDefinition = "enum('S','R')", nullable = false)
    private String paymentType;

    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`order_id`", columnDefinition = "varchar(30)", nullable = false)
    private String orderId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`request_date`", columnDefinition = "datetime", nullable = false)
    private LocalDateTime requestDate;

    @Column(name = "`payment_user_id`", columnDefinition = "bigint(20)", nullable = true)
    private Long paymentUserId;

    @Column(name = "`payment_method_cd`", columnDefinition = "varchar(4)", nullable = true)
    private String paymentMethodCd;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`payment_date`", columnDefinition = "datetime", nullable = true)
    private LocalDateTime paymentDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`last_payment_date`", columnDefinition = "datetime", nullable = true)
    private LocalDateTime lastPaymentDate;

    @Column(name = "`payment_general`", columnDefinition = "enum('P','B')", nullable = true)
    private String paymentGeneral;

    @Column(name = "`payment_div`", columnDefinition = "enum('K','T')", nullable = true)
    private String paymentDiv;

    @Column(name = "`kakao_tid`", columnDefinition = "varchar(50)", nullable = true)
    private String kakaoTid;

    @Column(name = "`kakao_aid`", columnDefinition = "varchar(50)", nullable = true)
    private String kakaoAid;

    @Column(name = "`kakao_sid`", columnDefinition = "varchar(50)", nullable = true)
    private String kakaoSid;

    @Column(name = "`toss_payment_key`", columnDefinition = "varchar(100)", nullable = true)
    private String tossPaymentKey;

    @NotNull(groups = { CreateValidationGroup.class})
    @Column(name = "`state`", columnDefinition = "enum('I','S','R','C','CY','RR')", nullable = false)
    private String state;

    @Column(name = "`regular_payment_yn`", columnDefinition = "enum('Y','N')", nullable = false)
    private String regularPaymentYn;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`cancel_request_date`", columnDefinition = "datetime", nullable = true)
    private LocalDateTime cancelRequestDate;

    @Column(name = "`cancel_reason`", columnDefinition = "varchar(300)", nullable = true)
    private String cancelReason;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`cancel_approval_date`", columnDefinition = "datetime", nullable = true)
    private LocalDateTime cancelApprovalDate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`refund_date`", columnDefinition = "datetime", nullable = true)
    private LocalDateTime refundDate;

    @Column(name = "`refund_process_id`", columnDefinition = "bigint(20)", nullable = true)
    private String refundProcessId;

    @Column(name = "`refund_reject_desc`", columnDefinition = "varchar(300)", nullable = true)
    private String refundRejectDesc;

    @Column(name = "`refund_account_num`", columnDefinition = "varchar(30)", nullable = true)
    private String refundAccountNum;

    @Column(name = "`refund_bank_cd`", columnDefinition = "varchar(50)", nullable = true)
    private String refundBankCd;

    @Column(name = "`refund_user_nm`", columnDefinition = "varchar(30)", nullable = true)
    private String refundUserNm;

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
    private String paymentTargetNm;

    @Transient
    private String paymentMethodNm;

    @Transient
    private String paymentDateDay;

    @Transient
    private String requestLecturerNm;

    @Transient
    private String refundProcessNm;

    @Transient
    private String refundBankNm;

    @Transient
    private String paymentUserNm;
}