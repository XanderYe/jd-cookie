package cn.xanderye.controller;

import cn.xanderye.config.Config;
import cn.xanderye.util.JavaFxUtil;
import cn.xanderye.util.PropertyUtil;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.Cookie;
import com.teamdev.jxbrowser.chromium.CookieStorage;
import com.teamdev.jxbrowser.chromium.javafx.BrowserView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.net.URL;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;

/**
 * @author XanderYe
 * @date 2020/2/6
 */
@Slf4j
public class MainController implements Initializable {
    @FXML
    private BorderPane borderPane;
    @FXML
    private ComboBox<String> numBox;

    private final Config config = Config.getInstance();

    private static final String JD_URL = "https://home.m.jd.com/myJd/home.action";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        PropertyUtil.init();
        Browser browser = new Browser();
        clearCookie(browser);
        browser.setUserAgent("Mozilla/5.0 (iPhone; CPU OS 11_0 like Mac OS X) AppleWebKit/604.1.25 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
        config.setBrowser(browser);
        BrowserView view = new BrowserView(browser);
        borderPane.setCenter(view);
        browser.loadURL(JD_URL);
        String numbers = PropertyUtil.get("phone.numbers");
        if (null != numbers) {
            String[] numberArray = numbers.split(",");
            for (String s : numberArray) {
                numBox.getItems().add(s);
            }
        }
    }

    public void inputNum() {
        String num = numBox.getValue();
        if (num == null) {
            JavaFxUtil.errorDialog("错误", "请先选择号码");
            return;
        }
        Browser browser = config.getBrowser();
        browser.executeJavaScript("var number='" + num + "';var evt=new InputEvent('input',{inputType:'insertText',data:number,dataTransfer:null,isComposing:false});document.getElementsByClassName('acc-input mobile J_ping')[0].value=number;document.getElementsByClassName('acc-input mobile J_ping')[0].dispatchEvent(evt);document.getElementsByClassName('policy_tip-checkbox')[0].click();");
    }

    public void copyCookie() {
        CookieStorage cookieStorage = config.getBrowser().getCookieStorage();
        List<Cookie> cookieList = cookieStorage.getAllCookies();
        String ptKey = null;
        String ptPin = null;
        for (Cookie cookie : cookieList) {
            if (ptKey != null && ptPin != null) {
                break;
            }
            if ("pt_key".equals(cookie.getName())) {
                ptKey = cookie.getValue();
            }
            if ("pt_pin".equals(cookie.getName())) {
                ptPin = cookie.getValue();
            }
        }
        String cookie = MessageFormat.format("pt_key={0};pt_pin={1};", ptKey, ptPin);
        log.info("获取到cookie: {}", cookie);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable trans = new StringSelection(cookie);
        clipboard.setContents(trans, null);
        JavaFxUtil.alertDialog("通知", "cookie已复制到剪切板");
    }

    public void clearAndRefresh() {
        clearCookie(config.getBrowser());
        config.getBrowser().loadURL(JD_URL);
        log.info("清除cookie并刷新页面");
    }

    private void clearCookie(Browser browser) {
        browser.getCacheStorage().clearCache();
        browser.getLocalWebStorage().clear();
        browser.getSessionWebStorage().clear();
        //清除cookie
        CookieStorage cookieStorage = browser.getCookieStorage();
        List<Cookie> cookieList = cookieStorage.getAllCookies();
        for (Cookie cookie : cookieList) {
            cookieStorage.delete(cookie);
        }
        cookieStorage.save();
    }
}
