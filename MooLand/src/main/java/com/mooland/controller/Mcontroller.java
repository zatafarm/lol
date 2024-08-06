package com.mooland.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.mooland.User.UserDTO;
import com.mooland.User.UserService;
import com.mooland.bj.AfreecaTVCrawler;
import com.mooland.bj.AllRiotAPIService;
import com.mooland.bj.BJDTO;
import com.mooland.bj.BjGameDto;
import com.mooland.bj.ImageDownloader;
import com.mooland.bj.PagingResponse;
import com.mooland.bj.RiotAPIService2;
import com.mooland.bj.SearchDto;
import com.mooland.bj.bjService;
import com.mooland.bj.postion2;
import com.mooland.bj.postion3;
import com.mooland.bj.searchbjdto;
import com.mooland.board.BoardDTO;
import com.mooland.board.BoardService;
import com.mooland.entry.gameDTO;
import com.mooland.entry.gameService;

@Controller
public class Mcontroller {
	@Autowired
    private  AllRiotAPIService rapi;

	@Autowired
    private postion2 po;
	@Autowired
    private bjService bs;
	@Autowired
    private gameService gs;
	@Autowired
    private UserService us;
	@Autowired
    private BoardService bas;
	@Autowired
    private ImageDownloader img;

	
    @GetMapping("/entry")
    public String entry() throws Exception{
        return "view/entry";
    }
    @GetMapping("/board")
    public String board(Model model) throws Exception{
    	List<BoardDTO> dto = bas.getboardlist();
    	model.addAttribute("boardlist", dto );
    	return "view/board";
    }
    @GetMapping("/boardregister")
    public String boardregister() throws Exception{
   
    	return "view/boardregit";
    }
    @GetMapping("/update/bjimg")
    public String bjimg() {
        try {
            List<BJDTO> list = bs.getbjdtoallimg();
            for (int a = 0; a < list.size(); a++) {
                String bjurl = list.get(a).getBJID();
                String fbjurl = bjurl.substring(0, 2);
                
                // 이미지 다운로드
                img.imgdown(bjurl, fbjurl);
                
                // 0.1초(100ms) 대기
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // 현재 스레드의 인터럽트 상태를 복원
            // 예외 처리 로직 추가
        }
        return "index";
    }

@PostMapping("/boardregit")
public String boardregit(@RequestParam("title") String title , @RequestParam("content") String content) throws Exception{
 	BoardDTO dto = new BoardDTO();
	dto.setContent(content);
	dto.setTitle(title);
	bas.insertboard(dto);
	return "redirect:/board";
}
    @GetMapping("/detail/{bno}")
    public String boarddetail(Model model, @PathVariable("bno") int bno) throws Exception{
    	BoardDTO dto = bas.getboarddetail(bno);
    	model.addAttribute("board", dto);

    	return "view/boarddetail";
    }
    
    @GetMapping("/detail/delete")
    public String boarddelete(Model model, @RequestParam("bno") int bno) throws Exception{
    	bas.deleteboard(bno);
    	return "redirect:/board";
    }
    @GetMapping("/detail/update")
    public String boardupdateform(Model model, @RequestParam("bno") int bno) throws Exception{
    	BoardDTO dto = bas.getboarddetail(bno);
    	model.addAttribute("board", dto);
    	return "view/boardupdate";
    }
    @PostMapping("/detail/update")
    public String boardupdate(Model model, @RequestParam("bno") int bno, @RequestParam("title") String title , @RequestParam("content") String content) throws Exception{
    	BoardDTO dto = new BoardDTO();
    	dto.setContent(content);
    	dto.setTitle(title);
    	dto.setBno(bno);
    	bas.updateboard(dto);
    	return "redirect:/detail/" + bno;
    }
    @GetMapping("/admin/bjid")
    public String adminbjid(Model model) throws Exception{
    	List<UserDTO> members = us.getmad();
    	model.addAttribute("members", members);
        return "view/adminpagebjid";
    }
    @GetMapping("/admin")
    public String admin() throws Exception{
  
        return "view/adminpage";
    }
    @GetMapping("/admin/bjlist")
    public String bjlist(Model model , RedirectAttributes redirectAttributes, @ModelAttribute("params") final SearchDto params) throws Exception{
    	   PagingResponse<BJDTO> response = bs.bjalllist(params);
          	model.addAttribute("response", response);
    	   return "view/adminpagebjlist";
    }

    @GetMapping("/update/position")
    public String position() throws Exception{
    	po.fetchLolStreamData2();
        return "redirect:/home";
    }
    @GetMapping("/update/puuid")
    public String puuid() {
        try {
            rapi.getPuuid();
        } catch (Exception e) {
            // 예외 처리 로직 (로그 기록, 에러 메시지 설정 등)
            e.printStackTrace(); // 예외를 콘솔에 출력
            return "redirect:/error"; // 에러 페이지로 리다이렉트
        }
        return "redirect:/home";
    }

    @GetMapping("/update/nickname")
    public String nickname() {
        try {
            rapi.getnickname();
        } catch (Exception e) {
            e.printStackTrace(); // 예외를 콘솔에 출력
            return "redirect:/error"; // 에러 페이지로 리다이렉트
        }
        return "redirect:/home";
    }

    @GetMapping("/update/bjtier")
    public String upbj() {
        try {
            rapi.getTIER();
        } catch (Exception e) {
            e.printStackTrace(); // 예외를 콘솔에 출력
            return "redirect:/error"; // 에러 페이지로 리다이렉트
        }
        return "redirect:/home";
    }

    @GetMapping("/mypage")
    public String mypage() throws Exception{
    
        return "view/mypage";
    }

    @GetMapping("/bjsearch")
    public String bjsearch(@RequestParam(value = "search", defaultValue = "default") String search, Model model , RedirectAttributes redirectAttributes, @ModelAttribute("params") final SearchDto params) throws Exception{
    	BJDTO bjdto = bs.getbjdto(search);
    	  if (bjdto == null) {
              redirectAttributes.addFlashAttribute("messagetop", "정보가 없습니다, BJ의 이름을 정확하게 입력해주세요");
              return "redirect:/home";
          }
    	String  tiersrc = "";
    	if(!bjdto.getLOLTier().equals("배치")) {
    	tiersrc = "/bootstrap/img/" + bjdto.getLOLTier() + ".png";
    	}
    	else {
    	tiersrc = "/bootstrap/img/unranked.png";
    	}
        String bjurl = bjdto.getBJID();
    	bjdto.setImgUrl("/bjimg/" + bjurl+ ".jpg");
        Pattern pattern = Pattern.compile("([^#]+)#([^#]+)");
	   	 Matcher matcher = pattern.matcher(bjdto.getLOLNickName());
        if (matcher.find()) {
           bjdto.setLOLNickName1(matcher.group(1));
           bjdto.setLOLNickName2(matcher.group(2));
        }
    	bjdto.setTiersrc(tiersrc);
    	BjGameDto gsmap2 = gs.getplayerwlint(search);
    	gsmap2.setRate(gsmap2.getWinRate());
    	List<gameDTO> vslist = gs.getvsplayename(search);
    	List<BjGameDto> dtolist = new ArrayList<BjGameDto>();
    	if (vslist.size() > 0) {
    	    int count = 0;
    	    for (int a = 0; a < vslist.size(); a++) {
    	        if (count >= 5) {break; // 최대 5명까지만 추가
    	        }
    	        BjGameDto adddto = gs.getvswlint(search, vslist.get(a).getBjname());
    	        adddto.setVsName(vslist.get(a).getBjname());
    	        adddto.setRate(adddto.getWinRate());
    	        // 승률이 40%에서 60% 사이인지 확인
    	        if (adddto.getRate() >= 40.0 && adddto.getRate() <= 60.0) {
    	        	if(adddto.getGames()>=10) {
    	            dtolist.add(adddto);
    	            count++;
    	        	}
    	        }
    	    }
    	}

    	   PagingResponse<searchbjdto> response = bs.getdata(search, params);
    	   List<searchbjdto> dtoList = new  ArrayList<searchbjdto>();
    	   if(response != null) {
    		   dtoList = response.getList();
               DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
               LocalDateTime now = LocalDateTime.now();
               DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
               
               for (searchbjdto dto : dtoList) {
                   boolean isWin = checkWinCondition(dto, search);
                   dto.setWins(isWin ? "승리" : "패배");

                   // 경기 날짜와 시간 비교
                   String gameDateTime = dto.getGamedate();
                   if (gameDateTime != null && !gameDateTime.isEmpty()) {
                       try {
                           LocalDateTime gameTime = LocalDateTime.parse(gameDateTime, formatter);

                           if (now.toLocalDate().equals(gameTime.toLocalDate())) {
                               // 같은 날인 경우 시간 차이 계산
                               Duration duration = Duration.between(gameTime, now);
                               long minutesAgo = duration.toMinutes();

                               if (minutesAgo >= 60) {
                                   long hoursAgo = minutesAgo / 60; // 시간을 계산
                                   dto.setTimeAgo(hoursAgo + "시간 전");
                               } else {
                                   dto.setTimeAgo(minutesAgo + "분 전");
                               }
                           } else {
                               // 날짜 차이 계산
                               LocalDate gameDate = gameTime.toLocalDate();
                               LocalDate currentDate = now.toLocalDate();
                               Period period = Period.between(gameDate, currentDate);

                               if (period.getYears() > 0) {
                                   dto.setTimeAgo(period.getYears() + "년 전");
                               } else if (period.getMonths() > 0) {
                                   dto.setTimeAgo(period.getMonths() + "달 전");
                               } else {
                                   dto.setTimeAgo(period.getDays() + "일 전");
                               }
                           }

                           // 초를 제외한 형식으로 변환하여 설정
                           dto.setGamedate(gameTime.format(outputFormatter));
                       } catch (Exception e) {
                           // 파싱 오류 처리
                           dto.setTimeAgo(null);
                           dto.setGamedate(null);
                       }
                   } else {
                       // null 또는 빈 문자열인 경우
                       dto.setTimeAgo(null);
                       dto.setGamedate(null);
                   }


        }
        response.setList(dtoList);
       	model.addAttribute("response", response);
    	   }
        // 원본 response 객체의 리스트를 수정된 dtoList로 갱신
  
    	model.addAttribute("bjdto", bjdto);
    	model.addAttribute("vslist", dtolist);
    	model.addAttribute("bjgamedto", gsmap2);
    	return "view/bjsearch";
    }
    
    private boolean checkWinCondition(searchbjdto dto, String search) {
        if (dto.getWin() == 1) {
            return dto.getLp1().equals(search) || 
                   dto.getLp2().equals(search) || 
                   dto.getLp3().equals(search) || 
                   dto.getLp4().equals(search) || 
                   dto.getLp5().equals(search);
        } else if (dto.getWin() == 2) {
            return dto.getRp1().equals(search) || 
                   dto.getRp2().equals(search) || 
                   dto.getRp3().equals(search) || 
                   dto.getRp4().equals(search) || 
                   dto.getRp5().equals(search);
        }
        return false;
    }
    
    @GetMapping("/bjregit")
    public String bjregisterF() throws Exception{
        return "view/bjregister";
    }
}


