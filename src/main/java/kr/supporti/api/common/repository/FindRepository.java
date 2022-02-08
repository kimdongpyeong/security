package kr.supporti.api.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.entity.UserEntity;

@Repository
public interface FindRepository extends JpaRepository<UserEntity, Long> {
}