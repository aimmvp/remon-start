package com.example.restapi;


import java.util.List;
import java.util.Set;
import java.io.IOException;
import java.net.URL;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.SilentCssErrorHandler;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebConsole;
import com.gargoylesoftware.htmlunit.html.FrameWindow;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;
import com.gargoylesoftware.htmlunit.html.parser.HTMLParserListener;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptEngine;
import com.gargoylesoftware.htmlunit.util.Cookie;

@Slf4j
@RestController
public class LoginController extends WebClient{

    // String loginUrl="https://swgssos.sktelecom.com/swing/skt/login.html"; //https://partnersso.sktelecom.com/swing/skt/login.html
    String loginUrl="https://partnersso.sktelecom.com/swing/skt/login.html";
    // String iid = "UX533";
    // String ipassword = "q1w2e3r4((";
    
    @GetMapping("loginChecker")
    //public boolean login (@RequestParam (value = "id", defaultValue = "UK664")  String id, @RequestParam (value = "password", defaultValue = "dbsdkek1!!") String password ) {
    public boolean isLogin(@RequestParam (value="iid") String rId, @RequestParam(value="ipwd") String rPwd) {
        setCssErrorHandler(new SilentCssErrorHandler());

        try(WebClient webClient = new WebClient()) {
            webClient.setHTMLParserListener(HTMLParserListener.LOG_REPORTER);
            webClient.setJavaScriptEngine(new JavaScriptEngine(webClient));
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.getCookieManager().setCookiesEnabled(true);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());
            webClient.getCache().setMaxSize(0);
            webClient.getOptions().setRedirectEnabled(true);
            // 쿠키 매니저 설정
            CookieManager cookieManager = webClient.getCookieManager();

            try {
                HtmlPage page = webClient.getPage(loginUrl);
                
                List<FrameWindow> frames = page.getFrames();
                HtmlPage formpage = null;
                for (FrameWindow frame : frames) {
                    log.error(frame.getFrameElement().getId());
                    if ("loginFormFrame".equals(frame.getFrameElement().getId())) {
                        formpage = (HtmlPage) frame.getEnclosedPage();
                    }
                }

                // ID와 Password 입력
                HtmlForm form = formpage.getFormByName("sso");
                HtmlTextInput idInput = form.getInputByName("USER");
                HtmlPasswordInput pwInput = (HtmlPasswordInput)form.getInputByName("PASSWORD");
                // idInput.type(iid);
                // pwInput.type(ipassword);

                idInput.type(rId);
                pwInput.type(rPwd);
                HtmlAnchor submitButton =  form.getFirstByXPath("//a[@class='submit']");
                //submitButton.click();
                HtmlPage nextPage = (HtmlPage)submitButton.click();
                String ssosession = webClient.getCookieManager().getCookie("SSOSESSION").getValue();
                
                //쿠키 추가
                // Cookie cookie = new Cookie("swings.sktelecom.com:18080","","SMLOGIN", "/", null, false);
                // cookieManager.addCookie(cookie);

                //webClient.waitForBackgroundJavaScript(5000);

                log.error("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                log.error("@@@@@@ nextPage",nextPage);
                // log.error("@@@@@@ cookie",cookie);

                log.error("@@@@@@ cookieManager",cookieManager);
                log.error("@@@@rId [{}]", rId);
                log.error("@@@@rPwd [{}]", rPwd);
                log.error("SMSESSION[{}]",  ssosession);
                //getID 'swing'

                //System.out.println(">>>>>>>>>>>>>>>>>>>nextPageUrl 2>>>>>>>>>>>>>>>>"+nextPageUrl);
                //webClient.getPage(new URL("https://"));
                //로긴 Error처리 
                //return URL 처리

                //Adding Cookie 값 가져오기
            } catch (FailingHttpStatusCodeException | IOException e) {
                //e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    //@GetMapping("getCookie")
    public String getCookieValue(String url) {
        try (final WebClient webClient = new WebClient()) {
            // 웹페이지에 접속
            webClient.getPage(url);

            // 현재 사용 중인 CookieManager에서 쿠키 가져오기
            Set<Cookie> cookies = webClient.getCookieManager().getCookies();

            // 쿠키 값 가져오기
            StringBuilder cookieValues = new StringBuilder();
            for (Cookie cookie : cookies) {
                cookieValues.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
            }
            return cookieValues.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}

