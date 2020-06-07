package sample;


import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import sample.pages.HistoryController;
import sample.pages.QuizController;
import sample.pages.SettingController;
import sample.pages.UserAchieveController;
import sample.storage.User;
import sample.util.MySqlUtil;
import sample.util.TimeTread;

import javax.print.DocFlavor;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;


public class MainController implements Initializable {

    @FXML
    public AnchorPane rightAnchor;
    @FXML
    public JFXListView<String> SideList;

    public AnchorPane SettingPane,HistoryPane,UserAchievePane,QuizPane;
    public HistoryController historyController;
    public QuizController quizController;
    public SettingController settingController;
    public UserAchieveController userAchieveController;

    public String quizType="normal";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadPage("UserAchieve");
        rightAnchor.getChildren().setAll(UserAchievePane);

        //预加载几个页面
        loadPage("History");
        loadPage("Setting");
        loadPage("Quiz");

        //增加多线程的时间记录程序
        TimeTread timeRecord = new TimeTread( "Thread-1");
        timeRecord.start();

    }

    public void loadPage(String page){
        URL location = getClass().getResource("pages/"+page+".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(location);
        fxmlLoader.setBuilderFactory(new JavaFXBuilderFactory());

        AnchorPane newPane=null;
        try {
            newPane = fxmlLoader.load(location.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //加载几个页面，并且将页面的主类指向这个MainController，让他们可以调用该页面的函数
        if(page=="History"){
            HistoryPane=newPane;
            historyController=fxmlLoader.getController();
            historyController.injectMainController(this);
        }else if(page=="UserAchieve"){
            UserAchievePane=newPane;
            userAchieveController=fxmlLoader.getController();
            userAchieveController.injectMainController(this);
        }else if(page=="Setting"){
            SettingPane=newPane;
            settingController=fxmlLoader.getController();
            settingController.injectMainController(this);
        }else if(page=="Quiz"){
            QuizPane=newPane;
            quizController=fxmlLoader.getController();
            quizController.injectMainController(this);
            quizController.injectHistoryController(historyController);

        }else{
            return;
        }
    }


    //左侧list点击的绑定跳转
    public void navHistory() {
        System.out.println("navHistory");
        rightAnchor.getChildren().setAll(HistoryPane);
        ini();
    }

    public void navUserAchieve() {
        System.out.println("navUserAchieve");
        rightAnchor.getChildren().setAll(UserAchievePane);
        ini();
    }

    public void navSetting() {
        System.out.println("navSetting");
        rightAnchor.getChildren().setAll(SettingPane);
        ini();
    }

    //QuizController的数据的初始化，来进行页面的加载
    public void ini(){
        QuizController.if_paper=false;
        QuizController.if_history=false;
        QuizController.if_review=false;
        QuizController.answers=new HashMap<Integer, String>();
    }

    //绑定跳转的页面，每个不同的类别都会调用quizController中不同函数
    public void navQuiz(String type) throws SQLException, IOException {
        System.out.println("navQuiz");
        if(type=="paper"){
            quizController.loadPaper();
        }else if(type=="quiz"){
            quizController.loadNormalQuiz();
        }else if(type=="history"){
            quizController.loadHistoryQuiz();
        }else if(type=="review"){
            quizController.if_review=true;
            quizController.loadHistoryQuiz();
        }else if(type=="history_paper"){
            quizController.loadHistoryPaper();
        }else{
            quizController.loadNormalQuiz();
        }
        rightAnchor.getChildren().setAll(QuizPane);
    }

}
