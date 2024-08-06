package com.mooland.bj;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.stereotype.Service;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

@Service
public class AfreecaTVCrawler {
    public BJDTO regitbjcheck(String bjid) throws Exception {

        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 헤드리스 모드
        options.addArguments("--disable-gpu"); // GPU 비활성화 (Linux에서는 필요할 수 있음)
        options.addArguments("--no-sandbox"); // 샌드박스 비활성화 (Linux에서는 필요할 수 있음)
        options.addArguments("--disable-dev-shm-usage"); // /dev/shm 사용 비활성화 (Linux에서는 필요할 수 있음)
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("pageLoadStrategy", "none"); 
        WebDriver driver = new ChromeDriver(options.merge(caps));



        try {

            driver.get("https://bj.afreecatv.com/" + bjid);

     
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

            // 닉네임과 ID를 포함하는 요소를 찾습니다.
            WebElement nickElement;
            try {
                nickElement = driver.findElement(By.className("nick"));
            } catch (Exception e) {
                // nickElement가 없으면 null 반환
                return null;
            }

            WebElement h2Element;
            try {
                h2Element = nickElement.findElement(By.tagName("h2"));
            } catch (Exception e) {
                // h2Element가 없으면 null 반환
                return null;
            }

            WebElement idElement;
            try {
                idElement = nickElement.findElement(By.className("id"));
            } catch (Exception e) {
                // idElement가 없으면 null 반환
                return null;
            }

            String nickname = h2Element.getText();
            String id = idElement.getText();

            // 괄호를 제거한 ID
            id = id.replace("(", "").replace(")", "");

            // BJDTO 객체 생성 및 데이터 설정
            BJDTO dto = new BJDTO();
            dto.setBJName(nickname);
            dto.setBJID(id);

            // 입력된 BJID와 크롤링한 ID가 일치하는지 확인
            if (id.equals(bjid)) {
                return dto;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // WebDriver 종료
            driver.quit();
        }

        return null;
    }
}
