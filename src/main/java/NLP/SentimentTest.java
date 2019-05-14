package NLP;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.ejml.simple.SimpleMatrix;

import java.util.Properties;

class SentimentClassification {
    public int veryPositive;
    public int positive;
    public int neutral;
    public int negative;
    public int veryNegative;

    public void make(int... args) {
        veryNegative = args[0];
        negative = args[1];
        neutral = args[2];
        positive = args[3];
        veryPositive = args[4];
    }

    @Override
    public String toString() {
        return String.format("very negative: %d%%, negative: %d%%, neutral: %d%%, positive: %d%%, very positive: %d%%",
                veryNegative, negative, neutral, positive, veryPositive);
    }
}

class SentimentResult {
    public String sentimentType;
    public int sentimentScore;
    public SentimentClassification sentimentClassification;
}

class SentimentAnalyzer {
    private StanfordCoreNLP pipeline;

    SentimentAnalyzer() {
        Properties properties = new Properties();
        properties.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        pipeline = new StanfordCoreNLP(properties);
    }

    public SentimentResult getSentimentResult(String text) {
        SentimentClassification classification = new SentimentClassification();
        SentimentResult sentimentResult = new SentimentResult();
        if (text != null && text.length() > 0) {
            Annotation annotation = pipeline.process(text);
            for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence.get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                SimpleMatrix simpleMatrix = RNNCoreAnnotations.getPredictions(tree);
                System.out.println(simpleMatrix);
                classification.make(
                        (int) Math.round(simpleMatrix.get(0) * 100d),
                        (int) Math.round(simpleMatrix.get(1) * 100d),
                        (int) Math.round(simpleMatrix.get(2) * 100d),
                        (int) Math.round(simpleMatrix.get(3) * 100d),
                        (int) Math.round(simpleMatrix.get(4) * 100d)
                );
                sentimentResult.sentimentType = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
                sentimentResult.sentimentClassification = classification;
                sentimentResult.sentimentScore = RNNCoreAnnotations.getPredictedClass(tree);
            }
        }
        return sentimentResult;
    }
}

public class SentimentTest {
    static public void main(String[] args) {
        String text = "JavaScript is best language in the world!";
        String text2 = "PHP is worst language in the world!";
        SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();
        SentimentResult sentimentResult = sentimentAnalyzer.getSentimentResult(text2);
        System.err.println(sentimentResult.sentimentClassification);
        System.err.println(sentimentResult.sentimentScore);
        System.err.println(sentimentResult.sentimentType);
    }
}
