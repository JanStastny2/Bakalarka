package cz.uhk.loadtesterapp.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TestSummary {

    private int totalRequests;
    private int successRequests;
    private int failRequests;
    private double successRate;
    private double avgResponseTime;


}
