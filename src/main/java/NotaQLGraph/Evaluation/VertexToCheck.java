package NotaQLGraph.Evaluation;

import com.tinkerpop.blueprints.Edge;
import com.tinkerpop.blueprints.Vertex;
import NotaQLGraph.model.vdata.AggregatedEdgeVData.*;

import java.util.HashSet;
import java.util.LinkedList;

/**
 *
 * Objekte die übergeben werden um zu testen ob die Property bei dem Vertex erfüllt ist oder nicht
 * Created by yannick on 16.12.15.
 */

public class VertexToCheck {
    private Vertex vertex; //welcher Knoten untersucht wird
    private Edge edge; //welche Kante untersucht wird
    private String property; //property für $ Predikator?!?
    private Operation aggregationoperation; //Aggregatsfunktion, welche bei den Edges angewandt wird
    private HashSet<String> hashSet; // wird benötigt um zu erkennen, dass Daten nicht doppelt vorkommen (z.b. count von freunden, wenn 2 kanten zu einem freund)
    private Vertex rootvertex; // wird benötigt um die Edges auszuwerten, dann ist die rootvertex der Knoten von der die kante startet und die "vertex" der Knoten des zielknotens
    private boolean getEdges = false; // wird benötigt um die passenden Edges zurück zugeben, für die @ Operator beim Kanten erzeugen
    private LinkedList<Edge> edgesPredicateTrue;//hier werden die passenden Edges für den @ Operator gespeichert

    public VertexToCheck(Vertex v, String property){
        this.vertex=v;
        this.property=property;
    }

    public VertexToCheck(Vertex v, String property, Operation o){ //neuer Konstruktor für Edges auszuwerten (dort muss operation schon lokal ausgewertet werden
        this.vertex=v;
        this.property=property;
        this.aggregationoperation=o;
    }

    public VertexToCheck(Vertex v, String property, Operation o, HashSet<String> h, Edge e){ //neuer Konstruktor für Edges auszuwerten (dort muss operation schon lokal ausgewertet werden
        this.vertex=v;
        this.property=property;
        this.aggregationoperation=o;
        this.hashSet=h;
        this.edge=e;
    }

    public VertexToCheck(Edge e, String property, Operation o){ //neuer Konstruktor für Edges auszuwerten (dort muss operation schon lokal ausgewertet werden
        this.edge=e;
        this.property=property;
        this.aggregationoperation=o;
    }

    public String getProperty() {
        return property;
    }
    public Vertex getVertex() {
        return vertex;
    }
    public Edge getEdge() {
        return edge;
    }
    public void setEdge(Edge edge) {
        this.edge = edge;
    }
    public Operation getOperation(){
        return aggregationoperation;
    }
    public void setAggregationoperation(Operation aggregationoperation) {
        this.aggregationoperation = aggregationoperation;
    }
    public HashSet<String> getHashSet() {
        return hashSet;
    }
    public Vertex getRootvertex() {
        return rootvertex;
    }
    public void setRootvertex(Vertex rootvertex) {
        this.rootvertex = rootvertex;
    }

    public void setVertex(Vertex vertex) {
        this.vertex = vertex;
    }
    public void setNewHashset(){this.hashSet=new HashSet<String>();}

    public LinkedList<Edge> getEdgesList() {
        return edgesPredicateTrue;
    }

    public void setEdgesList() {
        this.edgesPredicateTrue = new LinkedList<Edge>();
    }
    public void addEdgetoList (Edge e){
        this.edgesPredicateTrue.add(e);
    }

    public boolean isGetEdges() {
        return getEdges;
    }
    public void setGetEdges(boolean getEdges) {
        this.getEdges = getEdges;
    }
}
