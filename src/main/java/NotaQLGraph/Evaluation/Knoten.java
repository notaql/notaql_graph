package NotaQLGraph.Evaluation;

import NotaQLGraph.Database.Database;
import NotaQLGraph.Parser.*;
import NotaQLGraph.model.konstructor.EdgeAttributeVData;
import NotaQLGraph.model.konstructor.EdgeKonstructor;
import NotaQLGraph.model.predicate.RelationalPredicate;
import NotaQLGraph.model.vdata.*;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import NotaQLGraph.model.outpath.DollarPredicate;
import NotaQLGraph.model.outpath.NodeID;
import NotaQLGraph.model.outpath.Outpath;
import NotaQLGraph.model.predicate.Predicate;
import notaql.parser.antlr.NotaGraphParser;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yannick on 02.12.15.
 */
public class Knoten {
    OutPathVisitor outPathVisitor;
    VDataVisitor vDataVisitor;
    OutPathVisitorDollar outPathVisitorDollar;
    PredicateVisitor predicateVisitor;
    VDataVisitorEdgeConstructor vDataVisitorEdgeConstructor;
    LinkedList<Outpath> outpath = new LinkedList<Outpath>();
    LinkedList<VData> inpath = new LinkedList<VData>();
    LinkedList<Predicate> outpathEdge = new LinkedList<Predicate>();
    LinkedList<VData> inpathEdge = new LinkedList<VData>();
    Transformation transformation;


    public Knoten(OutPathVisitor outPathVisitor, VDataVisitor vDataVisitor, OutPathVisitorDollar outPathVisitorDollar, PredicateVisitor nodePredicateVisitor, VDataVisitorEdgeConstructor vDataVisitorEdgeConstructor, Transformation transformation) {
        this.transformation = transformation;
        this.outPathVisitor = outPathVisitor;
        this.vDataVisitor = vDataVisitor;
        this.outPathVisitorDollar = outPathVisitorDollar;
        this.predicateVisitor = nodePredicateVisitor;
        this.vDataVisitorEdgeConstructor = vDataVisitorEdgeConstructor;
    }

    public void initialisieren(List<NotaGraphParser.AttributeSpecificationContext> ctx) {
        for (NotaGraphParser.AttributeSpecificationContext c : ctx) {


            if (c.getChildCount() == 4) {
                outpath.add(outPathVisitor.visit(c.getChild(1)));
                inpath.add(vDataVisitor.visit(c.getChild(3)));
            }

            if (c.getChildCount() == 6) {//DolarPredicate hat 6 Werte da drin stehen
                Predicate out = new DollarPredicate(outPathVisitorDollar.visit(c.getChild(2)));

                outpath.add((DollarPredicate) out);
                inpath.add(vDataVisitor.visit(c.getChild(5)));
            }

            if (c.getChildCount() == 3) {//DolarPredicate ohne Prädikat hat 3 werte
                Predicate out = new DollarPredicate(new RelationalPredicate(new BooleanValue(true), new BooleanValue(true), RelationalPredicate.Operator.EQ)); //soll immer true sein
                outpath.add((DollarPredicate) out);
                inpath.add(vDataVisitor.visit(c.getChild(2)));
            }

            if (c.getChildCount() == 5) {//EDGES
                outpathEdge.add(predicateVisitor.visit(c.getChild(1)));
                inpathEdge.add(vDataVisitorEdgeConstructor.visit(c.getChild(4)));
            }
        }
    }

