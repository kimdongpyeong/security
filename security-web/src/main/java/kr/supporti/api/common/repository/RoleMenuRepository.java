package kr.supporti.api.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.entity.RoleMenuEntity;
import kr.supporti.api.common.entity.id.RoleMenuEntityId;

@Repository
public interface RoleMenuRepository extends JpaRepository<RoleMenuEntity, RoleMenuEntityId> {

}