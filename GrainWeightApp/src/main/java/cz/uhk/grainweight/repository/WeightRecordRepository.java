package cz.uhk.grainweight.repository;

import cz.uhk.grainweight.model.WeightRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

public interface WeightRecordRepository extends JpaRepository<WeightRecord, Long> {
}
