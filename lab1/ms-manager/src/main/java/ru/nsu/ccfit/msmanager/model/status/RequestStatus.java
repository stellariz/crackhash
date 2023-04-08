package ru.nsu.ccfit.msmanager.model.status;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import ru.nsu.ccfit.msmanager.model.response.HashCrackStatus;

@Document
@Data
@AllArgsConstructor
public class RequestStatus {
    @Id
    private String requestId;
    private HashCrackStatus hashCrackStatus;
    private int completedWorkers;
    private List<String> data;
    private LocalDateTime createdDate;
}
