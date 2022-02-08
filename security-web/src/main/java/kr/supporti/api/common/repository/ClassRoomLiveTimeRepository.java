package kr.supporti.api.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kr.supporti.api.common.entity.ClassRoomLiveTimeEntity;

@Repository
public interface ClassRoomLiveTimeRepository extends JpaRepository<ClassRoomLiveTimeEntity, Long> {

    @Query(value = "UPDATE tb_classroom_live_time SET zoom_link = :zoomLink WHERE id = :id", nativeQuery=true)
    public void updateZoomLinkById(@Param("id") Long id, @Param("zoomLink") String zoomLink);

}