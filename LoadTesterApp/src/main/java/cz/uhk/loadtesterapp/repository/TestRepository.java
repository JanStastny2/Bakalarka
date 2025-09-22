package cz.uhk.loadtesterapp.repository;


import cz.uhk.loadtesterapp.model.entity.TestRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<TestRun, Long>, JpaSpecificationExecutor<TestRun> {

    List<TestRun> findByCreatedBy_Id(Long ownerId);

    boolean existsByIdAndCreatedBy_Id(Long testId, Long ownerId);

//    @Query("""
//      SELECT t FROM TestRun t
//      WHERE (:status IS NULL OR t.status = :status)
//        AND (:mode IS NULL OR t.processingMode = :mode)
//        AND (:from IS NULL OR t.createdAt >= :from)
//        AND (:to IS NULL OR t.createdAt < :to)
//      """)
//    Page<TestRun> search(
//            @Param("status") TestStatus status,
//            @Param("mode") ProcessingMode mode,
//            @Param("from") Instant from,
//            @Param("to") Instant to,
//            Pageable pageable
//    );

}

