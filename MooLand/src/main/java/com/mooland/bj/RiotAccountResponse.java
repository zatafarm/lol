package com.mooland.bj;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class RiotAccountResponse {
    @JsonProperty("puuid")
    private String puuid;
    @JsonProperty("gameName")
    private String gameName;
    @JsonProperty("tagLine")
    private String tagLine;


 
}
