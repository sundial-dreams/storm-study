package OneTopology;

import org.apache.storm.task.OutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.IRichBolt;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.tuple.Values;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class EvenBolt extends BaseBasicBolt {
    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        long value = tuple.getLongByField("longValue");
        if (value % 2 == 0) basicOutputCollector.emit(new Values(value));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(
                new Fields("longValue")
        );
    }
}

class PhraseBolt extends BaseBasicBolt {
    static private boolean checkPhrase(String value, int i, int j) {
        if (i > j) return true;
        return value.charAt(i) == value.charAt(j)
                && checkPhrase(value, i + 1, j - 1);
    }

    private static boolean checkPhrase(long value) {
        String str = value + "";
        return checkPhrase(str, 0, str.length() - 1);
    }

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        long value = tuple.getLongByField("longValue");
        if (checkPhrase(value)) {
           System.err.println("phraseBolt:" + value);
            basicOutputCollector.emit(new Values(value));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(
                new Fields("longValue")
        );
    }
}

class OddBolt extends BaseBasicBolt {

    @Override
    public void execute(Tuple tuple, BasicOutputCollector basicOutputCollector) {
        long value = tuple.getLongByField("longValue");
        if (value % 2 == 1) {
            if (value > 60000)
                basicOutputCollector.emitDirect(0, new Values(value));
            else
                basicOutputCollector.emitDirect(1, new Values(value));
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(
                new Fields("longValue")
        );
    }
}

class EndBolt implements IRichBolt {
    private List<Long> phraseValue = new LinkedList<>();
    private OutputCollector collector;

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {

    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    @Override
    public void prepare(Map map, TopologyContext topologyContext, OutputCollector outputCollector) {
        collector = outputCollector;
    }

    @Override
    public void execute(Tuple tuple) {
        long value = tuple.getLongByField("longValue");
        phraseValue.add(value);
        System.err.println("endBolt:" + value);
    }

    @Override
    public void cleanup() {
        System.err.println("phrase value:");
        phraseValue.forEach(System.out::println);
    }
}