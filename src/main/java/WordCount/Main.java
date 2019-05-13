package WordCount;

import WordCount.Bolt.ReportBolt;
import WordCount.Bolt.TokenBolt;
import WordCount.Bolt.WordCountBolt;
import WordCount.Spout.ReadDocsSpout;
import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.generated.StormTopology;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.tuple.Fields;

import java.util.HashMap;
import java.util.Map;

public class Main {
    static public void main(String[] args) throws Exception {
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("wordCountTopology", new Config(), Topology.buildTopology());
    }
}

class Topology {
    static StormTopology buildTopology() {
        TopologyBuilder builder = new TopologyBuilder();
        Map<String, Object> conf = new HashMap<>();
        conf.put("filename", "./src/main/resources/doc.txt");
        builder.setSpout("readDocs", new ReadDocsSpout())
               .addConfigurations(conf);

        builder.setBolt("token", new TokenBolt(), 3)
               .shuffleGrouping("readDocs");

        builder.setBolt("wordCount", new WordCountBolt(), 6)
               .fieldsGrouping("token", new Fields("word"));

        builder.setBolt("report", new ReportBolt())
               .globalGrouping("wordCount");

        return builder.createTopology();
    }
}
