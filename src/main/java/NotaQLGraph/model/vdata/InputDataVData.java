package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;
import com.tinkerpop.blueprints.Vertex;

/**
 * Created by yannick on 30.11.15.
 */
public class InputDataVData implements VData {
    public enum Mode {
        //EdgeCreatorOWNNode wird dann erzeugt, wenn InputData mit IN. anfängt
        //Z.b. //(_id = IN.following) ==> id dann die id des vertex (zielknoten), IN.following dann die id des startknotens (bei kanten)
        NodeID, Name, EDGECreatorOWNNode

    }

    private Mode mode;
    private String path;
    private VData vdata;

    public InputDataVData(String path, Mode mode){
        this.mode = mode;
        this.path= path;
    }

    public InputDataVData(VData path, Mode mode){ //Konstruktorfunktion für EdgeCreatorOWNNode
        this.mode = mode;
        this.vdata= path;
    }



    public VData evaluate(VertexToCheck vtc) {
        Vertex vertex = vtc.getVertex();
        if(mode==Mode.NodeID){
            return new StringValue(vertex.getId().toString());
        }
        else if (mode==Mode.Name){
          //  System.out.println("else part");

            if (vertex.getProperty(path)!=null) {
                Object inhal=vertex.getProperty(path);
                String inhalt = inhal.toString();
            //    System.out.println("inhalt:"+inhalt);
                if (inhalt.equalsIgnoreCase("true")){
                    return new BooleanValue(true);
                }
                if (inhalt.equalsIgnoreCase("false")){
                    return new BooleanValue(false);
                }
                try {
                   return new NumberValue(Double.parseDouble(inhalt));
                }
                catch (Exception e) {
                    if (inhal instanceof String[]){
                        CollectionVData collectionVData= new CollectionVData();
                        String[] importarray = (String[]) inhal;
                        for (int i=0;i<importarray.length;i++){
                            collectionVData.add(new StringValue(importarray[i]),null);//Edge operator wird nur für @ zugriff benötigt, hab ich bei Import nicht
                        }
                       return collectionVData.evaluate(vtc);
                    }


                    return new StringValue(inhalt);
                }


            }
          //OLD  return new StringValue("null"); //damit geht dann auch der == null vergleich
            return new NoVData();
        }
        else{//EDGECreatorOWNNode [wird irgendwie benutzt um zwischen Zielknoten und eigenen zu unterscheiden]
            Vertex temp = vtc.getVertex();
            vtc.setVertex(vtc.getRootvertex());
            VData a = vdata.evaluate(vtc);
            vtc.setVertex(temp);
            return a;
        }
    }


    public String getFilterKey(){
        return path;
    }

    public Mode getMode(){
        return mode;
    }


}
