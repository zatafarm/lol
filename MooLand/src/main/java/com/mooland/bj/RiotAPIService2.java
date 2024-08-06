package com.mooland.bj;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mybatis.logging.Logger;
import org.mybatis.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import jakarta.annotation.PostConstruct;

@Service
public class RiotAPIService2 {
    private static final Logger logger = LoggerFactory.getLogger(RiotAPIService2.class);

    @Value("${riot.api.key}")
    private String key;

    @Autowired
    private BjMapper mapper;

    private String riot = "https://asia.api.riotgames.com";
    private final RestTemplate restTemplate = new RestTemplate();
    private final RestTemplate restTemplate2 = new RestTemplate();
    private String riot2 = "https://kr.api.riotgames.com";
    private final Pattern pattern = Pattern.compile("([^#]+)#([^#]+)");
    private int requestCount = 0;
    
    
    
    public BJDTO getTIER(String puuid) {
        if (requestCount >= 100) {
            try {
                System.out.println("100번의 요청 후 2분 대기");
                Thread.sleep(120000);
                requestCount = 0; 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            BJDTO dto = gettier(puuid);
            requestCount++;
            return dto;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("API 호출 중 예외 발생: " + e.getMessage());
            return null;
        }
    }
    
    public BJDTO gettier(String puuid) {
        BJDTO dto = new BJDTO();
        	String tier = null;
        	String rank = null;
        	if(puuid != null) {
        	       boolean success = false;
                   while (!success) {
                   	try {
                   	    rateLimitCheck();
                   	    String url = riot2 + "/lol/summoner/v4/summoners/by-puuid/" + puuid + "?api_key=" + key;
                   	    ResponseEntity<RiotAccountResponse2> responseEntity = restTemplate.getForEntity(url, RiotAccountResponse2.class);
                   	    if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                   	        String id = responseEntity.getBody().getId();
                   	        String tierurl = riot2 + "/lol/league/v4/entries/by-summoner/" + id + "?api_key=" + key;
                   	        ResponseEntity<RiotAccountResponse3[]> responseEntity1 = restTemplate2.getForEntity(tierurl, RiotAccountResponse3[].class);
                   	        RiotAccountResponse3[] tierResponses = responseEntity1.getBody();
                   	        boolean foundRankedFlex = false;

                   	        if (tierResponses.length > 0) {
                   	            for (RiotAccountResponse3 tierResponse : tierResponses) {
                   	                if ("RANKED_SOLO_5x5".equals(tierResponse.getQueueType())) {
                   	                    tier = tierResponse.getTier();
                   	                    rank = tierResponse.getRank();
                   	                    foundRankedFlex = true;
                   	                    break;
                   	                }
                   	            }
                   	        }

                   	        if (!foundRankedFlex) {
                   	            tier = "배치";
                   	            rank = "";
                   	        }

                   	        success = true;
                   	        dto.setLOLTier(tier);
                   	        dto.setLOLpuuid(puuid);
                   	        dto.setLOLrank(rank);
                   	     	return dto;
                   	    }
                   	} catch (HttpClientErrorException.TooManyRequests e) {
                   	    handleRateLimit(e);
                   	   	return null;
                   	} catch (Exception e) {
                   	    e.printStackTrace();
                   	    System.out.println("API 호출 중 예외 발생: " + e.getMessage());
                   	    success = true; 
                   	   	return null;
                   	}
                       }
            }
         	return dto;
        }
    
    
    @Scheduled(fixedRate = 50) 
    public void getnick() {
        if (requestCount >= 100) {
            try {
                System.out.println("100번의 요청 후 2분 대기");
                Thread.sleep(120000);
                requestCount = 0; 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


        try {
        	getnickname();
            requestCount++;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("API 호출 중 예외 발생: " + e.getMessage());
        }
    }
    
    public void getnickname() {
        int bjlist = mapper.bjint();
        BJDTO dto = new BJDTO();
        for (int a = 1; a <= bjlist; a++) {
        	String puuid = mapper.LOLpuuid(a);
        	if(puuid != null) {
            boolean success = false;
            while (!success) {
                try {
                    rateLimitCheck();
                    String url = riot + "/riot/account/v1/accounts/by-puuid/" + puuid + "?api_key=" + key;
                    ResponseEntity<RiotAccountResponse> responseEntity = restTemplate.getForEntity(url, RiotAccountResponse.class);
                    if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                        String gamename  = responseEntity.getBody().getGameName();
                        String tag = responseEntity.getBody().getTagLine();
                        String finalname = gamename + "#" + tag;
                        		dto.setLOLpuuid(puuid);
                        dto.setLOLNickName(finalname);
                        System.out.println(dto.getLOLpuuid());
                        mapper.Updatelolnickname(dto);
                        System.out.println("롤 닉네임 교체 성공");
                        success = true;
                    }
                } catch (HttpClientErrorException.TooManyRequests e) {
                    handleRateLimit(e);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("API 호출 중 예외 발생: " + e.getMessage());
                    success = true; 
                }
                }
            }
        }
        }
    
    
    
    @Scheduled(fixedRate = 50) 
    public String getPuuid(String lolnickname) {
        if (requestCount >= 100) {
            try {
                System.out.println("100번의 요청 후 2분 대기");
                Thread.sleep(120000); 
                requestCount = 0; 
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String puuid = null;

        try {
          puuid = processRequest(lolnickname);
            requestCount++;
            return puuid;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("API 호출 중 예외 발생: " + e.getMessage());
            
        }
        return puuid;
    }

    private String processRequest(String LOLnickname) {
        String searchlol = null;
        String puuid = null;
        String searchlol2 = null;
                if(LOLnickname != null) {
                Matcher matcher = pattern.matcher(LOLnickname);
                if (matcher.find()) {
                    searchlol = matcher.group(1);
                    searchlol2 = matcher.group(2);
                }

                boolean success = false;
                while (!success) {
                    try {
                        rateLimitCheck();
                        String url = riot + "/riot/account/v1/accounts/by-riot-id/" + searchlol + "/" + searchlol2 + "?api_key=" + key;
                        ResponseEntity<RiotAccountResponse> responseEntity = restTemplate.getForEntity(url, RiotAccountResponse.class);
                        if (responseEntity.getStatusCode().is2xxSuccessful() && responseEntity.getBody() != null) {
                            puuid = responseEntity.getBody().getPuuid();
                            success = true;
                            return puuid;
                    
                        }
                    } catch (HttpClientErrorException.TooManyRequests e) {
                        handleRateLimit(e);
                    } catch (Exception e) {
                        e.printStackTrace();
                        System.out.println("API 호출 중 예외 발생: " + e.getMessage());
                        success = true; // 다른 예외는 그냥 넘어가도록 처리
                    }
                }
            }
                return puuid;
    }

    private void rateLimitCheck() {

    }

    private void handleRateLimit(HttpClientErrorException.TooManyRequests e) {
        String retryAfter = e.getResponseHeaders().getFirst(HttpHeaders.RETRY_AFTER);
        if (retryAfter != null) {
            try {
                int retryAfterSeconds = Integer.parseInt(retryAfter);
                System.out.println("Rate limit exceeded. Retrying after " + retryAfterSeconds + " seconds.");
                Thread.sleep(retryAfterSeconds * 1000L);
            } catch (NumberFormatException | InterruptedException ex) {
                ex.printStackTrace();
            }
        } else {
            e.printStackTrace();
            System.out.println("Rate limit exceeded but no Retry-After header found.");
        }
    }
}