package NotaQLGraph.model.vdata;

import NotaQLGraph.Evaluation.VertexToCheck;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import NotaQLGraph.model.predicate.Predicate;

/**
 * Created by yannick on 17.12.15.
 */
public class MultiEdgeVData implements VData {
    Predicate edgeVdPredicate;
    MultiVertexVData mulitv;
    boolean endeAnKante;
    VData vdata;
    Direction direction = Direction.BOTH;
    String[] label = null;


    public MultiEdgeVData(Predicate edgeVdPredicate){//soll nur für LOOP benutzt werden
        this.edgeVdPredicate=edgeVdPredicate;
    }

    public MultiEdgeVData(Predicate edgeVdatapredicate, MultiVertexVData multiVertexVData) {//wenn ich auf weiteren Knoten zugreifen möchte
        endeAnKante = false;
        this.edgeVdPredicate = edgeVdatapredicate;
        mulitv = multiVertexVData;
    }

    public MultiEdgeVData(Predicate edgeVdatapredicate, VData edgeinputData) {//wenn ich auf Kante zugreifen möchte
        endeAnKante = true;
        this.edgeVdPredicate = edgeVdatapredicate;
        vdata = edgeinputData;
    }

    public VData evaluate(VertexToCheck vtc) {
        VData result = null;
        label = edgeVdPredicate.getLabel();
        if (label==null) label = new String[0];
        String dir = edgeVdPredicate.getDirection();
        if(dir!=null&&dir.equals("_outgoing")) {direction=Direction.OUT;}
        if (dir!=null&&dir.equals("_incoming")){direction=Direction.IN;}
        if (endeAnKante) {

            for (Edge e : vtc.getVertex().getEdges(direction, label)) {

                vtc.setEdge(e);
                if (edgeVdPredicate.evaluate(vtc)) {

                    VData v = vdata.evaluate(vtc);
                    if (vtc.getOperation()== AggregatedEdgeVData.Operation.PREDICATE&&!(v instanceof NoVData)){

                        return v;//wenn eins erfüllt ist, reicht mir das schon aus, dann kann ich den Wert zurückgeben und beim PathexistencePredicat dann sehen, dass es kein NOVData war
                    }
                    result = evaluateHelper(vtc, result, v,e);
               }
            }


        }else {

            if (direction == Direction.BOTH || direction == Direction.OUT) {

                for (Edge e : vtc.getVertex().getEdges(Direction.OUT, label)) {

                   vtc.setEdge(e);
                   if (edgeVdPredicate.evaluate(vtc)) {

                        VData v = mulitv.evaluate(new VertexToCheck(e.getVertex(Direction.IN), vtc.getProperty(), vtc.getOperation(), vtc.getHashSet(),e));
                       if (vtc.getOperation()== AggregatedEdgeVData.Operation.PREDICATE&&!(v instanceof NoVData)){

                           return v;//wenn eins erfüllt ist, reicht mir das schon aus, dann kann ich den Wert zurückgeben und beim PathexistencePredicat dann sehen, dass es kein NOVData war
                       }
                        result = evaluateHelper(vtc, result, v,e);
                    }
                }

            }

            if (direction == Direction.BOTH || direction == Direction.IN) {

                for (Edge e : vtc.getVertex().getEdges(Direction.IN, label)) {

                    vtc.setEdge(e);
                    if (edgeVdPredicate.evaluate(vtc)) {

                        VData v = mulitv.evaluate(new VertexToCheck(e.getVertex(Direction.OUT), vtc.getProperty(), vtc.getOperation(),vtc.getHashSet(),e));
                        if (vtc.getOperation()== AggregatedEdgeVData.Operation.PREDICATE&&!(v instanceof NoVData)){
                            System.out.println("error");
                            return v;//wenn eins erfüllt ist, reicht mir das schon aus, dann kann ich den Wert zurückgeben und beim PathexistencePredicat dann sehen, dass es kein NOVData war
                        }
                        result = evaluateHelper(vtc, result, v,e);
                    }
                }
            }
        }
        return (result == null) ? new NoVData() : result.evaluate(vtc); //wenn nix gemacht wurde (weil es das Feld z.b. net gibt, darf nicht null zurückgegebenn, sondern NoVdata (dann wird nix geschrieben)
    }

