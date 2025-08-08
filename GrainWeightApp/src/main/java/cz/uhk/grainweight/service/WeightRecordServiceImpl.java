package cz.uhk.grainweight.service;

import cz.uhk.grainweight.model.WeightRecord;
import cz.uhk.grainweight.repository.WeightRecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeightRecordServiceImpl implements WeightRecordService {

    private final WeightRecordRepository weightRecordRepository;

    @Autowired
    public WeightRecordServiceImpl(WeightRecordRepository weightRecordRepository) {
        this.weightRecordRepository = weightRecordRepository;
    }

    @Override
    public List<WeightRecord> getAllWeightRecords() {
        return weightRecordRepository.findAll();
    }

    @Override
    public WeightRecord getWeightRecord(Long id) {
        return weightRecordRepository.findById(id).orElse(null);
    }

    @Override
    public WeightRecord saveWeightRecord(WeightRecord weightRecord) {
        return weightRecordRepository.save(weightRecord);
    }

    @Override
    public void deleteWeightRecord(Long id) {
        weightRecordRepository.deleteById(id);
    }

}