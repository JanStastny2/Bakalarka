package cz.uhk.grainweight.rest;

import cz.uhk.grainweight.model.WeightRecord;
import cz.uhk.grainweight.service.WeightRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/records")
public class WeightRecordRestController {

    private final WeightRecordService weightRecordService;

    @Autowired
    public WeightRecordRestController(WeightRecordService weightRecordService) {
        this.weightRecordService = weightRecordService;
    }

    @GetMapping("/getall")
    public List<WeightRecord> getAllRecords() {
        return weightRecordService.getAllWeightRecords();
    }

    @GetMapping("/get/{id}")
    public WeightRecord getRecord(@PathVariable long id) {
        return weightRecordService.getWeightRecord(id);
    }

    @PostMapping("/new")
    public WeightRecord createOrUpdateRecord(@RequestBody WeightRecord weightRecord) {
        return weightRecordService.saveWeightRecord(weightRecord);
    }

    @DeleteMapping("/delete/{id}")
    public WeightRecord deleteRecord(@PathVariable long id) {
        WeightRecord record = weightRecordService.getWeightRecord(id);
        if (record != null) {
            weightRecordService.deleteWeightRecord(id);
        }
        return record;
    }
}
