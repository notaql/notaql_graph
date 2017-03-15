package NotaQLGraph.notaQLCommunication;

import NotaQLGraph.Database.Database;
import NotaQLGraph.Database.Neo4JDatabase;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONReader;
import com.tinkerpop.blueprints.util.io.graphson.GraphSONWriter;
import org.apache.avro.data.Json;

import java.io.*;
import java.util.Iterator;

/**
 * Created by yannick on 28.01.16.
 */
public class JSON {
    public static void main(String[] args) {
        int random = (int) Math.round(Math.random() * 100000);
        //int random2 = Math.round(random)
        String path = "/tmp/db" + String.valueOf(random);
        Database a = new Neo4JDatabase(path);
        Vertex a1 = a.getDatabase().addVertex(999);
        a1.setProperty("asdf", "asdf");
        a1.setProperty("hund", "katzeawe");
        Vertex a2 = a.getDatabase().addVertex(4);
        a2.setProperty("asdf", "asdf");
        a2.setProperty("hund", "affe");
        String path2 = writeJson(a.getDatabase());
        Database b = new Neo4JDatabase(path + "2");
        readJson(path2, b.getDatabase());
       //b.printGraph();
        b.getDatabase().shutdown();
        a.getDatabase().shutdown();
    }

    public static String writeJson(Graph input) {
        try {
            int random = (int) Math.round(Math.random() * 100000);
            String path = "/tmp/" + String.valueOf(random);
            PrintWriter writer = new PrintWriter(path, "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            for (Vertex a : input.getVertices()) {
                JsonNode node = mapper.createObjectNode();
                ((ObjectNode) node).put("_id", a.getId().toString());

                for (String key : a.getPropertyKeys()) {
                    if(a.getProperty(key).getClass().isArray()){
                        ArrayNode array = mapper.createArrayNode();
                        ((ObjectNode)node).set(key,array);
                        String[] s = a.getProperty(key);
                        for (String ss: s){
                            array.add(ss);
                        }
                    }
                    else{
                        ((ObjectNode) node).put(key, a.getProperty(key).toString());
                    }
                }
                writer.write(mapper.writeValueAsString(node) + "\n");

            }
            writer.flush();
            writer.close();
            return path;
        } catch (IOException e) {
            System.err.println(e);
            return null;
        }
    }

    public static void readJson(String path, Graph output) {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(path)));
            String s;
            while (null != (s = in.readLine())) {
                ObjectMapper m = new ObjectMapper();
                JsonNode node = m.readTree(s);
                Iterator<String> it = node.fieldNames();
                Vertex a;
                if (node.findValue("_id")!=null) {
                     a = output.addVertex(node.findValue("_id").asText());
                }
                else{
                    a = output.addVertex(null);
                }
                while (it.hasNext()) {
                    String key = it.next();
                    if (node.findValue(key).isArray()){//wenn ich eine ganze Liste von Knoten übergeben bekomme
                        ArrayNode arrayNode= (ArrayNode) node.get(key);
                        String[] arrayString = new String[arrayNode.size()];
                        for (int i=0;i<arrayNode.size(); i++){
                            arrayString[i]=arrayNode.get(i).textValue();
                        }
                        a.setProperty(key,arrayString);
                      //  System.out.println("arrayinhalt"+arrayNode.get(0));
                    }
                    else {
                        String value = node.findValue(key).asText();
                    //    System.out.println(key + " " + value);
                        if (!(key.equals("_id"))) {
                            a.setProperty(key, value);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}