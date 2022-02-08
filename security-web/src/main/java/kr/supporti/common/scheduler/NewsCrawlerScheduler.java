package kr.supporti.common.scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public class NewsCrawlerScheduler {

    public static void main(String[] args) {
        NewsCrawlerScheduler newsCrawler = new NewsCrawlerScheduler();
        newsCrawler.crawl("거리두기");
    }

    // WebDriver
    private WebDriver driver;
    private WebDriverWait wait;
    // Properties
    public static final String WEB_DRIVER_ID = "webdriver.chrome.driver";
    public static final String WEB_DRIVER_PATH = "src/main/resources/static/resources/lib/chromedriver/chromedriver2.exe";

    // 크롤링 할 URL
    private String base_url;
    private String keyword;

    public NewsCrawlerScheduler() {
        super();

        // System Property SetUp
        System.setProperty(WEB_DRIVER_ID, WEB_DRIVER_PATH);

        // Driver SetUp
        driver = new ChromeDriver();
        base_url = "https://search.naver.com/search.naver?where=news&sm=tab_jum&query=";
    }

    public List<Map<String, String>> crawl(String keyword) {
        List<Map<String, String>> newsList = new ArrayList<>();
        try {
            // get page (= 브라우저에서 url을 주소창에 넣은 후 request 한 것과 같다)
            try {
                driver.get(base_url);
                Thread.sleep(1000);
                // 검색창찾아서 원하는 키워드 입력
                WebElement search = driver.findElement(By.id("nx_query"));
                search.sendKeys(keyword);
                // 검색버튼 클릭
                WebElement searchBtn = driver.findElement(By.className("bt_search"));
                searchBtn.submit();
                System.out.println("검색완료");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            // 옵션 버튼 클릭
//            WebElement optionBtn = driver.findElement(By.className("option_filter"));
            WebElement optionBtn = driver.findElement(By.xpath("//*[@id=\"snb\"]/div[1]/div/div[2]"));
            optionBtn.click();
//            //1일 버튼 클릭
            WebElement day = driver.findElement(By.xpath("//*[@id=\"snb\"]/div[2]/ul/li[2]/div/div[1]/a[3]"));
            day.click();
            int cnt = 0, num = 1;
            boolean nextFlag = true;
            while (nextFlag) {
                // 뉴스 가져오기
                List<WebElement> listNews = driver
                        .findElements(By.xpath("//*[@id=\"main_pack\"]/section[1]/div/div[2]/ul/li"));
                for (WebElement list : listNews) {
                    Map<String, String> map = new WeakHashMap<>();
                    map.put("title", num + ") " + list.findElement(By.className("news_tit")).getText());
                    map.put("link", list.findElement(By.className("news_tit")).getAttribute("href"));
                    System.out.println(list.findElement(By.className("news_tit")).getText());
                    System.out.println(list.findElement(By.className("news_tit")).getAttribute("href"));
                    System.out.println();
                    newsList.add(map);
                    num++;
                }
                cnt++;
                String btnNext = driver.findElement(By.className("btn_next")).getAttribute("aria-disabled");
                System.out.println(btnNext);
                if (btnNext.equals("true")) {
                    nextFlag = false;
                } else {
                    nextFlag = true;
                    driver.findElement(By.className("btn_next")).click();
                }
                System.out.println(nextFlag);
                // 2페이지까지 크롤링 후 나가기
                if (cnt == 2)
                    break;
            }

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            driver.close();
        }
        return newsList;

    }
}
