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
@Table(name = "`tb_lecturer_board_file`")
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LecturerBoardFileEntity {

    @Null(groups = { CreateValidationGroup.class })
    @NotNull(groups = { ModifyValidationGroup.class })
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`", columnDefinition = "bigint(20)")
    private Long id;

    @Column(name = "`lecturer_board_id`", columnDefinition = "bigint(20)", nullable = true)
    private Long lecturerBoardId;

     @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`origin_file_nm`", columnDefinition = "varchar(200)", nullable = false)
    private String originFileNm;

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Column(name = "`save_file_nm`", columnDefinition = "varchar(200)", nullable = false)
    private String saveFileNm;

    @NotNull(groups = { CreateValidationGroup.class })
    @Column(name = "`file_size`", columnDefinition = "varchar(50)", nullable = false)
    private String fileSize;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`created_date`", columnDefinition = "datetime", nullable = false, updatable = false)
    private LocalDateTime createdDate;

}