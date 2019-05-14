package NLP;

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.Mention;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.sentiment.SentimentUtils;
import edu.stanford.nlp.simple.SentimentClass;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.ejml.simple.SimpleMatrix;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;

public class StanfordCoreNLPTest {

    public static void main(String[] args) {
        /*Properties properties = new Properties();
        properties.setProperty("annotators", "tokenize, ssplit, parse, sentiment");
        properties.setProperty("coref.algorithm", "neural");*/
        String text = "互联网最早只有简单的超文本文档内容，却逐渐发展成为了引领设计潮流，具备丰富多彩、身临其境和灵活动态体验的平台。" +
                "国内靠互联网起家的有：马化腾创建的腾讯、马云创建的阿里巴巴、张一鸣创建的字节跳动、李彦宏创建的百度等等。美国总统是特朗普，特朗普担任美国总统。";
        StanfordCoreNLP pipeline = new StanfordCoreNLP("StanfordCoreNLP-chinese.properties");
        CoreDocument document = new CoreDocument(text);
        pipeline.annotate(document);
        List<CoreLabel> token = document.tokens();
        System.err.println(Arrays.toString(token.toArray()));

        List<CoreSentence> sentence = document.sentences();
        System.err.println(Arrays.toString(sentence.toArray()));

        List<String> posTags = sentence.get(0).posTags();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < posTags.size(); i++) {
            stringBuilder.append(token.get(i).word()).append("/")
                         .append(posTags.get(i)).append(" ");
        }
        System.out.println(stringBuilder);

        List<String> nerTags = sentence.get(1).nerTags();
        List<CoreLabel> token1 = sentence.get(1).tokens();
        StringBuilder stringBuilder1 = new StringBuilder();
        for (int i = 0; i < token1.size(); i++) {
            stringBuilder1.append(token1.get(i).word()).append("/").append(nerTags.get(i)).append(" ");
        }
        System.err.println(stringBuilder1);
        /*
        Tree constituencyParse = sentence.get(0).constituencyParse();
        System.out.println(constituencyParse);
        System.out.println();
        SemanticGraph dependencyParse = sentence.get(0).dependencyParse();
        System.out.println(dependencyParse);
        System.out.println();
//        List<RelationTriple> relationTriples = sentence.get(0).relations();
//        System.out.println(Arrays.toString(relationTriples.toArray()));
//        System.out.println();
        List<CoreEntityMention> entityMentions = sentence.get(0).entityMentions();
        System.out.println(Arrays.toString(entityMentions.toArray()));
        System.out.println();
*/
        Annotation annotation = pipeline.process(text);
        List<CoreMap> sentences = annotation.get(CoreAnnotations.SentencesAnnotation.class);
        for (CoreMap sent : sentences) {
            for (CoreLabel tok : sent.get(CoreAnnotations.TokensAnnotation.class)) {
                String word = tok.get(CoreAnnotations.TextAnnotation.class);
                String pos = tok.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                String ner = tok.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                System.out.println(word + "/ (" + pos + ", " + ner + ")");
            }
            System.err.println(sent.get(CoreAnnotations.LemmaAnnotation.class));
        }
        for (CoreMap coreMap : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            for (Mention m : coreMap.get(CorefCoreAnnotations.CorefMentionsAnnotation.class)) {
                System.err.println(m);
            }
        }

    }
}
