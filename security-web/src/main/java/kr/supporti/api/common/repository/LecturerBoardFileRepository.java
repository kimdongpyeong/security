package kr.supporti.api.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.entity.LecturerBoardEntity;
import kr.supporti.api.common.entity.LecturerBoardFileEntity;
import kr.supporti.api.common.entity.NoticeEntity;

@Repository
public interface LecturerBoardFileRepository extends JpaRepository<LecturerBoardFileEntity, Long> {
}