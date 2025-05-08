package com.krenai.reviewandrating.masterTable.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

    @Autowired
    private StatusRepository statusRepository;

    public StatusCode createStatus(StatusCode statusCode)
    {
        return statusRepository.save(statusCode);
    }
}
