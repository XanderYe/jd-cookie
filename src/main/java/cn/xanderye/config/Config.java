package cn.xanderye.config;

import com.teamdev.jxbrowser.chromium.Browser;
import lombok.Data;

import java.util.List;

/**
 * @author XanderYe
 * @description:
 * @date 2021/8/19 20:36
 */
@Data
public class Config {

    private static final Config CONFIG = new Config();

    private String userDir = System.getProperty("user.dir");

    private Browser browser;

    private Config() {
    }

    public static Config getInstance() {
        return CONFIG;
    }
}
