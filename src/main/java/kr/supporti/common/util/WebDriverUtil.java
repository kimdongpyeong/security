package kr.supporti.common.util;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class WebDriverUtil {

    public static ChromeDriver openWeb(String driverPath, String url) {
        Path path = Paths.get(System.getProperty("user.dir"), driverPath); // 현재 package의

        // WebDriver 경로 설정
        System.setProperty("webdriver.chrome.driver", path.toString());

        // WebDriver 옵션 설정
        ChromeOptions options = new ChromeOptions();
//        options.addArguments("--start-maximized");            // 전체화면으로 실행
        options.addArguments("headless"); // 최소화화면으로실행
        options.addArguments("window-size=1920,1100");
        options.addArguments("--disable-popup-blocking"); // 팝업 무시
        options.addArguments("--disable-default-apps"); // 기본앱 사용안함

        // WebDriver 객체 생성
        ChromeDriver driver = new ChromeDriver(options);

        // 웹페이지 요청
        driver.get(url);
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)"); // 스크롤 내리기

        return driver;
    }

}
