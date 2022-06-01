package com.oup.cachedemo.sbcachedemo.bo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.java.Log;

@Log
@Data
@AllArgsConstructor
public class EmployeeBO implements Serializable {

    @JsonProperty("id")
    private  Integer userId;
    @JsonProperty("name")
    private String name;
    
}
