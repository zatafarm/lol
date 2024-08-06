package com.mooland.bj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class RiotAccountResponse3 {

    @JsonProperty("tier")
    private String tier;
    @JsonProperty("rank")
    private String rank;
    @JsonProperty("queueType")
    private String queueType;

    
    

 
}
