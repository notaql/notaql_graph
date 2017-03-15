package NotaQLGraph.Evaluation;

import NotaQLGraph.Database.Database;
import NotaQLGraph.NotaQLGraph;
import NotaQLGraph.Parser.*;
import com.tinkerpop.blueprints.Vertex;
import NotaQLGraph.model.predicate.Predicate;
import notaql.parser.antlr.NotaGraphParser;

import java.util.LinkedList;

/**
 * Created by yannick on 24.11.15.
 */
public class Transformation {
    private PredicateVisitor nodePredicateVisitor = null; //Visitor wird benutzt um bei Edges die Predikate der neuen Knoten zu analysieren
    private PredicateVisitor inPredicateVisitor = null;
    private VDataVisitorPredicate vDataVisitorPredicate =null;
    private VDataVisitorDollar vDataVisitorDollar =null;
    private Predicate inpredicate = null;
    private OutPathVisitor outPathVisitor = new OutPathVisitor();;
    private VDataVisitor vDataVisitor = new VDataVisitor(this);;
    private OutPathVisitorDollar outPathVisitorDollar = new OutPathVisitorDollar(this);
    private PredicateVisitorEdge edgePredicateVisitor = new PredicateVisitorEdge(this);
    private VDataVisitorPredicateEdge vDataVisitorPredicateEdge = null;
    private VDataVisitorEdgeConstructor vDataVisitorEdgeConstructor = null;

    //Statistik und Repeat Zeug
    public int inputFilterCounter=0;
    public int emitCounter=0;
    public int outputCounter=0;
    public int outputAggregateCounter=0;
    public int newEdgesCounter=0;
    public boolean percentageOK=true;

    public VDataVisitorPredicateEdge getvDataVisitorPredicateEdge(){
        if (vDataVisitorPredicateEdge ==null){vDataVisitorPredicateEdge = new VDataVisitorPredicateEdge(this);}
        return vDataVisitorPredicateEdge;
    }
    public VDataVisitorPredicate getvDataVisitorPredicate(){
        if (vDataVisitorPredicate ==null){vDataVisitorPredicate = new VDataVisitorPredicate(this);}
        return vDataVisitorPredicate;
    }
    public VDataVisitorDollar getvDataVisitorDollar(){
        if (vDataVisitorDollar ==null){vDataVisitorDollar = new VDataVisitorDollar(this);}
        return vDataVisitorDollar;
    }
    public PredicateVisitor getNodePredicateVisitor(){
        if (nodePredicateVisitor ==null){nodePredicateVisitor = new PredicateVisitor(this);}
        return nodePredicateVisitor;
    }
    public PredicateVisitorEdge getEdgePredicateVisitor(){
        if (edgePredicateVisitor ==null){edgePredicateVisitor = new PredicateVisitorEdge(this);}
        return edgePredicateVisitor;
    }
    public VDataVisitor getvDataVisitor(){
        return vDataVisitor;
    }
    public VDataVisitorEdgeConstructor getvDataVisitorEdgeConstructor(){
        if (vDataVisitorEdgeConstructor ==null){vDataVisitorEdgeConstructor = new VDataVisitorEdgeConstructor(this);}
        return vDataVisitorEdgeConstructor;
    }


