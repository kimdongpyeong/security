package kr.supporti.api.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.entity.EnterUserEntity;

@Repository
public interface EnterUserRepository extends JpaRepository<EnterUserEntity, Long> {

}