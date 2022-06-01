package com.oup.cachedemo.sbcachedemo.controller;

import java.util.List;

import javax.websocket.server.PathParam;

import com.oup.cachedemo.sbcachedemo.bo.EmployeeBO;
import com.oup.cachedemo.sbcachedemo.service.SampleService;

import org.apache.tomcat.jni.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SampleController {

    @Autowired
    @Qualifier(value = "samplesrv")
    SampleService service;
 
    @GetMapping(value = "/hello",produces = MediaType.APPLICATION_JSON_VALUE)
    public String sayHello(){
        return "{'message':'Hello World'}";
    }
    
    @GetMapping(value = "/employees",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<EmployeeBO> getAllEmployees(){
         return service.getAll();
    }
    @GetMapping(value = "/employees/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    public EmployeeBO getEmployee(@PathVariable Integer id){
         return service.getEmployee(id);
    }

    @PostMapping("/employees")
    public void createNewEmployee(@RequestBody EmployeeBO newEmployee) {
        service.add(newEmployee);
    }

    @PutMapping("/employees")
    public void updateEmployee(@RequestBody EmployeeBO updatedEmployee) {
        service.update(updatedEmployee);
    }

    
}
