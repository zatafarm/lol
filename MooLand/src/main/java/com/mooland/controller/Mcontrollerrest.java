package com.mooland.controller;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.mooland.Tier.TierDTO;
import com.mooland.Tier.TierService;
import com.mooland.User.EmailSendService;
import com.mooland.User.UserDTO;
import com.mooland.User.UserService;
import com.mooland.bj.AfreecaTVCrawler;
import com.mooland.bj.BJDTO;
import com.mooland.bj.BjGameDto;
import com.mooland.bj.ImageDownloader;
import com.mooland.bj.RiotAPIService2;
import com.mooland.bj.bjService;
import com.mooland.bj.postion3;
import com.mooland.entry.gameDTO;
import com.mooland.entry.gameDTO2;
import com.mooland.entry.gameService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;



@RestController
public class Mcontrollerrest {
	@Autowired
	private UserService serivce;
	@Autowired
	private TierService tier;
	@Autowired
	private gameService gs;
	@Autowired
	private bjService bs;
	@Autowired
    private  RiotAPIService2 rap;
	@Autowired
    private postion3 ps;
	@Autowired
    private ImageDownloader img;
	
    private final AtomicInteger progress = new AtomicInteger(0);
	@Autowired
	 private AfreecaTVCrawler af;
    @GetMapping("/check-username")
    public boolean checkUsername(@RequestParam("username") String username) throws Exception {
        return serivce.checkid(username);
    }
    @PostMapping("/admin/changerole")
    public ResponseEntity<String> changeRole(@RequestParam("loginId") String loginId, @RequestParam("role") String role) {
        try {
            
            serivce.updateUserRole(loginId, role);
   
            return ResponseEntity.ok("변경 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("변경 실패");
        }
    }
    
    @PostMapping("/admin/deletebj")
    public ResponseEntity<String> deletebj(@RequestParam("bjId") String bjId) {
        try {
            
            serivce.deletebj(bjId);

            return ResponseEntity.ok("삭제 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("삭제 실패");
        }
    }
    
    @GetMapping("/api/bj/progress/status")
    @ResponseBody
    public int getProgress() {
        return progress.get();
    }

    @PostMapping("/api/bj/bjregit")
    public String bjregister(@RequestParam("bjname") String bjName,
                             @RequestParam("bjid") String bjId,
                             @RequestParam("loln") String lolNickname,
                             Model model) throws Exception {
  	  progress.set(10);
    		BJDTO dtosame = bs.getbjdtoid(bjId);
    		if(dtosame != null) {
    			return "same";
    		}
    		BJDTO dtosame2 = bs.getbjdto(bjName);
    		if(dtosame2 != null) {
    			return "same";
    		}
    		BJDTO dtosame3 = bs.getbjdtolol(lolNickname);
    		if(dtosame3 != null) {
    			return "samelol";
    		}
    	  BJDTO dto = af.regitbjcheck(bjId);
          if(dto != null && dto.getBJName().equals(bjName)) {
    try {
        String bjurl = bjId;
        String fbjurl = bjurl.substring(0, 2);
        img.imgdown(bjurl, fbjurl);
    	
    	  progress.set(20);
          
          // LoL PUUID 및 티어 정보 가져오기
          String puuid = rap.getPuuid(lolNickname);
          if(puuid == null) {
        	  return "null";
          }
          BJDTO dto2 = rap.getTIER(puuid);
          dto.setLOLpuuid(puuid);
          progress.set(40);
          dto.setLOLTier(dto2.getLOLTier());
          dto.setLOLrank(dto2.getLOLrank());
          progress.set(60);

          // 포지션 정보 가져오기
          BJDTO dto3 = ps.getLolPosition(lolNickname);
          dto.setLOLBTier(dto3.getLOLBTier());
          dto.setLOLMPosition(dto3.getLOLMPosition());
          dto.setLOLSPosition(dto3.getLOLSPosition());
          progress.set(80);
          dto.setLOLNickName(lolNickname);
          bs.intsertbj(dto);
          progress.set(100);
        return "ok";
    } catch (Exception e) {
        // 예외 처리: 오류 메시지를 출력하고 "null"을 반환
        System.err.println("삽입 중 오류 발생: " + e.getMessage());
        return "null";
    }
    }
          return "bjnull";
    }


    @GetMapping("/entryloaddata")
    public Map<String, Object> loadData(HttpSession session) throws Exception {
        String idString = (String)session.getAttribute("id");
        gameDTO dto = gs.getdata(idString);
        if(dto != null) {
        int gameid = dto.getGameid();
        gameDTO2 redteam = gs.teamdata(gameid, dto.getLtn());
        gameDTO2 blueteam = gs.teamdata(gameid, dto.getRtn());
        Map<String, Object> response = new HashMap<>();
        response.put("t1p1", redteam.getP1());
        response.put("t1p2", redteam.getP2());
        response.put("t1p3", redteam.getP3());
        response.put("t1p4", redteam.getP4());
        response.put("t1p5", redteam.getP5());
        
        response.put("t2p1", blueteam.getP1());
        response.put("t2p2", blueteam.getP2());
        response.put("t2p3", blueteam.getP3());
        response.put("t2p4", blueteam.getP4());
        response.put("t2p5", blueteam.getP5());
        
        response.put("t1", dto.getLtn());
        response.put("t2", dto.getRtn());

        return response;
        }
        return null;
    }
    
    @GetMapping("/check-nickname")
    public boolean checknickname(@RequestParam("nickname") String nickname) throws Exception {
        return serivce.checknickname(nickname);
    }
    
    @GetMapping("/check-email")
    public boolean checkemail(@RequestParam("email") String email) throws Exception {
        return serivce.checkemail(email);
    }
    

    @Autowired
    private EmailSendService emailSendService;

    @PostMapping("/signup/email")
    public Map<String, String> mailSend(@RequestBody @Valid UserDTO emailRequestDto) {
        String code = emailSendService.joinEmail(emailRequestDto.getEmail());

        Map<String, String> response = new HashMap<>();
        response.put("code", code);

        return response;
    }
    
    @PostMapping("/signup/emailAuth")
    public String authCheck(@RequestBody @Valid UserDTO emailCheckDto) {
        try {
            Boolean checked = emailSendService.checkAuthNum(emailCheckDto.getEmail(), emailCheckDto.getAuthNum());
            if (checked) {
                return "이메일 인증 성공!";
            } else {
                return null;
            }
        } catch (Exception e) {
            throw e; 
        }
    }
    
    @PostMapping("/entry/creategame")
    public int creategame(@RequestBody gameDTO game,HttpSession session) throws Exception {
    	String idString = (String)session.getAttribute("id");
    	int createid = Integer.parseInt(idString);
    	String RedTeamName = game.getRtn();
    	String BlueTeamName = game.getLtn();
    	int mlt = game.getMlt();
    	gameDTO dto = new gameDTO();
    	dto.setLtn(RedTeamName);
    	dto.setRtn(BlueTeamName);
    	dto.setMlt(mlt);
    	dto.setCreateid(createid);
    	gs.creategame(dto);
    	return dto.getGameid();
    }
    @PostMapping("/entry/finalwin")
    public String finalwin(@RequestBody  Map<String, String> game,HttpSession session) throws Exception {
    	String gameid = game.get("sendgameid");
    	String finalwin = game.get("win");
    	gameDTO dto = new gameDTO();
    	int finalwinint = 0;
    	if(finalwin.equals("red")) {
    		finalwinint = 1;
    		dto.setFin(1);    	
        	
    	}
    	else if (finalwin.equals("blue")) {
    		finalwinint = 2;
    		dto.setFin(1);    	
        	
    	}
    	else if (finalwin.equals("tie")) {
    		dto.setFin(2);
    	}

    	dto.setGameid(Integer.parseInt(gameid));
    	dto.setWin(finalwinint);
    	dto.setLtn(game.get("t1"));
    	dto.setRtn(game.get("t2"));
    	dto.setLscore(Integer.parseInt(game.get("redscore")));
    	dto.setRscore(Integer.parseInt(game.get("bluescore")));
    	gs.finalgame(dto);
    	return "ok";
    	
    }
    @PostMapping("/entry/save")
    public String savegame(@RequestBody Map<String, String> data,HttpSession session) throws Exception {
        Map<String, String> data2 = new HashMap<>();
        String changegameid = data.get("sendgameid");
        data2.put("gameid", changegameid);
        for (int b = 1; b <3 ; b++) {
        	for(int a = 1 ; a <6  ; a++) {
        		String c = "t" + b + "p" + a;
        		data2.put("p" + a , data.get(c));
        		
        	}
        	data2.put("team", data.get("t"+ b));
            gs.savegame(data2);
            data2 = new HashMap<>();
            data2.put("gameid", changegameid);
        }

    	return "ok";
    	
    }
    @GetMapping("/searchvsbj")
    public BjGameDto searchbj(@RequestParam("bjname") String vsName,@RequestParam("playerbjName") String playerName) throws Exception {
        BjGameDto dto = gs.getvswlint(playerName, vsName);
        dto.setPlayerName(playerName);
        dto.setVsName(vsName);
        dto.setRate(dto.getWinRate());
    	return dto;
    	
    }


    @GetMapping("/getAdditionalData")
    public Map<String, Object> getAdditionalData(
            @RequestParam("playerName") String playerName, 
            @RequestParam(value = "vsName", required = false, defaultValue = "defaultVsName") String vsName) throws Exception {
    	String records = "";
    	
    	Map<String, Object> response2 = new HashMap<>();
    	List<Map<String, String>> gsmap = gs.getplayerwl(playerName);
    	BjGameDto gsmap2 = gs.getplayerwlint(playerName);
    	BjGameDto gsmapvsint = new BjGameDto();
        if(gsmap.size() >= 1) {
        for (int a = 0 ; a < gsmap.size() ; a++) {
        	if(a<5) {
        		String winteam = gsmap.get(a).get("result");
        	if(winteam.equals("win")) {
        		records += "승";
        	}
        	else {
        		records += "패";
        	}
        	}
        	else {
        		break;
        	}
     
        }

        }
        if(!vsName.equals("undefined")) {      
    	gsmapvsint = gs.getvswlint(playerName, vsName);
        response2.put("gsmapvs", gsmapvsint);
        }
        response2.put("totalgame", records);
        response2.put("Sgsmap2", gsmap2);
        
        
        return response2;
    }

    
    @PostMapping("/entry/setwin")
    public String setgamewin(@RequestBody Map<String, String> data,HttpSession session) throws Exception {
    	String win = data.get("winteam");
        gameDTO windto = new gameDTO();
        gameDTO losedto = new gameDTO();
        String changegameid = data.get("sendgameid");
        windto.setGameid(Integer.parseInt(changegameid));
        losedto.setGameid(Integer.parseInt(changegameid));
        
    	gameDTO dto = new gameDTO();
    	dto.setGameid(Integer.parseInt(changegameid));
    	dto.setLtn(data.get("t1"));
    	dto.setRtn(data.get("t2"));
    	dto.setLscore(Integer.parseInt(data.get("redscore")));
    	dto.setRscore(Integer.parseInt(data.get("bluescore")));
    	gs.set2game(dto);
    	if(win.equals("red") || win.equals("blue")) {
    		int b = 0;
    		int c = 0;
    		if(win.equals("red")) {
    			b = 1;
    			c = 2;
    		}
    		else {
    			b = 2;
    			c = 1;
    		}
        	for(int a = 1 ; a <6  ; a++) {

        	windto.setBjname(data.get("t"+ b + "p" + a));
        	windto.setRB(b);
        	windto.setSetwin(1);

        	losedto.setBjname(data.get("t"+ c + "p" + a));
        	losedto.setRB(c);
        	losedto.setSetwin(2);
        	if(a == 1) {
        		windto.setLane("TOP");
        		losedto.setLane("TOP");
        	}
        	else if(a == 2) {
        		windto.setLane("JUNGLE");
        		losedto.setLane("JUNGLE");
        	}
        	else if(a == 3) {
        		windto.setLane("MID");
        		losedto.setLane("MID");
        	}
        	else if(a == 4) {
        		windto.setLane("ADC");
        		losedto.setLane("ADC");
        	}
        	else if(a == 5) {
        		windto.setLane("SUPPORT");
        		losedto.setLane("SUPPORT");
        	}

    		 gs.setgame(windto);
    		  gs.setgame(losedto);
        	}
    	}

    	return "ok";
    	
    }
   
    @GetMapping("api/live")
    public Map<String, String> getLiveStream(@RequestParam("BJID") String bjid) throws Exception {
        Map<String, String> response = new HashMap<>();
        boolean isBroadcasting = true;
    	 try {
         
             String url = "https://play.afreecatv.com/" + bjid; 
             Document document = Jsoup.connect(url).get();
             Element divElement = document.select("div.broadcast_title").first();

             if (divElement != null) {
                 Elements spanElements = divElement.select("span");

                 for (Element spanElement : spanElements) {
                     String spanText = spanElement.text();
                     if (spanText.contains("방송중이지 않습니다.")) {
                         isBroadcasting = false;
                         break;
                     }
                 }
                 

                 if (isBroadcasting) {
                 }
         }
    	 }catch (IOException e) {
             e.printStackTrace();
         }
    	if (isBroadcasting == true) {
        response.put("url", "https://play.afreecatv.com/" + bjid);
        return response;
    	}
    	else {
    		return null;
    	}
    }
    @GetMapping("/getPlayerData")
    public Map<String, Object> getPlayerData(@RequestParam(value = "playerName", required = true) String playerName) throws Exception {
        String url = "/bootstrap/img/defult.png";
        Map<String, Object> response = new HashMap<>();
	    Pattern pattern = Pattern.compile("([^#]+)#([^#]+)");
        int a = tier.getonec(playerName);
        if (a == 1) {
            TierDTO tier2 = tier.getdto(playerName);
            String bjurl = tier2.getBJID();
       	 Matcher matcher = pattern.matcher(tier2.getLOLNickName());
         if (matcher.find()) {
            tier2.setLOLNickName1(matcher.group(1));
            tier2.setLOLNickName2(matcher.group(2));
         }
            if (bjurl != null) {
                url = "/bjimg/" + bjurl+ ".jpg";
                tier2.setImgUrl(url);
            }
            response.put("data", tier2);
        } else {
            response.put("data", new HashMap<>());  // 기본값을 설정하여 클라이언트에서 처리할 수 있게 함
        }
        return response;
    }

    @GetMapping("/search-bj")
    public List<String> getSearchSuggestions(@RequestParam("search") String search) throws Exception {
        List<String> data = bs.getbjsearchlist(search);
        return data.stream()
                   .filter(item -> item.toLowerCase().contains(search.toLowerCase()))
                   .collect(Collectors.toList());
    }


}
