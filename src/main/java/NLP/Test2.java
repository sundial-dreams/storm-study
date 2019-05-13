package NLP;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.FileInputStream;
import java.io.InputStream;

public class Test2 {
    static public void main(String[] args) throws Exception {
        String sentence = "Hi. i am dpf. we provide free tutorials on various technologies";
        InputStream inputStream = new FileInputStream("./src/main/resources/en-sent.bin");
        SentenceModel model = new SentenceModel(inputStream);
        SentenceDetectorME detectorME = new SentenceDetectorME(model);
        String[] sentences = detectorME.sentDetect(sentence);
        for (String sent : sentences) {
            System.out.println(sent);
        }
    }
}
