package org.dimigo.gui.translator;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import java.awt.Desktop;
import java.io.*;
import java.net.*;

import java.net.URI;
import java.util.Map;
import java.util.ResourceBundle;

public class TranslatorController implements Initializable {
    @FXML
    ComboBox<SearchType> cbSearch;
    @FXML
    TextArea korean;
    @FXML
    TextArea changed;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        ObservableList<SearchType> comboItems = FXCollections.observableArrayList();
        comboItems.add(new SearchType("영어","en"));
        comboItems.add(new SearchType("중국어 간체","zh-CN"));
        comboItems.add(new SearchType("일본어","ja"));
        comboItems.add(new SearchType("독일어","de"));
        comboItems.add(new SearchType("베트남어","vi"));
        comboItems.add(new SearchType("스페인어","es"));
        comboItems.add(new SearchType("이탈리아어","it"));
        comboItems.add(new SearchType("프랑스어","fr"));
        comboItems.add(new SearchType("러시아어","ru"));
        cbSearch.setItems(comboItems);
    }

    @FXML
    public void handleSearchAction(ActionEvent event) {
        SearchType item = cbSearch.getSelectionModel().getSelectedItem();
        String type = item.getValue();
        Translate(type, korean.getText());
    }

    @FXML
    public void copyText() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent content = new ClipboardContent();
        content.putString(changed.getText());
        clipboard.setContent(content);

    }

    @FXML
    public void goToPapago() {
        try {
            Desktop.getDesktop().browse(new URI("http://papago.naver.com"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void copyFile(ActionEvent event) {
        try{
            //파일 객체 생성
            // pathname안에는 파일을 저장하고 싶은 곳의 경로 + translated.txt
            File file = new File("C:\\Users\\Samsung\\Desktop\\진성\\KDMHS 2508\\자바 프로젝트\\Translator\\translated.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));

            if(file.isFile() && file.canWrite()){
                //쓰기
                bufferedWriter.write(korean.getText() + "\n");
                bufferedWriter.write(changed.getText());

                bufferedWriter.close();
            }
        }catch (IOException e) {
            System.out.println(e);
        }

    }


    public void Translate(String language, String text) {
        String clientId = "n6H9K9Iue7cz0yf4ENtb";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "mtrAqcBSmG";//애플리케이션 클라이언트 시크릿값";
        try {
            text = URLEncoder.encode(text, "UTF-8");
            String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("X-Naver-Client-Id", clientId);
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
            // post request
            String postParams = "source=ko&target="+language+"&text=" + text;
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println("#####" + response.toString());
            Map<String, Object> map = new ObjectMapper().readValue(response.toString(), Map.class);
            Map<String, Object> message = (Map<String, Object>) map.get("message");
            Map<String, String> result = (Map<String, String>) message.get("result");
            changed.setText(result.get("translatedText"));








        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
