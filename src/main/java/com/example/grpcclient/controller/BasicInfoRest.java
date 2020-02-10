package com.example.grpcclient.controller;


import com.example.grpcclient.model.BasicInfo;
import com.example.grpcclient.service.BasicInfoNonBlockingService;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author z.Taghizadeh
 */
@RestController
@RequestMapping("/basic-information")
public class BasicInfoRest {

    @Autowired
    private BasicInfoNonBlockingService service;


    @GetMapping(value = "/getById/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public BasicInfo getByIdBasicInfo(@PathVariable Long id) throws Exception {

        return service.getBasicInfoById(id);
    }


    @GetMapping(value = "/getAll",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<BasicInfo> getAllBasicInfo() throws Exception {

        return service.getAllBasicInfo();
    }


    @PostMapping(value = "/save",
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> insertToBasicInfo(@Valid @RequestBody BasicInfo basicInfo) {

        return ResponseEntity.ok(service.saveBasicInfo(basicInfo));
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleException(Exception e) {
        String bodyOfResponse = "check console....";

        if ((((e).getCause().getCause())) instanceof Exception) {
            Status status = ((StatusRuntimeException) ((e).getCause().getCause()))
                    .getStatus();
            if (Status.INTERNAL.equals(status))
                bodyOfResponse = (status.getDescription());
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(bodyOfResponse);
    }
}
