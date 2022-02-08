package kr.supporti.api.common.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import kr.supporti.api.common.entity.id.ClassRoomChatNoticeEntityId;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "`tb_classroom_chat_notice`")
@IdClass(value = ClassRoomChatNoticeEntityId.class)
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClassRoomChatNoticeEntity {

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Id
    @Column(name = "`classroom_id`", columnDefinition = "bigint(20)", nullable = false)
    private Long classroomId;

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Id
    @Column(name = "`message_id`", columnDefinition = "varchar(50)", nullable = false)
    private String messageId;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`created_date`", columnDefinition = "datetime", nullable = false, updatable = false)
    private LocalDateTime createdDate;

}