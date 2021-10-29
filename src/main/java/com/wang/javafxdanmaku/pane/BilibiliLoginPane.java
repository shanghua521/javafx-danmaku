package com.wang.javafxdanmaku.pane;

import com.wang.javafxdanmaku.GlobalData;
import com.wang.javafxdanmaku.MyCookieStore;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.concurrent.Worker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Optional;

public class BilibiliLoginPane extends AnchorPane {
    public final SimpleBooleanProperty loginSucceedProperty = new SimpleBooleanProperty(false);

    private final WebView webView = new WebView();
    private final WebEngine engine = webView.getEngine();

    public BilibiliLoginPane() {
        super();
        java.net.CookieManager manager = new CookieManager(GlobalData.applicationContext.getBean(MyCookieStore.class), CookiePolicy.ACCEPT_ALL);
        java.net.CookieHandler.setDefault(manager);

        webView.setZoom(0.8);
        webView.setPrefSize(800, 380);
        engine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                var document = engine.getDocument();
                if (document != null) {
                    Optional.ofNullable(document.getElementById("internationalHeader")).ifPresent(element -> element.setAttribute("style", "display: none"));
                    try {
                        engine.executeScript("document.getElementsByClassName('international-footer')[0].style='display:none'");
                        engine.executeScript("document.getElementsByClassName('top-banner')[0].style='display:none'");
                    } catch (netscape.javascript.JSException ignored) {
                    }
                }
            }
        });
        engine.locationProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            if (newValue.startsWith("https://www.bilibili.com/") || newValue.equals("https://passport.bilibili.com/account/security#/home")) {
                loginSucceedProperty.setValue(true);
            }
        });
        engine.load("https://passport.bilibili.com/login");
        this.getChildren().add(webView);
    }
}
