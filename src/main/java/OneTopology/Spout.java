package OneTopology;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.util.*;

class NumberSpoutFirst extends BaseRichSpout {
    private TopologyContext context;
    private SpoutOutputCollector collector;
    private List<Long> data = new ArrayList<>();

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        context = topologyContext;
        collector = spoutOutputCollector;
        for (long i = 0; i < ((long) map.get("first")); i++) {
            data.add(i);
        }
    }

    @Override
    public void nextTuple() {
        collector.emit(new Values(
                data.get(new Random().nextInt(data.size()))
        ));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(
                new Fields("longValue")
        );
    }
}

class NumberSpoutSecond extends BaseRichSpout {
    private TopologyContext context;
    private SpoutOutputCollector collector;
    private List<Long> data = new ArrayList<>();

    @Override
    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector spoutOutputCollector) {
        collector = spoutOutputCollector;
        context = topologyContext;
        for (long i = ((long) map.get("first")); i < ((long) map.get("second")); i ++) {
            data.add(i);
        }
    }

    @Override
    public void nextTuple() {
        Long d = data.get(new Random().nextInt(data.size()));
        if (d > 75000)
            collector.emitDirect(0, new Values(d)) ;
        else
            collector.emitDirect(1, new Values(d));
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer outputFieldsDeclarer) {
        outputFieldsDeclarer.declare(
                new Fields("longValue")
        );
    }
}
