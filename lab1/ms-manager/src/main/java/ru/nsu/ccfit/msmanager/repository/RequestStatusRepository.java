package ru.nsu.ccfit.msmanager.repository;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.nsu.ccfit.msmanager.model.response.HashCrackStatus;
import ru.nsu.ccfit.msmanager.model.status.RequestStatus;

public interface RequestStatusRepository extends MongoRepository<RequestStatus, String> {
    List<RequestStatus> findRequestStatusByHashCrackStatus(HashCrackStatus hashCrackStatus);
}
