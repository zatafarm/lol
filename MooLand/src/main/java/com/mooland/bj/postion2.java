package com.mooland.bj;



import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.bonigarcia.wdm.WebDriverManager;


@Service
public class postion2 {
    private static final Logger logger = LoggerFactory.getLogger(postion2.class);
    
    @Autowired
    private BjMapper mapper;
    

	

    public void fetchLolStreamData2() {

    	int bjlist = mapper.bjint();
    	Pattern pattern = Pattern.compile("([^#]+)#([^#]+)");
    	for (int a = 79; a<= bjlist ; a++) {
    		  String LOLnickname = mapper.LOLNickName(a);
    		  if(LOLnickname != null) {
    			  Matcher matcher = pattern.matcher(LOLnickname);
              String searchlol = null;
              String searchlol1 = null;
              if (matcher.find()) {
                  searchlol1 = matcher.group(1);
                  searchlol = matcher.group(2);
              }

        String LOL_DEEP_URL = "https://fow.kr/find/" + searchlol1 + "-" + searchlol;
        String LOL_DEEP_URL2 = "https://www.op.gg/summoners/kr/" + searchlol1 + "-" + searchlol;
        logger.info("크롤링 실행");
        String btierF = null;

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(); 
        WebDriver driver2 = new ChromeDriver();

        try {
            // 첫 번째 웹사이트 처리
            driver.get(LOL_DEEP_URL);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            wait.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            String pageSource = driver.getPageSource();
            Document doc = Jsoup.parse(pageSource);

            // 두 번째 웹사이트 처리
            driver2.get(LOL_DEEP_URL2);
            WebDriverWait wait2 = new WebDriverWait(driver2, Duration.ofSeconds(10));
            wait2.until(ExpectedConditions.jsReturnsValue("return document.readyState === 'complete'"));
            String pageSource2 = driver2.getPageSource();
            Document doc2 = Jsoup.parse(pageSource2);

            // 첫 번째 웹사이트에서 span 요소 선택
            Elements spanElements = doc.select("span[style=\"color:red;\"]");

            // 두 번째 웹사이트에서 li 요소 선택 및 정렬
            Elements liElements = doc2.select("div.positions ul li");
            List<Element> sortedLiElements = new ArrayList<>();
            for (Element li : liElements) {
                sortedLiElements.add(li);
            }
            sortedLiElements.sort((e1, e2) -> {
                double height1 = extractHeight(e1);
                double height2 = extractHeight(e2);
                return Double.compare(height2, height1); // 내림차순 정렬
            });

            // 상위 2개의 li 요소 추출
            List<Element> topTwoLiElements = sortedLiElements.subList(0, 2);
           String style = topTwoLiElements.get(1).select("div.gauge").attr("style");
            String heightStr = style.replaceAll("[^0-9.]", "");
            System.out.println(heightStr);
            String MP = topTwoLiElements.get(0).select("div.position img").attr("alt");
            String SP = null;
            if(!heightStr.equals("0")) {
            SP = topTwoLiElements.get(1).select("div.position img").attr("alt");
            }
            else {
            SP = "없음";
            }
            for (Element span : spanElements) {
                if (span.text().contains("S14_1:")) {
                    String btier = span.text();
                    String btier1 = btier.split(": ")[1];
                    btierF = btier1;
                }
            }


            if (LOLnickname != null) {
                BJDTO dto = new BJDTO();
                dto.setLOLBTier(btierF);
                dto.setLOLMPosition(MP);
                dto.setLOLSPosition(SP);
                dto.setLOLNickName(LOLnickname);
                mapper.UpdateBJtierandposition(dto);
            }
        } catch (Exception e) {
            // 예외 발생 시 적절한 로깅 및 처리 추가
            logger.error("크롤링 중 예외 발생", e);
        } finally {
            driver.quit(); // 첫 번째 브라우저 닫기
            driver2.quit(); // 두 번째 브라우저 닫기
        }
    	}
    	}
    }
    private static double extractHeight(Element li) {
        String style = li.select("div.gauge").attr("style");
        String heightStr = style.replaceAll("[^0-9.]", ""); // 숫자와 '.'만 추출
        return Double.parseDouble(heightStr);
    }
}
