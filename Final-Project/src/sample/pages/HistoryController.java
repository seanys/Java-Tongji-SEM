package sample.pages;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import sample.MainController;
import sample.storage.HistoryRecord;
import sample.storage.User;
import sample.util.MySqlUtil;
import sample.util.StringWays;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;


public class HistoryController implements Initializable {

    private MainController mainController;
    public AnchorPane root;
    public FlowPane flow_root;
    public static int clicked_quiz_id;
    public List<HistoryRecord> history_list=new ArrayList<HistoryRecord>();

    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadHistory();
    }

    //加载到历史的页面
    public void navHistoryQuiz(ActionEvent actionEvent) throws SQLException, IOException {
        String actionString=actionEvent.getTarget().toString();
        String string_array[]=actionString.split("=");
        string_array=string_array[1].split("_");
        clicked_quiz_id=Integer.parseInt(string_array[0]);
        HistoryRecord record_object=new HistoryRecord();
        for(int i=0;i<history_list.size();i++){
            record_object=history_list.get(i);
            if(record_object.quiz_id==clicked_quiz_id){
                break;
            }
        }
        if(record_object.type==1){
            mainController.navQuiz("history_paper");
        }else{
            mainController.navQuiz("history");
        }
    }

    //该页面的循环加载
    public void loadHistory() {
        JsonObject result= null;
        System.out.println("输出初始children");
        System.out.println(flow_root.getChildren());

        flow_root.getChildren().setAll();
        history_list=new ArrayList<HistoryRecord>();
        System.out.println("输出后续的children");
        System.out.println(flow_root.getChildren());
        flow_root.setVgap(30);
        flow_root.setHgap(50);
        try {
            result = MySqlUtil.query(MySqlUtil.connection, "select * from history_quiz where user_id=1 order by quiz_id desc");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        JsonArray history_array= result.getJsonArray("results");
        System.out.println(history_array);

        //进行历史情况的数据存储
        for(int i=0;i<history_array.size();i++){
            HistoryRecord record=new HistoryRecord();
            record.achieve_time= StringWays.getRealString(String.valueOf(history_array.get(i).asJsonObject().get("achieve_time")));
            record.quiz_id= Integer.parseInt(String.valueOf(history_array.get(i).asJsonObject().get("quiz_id")));
            record.learn_section= Integer.parseInt(String.valueOf(history_array.get(i).asJsonObject().get("learn_section")));
            record.type= Integer.parseInt(String.valueOf(history_array.get(i).asJsonObject().get("type")));
            record.current_rate= Double.parseDouble(StringWays.getRealString(String.valueOf(history_array.get(i).asJsonObject().get("current_rate"))));
            history_list.add(record);
        }

        //进行循环加载
        for(int i=0;i<history_list.size();i++){
            HistoryRecord record_object=history_list.get(i);

            JFXDialogLayout recordDialog=new JFXDialogLayout();
            recordDialog.styleProperty().setValue("-fx-background-color: WHITE; -fx-background-radius: 3;-fx-margin:50px;");
            Label headLabel=new Label(record_object.achieve_time+"完成   "+record_object.current_rate*100+"分");
            String body_text=(record_object.current_rate<0.6?"测试情况不佳":"测试情况良好")+"，整体正确率"+record_object.current_rate;
            if(record_object.learn_section==0){
                body_text=body_text+"，属于复习测试;";
            }else{
                body_text=body_text+"，属于第"+record_object.learn_section+"部分测试;";
            }
            String encourageArray[]={"再接再厉","加油","继续努力","你很优秀"};
            int random=(int)(Math.random()*4);
            body_text=body_text+encourageArray[random];
            Label bodyLabel=new Label(body_text);
            recordDialog.setHeading(headLabel);
            recordDialog.setBody(bodyLabel);

            JFXButton detailButton = new JFXButton("查看详情");
            detailButton.getStyleClass().add("dialog-accept");
            detailButton.styleProperty().setValue("-fx-text-fill:WHITE;-fx-background-color:#5264AE;-fx-font-size:14px;");
            detailButton.setId(record_object.quiz_id+"_quiz");
            recordDialog.setActions(detailButton);

            detailButton.setOnAction(
                    e-> {
                        try {
                            navHistoryQuiz(e);
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });

            flow_root.getChildren().add(recordDialog);
        }
        System.out.println("输出最后的children");
        System.out.println(flow_root.getChildren());
    }
}
