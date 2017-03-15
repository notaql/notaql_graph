package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;
import com.tinkerpop.blueprints.Vertex;

/**
 * Created by yannick on 16.12.15.
 */
public class AtVData implements  VData {
    public VData evaluate(VertexToCheck vtc) {
        String path= vtc.getProperty();
        Vertex vertex = vtc.getVertex();
        if (vertex.getProperty(path)!=null) {
            String inhalt = vertex.getProperty(path).toString();
            //    System.out.println("inhalt:"+inhalt);
            if (inhalt.equalsIgnoreCase("true")){
                return new BooleanValue(true);
            }
            if (inhalt.equalsIgnoreCase("false")){
                return new BooleanValue(false);
            }
            try {
                //  System.out.println("numbervalue");
                return new NumberValue(Double.parseDouble(inhalt));
            }
            catch (Exception e) {
                //    System.out.println("exeeeeeeeption");
                return new StringValue(inhalt);
            }


        }
        //OLD  return new StringValue("null"); //damit geht dann auch der == null vergleich
        return new NoVData();
    }
}
