package sample.pages;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import sample.MainController;
import sample.storage.User;
import sample.util.MySqlUtil;
import sample.util.StringWays;

import javax.json.JsonArray;
import javax.json.JsonObject;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class QuizController implements Initializable {

    @FXML
    public ScrollPane scroll;
    private MainController mainController;
    private HistoryController historyController;
    public static int question_id,module_id,internal_id=0,quiz_id,writing_id,question_type=0;
    public static String question_header,module_header,module_attachment,writing_answer;
    public String question_section;
    public JsonObject query_result=null;
    public JsonArray modules= null,questions=null,query_array=null;
    public static Map<Integer,String> answers = new HashMap<Integer, String>();
    public static Map<Integer,Integer> questions_type = new HashMap<Integer, Integer>();
    public static Map<Integer,String> history_answers = new HashMap<Integer, String>();
    public static Map<Integer,String> right_answers = new HashMap<Integer, String>();
    public static Map<String,Integer> CharToNum = new HashMap<String, Integer>();
    public JFXButton bottom_button;
    public static boolean if_history=false,if_judgement=false,if_listening=false,if_review=false,if_paper=false;

    public void injectMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public void injectHistoryController(HistoryController historyController) { this.historyController = historyController; }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CharToNum.put("A",0);
        CharToNum.put("B",1);
        CharToNum.put("C",2);
        CharToNum.put("D",3);
    }

    public void loadNormalQuiz() throws SQLException, IOException {
        System.out.println(User.current_section);
        query_result = MySqlUtil.query(MySqlUtil.connection, "select * from question where learn_section="+User.current_section+" and difficulty>="+(getFinalDifficulty()-40)+" and difficulty<= "+(getFinalDifficulty()+40)+" order by rand() limit 5");
        questions = query_result.getJsonArray("results");
        System.out.println(questions);
        loadQuiz(if_history);
    }

    public void loadHistoryQuiz() throws SQLException, IOException {
        if(if_review){
            query_result = MySqlUtil.query(MySqlUtil.connection, "select * from history_quiz where user_id="+User.user_id+" and type=0 order by current_rate asc");
            quiz_id=Integer.parseInt(String.valueOf(query_result.get("results").asJsonArray().get(0).asJsonObject().get("quiz_id")));
            System.out.println("quiz_id:"+quiz_id);
        }else{
            if_history=true;
            quiz_id=HistoryController.clicked_quiz_id;
        }
        query_result = MySqlUtil.query(MySqlUtil.connection, "select history_answer.*,question.learn_section,question.tip,question.module_id,question.header,question.answer,question.difficulty from history_answer left join question on question.question_id=history_answer.question_id where history_answer.quiz_id="+quiz_id);
        JsonArray merge_array= query_result.getJsonArray("results");
        questions=merge_array;
        for(int i=0;i<merge_array.size();i++){
            int internal_question_id=Integer.parseInt(String.valueOf(merge_array.get(i).asJsonObject().get("question_id")));
            String internal_question_answer=String.valueOf(merge_array.get(i).asJsonObject().get("content"));
            history_answers.put(internal_question_id,StringWays.getRealString(internal_question_answer));
        }
        loadQuiz(if_history);
        if(!if_review){
            bottom_button.setText("查看完成");
            bottom_button.setOnAction(e->mainController.navHistory());
            if_history=false;
        }
    }

    public void loadHistoryPaper() throws IOException, SQLException {
        quiz_id=HistoryController.clicked_quiz_id;
        VBox box=new VBox();
        VBox inner_box;
        AnchorPane pane= new AnchorPane();
        //加载听力部分
        query_result = MySqlUtil.query(MySqlUtil.connection, "select history_answer.*,question.learn_section,question.module_id,question.header,question.answer,question.difficulty from history_answer left join question on question.question_id=history_answer.question_id where history_answer.type=2 and history_answer.quiz_id="+quiz_id);
        questions = query_result.getJsonArray("results");
        System.out.println(questions);
        if_listening=true;
        question_type=2;
        inner_box=loadQuestions(true);
        box.getChildren().add(inner_box);
        if_listening=false;
        //加载判断题部分
        query_result = MySqlUtil.query(MySqlUtil.connection, "select history_answer.*,question.learn_section,question.module_id,question.header,question.answer,question.difficulty from history_answer left join question on question.question_id=history_answer.question_id where history_answer.type=1 and history_answer.quiz_id="+quiz_id);
        questions = query_result.getJsonArray("results");
        if_judgement=true;
        question_type=1;
        inner_box=loadQuestions(true);
        box.getChildren().add(inner_box);
        if_judgement=false;
        //加载阅读理解
        module_id = Integer.parseInt(String.valueOf(MySqlUtil.query(MySqlUtil.connection, "select history_answer.*,question.learn_section,question.module_id,question.header,question.answer,question.difficulty from history_answer left join question on question.question_id=history_answer.question_id where history_answer.type=3 and history_answer.quiz_id="+quiz_id).getJsonArray("results").get(0).asJsonObject().get("module_id")));
        query_result=MySqlUtil.query(MySqlUtil.connection, "select * from module where module_id= "+module_id).getJsonArray("results").get(0).asJsonObject();
        module_header =String.valueOf(query_result.get("header"));
        module_attachment =StringWays.getRealString(String.valueOf(query_result.get("attachment_url")));
        question_type=3;
        pane = FXMLLoader.load(getClass().getResource("../components/Comprehension.fxml"));
        box.getChildren().add(pane);
        //加载作文
        question_type=4;
        pane = FXMLLoader.load(getClass().getResource("../components/Writing.fxml"));
        box.getChildren().add(pane);
        scroll.setContent(box);
        bottom_button.setText("查看完成");
        bottom_button.setOnAction(e->mainController.navHistory());
        if_history=false;
    }

    public void loadPaper() throws IOException, SQLException {
        if_paper=true;
        VBox box=new VBox();
        VBox inner_box;
        AnchorPane pane= new AnchorPane();
        //加载听力
        query_result = MySqlUtil.query(MySqlUtil.connection, "select * from question where type=3");
        questions = query_result.getJsonArray("results");
        if_listening=true;
        inner_box=loadQuestions(false);
        box.getChildren().add(inner_box);
        if_listening=false;
        //加载判断题
        query_result = MySqlUtil.query(MySqlUtil.connection, "select * from question where type=2 order by rand() limit 2");
        questions = query_result.getJsonArray("results");
        if_judgement=true;
        inner_box=loadQuestions(false);
        box.getChildren().add(inner_box);
        if_judgement=false;
        //加载阅读理解
        query_result=MySqlUtil.query(MySqlUtil.connection, "select * from module order by rand() limit 1 ").getJsonArray("results").get(0).asJsonObject();
        module_id = Integer.parseInt(String.valueOf(query_result.get("module_id")));
        module_header =String.valueOf(query_result.get("header"));
        module_attachment =StringWays.getRealString(String.valueOf(query_result.get("attachment_url")));
        pane = FXMLLoader.load(getClass().getResource("../components/Comprehension.fxml"));
        box.getChildren().add(pane);
        //加载作文题
        pane = FXMLLoader.load(getClass().getResource("../components/Writing.fxml"));
        box.getChildren().add(pane);
        scroll.setContent(box);
        System.out.println(right_answers);
    }

    //用户提交答案
    public void submitAnswer(ActionEvent actionEvent) throws SQLException {
        //判断是否已经回答完成
        for (Integer key : answers.keySet()) {
            if(answers.get(key)==""){
                System.out.println("存在未选择");
                Remind(scroll.getScene().getWindow(),"温馨提醒","您还有内容没有完成",0);
                return;
            }
        }
        if(if_paper){
            if(writing_answer==""){
                Remind(scroll.getScene().getWindow(),"温馨提醒","您还有内容没有完成",0);
                return;
            }
        }
        System.out.println(answers);
        //记录正确与错误率
        Double right_num=0.0,wrong_num=0.0,current_rate;
        for (Integer key : answers.keySet()) {
            if(answers.get(key)==right_answers.get(key)){
                right_num++;
            }else{
                wrong_num++;
            }
        }
        current_rate=2/(right_num+wrong_num);
        //判断是否是回顾习题
        if(if_review){
            MySqlUtil.update(MySqlUtil.connection, "update history_quiz set current_rate="+current_rate +" where user_id="+User.user_id+" and quiz_id="+quiz_id);
            for (Integer key : answers.keySet()) {
                MySqlUtil.update(MySqlUtil.connection, "update history_answer set content='"+answers.get(key) +"' where quiz_id="+quiz_id+" and question_id="+key);
            }
            if_review=false;
        }else{
            MySqlUtil.insert(MySqlUtil.connection, "insert into history_quiz(user_id,current_rate,difficulty) values("+ User.user_id+",'"+current_rate+"',"+getFinalDifficulty()+")");
            int new_quiz_id = Integer.parseInt(String.valueOf(MySqlUtil.query(MySqlUtil.connection, "select * from history_quiz where user_id=1 order by quiz_id desc limit 1 ").getJsonArray("results").get(0).asJsonObject().get("quiz_id")));
            //通过hash map参数answer来判断
            if(questions_type.size()>0){
                for (Integer key : answers.keySet()) {
                    MySqlUtil.insert(MySqlUtil.connection, "insert into history_answer(quiz_id,question_id,content,type) values("+new_quiz_id+","+key+",'"+answers.get(key)+"',"+questions_type.get(key)+")");
                }
                questions_type=new HashMap<Integer, Integer>();
            }else{
                for (Integer key : answers.keySet()) {
                    MySqlUtil.insert(MySqlUtil.connection, "insert into history_answer(quiz_id,question_id,content) values("+new_quiz_id+","+key+",'"+answers.get(key)+"')");
                }
            }

            updateDifficulty();
            //如果是试卷模式的额外护理
            if(if_paper){
                MySqlUtil.update(MySqlUtil.connection, "update history_quiz set type=1  where user_id="+User.user_id+" and quiz_id="+new_quiz_id);
                MySqlUtil.update(MySqlUtil.connection, "update history_quiz set learn_section=0  where user_id="+User.user_id+" and quiz_id="+new_quiz_id);
                MySqlUtil.insert(MySqlUtil.connection, "insert into history_answer(quiz_id,question_id,content,type) values("+new_quiz_id+","+writing_id+",'"+writing_answer+"',4)");
                writing_answer="";
            }
        }
        //通过历史的正确率情况，进行鼓励和提醒，比如章节学习不够，成绩进步之类的
        double recent_current_rate=Double.parseDouble(StringWays.getRealString(String.valueOf(MySqlUtil.query(MySqlUtil.connection, "select * from history_quiz order by quiz_id desc").getJsonArray("results").get(0).asJsonObject().get("current_rate"))));
        int max_section=Integer.parseInt(String.valueOf(MySqlUtil.query(MySqlUtil.connection, "select * from history_quiz order by learn_section desc").getJsonArray("results").get(0).asJsonObject().get("learn_section")));
        System.out.println("recent_current_rate："+recent_current_rate);
        System.out.println("max_section："+max_section);
        if(current_rate>recent_current_rate){
            if(max_section<6){
                Remind(scroll.getScene().getWindow(),"继续努力","还有较多章节需要您继续学习！",1);
            }else{
                Remind(scroll.getScene().getWindow(),"恭喜","您的成绩有进步，再接再厉！",1);
            }
        }else{
            Remind(scroll.getScene().getWindow(),"加油","您的正确率有退步，需要加油噢！",1);
        }

    }

    //获得应该采用的推荐习题难度基础
    public int getFinalDifficulty(){
        int final_difficulty=60;
        if(User.which_difficulty.equals("recommend_difficulty")){
            final_difficulty=User.recommend_difficulty;
        }else{
            final_difficulty=User.freedom_difficulty;
        }
        return final_difficulty;
    }

    public void loadQuiz(boolean answer) throws IOException, SQLException {
        VBox box;
        box=loadQuestions(answer);
        scroll.setContent(null);
        scroll.setContent(box);
    }

    //通过questions参数来加载问题
    public VBox loadQuestions(boolean answer) throws IOException {
        VBox box=new VBox();
        box.getChildren().setAll();
        AnchorPane pane= new AnchorPane();
        for(int i=0;i<questions.size();i++){
            question_id=Integer.parseInt(String.valueOf(questions.get(i).asJsonObject().get("question_id")));
            question_header= StringWays.getRealString(String.valueOf(questions.get(i).asJsonObject().get("header")));
            if(if_judgement){
                question_header="是否有语法错误："+question_header;
            }
            right_answers.put(question_id,StringWays.getRealString(String.valueOf(questions.get(i).asJsonObject().get("answer"))));
            pane = FXMLLoader.load(getClass().getResource("../components/Choose.fxml"));
            box.getChildren().add(pane);
            if(answer){
                Label explanation=new Label();
                question_section=String.valueOf(questions.get(i).asJsonObject().get("learn_section"));
                if(question_type==2){
                    explanation.setText("分析：该题目为基础听力，正确答案为"+questions.get(i).asJsonObject().get("answer")+"，您的答案为"+questions.get(i).asJsonObject().get("content"));
                }else if(question_type==1){
                    explanation.setText("分析：该题为语法判断题，正确答案为"+questions.get(i).asJsonObject().get("answer")+"，您的答案为"+questions.get(i).asJsonObject().get("content"));
                }else{
                    explanation.setText("分析："+StringWays.getRealString(String.valueOf(questions.get(i).asJsonObject().get("tip")))+"；正确答案为"+right_answers.get(question_id)+"，您的答案为"+history_answers.get(question_id)+"，属于第"+(Integer.parseInt(question_section)+1)+"章节");
                }
                explanation.setLayoutX(10);
                box.getChildren().add(explanation);
            }
        }
        question_type=0;
        return box;
    }

    //用户完成试卷后，根据不同的情况刷新推荐的难度
    public void updateDifficulty() throws SQLException {
        JsonObject query_result=null;
        JsonArray query_array=null;
        query_result = MySqlUtil.query(MySqlUtil.connection, "select * from history_quiz where user_id="+User.user_id+" order by quiz_id desc");
        query_array=query_result.getJsonArray("results");
        System.out.println(query_array.size());
        int recent_difficulty=Integer.parseInt(String.valueOf(query_array.get(0).asJsonObject().get("difficulty")));
        int final_difficulty=recent_difficulty;
        //如果测试次数大于5，则根据前两次的结果判断
        if(query_array.size()>5){
            //如果最近两次的准确率大于等于0.7
            if(recent_difficulty==Integer.parseInt(String.valueOf(query_array.get(1).asJsonObject().get("difficulty")))){
                if(Double.parseDouble(StringWays.getRealString(String.valueOf(query_array.get(0).asJsonObject().get("current_rate"))))>=0.7&&Double.parseDouble(StringWays.getRealString(String.valueOf(query_array.get(1).asJsonObject().get("current_rate"))))>=0.6){
                    final_difficulty=recent_difficulty+10;
                }else if(Double.parseDouble(StringWays.getRealString(String.valueOf(query_array.get(0).asJsonObject().get("current_rate"))))<=0.5&&Double.parseDouble(StringWays.getRealString(String.valueOf(query_array.get(1).asJsonObject().get("current_rate"))))<=0.5){
                    final_difficulty=recent_difficulty-10;
                }
            }else {
                System.out.println("最近两次的难度不一致，不进行处理");
            }
        }else{
            if(Double.parseDouble(StringWays.getRealString(String.valueOf(query_array.get(0).asJsonObject().get("current_rate"))))>=0.7){
                final_difficulty=recent_difficulty+10;
            }else if(Double.parseDouble(StringWays.getRealString(String.valueOf(query_array.get(0).asJsonObject().get("current_rate"))))<=0.5){
                final_difficulty=recent_difficulty-10;
            }
        }
        MySqlUtil.update(MySqlUtil.connection, "update users set freedom_difficulty="+final_difficulty+" where user_id = "+User.user_id);
        User.recommend_difficulty=final_difficulty;
    }

    //绑定提醒的函数，比如没有完成试卷就要提交了之类的
    public void Remind(Window window, String title, String content,int type){
        JFXAlert alert = new JFXAlert((Stage) window);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);
        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label(title));
        layout.setBody(new Label(content));
        JFXButton closeButton = new JFXButton("好的");
        closeButton.getStyleClass().add("dialog-accept");
        closeButton.styleProperty().setValue("-fx-text-fill:WHITE;-fx-background-color:#5264AE;-fx-font-size:14px;");
        closeButton.setOnAction(event ->{
            alert.hideWithAnimation();
            if(type==1){
                historyController.loadHistory();
                try {
                    UserAchieveController.updateAchieve();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                mainController.navHistory();
                if_paper=false;
            }

        }

        );
        layout.setActions(closeButton);
        alert.setContent(layout);
        alert.show();
    }


}
