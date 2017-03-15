package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;
import NotaQLGraph.model.predicate.Predicate;

/**
 * Created by yannick on 17.12.15.
 */
public class MultiVertexVData implements VData {
    Predicate predicate;
    VData nextNode;
    VData attrribute;
    boolean auswerten;

    public MultiVertexVData(Predicate knotenPredicat, VData nextNode, VData attribute, boolean auswerten) {
        this.predicate = knotenPredicat;
        this.nextNode= nextNode;
        this.attrribute=attribute;
        this.auswerten=auswerten;
    }


    public VData evaluate(VertexToCheck vtc) {//achtung, hier gibt es 2 Evaluate funktionen, die beide aufgerufen werden müssen


        if (vtc.getOperation()== AggregatedEdgeVData.Operation.PATHLENGTH){//wenn ich pfadlänge berechnen will, muss ich ein paar andere sachen machen
            //kanten sind dafür uninteressant
            if (predicate.evaluate(vtc)){
                return new NumberValue(0); //Der Fall, dass ich der Zielknoten bin
            }else {

                if (nextNode != null) {//einfach nur die Kante auswerten
                    return nextNode.evaluate(vtc);
                } else {//Der fall sollte eigentlich nie eintreten, wenn kanten kein ende hat
                    return new NoVData();
                }
            }
        }else { //für alle vorherigen fälle
            if (predicate.evaluate(vtc)) {
                VData result = null;
                if (auswerten) {//Attribute auswerten
              //      System.out.println("auswerten wird gemacht");
             //       System.out.println(vtc.getOperation());
            //        System.out.println(vtc.getHashSet());

                    if(vtc.getOperation()== AggregatedEdgeVData.Operation.NONE || !(vtc.getHashSet().contains(vtc.getVertex().toString()))){//Wenn noch nicht im Hashset enthalten mache folgendes:

                   // if(!(vtc.getHashSet().contains(vtc.getVertex().toString()))){//Wenn noch nicht im Hashset enthalten mache folgendes:
                        VData v = attrribute.evaluate(vtc);
                        if (vtc.getOperation()== AggregatedEdgeVData.Operation.PREDICATE&&!(v instanceof NoVData)){
                            return v;//wenn eins erfüllt ist, reicht mir das schon aus, dann kann ich den Wert zurückgeben und beim PathexistencePredicat dann sehen, dass es kein NOVData war
                        }
                        result = MultiEdgeVData.evaluateHelper(vtc, result, v,vtc.getEdge());
             //           System.out.println("in if wird gemacht");
                        vtc.getHashSet().add(vtc.getVertex().toString());
                    }
                }
                if (nextNode!=null) {//Kanten auswerten
                    VData v = nextNode.evaluate(vtc);
                    result = MultiEdgeVData.evaluateHelper(vtc, result, v,vtc.getEdge());
              //      System.out.println("nextnode wird gemacht");
                }

                return (result == null) ? new NoVData() : result.evaluate(vtc); //wenn nix gemacht wurde (weil es das Feld z.b. net gibt, darf nicht null zurückgegebenn, sondern NoVdata (dann wird nix geschrieben)
            }
            else {
                return new NoVData();
            }
        }


    }

    public void setEdge(MultiEdgeVData lastEdge) {
        nextNode=lastEdge;
    }
}
