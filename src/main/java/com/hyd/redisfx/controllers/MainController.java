package com.hyd.redisfx.controllers;

import com.hyd.redisfx.App;
import com.hyd.redisfx.Fx;
import com.hyd.redisfx.controllers.client.JedisManager;
import com.hyd.redisfx.controllers.dialogs.ChangeDatabaseDialog;
import com.hyd.redisfx.controllers.tabs.AbstractTabController;
import com.hyd.redisfx.controllers.tabs.Tabs;
import com.hyd.redisfx.event.EventType;
import com.hyd.redisfx.i18n.I18n;
import javafx.event.ActionEvent;
import javafx.scene.control.Menu;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * (description)
 * created at 2017/3/14
 *
 * @author yidin
 */
public class MainController {

    static final Logger LOG = LoggerFactory.getLogger(MainController.class);

    public TabPane tabs;

    public Menu mnuRecentConnections;

    private Stage primaryStage;

    public void initialize() {
        App.setMainController(this);
        Tabs.setTabs(tabs);
        initializeTabs();
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.addEventHandler(WindowEvent.WINDOW_SHOWN, event -> {
            openConnectionManager(null);
        });
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    private void initializeTabs() {
        this.tabs.getTabs().forEach(tab -> {
            String tabName = (String) tab.getUserData();
            AbstractTabController tabController = Tabs.getTabController(tabName);
            if (tabController != null) {
                tabController.setTab(tab);
            }
        });

        App.getEventBus().on(EventType.ConnectionOpened, event -> {
            tabs.setVisible(true);
            updateTitle();
        });

        App.getEventBus().on(EventType.DatabaseChanged, event -> updateTitle());
    }

    private void updateTitle() {

        JedisManager.withJedis(jedis -> {
            primaryStage.setTitle(
                    I18n.getString("app_title") +
                            " - " + JedisManager.getHost() +
                            ":" + JedisManager.getPort() +
                            ":" + jedis.getDB());
        });
    }

    public void openConnectionManager(ActionEvent actionEvent) {
        String fxml = "/fxml/conn/ConnectionManager.fxml";
        Fx.showDialog(primaryStage, I18n.getString("title_conn_manager"), fxml);
    }

    public void openConnection(ActionEvent actionEvent) {
        String host = "localhost";
        int port = 6379;

        try {
            JedisManager.connect(host, port);
            App.getEventBus().post(EventType.ConnectionOpened);
        } catch (Exception e) {
            LOG.error("", e);
            Fx.error("连接失败", "连接到 " + host + ":" + port + " 失败：\n\n" + e.toString());
        }
    }

    public void changeDatabaseClicked() {
        ChangeDatabaseDialog dialog = new ChangeDatabaseDialog(App.getDatabases());
        dialog.show();
    }

    public void switchDatabaseClicked() {
    }

    public void clearDatabaseClicked() {
    }
}
