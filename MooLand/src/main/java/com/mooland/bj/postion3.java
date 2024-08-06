package com.mooland.bj;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class postion3 {
    private static final Logger logger = LoggerFactory.getLogger(postion3.class);

    public BJDTO getLolPosition(String LOLnickname) {
        Pattern pattern = Pattern.compile("([^#]+)#([^#]+)");
        if (LOLnickname != null) {
            Matcher matcher = pattern.matcher(LOLnickname);
            String searchlol = null;
            String searchlol1 = null;
            if (matcher.find()) {
                searchlol1 = matcher.group(1);
                searchlol = matcher.group(2);
            }

            String LOL_DEEP_URL = "https://fow.kr/find/" + searchlol1 + "-" + searchlol;
            String LOL_DEEP_URL2 = "https://www.op.gg/summoners/kr/" + searchlol1 + "-" + searchlol;
            logger.info("크롤링 시작");

            WebDriverManager.chromedriver().setup();
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless");
            options.addArguments("--disable-gpu");
            options.addArguments("disable-gpu");
            options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
            options.addArguments("--disable-extensions"); // 확장 프로그램 비활성화
            options.addArguments("--disable-popup-blocking"); // 팝업 차단 비활성화
            options.addArguments("--disable-infobars"); // 정보 표시줄 비활성화
            options.addArguments("--disable-notifications"); // 알림 비활성화
            WebDriver driver = new ChromeDriver(options);
            WebDriver driver2 = new ChromeDriver(options);
            

            try {
                // 첫 번째 페이지 로드 및 처리
                Document doc = loadPage(driver, LOL_DEEP_URL, "span[style=\"color:red;\"]");
                String btierF = extractTier(doc);

                // 두 번째 페이지 로드 및 처리
                driver.get(LOL_DEEP_URL2);
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("div.positions ul li"))); // 필요한 요소가 로드될 때까지 기다림
                String pageSource2 = driver.getPageSource();
                Document doc2 = Jsoup.parse(pageSource2);

                List<Element> topTwoLiElements = extractTopPositions(doc2);

                String MP = topTwoLiElements.get(0).select("div.position img").attr("alt");
                String SP = extractSecondaryPosition(topTwoLiElements);

                BJDTO dto = new BJDTO();
                dto.setLOLBTier(btierF);
                dto.setLOLMPosition(MP);
                dto.setLOLSPosition(SP);
                dto.setLOLNickName(LOLnickname);
                return dto;

            } catch (Exception e) {
                logger.error("크롤링 중 예외 발생", e);
                return null;
            } finally {
                driver.quit();
            }
        }
        return null;
    }

    private Document loadPage(WebDriver driver, String url, String cssSelector) throws InterruptedException {
        driver.get(url);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(cssSelector))); // 필요한 요소가 로드될 때까지 기다림
        String pageSource = driver.getPageSource();
        return Jsoup.parse(pageSource);
    }

    private String extractTier(Document doc) {
        Elements spanElements = doc.select("span[style=\"color:red;\"]");
        for (Element span : spanElements) {
            if (span.text().contains("S14_1:")) {
                String btier = span.text();
                return btier.split(": ")[1];
            }
        }
        return null;
    }

    private List<Element> extractTopPositions(Document doc2) {
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
        return sortedLiElements.subList(0, 2);
    }

    private String extractSecondaryPosition(List<Element> topTwoLiElements) {
        String style = topTwoLiElements.get(1).select("div.gauge").attr("style");
        String heightStr = style.replaceAll("[^0-9.]", "");
        if (!heightStr.equals("0")) {
            return topTwoLiElements.get(1).select("div.position img").attr("alt");
        } else {
            return "없음";
        }
    }

    private static double extractHeight(Element li) {
        String style = li.select("div.gauge").attr("style");
        String heightStr = style.replaceAll("[^0-9.]", ""); // 숫자와 '.'만 추출
        return Double.parseDouble(heightStr);
    }
}
