package sample;

import com.sun.org.apache.xerces.internal.dom.ChildNode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.storage.User;
import sample.util.MySqlUtil;
import sample.util.StringWays;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.sql.*;

/*
*
*  启动程序页面
*
* */
public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        MySqlUtil.openConnection(); //数据库连接
        loadUser(); //加载用户信息

        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setTitle("英语辅助学习");

        primaryStage.setScene(new Scene(root, 1200, 700));
        //增加css引入
        root.getStylesheets().add("sample/css/jfoenix-main.css");
        root.getStylesheets().add("sample/css/jfoenix-design.css");
        root.getStylesheets().add("sample/css/jfoenix-components.css");
        root.getStylesheets().add("sample/css/main.css");

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);

    }

    public static void loadUser(){
        JsonObject result= null;
        try {
            result = MySqlUtil.query(MySqlUtil.connection, "select * from users where user_id=1");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JsonObject user= result.getJsonArray("results").get(0).asJsonObject();

        //更新本地存储的数据
        User.nickname= StringWays.getRealString(String.valueOf(user.get("nickname")));
        User.introduction= StringWays.getRealString(String.valueOf(user.get("introduction")));
        User.head_img= StringWays.getRealString(String.valueOf(user.get("head_img")));
        User.which_difficulty= StringWays.getRealString(String.valueOf(user.get("which_difficulty")));
        User.recommend_difficulty= Integer.parseInt(String.valueOf(user.get("recommend_difficulty")));
        User.freedom_difficulty= Integer.parseInt(String.valueOf(user.get("freedom_difficulty")));
        User.student_type= Integer.parseInt(String.valueOf(user.get("student_type")));

    }

    public static void updateHistory(){

    }

}
