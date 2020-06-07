package sample.components;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import sample.pages.QuizController;
import sample.util.MySqlUtil;
import sample.util.StringWays;

import javax.json.JsonArray;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

//阅读理解由于存在前面的文章还有多个选择题，所以通过module_id进行整体加载
public class ComprehensionController implements Initializable {
    int module_id,question_id;
    public JsonArray questions=null;
    public VBox root;
    public Label header;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        module_id= QuizController.module_id;
        try {
            questions = MySqlUtil.query(MySqlUtil.connection, "select * from question where module_id="+module_id).getJsonArray("results");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        AnchorPane pane= new AnchorPane();

        for (String label_text: QuizController.module_header.split("\\\\n")){
            Label label=new Label(label_text);
            label.setWrapText(true);
            label.setPrefWidth(850);
            root.getChildren().add(label);
        }

        //加载图片部分
        System.out.println(QuizController.module_attachment);
        Image image = new Image(QuizController.module_attachment);
        ImageView imageView = new ImageView();
        imageView.setImage(image);
        root.getChildren().add(imageView);

        //通过choose来镶嵌加载
        for(int j=0;j<questions.size();j++){
            QuizController.question_id=Integer.parseInt(String.valueOf(questions.get(j).asJsonObject().get("question_id")));
            QuizController.question_header= StringWays.getRealString(String.valueOf(questions.get(j).asJsonObject().get("header")));
            QuizController.right_answers.put(QuizController.question_id,StringWays.getRealString(String.valueOf(questions.get(j).asJsonObject().get("answer"))));
            QuizController.questions_type.put(QuizController.question_id,3);
            try {
                pane = FXMLLoader.load(getClass().getResource("../components/Choose.fxml"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            root.getChildren().add(pane);
            if(QuizController.question_type==3){
                Label explanation=new Label();
                explanation.setText("分析：该题属于阅读理解，正确答案为"+QuizController.right_answers.get(QuizController.question_id));
                root.getChildren().add(explanation);
            }
        }
        QuizController.question_type=0;
    }
}
