package NotaQLGraph.PerformanceTest;

import NotaQLGraph.Database.Database;
import NotaQLGraph.Database.Neo4JDatabase;
import com.tinkerpop.blueprints.Graph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.util.wrappers.id.IdGraph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.StringTokenizer;

/**
 * Created by yannick on 17.02.16.
 */
public class AnalyseJson {
    public static void main (String[] args) {

        BufferedReader reader = null;


        try {
            File file = new File("/home/yannick/graph/Quelldaten/personen.json");
            reader = new BufferedReader(new FileReader(file));
            String line;

            int anzahl = 0;
            int zahl=0;
            int max=0;

            int[] a = new int[1632804];
            knoten:
            while ((line = reader.readLine()) != null) {

                if (anzahl % 1000 == 0) System.out.println("Anzahl:" +anzahl);


                zahl = Integer.parseInt(line.substring(line.indexOf("_id")+6,line.indexOf("user-id")-3));
                a[zahl]=a[zahl]+1;
                if (zahl>max){
                    max=zahl;
                }
                anzahl++;
            }
            System.out.println("maximum:"+max);

            for (int i=0;i<a.length;i++){
                if (a[i]==0){
                    System.out.println("fehlt:"+i);

                }
                if (a[i]>1){
                    System.out.println("doppelt:"+i);

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
