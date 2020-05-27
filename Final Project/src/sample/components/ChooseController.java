package sample.components;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import sample.pages.QuizController;
import sample.storage.ArrayObjects;
import sample.util.MySqlUtil;
import sample.util.StringWays;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class ChooseController implements Initializable {

    public JsonObject query_result=null;
    public JsonArray choices= null,questions=null;
    public Label header;
    public VBox box;
    public int question_id,internal_id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        question_id= QuizController.question_id;
        internal_id= QuizController.internal_id;
        QuizController.answers.put(question_id,"");
        QuizController.internal_id++;
        ToggleGroup myToggleGroup=new ToggleGroup();;
        header.setText(QuizController.question_header);
        try {
            if(QuizController.if_judgement){
                //如果是判断题，那就加载正确与错误的选项
                RadioButton yes_choice=new RadioButton(),no_choice=new RadioButton();
                yes_choice.setText("Yes");
                no_choice.setText("No");
                yes_choice.styleProperty().setValue("-fx-padding:5px");
                no_choice.styleProperty().setValue("-fx-padding:5px");
                box.getChildren().add(yes_choice);
                box.getChildren().add(no_choice);
                yes_choice.setToggleGroup(myToggleGroup);
                no_choice.setToggleGroup(myToggleGroup);
                QuizController.questions_type.put(question_id,1);
            }else{
                //如果该问题是一个听力，那就就加载媒体
                if(QuizController.if_listening){
                    QuizController.questions_type.put(question_id,2);
                    System.out.println("加载听力");
                    Media media = new Media(new File("/Users/sean/Documents/MyProjects/IdeaProjects/1651413羊山/src/sample/media/video.mp4").toURI().toString());
                    MediaPlayer mediaPlayer = new MediaPlayer(media);
                    MediaView mediaView=new MediaView(mediaPlayer);
                    mediaView.setFitWidth(100);
                    mediaView.setFitHeight(100);
                    box.getChildren().add(mediaView);
                    //增加按钮，点击即可播放
                    JFXButton playVideoButton=new JFXButton("播放音频（仅可播放一次）");
                    playVideoButton.setOnAction(
                            e-> {
                                mediaPlayer.play();
                            });
                    box.getChildren().add(playVideoButton);
                }
                //加载具体的选项
                query_result = MySqlUtil.query(MySqlUtil.connection, "select * from question_choice where question_id="+QuizController.question_id);
                choices=query_result.getJsonArray("results");
                for(int i=0;i<choices.size();i++){
                    String content=StringWays.getRealString(String.valueOf(choices.get(i).asJsonObject().get("content")));
                    RadioButton new_choice=new RadioButton();
                    new_choice.setText(ArrayObjects.ChooseChar[i]+"."+content);
                    new_choice.styleProperty().setValue("-fx-padding:5px");
                    box.getChildren().add(new_choice);
                    new_choice.setToggleGroup(myToggleGroup);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        //绑定toggle单项选择
        myToggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            RadioButton selectedRadioButton = (RadioButton) myToggleGroup.getSelectedToggle();
            String selectedValue = selectedRadioButton.getText();
            QuizController.answers.put(question_id,dealChoice(selectedValue));
            System.out.println(QuizController.answers.get(question_id));
        });

        //如果是历史试卷，就会加载一下该问题的答案
        if(QuizController.if_history){
            myToggleGroup.selectToggle(myToggleGroup.getToggles().get(QuizController.CharToNum.get(QuizController.right_answers.get(question_id))));

        }
    }

    //增加答案前的选项
    public String dealChoice(String selectedValue){
        String A = "^A.*";
        String B = "^B.*";
        String C = "^C.*";
        if(Pattern.matches(A,selectedValue)){
            return "A";
        }else if(Pattern.matches(B,selectedValue)){
            return "B";
        }else if(Pattern.matches(C,selectedValue)){
            return "C";
        }else if(selectedValue.equals("Yes")){
            return "Yes";
        }else if(selectedValue.equals("No")){
            return "No";
        }else{
            return "D";
        }
    }

}
