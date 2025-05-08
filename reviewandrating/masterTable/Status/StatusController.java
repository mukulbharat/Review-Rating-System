package com.krenai.reviewandrating.masterTable.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class StatusController {

    @Autowired
    private StatusService statusService;

    @PostMapping("/add")
    public ResponseEntity<StatusCode> addStatus(@RequestBody StatusCode statusCode)
    {
        return new ResponseEntity<StatusCode>(statusService.createStatus(statusCode), HttpStatus.CREATED);
    }
}
