package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;
import com.tinkerpop.blueprints.Edge;
import org.apache.commons.collections.Bag;

import java.util.*;

/**
 * Created by yannick on 18.01.16.
 * Eine Collection, die verwendet wird um eine Mehrdeutige Ausgabe zurückzugeben. Z.b. wenn ich In_v_.gehalt vergleichen will etc ....5
 */
public class CollectionVData implements VData {

    private ArrayList<VData> list = new ArrayList<VData>();
    private ArrayList<Edge> edge = new ArrayList<Edge>(); //wird benötigt um die passenden Edges für den @-Operator zurückzugeben


    public void add(VData a, Edge e){
        list.add(a);
        edge.add(e);
    }

    public void addAll (CollectionVData a){
        list.addAll(a.list);
        edge.addAll(a.edge);
    }

    public VData get (int i){
        return list.get(i);
    }
    public Edge getEdge(int i){return edge.get(i);}
    public int size(){
        return list.size();
    }

    public VData evaluate(VertexToCheck vtc) {
      //  if (size()>1){
            return this;
        //  }
     //  return list.get(0).evaluate(vtc);
    }

    public String[] getAllData(){
        String[] a = new String[list.size()];
        for (int i=0;i<list.size();i++){
            a[i]=list.get(i).toString();
        }
        return a;
    }

}