    public void evaluate(NotaGraphParser.TransformationContext t, Database inputDatabase, Database outputDatabase, String percentageName, Double percentage) {
      //schauen ob es ein infilter gibt

        if (t.inPredicate()!=null){
            inPredicateVisitor=new PredicateVisitor(this);
            inpredicate = inPredicateVisitor.visitInPredicate(t.inPredicate());
        }

       //Attributspezifikationen
        Knoten knoten = new Knoten(outPathVisitor, vDataVisitor, outPathVisitorDollar, getNodePredicateVisitor(), getvDataVisitorEdgeConstructor(), this);
        knoten.initialisieren(t.attributeSpecification());

        //Bereits bei der Datenbank einige Knoten herausfiltern
        Filter getAttribute;
        Iterable<Vertex> alleKnotenDBFilter;
        LinkedList<Filter> filterList;
        if (inpredicate!=null&&(filterList= inpredicate.getAttribute(null))!=null&&filterList.get(0).getSize()==1){
            getAttribute=filterList.get(0);//nimm einfach immer das erste ==> user soll zuerst das stärkeste hinschreiben
       //     System.out.println("Attribute:" + getAttribute[0]+""+getAttribute[1]+"");
       //     System.out.println("Attribute:" + getAttribute[0].getClass()+""+getAttribute[1].getClass()+"");
            if (getAttribute!=null){
                alleKnotenDBFilter = inputDatabase.getDatabase().getVertices(getAttribute.getKey(),getAttribute.getValue().get(0));
            }else {
                alleKnotenDBFilter = inputDatabase.getDatabase().getVertices();
            }
        } else {//fall das ich keine Filterbedinung anwenden kann
            alleKnotenDBFilter = inputDatabase.getDatabase().getVertices();
        }


        //Kopieren der Knoten
        int counter = 0;
        for (Vertex v:alleKnotenDBFilter){
            counter++;
          // System.out.println("Knoten"+counter);
            if (counter%NotaQLGraph.maxNodesCommit==0){
                System.out.println("Kopiere Knoten"+counter);
                outputDatabase.commit();
            }
            if (inpredicate==null||inpredicate.evaluate(new VertexToCheck(v,null))) { //wenn Filter erfüllt, oder wenn es keinen Filter gibt
                inputFilterCounter++;
                knoten.store(outputDatabase,v, percentageName, percentage);
            }
        }


       //nochmal neu setzen, weil Iterable teilweise nicht doppelt durchgegangen werden darf...
        filterList = null;
        if (inpredicate!=null&&(filterList= inpredicate.getAttribute(null))!=null&&filterList.get(0).getSize()==1){
            getAttribute=filterList.get(0);//nimm einfach immer das erste ==> user soll zuerst das stärkeste hinschreiben
           // System.out.println("Attribute:" + getAttribute[0]+""+getAttribute[1]+"");
           // System.out.println("Attribute:" + getAttribute[0].getClass()+""+getAttribute[1].getClass()+"");
            if (getAttribute!=null){

                alleKnotenDBFilter = inputDatabase.getDatabase().getVertices((String)getAttribute.getKey(),getAttribute.getValue().get(0));
            }else {
                alleKnotenDBFilter = inputDatabase.getDatabase().getVertices();
            }
        } else {//fall das ich keine Filterbedinung anwenden kann
            alleKnotenDBFilter = inputDatabase.getDatabase().getVertices();
        }


        //für Kopieren der Kanten
        int counter2=0;
        for (Vertex v:alleKnotenDBFilter){
            counter2++;
        //    System.out.println("Kante"+counter2 + ": "+v.getId());
            if (counter2%NotaQLGraph.maxEdgesCommit==0){
                outputDatabase.commit();
                System.out.println("Kopiere Kante "+counter2);
            }

            if (inpredicate==null||inpredicate.evaluate(new VertexToCheck(v,null))) {    //wenn INFilter erfüllt, oder wenn es keinen Filter gibt-
                knoten.storeEdges(outputDatabase, v);

            }
        }


        //tmp werte in Datenbank entfernen. Herkunft: 1) Import 2) Aggregationen
        int counter3 = 1;

        for (Vertex v:outputDatabase.getDatabase().getVertices()){
            if (counter3%NotaQLGraph.maxNodesCommit==0){
                System.out.println("Lösche Temp werte"+counter3+"löschen");
                outputDatabase.commit();
            }
            counter3++;
           for (String s:v.getPropertyKeys()){
               if (s.endsWith("_tmp")){
                   Object old = v.getProperty(s.substring(0,s.length()-4));
                   Object neu = v.getProperty(s);
                   knoten.calculateAbweichung(percentage,percentageName,old,neu.toString(),s.substring(0,s.length()-4));
                   if (old==null){//davor nix,jetzt was da also änderung
                       outputAggregateCounter++;
                   }
                   if (old!=null&&!old.toString().equals(neu.toString())){//wenn davor der Wert ein anderer => änderung
                       outputAggregateCounter++;
                   }

                   v.setProperty(s.substring(0,s.length()-4),v.getProperty(s));
                   v.removeProperty(s);
               }
               else if (s.endsWith("_tmp_edge")) {
                   v.removeProperty(s);
               }else if (s.endsWith("_tmp_count")){
                 v.removeProperty(s);
               }
           }
        }
    }


    public void printStatistic(){
        System.out.println("Input: "+inputFilterCounter);
        System.out.println("Emit: "+emitCounter);
        System.out.println("Output: "+outputCounter);
        System.out.println("Output Aggregations: "+outputAggregateCounter);
        System.out.println("New Edges: "+newEdgesCounter);
        System.out.println("###### Transformation End ######");
    }
}
