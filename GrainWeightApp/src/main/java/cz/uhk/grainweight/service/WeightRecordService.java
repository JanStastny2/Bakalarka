package cz.uhk.grainweight.service;

import cz.uhk.grainweight.model.WeightRecord;
import java.util.List;

public interface WeightRecordService {

    List<WeightRecord> getAllWeightRecords();

    WeightRecord getWeightRecord(Long id);

    WeightRecord saveWeightRecord(WeightRecord weightRecord);

    void deleteWeightRecord(Long id);
}
