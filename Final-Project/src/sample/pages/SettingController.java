package sample.pages;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXToggleButton;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.input.DragEvent;
import javafx.util.Callback;
import sample.MainController;
import sample.storage.ArrayObjects;
import sample.storage.User;
import sample.util.MySqlUtil;
import sample.util.TimeTread;

import javax.json.JsonObject;
import java.net.URL;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class SettingController implements Initializable {

    @FXML
    public Label nicknameLabel;
    @FXML
    public Label introductionLabel;
    @FXML
    public JFXSlider difficultySlider;
    @FXML
    public JFXComboBox<String> studentTypeComboBox;
    public CheckBox difficultyCheckbox;

    private MainController mainController;

    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //按照用户的信息刷新页面的显示
        nicknameLabel.setText(User.nickname);
        introductionLabel.setText(User.introduction);
        studentTypeComboBox.setPromptText(ArrayObjects.Grades[User.student_type]);
        String recommend_difficulty="recommend_difficulty";
        System.out.println(recommend_difficulty=="recommend_difficulty");
        System.out.println("which_difficulty:"+User.which_difficulty);

        //预加载用户的难度选择
        if(User.which_difficulty.equals(recommend_difficulty)){
            System.out.println(User.which_difficulty);
            System.out.println(User.recommend_difficulty);
            difficultyCheckbox.setSelected(false);
            difficultySlider.disableProperty().setValue(Boolean.TRUE);
            difficultySlider.setValue(User.recommend_difficulty);
        }else{
            System.out.println(User.which_difficulty);
            System.out.println(User.freedom_difficulty);
            difficultyCheckbox.setSelected(true);
            difficultySlider.setValue(User.freedom_difficulty);
        }

        //增加难度选择的监听器
        difficultyCheckbox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if(User.which_difficulty.equals("recommend_difficulty")){
                System.out.println("选择自主难度");
                System.out.println("freedom_difficulty："+User.freedom_difficulty);
                System.out.println("recommend_difficulty："+User.recommend_difficulty);
                difficultySlider.disableProperty().setValue(Boolean.FALSE);
                difficultySlider.setValue(User.freedom_difficulty);
                User.which_difficulty="freedom_difficulty";
                try {
                    MySqlUtil.update(MySqlUtil.connection, "update users set which_difficulty='freedom_difficulty' where user_id = "+User.user_id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }else{
                System.out.println("选择推荐难度");
                System.out.println("freedom_difficulty："+User.freedom_difficulty);
                System.out.println("recommend_difficulty："+User.recommend_difficulty);
                System.out.println(User.recommend_difficulty);
                difficultySlider.disableProperty().setValue(Boolean.TRUE);
                difficultySlider.setValue(User.recommend_difficulty);
                User.which_difficulty="recommend_difficulty";
                try {
                    MySqlUtil.update(MySqlUtil.connection, "update users set which_difficulty='recommend_difficulty' where user_id = "+User.user_id);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        //增加自由难度变化的监听器
        difficultySlider.setValueFactory(new Callback<JFXSlider, StringBinding>() {
            @Override
            public StringBinding call(JFXSlider arg0) {
                return Bindings.createStringBinding(new java.util.concurrent.Callable<String>(){
                    @Override
                    public String call() throws Exception {
                        DecimalFormat df = new DecimalFormat("#.0");
                        Double new_value=difficultySlider.getValue();
                        if(new_value!=User.recommend_difficulty){
                            System.out.println(Math.abs(new_value.intValue()-User.freedom_difficulty));
                            User.freedom_difficulty=new_value.intValue();
                            System.out.println(new_value.intValue());
                            System.out.println("freedom_difficulty："+User.freedom_difficulty);
                            MySqlUtil.update(MySqlUtil.connection, "update users set freedom_difficulty="+new_value+" where user_id = "+User.user_id);
                        }
                        return df.format(difficultySlider.getValue());
                    }
                }, difficultySlider.valueProperty());
            }
        });

    }


    //绑定更新用户的年级
    public void studentTypeChange(ActionEvent actionEvent) throws SQLException {
        System.out.println("触发变更");
        String typeString=studentTypeComboBox.getPromptText();
        int typeInteger=0;
        if(typeString=="高一"){
            typeInteger=0;
        }else if(typeString=="高一"){
            typeInteger=1;
        }else if(typeString=="高二"){
            typeInteger=2;
        }else{
            typeInteger=3;
        }
        MySqlUtil.update(MySqlUtil.connection, "update users set student_type= "+typeInteger+" where user_id = "+User.user_id);
    }

}
