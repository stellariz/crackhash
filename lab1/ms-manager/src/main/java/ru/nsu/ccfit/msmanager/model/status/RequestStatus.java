package ru.nsu.ccfit.msmanager.model.status;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import ru.nsu.ccfit.msmanager.model.response.HashCrackStatus;

@Data
@AllArgsConstructor
public class RequestStatus {
    private HashCrackStatus hashCrackStatus;
    private int completedWorkers;
    private List<String> data;
}
