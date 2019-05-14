package NLP;

import opennlp.tools.cmdline.parser.ParserTool;
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.parser.Parse;
import opennlp.tools.parser.Parser;
import opennlp.tools.parser.ParserFactory;
import opennlp.tools.parser.ParserModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.SimpleTokenizer;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import java.io.FileInputStream;
import java.io.InputStream;

public class openNLPTest {
    private static String path = "./src/main/resources/en";

    public static void main(String[] args) throws Exception {
        String text = "We’re Progress. We make software that developers love. We’ve heard that you’re a big fan of React, which is great, because we are too. In fact, we make a pretty awesome tool for React that you really need to know about: Kendo UI. Kendo UI is a set of modular UI components you can use to enhance your apps—think calendars, grids, charts, and such. Sure, there are other grids and charts and stuff out there, but not like ours. See for yourself! You’ll find features that you just won’t see elsewhere. We even make it easy to use our components, too, with popular out-of-the-box themes and a custom theme editor. Pretty cool? Our customers seem to think so.";
        InputStream enSentTrain = new FileInputStream(path + "/en-sent.bin");
        SentenceModel sentModel = new SentenceModel(enSentTrain);
        SentenceDetectorME detectorME = new SentenceDetectorME(sentModel);
        String[] sentences = detectorME.sentDetect(text);
        System.err.println("sentence Detector");
        for (String sentence : sentences) {
            System.out.println(sentence);
        }
        enSentTrain.close();

        InputStream enTokenTrain = new FileInputStream(path + "/en-token.bin");
        TokenizerModel tokenModel = new TokenizerModel(enTokenTrain);
        Tokenizer tokenizer = new TokenizerME(tokenModel);
        String[] tokens = tokenizer.tokenize(text);
        System.err.println("token");
        for (String token : tokens) {
            System.out.println(token);
        }
        enTokenTrain.close();

        InputStream findPersonTrain = new FileInputStream(path + "/en-ner-person.bin");
        TokenNameFinderModel model = new TokenNameFinderModel(findPersonTrain);
        findPersonTrain.close();
        NameFinderME nameFinderME = new NameFinderME(model);
        Span[] nameSpans = nameFinderME.find(new String[]{"Mike", "china", "google"});
        System.err.println("named entity find");
        for (Span s : nameSpans) {
            System.err.println(s.toString());
        }


        InputStream posMaxentTrain = new FileInputStream(path + "/en-pos-maxent.bin");
        POSModel posModel = new POSModel(posMaxentTrain);
        POSTaggerME posTaggerME = new POSTaggerME(posModel);
        String[] words = SimpleTokenizer.INSTANCE.tokenize(text);
        String[] posResult = posTaggerME.tag(words);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < posResult.length; i++) {
            stringBuilder.append(words[i]).append("/").append(posResult[i]).append(" ");
        }
        System.err.println("pos tag");
        System.out.println(stringBuilder);

        InputStream parserTrain = new FileInputStream(path + "/en-parser-chunking.bin");
        ParserModel parserModel = new ParserModel(parserTrain);
        Parser parser = ParserFactory.create(parserModel);
        Parse[] topParses = ParserTool.parseLine("i am dpf", parser, 1);
        System.err.println("parser");
        for (Parse parse : topParses) {
            parse.show();
        }
        parserTrain.close();
    }
}
