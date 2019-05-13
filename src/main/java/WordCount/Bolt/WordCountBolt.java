package WordCount.Bolt;

import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.HashMap;
import java.util.Map;

public class WordCountBolt extends BaseBasicBolt {
    private Map<String, Integer> wordCounts = new HashMap<>();
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        String word = tuple.getStringByField("word").trim();
        if (wordCounts.containsKey(word)) wordCounts.compute(word, (k, v) -> v + 1);
        else wordCounts.put(word, 1);
        basicOutputCollector.emit(new Values(word, wordCounts.get(word)));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(
                new Fields("word", "count")
        );
    }
}
