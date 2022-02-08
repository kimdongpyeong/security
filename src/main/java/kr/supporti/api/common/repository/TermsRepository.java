package kr.supporti.api.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.entity.TermsEntity;
import kr.supporti.api.common.entity.UserEntity;

@Repository
public interface TermsRepository extends JpaRepository<TermsEntity, Long> {

}