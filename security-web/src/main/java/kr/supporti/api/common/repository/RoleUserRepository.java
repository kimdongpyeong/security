package kr.supporti.api.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.entity.RoleUserEntity;

@Repository
public interface RoleUserRepository extends JpaRepository<RoleUserEntity, Long> {
    @Query(value = "DELETE FROM tb_role_user WHERE user_id = :user_id", nativeQuery = true)
    public void deleteByUserId(@Param("user_id") Long id);
}