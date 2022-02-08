package kr.supporti.api.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.entity.RoleApiEntity;
import kr.supporti.api.common.entity.id.RoleApiEntityId;

@Repository
public interface RoleApiRepository extends JpaRepository<RoleApiEntity, RoleApiEntityId> {

}