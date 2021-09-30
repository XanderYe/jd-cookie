package cn.xanderye.util;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Created on 2020/9/2.
 *
 * @author XanderYe
 */
@Slf4j
public class JavaFxUtil {

    private static TextArea logArea = null;

    /**
     * 日志方法首先需要在Controller的initialize中执行
     * @param textArea
     * @return void
     * @author XanderYe
     * @date 2020/9/4
     */
    public static void initLog(TextArea textArea) {
        logArea = textArea;
    }

    /**
     * 打印日志
     * @param msg
     * @return void
     * @author XanderYe
     * @date 2020/9/4
     */
    public static void log(String msg) {
        log.info(msg);
        msg = msg.trim() + "\r\n";
        if (logArea != null) {
            String finalMsg = msg;
            Platform.runLater(() -> logArea.appendText(finalMsg));
        }
    }

    /**
     * 确认弹窗
     * @param header
     * @param message
     * @return boolean
     * @author XanderYe
     * @date 2020/9/2
     */
    public static boolean confirmDialog(String header, String message) {
        ButtonType confirm = new ButtonType("确定", ButtonBar.ButtonData.YES);
        ButtonType cancel = new ButtonType("取消", ButtonBar.ButtonData.NO);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, confirm, cancel);
        alert.setTitle("确认");
        alert.setHeaderText(header);
        Optional<ButtonType> buttonType = alert.showAndWait();
        return buttonType.orElse(cancel).getButtonData().equals(ButtonBar.ButtonData.YES);
    }

    /**
     * 信息弹窗
     * @param header
     * @param message
     * @return boolean
     * @author XanderYe
     * @date 2020/9/2
     */
    public static void alertDialog(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("信息");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * 错误弹窗
     * @param header
     * @param message
     * @return boolean
     * @author XanderYe
     * @date 2020/9/2
     */
    public static void errorDialog(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();
    }
    /**
     * 警告弹窗
     * @param header
     * @param message
     * @return boolean
     * @author XanderYe
     * @date 2020/9/2
     */
    public static void warnDialog(String header, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("警告");
        alert.setHeaderText(header);
        alert.setContentText(message);
        alert.show();
    }
}
