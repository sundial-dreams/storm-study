package WordCount.Spout;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.FileReader;
import java.util.Map;
import java.util.Scanner;

public class ReadDocsSpout extends BaseRichSpout {
    private TopologyContext context;
    private SpoutOutputCollector collector;
    private boolean completed = false;
    private Scanner scanner;

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        context = topologyContext;
        collector = spoutOutputCollector;
        try {
            scanner = new Scanner(new FileReader(map.get("filename").toString()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void nextTuple() {
        if (completed) return;
        try {
            while (scanner.hasNextLine()) {
                collector.emit(new Values(scanner.nextLine()));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            completed = true;
        }
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(
                new Fields("line")
        );
    }

}
