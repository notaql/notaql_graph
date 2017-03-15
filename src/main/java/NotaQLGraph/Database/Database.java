package NotaQLGraph.Database;




import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.id.IdGraph;

import java.io.*;

/**
 * Created by yannick on 20.11.15.
 */
public abstract class Database {

    private static int idCounter = 0;
    protected Graph graph;
    protected String path="";

    class MyIdFactory implements IdGraph.IdFactory{
        //Methode zum erzeugen einer ID, wenn der mitgegeben ID Value = null ist. Es muss! manuell überprüft werden ob der Eintrag bereits exisitiert
        public Object createId() {
           while (graph.getVertex(idCounter)!=null){
               idCounter++;
           }
            return ""+idCounter++;
        }
    }

    public abstract void commit();

    public Graph getDatabase(){
        return graph;
    }

    public void printGraph(){
        printGraph(this.graph, this.path);

    }

    //Methode um Graph mit allen Attributen und Kanten auszugeben
    static void printGraph(Graph graph, String path){


        try {
            FileWriter fw = new FileWriter(path+"DB.txt");
            BufferedWriter bw = new BufferedWriter(fw);

            for (Vertex x: graph.getVertices()){
                bw.write("######Knoten:"+x.getId()+"\n");
                for (String p:x.getPropertyKeys()){
                    bw.write(p+":"+x.getProperty(p)+"\n");
                }
                bw.write("OUT[");
                for (Edge y:x.getEdges(Direction.OUT)){
                    bw.write(y.getLabel()+""+y.getVertex(Direction.IN).getId());
                    bw.write(",");
                }
                bw.write("]\n");
                bw.write("IN[");
                for (Edge y:x.getEdges(Direction.IN)){
                    bw.write(y.getLabel()+""+y.getVertex(Direction.OUT).getId());
                    bw.write(",");
                }
                bw.write("]\n");
            }
            bw.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    public static void main(String args[]) {


        Database g = new Neo4JDatabase("/tmp/test.db");
        Graph graph1 = g.getDatabase();
        Vertex a = graph1.addVertex(1);
        Vertex b = graph1.addVertex(2);
        Vertex c = graph1.addVertex(3);
        Vertex d = graph1.addVertex(4);
        Vertex e = graph1.addVertex(5);
        a.setProperty("gender", 1);
        b.setProperty("gender", 0);
        c.setProperty("gender", 1);
        d.setProperty("gender", 0);
        e.setProperty("gender", 0);
        a.setProperty("AGE", 33);
        b.setProperty("AGE", 33);
        c.setProperty("AGE", 33);
        d.setProperty("AGE", 33);
        e.setProperty("AGE", 33);


        graph1.addEdge(null, a, b, "kennt");
        graph1.addEdge(null, b, c, "kennt");
        graph1.addEdge(null, b, d, "kennt");
        graph1.addEdge(null, b, e, "kennt");
        graph1.addEdge(null, c, e, "kennt");
        g.printGraph();



        graph1.shutdown();


        /* Graph graph1 = new Neo4j2Graph("/home/yannick/graph/graph10.db");
        Graph graph2 = new Neo4j2Graph("/home/yannick/graph/graph.db");

        LinkedList<String> labels = new LinkedList<String>();
        labels.add("Person");
        labels.add("Student");

        Vertex a = graph1.addVertex(null);
        Vertex b = graph1.addVertex(null);
        a.setProperty("geschlecht", "mann");
        b.setProperty("name", "hallo");
        a.setProperty("label", labels);
        Edge e = graph1.addEdge(null, a, b, "knows");


        System.out.println(a.getPropertyKeys());
        System.out.println(a.getProperty("label"));

        printGraph(graph1, "/tmp/print");
        graph1.shutdown();
        graph2.shutdown();





        Database a = new Neo4JDatabase("/home/yannick/graph/test.db");
        a.initialisieren();

        Vertex b = a.getDatabase().getVertex(888);
        b.addEdge("test",b);
        b.addEdge("test",b);
        a.printGraph();
        System.out.println(b);
        for (Edge e : b.getEdges(Direction.IN, new String[0])){
            System.out.println(e.getVertex(Direction.IN).getId());
        }

        a.getDatabase().shutdown();*/
    }
    public  void  initialisieren(){
       if (graph.getVertex(999)==null) {
           Vertex a = graph.addVertex(999);
           Vertex b = graph.addVertex(888);
           Vertex c = graph.addVertex(777);
           Vertex d = graph.addVertex(666);
           Vertex e = graph.addVertex(555);
           Vertex f = graph.addVertex(444);
           Vertex g = graph.addVertex(333);
           a.setProperty("name", "chantal");
           a.setProperty("hund", "Hundy");
           a.setProperty("alter", 16);
           a.setProperty("geschlecht", "weiblich");
           a.addEdge("kennt",b);

           b.setProperty("name", "horst");
           b.setProperty("alter", 15);
           b.setProperty("firma", "juwi");
           b.setProperty("gehalt", 5000);
           //b.addEdge("istChefvon", c);

           c.setProperty("name", "johannes");
           c.setProperty("firma", "uni kl");
           c.setProperty("gehalt", 15000);
           c.setProperty("geschlecht", "männlich");

           d.setProperty("name", "stefan");
           d.setProperty("firma", "uni kl");
           d.setProperty("gehalt", 10000);
           d.setProperty("geschlecht", "männlich");
           d.addEdge("istChefvon", c);
           d.addEdge("istChef2von", c);


           e.setProperty("name", "julia");
           e.setProperty("firma", "juwi");
           e.setProperty("gehalt", 17000);
           e.setProperty("alter", 15);
           Edge ec = e.addEdge("kennt", c);
           ec.setProperty("seit", 2016);
           e.addEdge("kennt", f);

           f.setProperty("name", "florian");
           f.setProperty("gehalt", 15000);
           f.setProperty("firma", "juwi");
           f.setProperty("hund", "jill");
           f.setProperty("Autos", 15);
           f.addEdge("kennt", a);
           f.addEdge("istChefvon", b);

           g.setProperty("name", "herbert");
           g.setProperty("hund", "Ben");
           g.setProperty("alter", 12);
           g.setProperty("geschlecht", "männlich");
           g.setProperty("dist",0);
           Edge ag = g.addEdge("kennt", a);
           Edge bg = g.addEdge("kennt", b);
           Edge cg = g.addEdge("kennt", c);
           Edge eg = g.addEdge("kennt", e);
           ag.setProperty("seit", 2015);
           bg.setProperty("seit", 2011);
           cg.setProperty("seit", 2012);
           eg.setProperty("seit", 1999);


           //Pagerank

           Vertex x = graph.addVertex(1001);
           x.setProperty("url", "seite1");
           x.setProperty("Label", "Seite");
           x.setProperty("Pagerank", 0.2);
           Vertex xx = graph.addVertex(1002);
           xx.setProperty("url", "seite2");
           xx.setProperty("Label", "Seite");
           xx.setProperty("Pagerank", 0.2);
           Vertex xxx = graph.addVertex(1003);
           xxx.setProperty("url", "seite3");
           xxx.setProperty("Label", "Seite");
           xxx.setProperty("Pagerank", 0.2);
           Vertex xxxx = graph.addVertex(1004);
           xxxx.setProperty("url", "seite4");
           xxxx.setProperty("Label", "Seite");
           xxxx.setProperty("Pagerank", 0.2);
           Vertex xxxxx = graph.addVertex(1005);
           xxxxx.setProperty("url", "seite5");
           xxxxx.setProperty("Label", "Seite");
           xxxxx.setProperty("Pagerank", 0.2);

           x.addEdge("verlinkt", xxxxx);
           x.addEdge("verlinkt", xx);
           xx.addEdge("verlinkt", x);
           xx.addEdge("verlinkt", xxxxx);
           xxx.addEdge("verlinkt", xxxxx);
           xxx.addEdge("verlinkt", xx);
           xxxx.addEdge("verlinkt", xxx);
           xxxxx.addEdge("verlinkt", xxxx);


       }
   }

    public static Database createDatabase(String typ, String path){
        if (typ.equalsIgnoreCase("neo4j")){
            return new Neo4JDatabase(path);
        }
        if (typ.equalsIgnoreCase("tinkergraph")||typ.equalsIgnoreCase("tinkergraph2")){
            if (path==null){
                return new TinkerGraphDatabase();
            }
            return new TinkerGraphDatabase(path);
        }
        if (typ.equalsIgnoreCase("titangraph")){
            return new TitanGraphDatabase(path);
        }

        return new Neo4JDatabase(path);
    }

    public static boolean supportDatabase(String typ){
       if (typ.equalsIgnoreCase("neo4j")||typ.equalsIgnoreCase("tinkergraph")||typ.equalsIgnoreCase("tinkergraph2")){
            return true;
       }
       return false;
    }
}