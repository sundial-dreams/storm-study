package NLP;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

public class Test2 {
    static public void main(String[] args) throws Exception {
        Annotation annotation = new Annotation("I am dpf and 20 years old.");
        StanfordCoreNLP pipeline = new StanfordCoreNLP();
        pipeline.annotate(annotation);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sentence : sentences) {
            System.out.println(sentence.get(CoreAnnotations.LemmaAnnotation.class));
        }
    }
}
