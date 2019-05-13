package OneTopology;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;

import java.util.HashMap;
import java.util.Map;

public class Topology {
    static public StormTopology buildTopology() {
        TopologyBuilder builder = new TopologyBuilder();
        Map<String, Object> map = new HashMap<>();
        map.put("first", 50000L);
        map.put("second", 100000L);
        builder.setSpout("spout:number1", new NumberSpoutFirst())
               .addConfigurations(map);
        builder.setSpout("spout:number2", new NumberSpoutSecond())
               .addConfigurations(map);
        builder.setBolt("bolt:even", new EvenBolt())
               .shuffleGrouping("spout:number1")
               .directGrouping("spout:number2");
        builder.setBolt("bolt:phrase", new PhraseBolt())
               .shuffleGrouping("bolt:even");
        builder.setBolt("bolt:odd", new OddBolt())
               .directGrouping("spout:number2");
        builder.setBolt("bolt:largerAndSmaller", new PhraseBolt(), 2)
               .directGrouping("bolt:odd");
        builder.setBolt("bolt:end", new EndBolt())
               .shuffleGrouping("bolt:phrase")
               .shuffleGrouping("bolt:largerAndSmaller");
        return builder.createTopology();
    }

    static public void main(String[] args) throws InterruptedException {
        Config config = new Config();
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("topology", config, buildTopology());
    }
}
