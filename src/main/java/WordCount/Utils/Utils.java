package WordCount.Utils;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;

public class Utils {
    public static class NLPHelp {
        private static StanfordCoreNLP pipeline;

        public static synchronized StanfordCoreNLP makePipeline() {
            return pipeline == null ? (pipeline = new StanfordCoreNLP("StanfordCoreNLP-chinese.properties")) : pipeline;
        }
    }

}
