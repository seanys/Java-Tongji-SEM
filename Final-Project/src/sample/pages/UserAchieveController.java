package sample.pages;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.shape.Arc;
import sample.MainController;
import sample.storage.User;
import sample.util.MySqlUtil;
import sample.util.StringWays;

import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class UserAchieveController implements Initializable {

    @FXML
    public Arc historyCurrentRate;
    @FXML
    public Arc achieveProgress;

    private MainController mainController;

    public JFXComboBox sectionChooseComboBox;
    public AnchorPane root;

    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            updateAchieve();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //预加载用户的进度和正确率，采用一个圆显示
        historyCurrentRate.setLength(User.history_current_rate*360);
        achieveProgress.setLength(User.achieve_progress*360);
        sectionChooseComboBox.setPromptText("第"+User.current_section+"章");
    }

    public void beginQuiz() throws IOException, SQLException {
        mainController.navQuiz("quiz");
    }

    public void beginPaper() throws IOException, SQLException {
        mainController.navQuiz("paper");
    }

    public void beginReview() throws IOException, SQLException {
        mainController.navQuiz("review");
    }

    //用户每次完成试卷后都会调用这个函数，来更新正确率和进度
    public static void updateAchieve() throws SQLException {
        JsonObject result= null;
        result = MySqlUtil.query(MySqlUtil.connection, "select avg(current_rate) as avg_current from history_quiz where user_id="+User.user_id);
        User.history_current_rate=Double.parseDouble(String.valueOf(result.getJsonArray("results").get(0).asJsonObject().get("avg_current")));
        result = MySqlUtil.query(MySqlUtil.connection, "select max(learn_section) as max_section from history_quiz where user_id="+User.user_id);
        User.achieve_progress=Double.parseDouble(String.valueOf(result.getJsonArray("results").get(0).asJsonObject().get("max_section")))/10.0;
        User.current_section=Integer.parseInt(String.valueOf(result.getJsonArray("results").get(0).asJsonObject().get("max_section")))+1;
    }


    //用户自主选择进度
    public void ChooseSection(ActionEvent actionEvent) {
        System.out.println(sectionChooseComboBox.getValue());
        String string_array[]=sectionChooseComboBox.getValue().toString().split("'");
        String result=string_array[1].replace("第","");
        result=result.replace("章","");
        User.current_section=Integer.parseInt(result);
        System.out.println("result:"+result);
    }
}
