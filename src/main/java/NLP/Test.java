package NLP;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;

public class Test {
    static public void main(String[] args) {
        String text = "腾讯和字节跳动，该选择哪一个？腾讯是马化腾创建的，字节跳动是张一鸣同学创建的。所以还是选择保研。";
        // System.out.println(new Segmentation(text).getText());
        System.err.println(new PosTag(text).getText());
    }
}

class CoreNLP {
    private static CoreNLP instance = null;
    private StanfordCoreNLP pipeline;

    private CoreNLP() {
        pipeline = new StanfordCoreNLP("StanfordCoreNLP-chinese.properties");
    }

    public static CoreNLP getInstance() {
        return instance == null ? (instance = new CoreNLP()) : instance;
    }

    public StanfordCoreNLP getPipeline() {
        return pipeline;
    }
}

class Segmentation {
    private String text;

    public String getText() {
        return text;
    }

    public Segmentation(String text) {
        CoreNLP coreNLP = CoreNLP.getInstance();
        StanfordCoreNLP pipeline = coreNLP.getPipeline();
        Annotation annotation = new Annotation(text);
        pipeline.annotate(annotation);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        StringBuilder stringBuilder = new StringBuilder();
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                stringBuilder.append(word);
                stringBuilder.append(" ");
            }
        }
        this.text = stringBuilder.toString().trim();
    }
}

class PosTag {
    private String text;

    public String getText() {
        return text;
    }

    public PosTag(String text) {
        CoreNLP coreNLP = CoreNLP.getInstance();
        StanfordCoreNLP pipeline = coreNLP.getPipeline();
        Annotation annotation = new Annotation(text);
        pipeline.annotate(annotation);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        StringBuilder stringBuilder = new StringBuilder();
        for (CoreMap sentence : sentences) {
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                stringBuilder.append(word)
                             .append("/(")
                             .append(pos)
                             .append(", ")
                             .append(ne)
                             .append(") ");
            }
        }

        this.text = stringBuilder.toString();
    }
}



