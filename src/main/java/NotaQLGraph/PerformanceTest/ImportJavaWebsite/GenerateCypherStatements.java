package NotaQLGraph.PerformanceTest.ImportJavaWebsite;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

import java.io.*;


/**
 * Created by yannick on 23.02.16.
 */
public class GenerateCypherStatements {


    public static void main (String[] args) {
        long davor = System.currentTimeMillis();
        String jsonpath = "/tmp/personen_15000.json";

        try {
            //output
            FileWriter fw = new FileWriter("/tmp/nodes.cql");
            BufferedWriter bw = new BufferedWriter(fw);

        //input
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(jsonpath)));
            String s;
            while (null != (s = in.readLine())) {
                ObjectMapper m = new ObjectMapper();
                JsonNode node = m.readTree(s);
                String cypher=  "CREATE (b";
                cypher = cypher+node.get("username")+":User { ";

                cypher=cypher+"userid: "+node.get("user-id")+", ";
                cypher=cypher+"username: "+node.get("username")+", ";
                cypher=cypher+"avatar: "+node.get("avatar");

                cypher=cypher+"});";
                bw.write(cypher+"\n");
            }


            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //kanten
        try {
            //output
            FileWriter fw = new FileWriter("/tmp/relationships.cql");
            BufferedWriter bw = new BufferedWriter(fw);

            //input
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(jsonpath)));
            String s;
            while (null != (s = in.readLine())) {
                ObjectMapper m = new ObjectMapper();
                JsonNode node = m.readTree(s);
                ArrayNode arrayNode= (ArrayNode) node.get("following");
               if (arrayNode!=null) {
                   for (int i = 0; i < arrayNode.size(); i++) {
                       bw.write("MATCH (a), (b) WHERE a.username =" + node.get("username") + " AND b.username =" + arrayNode.get(i).textValue() + " CREATE (a)-[:FOLLOWING]->(b);\n");
                   }
               }
            }

            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        long danach = System.currentTimeMillis();
        long dauer = danach-davor;
        System.out.println(dauer/1000+"Sekunden");

    }
}