    public void store(Database outputDB, Vertex v, String percentageName, Double percentage) {

        LinkedList<Vertex> neu = new LinkedList<Vertex>();
        LinkedList<Boolean> veraenderung = new LinkedList<Boolean>();
        //ID ANFANG
        if (outpath.contains(new NodeID("_id"))) {
            VData inp = inpath.get(outpath.indexOf(new NodeID("_id"))).evaluate(new VertexToCheck(v, null));
            int loopEnde = 1;
            if (inp instanceof CollectionVData) {//Fall 1: Ich erstelle mehre Knoten, weil bei ID sowas steht: OUT._ID <- IN._e_.name
                loopEnde = ((CollectionVData) inp).size();
            }
            if (inp instanceof NoVData){//wenn ich z.b. out.id <- in._e_._id stehen habe, der Knoten aber keine ausgehende ID hat, dann will ich dafür keinen Knoten erzeugen
                //sondern gar nichts machen
                //genauso wenn ich nach dem alter sortiere, das alter aber nicht angegeben ist...
                loopEnde=0;
            }
            String knotenID = inp.toString();
            for (int i = 0; i < loopEnde; i++) {//wenn Collection mach es mehrmals, sonst genau ein Mal
                if (inp instanceof CollectionVData) {
                    knotenID = ((CollectionVData) inp).get(i).toString();
                }

                if (outputDB.getDatabase().getVertex(knotenID) == null) {
                    //Fall 1: Knoten mit angegebener ID existiert noch nicht
                    neu.add(outputDB.getDatabase().addVertex(knotenID));
                    veraenderung.add(true);
                    transformation.emitCounter++;
                } else {
                    //Fall 2: Knoten mit angegebener ID existiert bereits
                    neu.add(outputDB.getDatabase().getVertex(knotenID));
                    veraenderung.add(false);
                }
            }

        } else {
            //Fall 4: Knoten ohne angegebene ID ==> wird immer neu erzeugt (nummerierung ab 0)
            neu.add(outputDB.getDatabase().addVertex(null));
            veraenderung.add(true);
            transformation.emitCounter++;
        }//ID ENDE


        for (int h = 0; h < neu.size(); h++) {//Wenn ein Knoten mehrdeutig (OUT.ID = in._e_,name) führe das ganze mehrmals aus...
            for (int i = 0; i < outpath.size(); i++) {//Danach die Knoten durchgehen

                //Dollar Predicate checken. Wenn DollarPredicate, muss ich eventuell mehrere Knotenattribute erstellen
                //Wenn kein Dollarpredicat, genau eins (wenn der Path keine ID ist)
                if (outpath.get(i) instanceof DollarPredicate) {
                    for (String property : v.getPropertyKeys()) {
                        if (((DollarPredicate) outpath.get(i)).evaluate(new VertexToCheck(v, property))) {
                            String value = inpath.get(i).evaluate(new VertexToCheck(v, property)).toString();
                            calculateAbweichung(percentage,percentageName,neu.get(h).getProperty(property),value,property);
                            neu.get(h).setProperty(property, value );
                            veraenderung.set(h,true);transformation.emitCounter++;
                        }
                    }
                } else if (!outpath.get(i).equals(new NodeID("_id"))) {  Iterable<Vertex> alleKnotenDBFilter;//id muss nicht mehr kopiert werden, dass haben wir ja bereits gemacht
                    VData inp = inpath.get(i).evaluate(new VertexToCheck(v, null));

                    if (inpath.get(i) instanceof AggregatedVData) {//Wenn aggregated data mach aggregated

                        if (!(inp instanceof NoVData)) {//wenn der Wert NoVData ist, will ich nix machen (nicht vorhanden), jedoch will ich etwas machen, wenn ich Zähle (da hab ich jetzt emptyvdata)
                            storeAggregatedData(neu.get(h), outpath.get(i).getValue(), inp.toString(), ((AggregatedVData) inpath.get(i)).getOperation());
                        }

                    } else if (inpath.get(i) instanceof AggregatedEdgeVData){
                        if (inp instanceof CollectionVData){
                            if (((AggregatedEdgeVData) inpath.get(i)).getOperation()== AggregatedEdgeVData.Operation.LISTE){
                                neu.get(h).setProperty(outpath.get(i).getValue(), ((CollectionVData) inp).getAllData()); //schreibe wirklich das ganze Array da rein
                                veraenderung.set(h,true);transformation.emitCounter++;
                            }
                            else if (((CollectionVData) inp).size() > 1){ //Fehler werfen, da bei Kanten mehrere Ergebnisse rauskommen (nicht eindeutig wie "name von mutter"
                                //Der Fall das ich keinen Operator hingeschrieben hab (also NONE)
                                throw new RuntimeException("IN." + outpath.get(i).toString() + ": nicht eindeutig. Bitte eine Aggregatsfunktion auswählen ");
                            } else {//alle anderen Fälle wie COUNTE, MINE; MAXE... ==> Collection hat immer nur 1 Element
                                calculateAbweichung(percentage,percentageName,neu.get(h).getProperty(outpath.get(i).getValue()),((CollectionVData) inp).get(0).toString(),outpath.get(i).getValue());
                                neu.get(h).setProperty(outpath.get(i).getValue(), ((CollectionVData) inp).get(0).toString());
                                veraenderung.set(h,true);transformation.emitCounter++;
                            }
                        } else if (!(inp instanceof NoVData)) { //in dem Fall NOVDATA mache gar nix (nicht null ausgeben)

                            calculateAbweichung(percentage,percentageName,neu.get(h).getProperty(outpath.get(i).getValue()),inp.toString(),outpath.get(i).getValue());
                            neu.get(h).setProperty(outpath.get(i).getValue(), inp.toString());
                            veraenderung.set(h,true);transformation.emitCounter++;
                        }
                    } else if (!(inp instanceof NoVData)) { //in dem Fall NOVDATA mache gar nix (nicht null ausgeben)
                        //wenn keine Aggregated Data einfach Wert setzen (und evtl überschreiben)
                        calculateAbweichung(percentage,percentageName,neu.get(h).getProperty(outpath.get(i).getValue()),inp.toString(),outpath.get(i).getValue());
                        neu.get(h).setProperty(outpath.get(i).getValue(), inp.toString());//to String methode ist nur bei den atomare typen definiert, weil der rest am Ende in solch einen Umgewandelt wird
                        veraenderung.set(h,true);transformation.emitCounter++;
                    }
                }
            }
        }
        for (int h =0;h<neu.size();h++) {
            if (veraenderung.get(h)) {
                transformation.outputCounter++;
            }
        }
    }


