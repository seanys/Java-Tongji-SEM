package sample.components;

import javafx.beans.value.ChangeListener;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import sample.pages.QuizController;
import sample.util.MySqlUtil;

import javax.json.JsonObject;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class WritingController implements Initializable{
    public JsonObject writing_question;
    public TextArea writingTextArea;
    public VBox root,attachment;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        root.setSpacing(10);
        //随机选择作文题目
        try {
            writing_question = MySqlUtil.query(MySqlUtil.connection, "select * from question where type=4 order by rand() limit 1").getJsonArray("results").get(0).asJsonObject();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        QuizController.writing_id=Integer.parseInt(String.valueOf(writing_question.get("question_id")));
        QuizController.writing_answer="";

        for (String label_text: String.valueOf(writing_question.get("header")).split("\\\\n")){
            javafx.scene.control.Label label=new javafx.scene.control.Label(label_text);
            label.setWrapText(true);
            label.setPrefWidth(850);
            root.getChildren().add(label);
        }

        QuizController.questions_type.put(QuizController.writing_id,4);

        //绑定用户的输入
        writingTextArea.textProperty().addListener(e->{
            String actionString=writingTextArea.textProperty().toString();
            System.out.println(actionString);
            String string_array[]=actionString.split(":");
            string_array=string_array[3].split("]");
            string_array=string_array[0].split(" ");
            QuizController.writing_answer=string_array[1];
        });

        //加载参考范文
        if(QuizController.question_type==4){
            Label explanation=new Label();
            explanation.setText("分析：该作文题摘自高考，参考范文如下");
            attachment.getChildren().add(explanation);
            for (String label_text: String.valueOf(writing_question.get("answer")).split("\\\\n")){
                javafx.scene.control.Label label=new javafx.scene.control.Label(label_text);
                label.setWrapText(true);
                label.setPrefWidth(850);
                attachment.getChildren().add(label);
            }
            QuizController.question_type=0;
        }

    }
}
