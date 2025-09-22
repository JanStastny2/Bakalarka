package cz.uhk.loadtesterapp.mapper;


import cz.uhk.loadtesterapp.model.dto.*;
import cz.uhk.loadtesterapp.model.entity.*;
import org.mapstruct.*;

import java.util.List;

@Mapper(config = MapperConfig.class)
public interface TestMapper {

    // RequestDefinition
    RequestDefinition toEntity(RequestDefinitionDto dto);

    RequestDefinitionDto toDto(RequestDefinition entity);

    //TestSummary
    TestSummaryDto toDto(TestSummary entity);

    //Create testRun
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "effectiveUrl", ignore = true)
    @Mapping(target = "errorMessage", ignore = true)
    @Mapping(target = "summary", ignore = true)
    @Mapping(target = "hwSummary", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "startedAt", ignore = true)
    @Mapping(target = "finishedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    TestRun toEntity(TestRunCreateRequest req);

    //update testRun
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "effectiveUrl", ignore = true)
    @Mapping(target = "errorMessage", ignore = true)
    @Mapping(target = "summary", ignore = true)
    @Mapping(target = "hwSummary", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "startedAt", ignore = true)
    @Mapping(target = "finishedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    void updateEntity(TestUpdateRequest req, @MappingTarget TestRun entity);

    //response
    @Mapping(target = "request", source = "request")
    @Mapping(target = "summary", source = "summary")
    @Mapping(target = "hwSummary", source = "hwSummary")
    @Mapping(target = "createdBy", source = "createdBy")
    TestRunResponse toResponse(TestRun entity);

    HwSummaryDto toDto(HwSummary entity);

    HwSampleDto toDto(TestRunHwSample entity);
    List<HwSampleDto> toSampleDto(List<TestRunHwSample> list);


}