    void storeAggregatedData(Vertex neu, String outpathold, String inpath, AggregatedVData.Operation op) {
        String outpath = outpathold + "_tmp"; //wird angefügt, damit nur temporäre Werte erzeugt werden, die dann am Schluss wieder gelöscht werden können
        Object oldvalue = neu.getProperty(outpath);
        if (oldvalue == null) {//Fall, das der erste Wert abgespeichert wird

            switch (op) {
                case SUM:
                    neu.setProperty(outpath, inpath);
                    break;
                case AVG:
                    neu.setProperty(outpath, inpath);
                    //    neu.setProperty(outpath+"_sum", inpath); //sum wird nicht mehr benötigt, da alternative rechnugn
                    neu.setProperty(outpath + "_count", 1);
                    break;
                case COUNT:
                    neu.setProperty(outpath, 1);
                    break;
                case MIN:
                    neu.setProperty(outpath, inpath);
                    break;
                case MAX:
                    neu.setProperty(outpath, inpath);
                    break;
            }
            transformation.emitCounter++;

        } else {
            Double old;
            if (!(oldvalue instanceof Double)) { //alte Wert als Double einlesen
                old = Double.parseDouble(oldvalue.toString());
            } else {
                old = (Double) oldvalue;
            }
            switch (op) {
                case SUM:
                    neu.setProperty(outpath, Double.parseDouble(inpath) + old);
                    transformation.emitCounter++;
                    break;
                case AVG:
                    Double count = Double.parseDouble(neu.getProperty(outpath + "_count").toString()) + 1;
                    Double newvalue = Double.parseDouble(inpath);
                    neu.setProperty(outpath + "_count", count);
                    neu.setProperty(outpath, (old * (count - 1) + newvalue) / count);
                    transformation.emitCounter++;

                    break;
                case COUNT:
                    neu.setProperty(outpath, old + 1);
                    transformation.emitCounter++;
                    break;
                case MIN:
                    if (Double.parseDouble(inpath) < old) {
                        neu.setProperty(outpath, Double.parseDouble(inpath));
                        transformation.emitCounter++;
                    }
                    break;
                case MAX:
                    if (Double.parseDouble(inpath) > old) {
                        neu.setProperty(outpath, Double.parseDouble(inpath));
                        transformation.emitCounter++;
                    }
                    break;
            }
        }
    }


