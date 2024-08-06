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
public class AllRiotAPIService {
    private static final Logger logger = LoggerFactory.getLogger(AllRiotAPIService.class);

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
    
    
    
    @Scheduled(fixedRate = 200) 
    public void getTIER() {
        if (requestCount >= 100) {
            try {
                System.out.println("100번의 요청 후 2분 대기");
                Thread.sleep(6000);
                requestCount = 0;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

 
        try {
        	gettier();
            requestCount++;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("API 호출 중 예외 발생: " + e.getMessage());
        }
    }
    public void gettier() {
        int bjlist = mapper.bjint();
        BJDTO dto = new BJDTO();
        for (int a = 1; a <= bjlist; a++) {
        	String tier = null;
        	String rank = null;
        	String puuid = mapper.LOLpuuid(a);
        	System.out.println(puuid);
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
                   	        mapper.UpdateNtier(dto);
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
    public void getPuuid() {
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
            processRequest();
            requestCount++;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("API 호출 중 예외 발생: " + e.getMessage());
        }
    }

    private void processRequest() {
        String searchlol = null;
        String searchlol2 = null;
        int bjlist = mapper.bjint();
        BJDTO dto = new BJDTO();

        for (int a = 1; a <= bjlist; a++) {
            String puuidcheck = mapper.LOLpuuid(a);
            if (puuidcheck == null) {
                String LOLnickname = mapper.LOLNickName(a);
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
                            String puuid = responseEntity.getBody().getPuuid();
                            dto.setLOLpuuid(puuid);
                            dto.setLOLNickName(LOLnickname);
                            System.out.println(dto.getLOLpuuid());
                            System.out.println(dto.getLOLNickName());
                            mapper.UpdateBJpuuid(dto);
                            System.out.println("성공");
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