    public static VData evaluateHelper(VertexToCheck vtc, VData result, VData v, Edge e) {

        switch (vtc.getOperation()) {

            case PREDICATE:
                return v;

            case LISTE:
            case NONE:
                if (result == null) {//Fall, dass ich aller erste Element erzeuge
                    if (v instanceof CollectionVData) {
                                  result = v;
                    } else if (!(v instanceof NoVData)) {
                        result = new CollectionVData();
                        ((CollectionVData) result).add(v, e);
                    }
                } else {// der Fall, dass im Result schon eine CollectionVDATA enthalten ist
                    if (v instanceof CollectionVData){//Fall, dass ich 2 Collections vereinen möchte
                        ((CollectionVData) result).addAll((CollectionVData) v);
                    }else if  (!(v instanceof NoVData)){ //Fall das ich eine Collection habe und einen einzelen Wert hinzufügen will
                        ((CollectionVData) result).add(v,e);
                    }
                }

                break;
            case SUM:
                if (result == null) {
                    if (v instanceof NumberValue) {
                        result = v;
                    }
                } else {
                    if (v instanceof NumberValue) {
                        Double a = ((NumberValue) result).getValue().doubleValue();
                        Double b = ((NumberValue) v).getValue().doubleValue();
                        result = new NumberValue(a + b);
                    }
                }
                break;
            case AVG:
                if (result == null) {
                    if (v instanceof NumberValue) {//Für das Letze Elemente
                        result = new AverageValue(((NumberValue) v).getValue().doubleValue(), 1);
                    }
                    else if (v instanceof  AverageValue){
                        result = v;
                    }
                } else {
                    if (v instanceof NumberValue) {
                        Double a = ((NumberValue) v).getValue().doubleValue();
                        Double b = ((AverageValue) result).getValue().doubleValue();
                        int countb = ((AverageValue) result).getCounter();
                        result = new AverageValue((b * countb + a) / (countb + 1), countb + 1);
                    }
                    else if (v instanceof AverageValue){//fall für innere Baum (erklärung siehe count)
                        Double a = ((AverageValue) v).getValue().doubleValue();
                        int counta = ((AverageValue) v).getCounter();
                        Double b = ((AverageValue) result).getValue().doubleValue();
                        int countb = ((AverageValue) result).getCounter();
                        result = new AverageValue((b * countb + a*counta) / (countb + counta), countb + counta);
                    }
                }
                break;
            case MIN:
                if (result == null) {
                    if (v instanceof NumberValue) {
                        result = v;
                    }
                } else {
                    if (v instanceof NumberValue) {
                        Double a = ((NumberValue) result).getValue().doubleValue();
                        Double b = ((NumberValue) v).getValue().doubleValue();
                        if (b < a) {
                            result = new NumberValue(b);
                        }
                    }

                }
                break;
            case MAX:
                if (result == null) {
                    if (v instanceof NumberValue) {
                        result = v;
                    }
                } else {
                    if (v instanceof NumberValue) {
                        Double a = ((NumberValue) result).getValue().doubleValue();
                        Double b = ((NumberValue) v).getValue().doubleValue();
                        if (a < b) {
                            result = new NumberValue(b);
                        }
                    }
                }
                break;
            case COUNT:
                if (result == null) {
                    if (v instanceof CountValue) {//Innerer "Baum" beim Aufaddieren von bereits existierenden Count Werten
                        result = v;
                    } else if (!(v instanceof NoVData)) {//hier kann alles gezählt werdne, nicht nur Numbervalue, sozusagen für "Blätter"
                        result = new CountValue(1);
                    }
                } else {
                    if (v instanceof CountValue) {//Innerer "Baum" beim Aufaddieren von bereits existierenden Count Werten
                        result = new CountValue(((CountValue) result).getValue().intValue() + ((CountValue) v).getValue().intValue());
                    } else if (!(v instanceof NoVData)) {//hier kann alles gezählt werdne, nicht nur Numbervalue, sozusagen für "Blätter"
                        result = new CountValue(((CountValue) result).getValue().intValue() + 1);
                    }
                }
                break;
            case PATHLENGTH:
                if (result == null) {
                    if (v instanceof NumberValue) {//Fall das bisher noch kein wert ausgewählt, dann nehm den
                        result = new NumberValue(((NumberValue) v).getValue().doubleValue()+1);
                    }
                } else {//fall das schon ein wert ausgewählt
                    if (v instanceof NumberValue) {
                        Double a = ((NumberValue) result).getValue().doubleValue();
                        Double b = ((NumberValue) v).getValue().doubleValue()+1;
                        if (b < a) {
                            result = new NumberValue(b);//im unterschid zu min muss man überall eins hochzählen
                        }
                    }

                }
                break;

            default:
                throw new RuntimeException("Unknown operator: " + vtc.getOperation());
        }
      //  System.out.println(result.getClass());
        return result;
    }

    public void setVertex(MultiVertexVData lastVertex) {
        mulitv=lastVertex;
    }
}