    public void storeEdges(Database outputDatabase, Vertex startKnoten) {
        LinkedList<LinkedList<Filter>> edgeFilter = new LinkedList<LinkedList<Filter>>();
        for (int i = 0; i < outpathEdge.size(); i++) {//wenn es mehre Out.e zeilen gibt
            if (outpathEdge.get(i) != null) {
                LinkedList<Filter> result = outpathEdge.get(i).getAttribute(startKnoten);
                edgeFilter.add(result);
            }
        }
    //    System.out.println(edgeFilter.size()+ "EdgeFiltersize");
    //    System.out.println(outpathEdge.size()+ "outpathEdgesize");

        for (int outpathEdgeLine = 0; outpathEdgeLine < outpathEdge.size(); outpathEdgeLine++) {//WENN ICH MEHRERE Out._e_ zeilen habe
            if (edgeFilter.get(outpathEdgeLine)==null){//Der Fall das kein Filter existiert
           //     System.out.println("outpathEdge = null, es existiert kein Filter");
                Iterable<Vertex> zielKnotenGefiltert = outputDatabase.getDatabase().getVertices();
                for (Vertex zielKnoten : zielKnotenGefiltert) {
        //            System.out.println(startKnoten.getId()+ "Ziel:"+zielKnoten.getId());
                    storeEdgeZielKnoten(startKnoten, zielKnoten, outputDatabase, outpathEdgeLine);
                }
            }
            else {//Der Fall, dass ein Filter existiert
                for (int i=0;i<edgeFilter.get(outpathEdgeLine).size();i++){
                    Filter filter = edgeFilter.get(outpathEdgeLine).get(i);
                    if (!(filter.isCollectionIsEmpty())) {//Erzeuge nur Kanten, wenn die Collection nicht leer war.
                        for (int filterI = 0; filterI < filter.getSize(); filterI++) {//wenn ich mehrer Filter hab, also die Collection _id = [1,2,3] importiert werden soll
                            if (filter.getKey().equals("_id")) {
                                Vertex zielKnoten = outputDatabase.getDatabase().getVertex(filter.getValue().get(filterI));
           //                     System.out.println(startKnoten.getId()+ "Ziel2:"+zielKnoten.getId()+"filtersize:"+filter.getSize());
            //                    System.out.println(filter.getValue());
                                storeEdgeZielKnoten(startKnoten, zielKnoten, outputDatabase, outpathEdgeLine);
                            } else {
                                Iterable<Vertex> zielKnotenGefiltert = outputDatabase.getDatabase().getVertices(filter.getKey(), filter.getValue().get(filterI));
                                for (Vertex zielKnoten : zielKnotenGefiltert) {
                         //           System.out.println(startKnoten.getId()+ "Ziel3:"+zielKnoten.getId());
                                    storeEdgeZielKnoten(startKnoten, zielKnoten, outputDatabase, outpathEdgeLine);
                                }
                            }
                        }
                    }
                }
            }
        }//out._e_ zeilen
    }

    private void storeEdgeZielKnoten(Vertex startKnoten, Vertex zielKnoten, Database outputDatabase, int outpathEdgeLine){
       // System.out.println(startKnoten.getId()+" : "+zielKnoten.getId());
        VertexToCheck vtc = new VertexToCheck(zielKnoten, null);
        vtc.setRootvertex(startKnoten);
        vtc.setGetEdges(true);
        vtc.setEdgesList();
        if (outpathEdge.get(outpathEdgeLine).evaluate(vtc)) {//überprüfe ob die Bedingung für einen Wert in der Liste richtig ist
            List<Edge> l = vtc.getEdgesList();
    //        System.out.println(l.size() + " l.size()" );
             //    System.out.println("quelle:"+vertexTODo.getId()+"dest:" +v.getId()+ "size"+l.size());
            if (l.size() == 0) { //nur einmal (ohne @)
               storeEdgeinDatabase(vtc, null, startKnoten, outputDatabase, zielKnoten,outpathEdgeLine);
            } else if (l.get(0) != null) {//hier hat man normalerweise edges abgelegt, nur bei import ist allse null
                for (Edge e : l) {//führe das ganze Mehrmals aus (für IN._e[@]
                   storeEdgeinDatabase(vtc, e, startKnoten, outputDatabase, zielKnoten, outpathEdgeLine);
                }
            } else {//Fall den man für den Import braucht, Problem: alle Edges werden als "null" abgespeichert
                for (int j = 0; j < l.size(); j++) { //TODO nochmal überprüfen ob ich die schleife brauch
                    storeEdgeinDatabase(vtc, null, startKnoten, outputDatabase, zielKnoten, outpathEdgeLine);
                }
            }
        }
    }

