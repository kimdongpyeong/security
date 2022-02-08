package kr.supporti.api.common.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import kr.supporti.api.common.entity.id.RoleMenuEntityId;
import kr.supporti.common.validation.group.CreateValidationGroup;
import kr.supporti.common.validation.group.ModifyValidationGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "`tb_role_menu`")
@IdClass(value = RoleMenuEntityId.class)
@EntityListeners(value = { AuditingEntityListener.class })
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoleMenuEntity {

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Id
    @Column(name = "`role_id`", columnDefinition = "bigint(20)")
    private Long roleId;

    @NotNull(groups = { CreateValidationGroup.class, ModifyValidationGroup.class })
    @Id
    @Column(name = "`menu_id`", columnDefinition = "bigint(20)")
    private Long menuId;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "`role_id`", referencedColumnName = "`id`", insertable = false, updatable = false)
    private RoleEntity role;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "`menu_id`", referencedColumnName = "`id`", insertable = false, updatable = false)
    private MenuEntity menu;

}