package kr.supporti.api.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.entity.UserPeristalsisEntity;
import kr.supporti.api.common.entity.UserTermsEntity;
import kr.supporti.api.common.entity.id.UserTermsEntityId;

@Repository
public interface UserPeristalsisRepository extends JpaRepository<UserPeristalsisEntity, Long> {

}