    /**
     * Ich bekomme den Start und Zielknoten, sowie falls vorhanden die alte Kante zum auslesen von Label und Attribute von alten Kanten
     * und erzeuge dadurch eine oder mehrer neue Kanten. Der Startknoten muss noch auf den Zielknoten gemappt werden.

     */
    private void storeEdgeinDatabase(VertexToCheck vtc, Edge e, Vertex startKnoten,Database outputDatabase, Vertex zielKnoten, int outpathEdgeLine){
        vtc.setEdge(e);

        String label = ((EdgeKonstructor) inpathEdge.get(outpathEdgeLine)).getLabel().evaluate(vtc).toString();
        Direction direction = ((EdgeKonstructor) inpathEdge.get(outpathEdgeLine)).getDirection();
        //   if outpathEdge.get(i) instanceof CollectionVData


        //mehre Startknoten werden dadurch möglich
        LinkedList<String> startKnotenTransform = mapID(startKnoten);
        for (String idStartKnoten : startKnotenTransform) {
        //    System.out.println("write edge:" + idStartKnoten + ", " + zielKnoten.getId());
            if (direction == Direction.IN) {
                e = zielKnoten.addEdge(label, outputDatabase.getDatabase().getVertex(idStartKnoten));
            } else {
                e = outputDatabase.getDatabase().getVertex(idStartKnoten).addEdge(label, zielKnoten);
            }
            transformation.newEdgesCounter++;
            //ATTRIBUTE SETZEN
            for (VData ek : ((EdgeKonstructor) inpathEdge.get(outpathEdgeLine)).getAttributes()) {
             //   System.out.println(((((EdgeAttributeVData) ek).getKey())));
             //   System.out.println(ek.evaluate(vtc));
                e.setProperty(((EdgeAttributeVData) ek).getKey(), ek.evaluate(vtc).toString());
            }
        }
    }

    /*
    Übernimmt Mapping von KnotenID auf der alter Datenbank zu der ID auf der neuen Datenbank
     */
    private LinkedList<String> mapID(Vertex v) {
        LinkedList<String> result = new LinkedList<String>();
        if (outpath.contains(new NodeID("_id"))) {
            VData inp = inpath.get(outpath.indexOf(new NodeID("_id"))).evaluate(new VertexToCheck(v, null));
            int loopEnde = 1;//Fall 2
            if (inp instanceof CollectionVData) {//Fall 1: Ich erstelle mehre Knoten, weil bei ID sowas steht: OUT._ID <- IN._e_.name
                loopEnde = ((CollectionVData) inp).size();
            }
            String knotenID = inp.toString();
            for (int i = 0; i < loopEnde; i++) {//wenn Collection mach es mehrmals, sonst genau ein Mal
                if (inp instanceof CollectionVData) {
                    knotenID = ((CollectionVData) inp).get(i).toString();
                }
                result.add(knotenID);
            }

        }
        return result;
    }

    public void calculateAbweichung(Double percentage, String percentageName, Object altObject, String neu, String attribute){
        if (altObject==null){return;}
        String alt = altObject.toString();
        try {
            Double alterWert = Double.parseDouble(alt);
            Double neuerWert = Double.parseDouble(neu);
            calculateAbweichung(percentage, percentageName, alterWert, neuerWert, attribute);
        }
        catch (Exception e) {
            transformation.percentageOK=false;
        }
    }

    private void calculateAbweichung(Double percentage, String percentageName, Double alterWert, Double neuerWert, String attribute){
          if (percentageName!=null&&transformation.percentageOK&&attribute.equals(percentageName)){
              Double abweichung = (neuerWert-alterWert)/alterWert /100;
              if (percentage<abweichung||-percentage>abweichung){
                  transformation.percentageOK=false;
            }
        }
    }
}


