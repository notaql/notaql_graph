package NotaQLGraph.model.predicate;

import NotaQLGraph.Evaluation.Filter;
import NotaQLGraph.Evaluation.VertexToCheck;
import com.tinkerpop.blueprints.Vertex;
import NotaQLGraph.model.vdata.*;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Klasse für Relationale Predikate
 * also z.b. "10<gehalt
 * Created by yannick on 25.11.15.
 */
public class RelationalPredicate implements Predicate {

    private VData left;
    private VData right;
    private Operator operator;

    public RelationalPredicate(VData left, VData right, Operator operator) {
        this.left = left;
        this.right = right;
        this.operator = operator;
    }

    public boolean evaluate(VertexToCheck vtc) {
        Vertex v = vtc.getVertex();
        VData l = left.evaluate(vtc);
        VData r = right.evaluate(vtc);
//        System.out.println(l+" " +r);


        if (l instanceof CollectionVData&&!(r instanceof CollectionVData)){//Links = mehrere Ergebnisse
            boolean result = false;
            for (int i =0; i < ((CollectionVData)l).size();i++){
                boolean zwischenergebnis = operatorAnwenden(((CollectionVData) l).get(i), r, operator);
                if (vtc.isGetEdges()==true){
                    if (zwischenergebnis){

                        result = true;
                        vtc.addEdgetoList(((CollectionVData) l).getEdge(i));
                    }
                }else {
                   if (zwischenergebnis) {
                        return true;
                   }
                }
            }

            return result;
        }
        else if (!(l instanceof  CollectionVData) && r instanceof CollectionVData){//Rechts = mehrere Ergebnisse
            boolean result = false;
            for (int i =0; i < ((CollectionVData)r).size();i++){
                boolean zwischenergebnis=  operatorAnwenden(l,((CollectionVData) r).get(i),operator);
                if (vtc.isGetEdges()==true){
                    if (zwischenergebnis){

                        result = true;
                        vtc.addEdgetoList(((CollectionVData) r).getEdge(i));
                    }
                }else {
                    if (zwischenergebnis) {

                        return true;
                    }
                }
            }
            return result;
        }
        else if (l instanceof  CollectionVData && r instanceof CollectionVData){//Links&&Rects = mehrere Ergebnisse
            boolean result = false;
            for (int i =0; i < ((CollectionVData)l).size();i++){
                for (int j =0; j < ((CollectionVData)r).size();j++){
                    boolean zwischenergebnis=  operatorAnwenden(((CollectionVData) l).get(i),((CollectionVData) r).get(i),operator);
                    //Hier wurde der Fall mit vtc.addEdge weggelassen, da so eine Anfrage nicht vorkommen sollte bei dem vergleich (_id = IN._e?(_incoming)_.id <- EDGE (_outgoing, _l <-IN._e[@]_l
                    if (zwischenergebnis) {
                            return true;

                    }
                }
            }
            return result;
        }
        else{//Links && Rechts jeweils nur ein Erbebnis
       //     System.out.println("bla") ;
       //     System.out.println(left.getClass()+""+right.getClass());
       //     System.out.println(operatorAnwenden(l,r,operator));
            return operatorAnwenden(l,r,operator);

        }
    }




    private boolean operatorAnwenden(VData l, VData r, RelationalPredicate.Operator o){
        //wenn ich die nächsten zeilen nicht mache, wird die Rückgabe true,  wenn ich Number 33.0 mit String 33 vergleichen möchte
        if (l instanceof StringValue){
        try {
            l =new NumberValue(Double.parseDouble(l.toString()));
        }
        catch (Exception e) {
        }}
        if (r instanceof StringValue){
            try {
                r =new NumberValue(Double.parseDouble(r.toString()));
            }
            catch (Exception e) {
            }}

        switch (o) {
            case LT:
                return lowerThan(l,r);
            case LTEQ:
                return lowerThan(l,r)||equal(l,r);
            case GT:
                return greaterThan(l,r)||equal(l,r);
            case GTEQ:
                return greaterThan(l,r);
            case EQ:
                return equal(l,r);
            case NEQ:
                return !equal(l,r);
        }
        throw new RuntimeException("RelationPredicate Runtimeexeption"+o);
    }

    private boolean lowerThan (VData l, VData r) {
        if (l instanceof NumberValue && r instanceof NumberValue) {
            return (((NumberValue) l).compareTo((NumberValue) r)<0); //1 kommt raus, wenn der Wert größer ist, 0 wenn gleich -1 wenn kleiner
        }
        if (l instanceof StringValue && r instanceof StringValue) {
            return (((StringValue) l).compareTo((StringValue) r)<0); //1 kommt raus, wenn der wert größer ist, 0 wenn gleich -1 wenn kleiner
        }
        if (l instanceof NumberValue && r instanceof StringValue) {
            return l.toString().compareTo(((StringValue) r).getValue())<0; //1 kommt raus, wenn der wert größer ist, 0 wenn gleich -1 wenn kleiner
        }
        if (l instanceof StringValue && r instanceof NumberValue) {
            return((((StringValue) l).getValue()).compareTo(r.toString())<0); //1 kommt raus, wenn der wert größer ist, 0 wenn gleich -1 wenn kleiner
        }
        if (l instanceof NoVData || r instanceof NoVData){
            return false;
        }

        throw new RuntimeException(l.toString() + "Boolean kann nicht mit Lower than verglichen werden"+r.toString());
    }

    private boolean greaterThan (VData l, VData r) {//wegen VDATA fall brauch ich auch greater than, weil bei novdata zählt umkehrung nicht [novadta < novdata = false; novadta > novdata = true ==> error)
        if (l instanceof NumberValue && r instanceof NumberValue) {
            return (((NumberValue) l).compareTo((NumberValue) r)>0); //1 kommt raus, wenn der Wert größer ist, 0 wenn gleich -1 wenn kleiner
        }
        if (l instanceof StringValue && r instanceof StringValue) {
            return (((StringValue) l).compareTo((StringValue) r)>0); //1 kommt raus, wenn der wert größer ist, 0 wenn gleich -1 wenn kleiner
        }
        if (l instanceof NumberValue && r instanceof StringValue) {
            return l.toString().compareTo(((StringValue) r).getValue())>0; //1 kommt raus, wenn der wert größer ist, 0 wenn gleich -1 wenn kleiner
        }
        if (l instanceof StringValue && r instanceof NumberValue) {
            return((((StringValue) l).getValue()).compareTo(r.toString())>0); //1 kommt raus, wenn der wert größer ist, 0 wenn gleich -1 wenn kleiner
        }
        if (l instanceof NoVData || r instanceof NoVData){
            return false;
        }

        throw new RuntimeException(l.toString() + "Boolean kann nicht mit greaterthan verglichen werden"+r.toString());
    }
    private boolean equal (VData l, VData r) {


        if (l instanceof NumberValue && r instanceof NumberValue) {
            return (((NumberValue) l).compareTo((NumberValue) r)==0);
        }

        if (l instanceof StringValue && r instanceof StringValue) {
            return (((StringValue) l).compareTo((StringValue) r)==0);
        }
        if (l instanceof  BooleanValue && r instanceof  BooleanValue) {
            return ((BooleanValue) l).getValue().equals(((BooleanValue) r).getValue());
        }

        if (l instanceof NumberValue && r instanceof StringValue) {
            return l.toString().compareTo(((StringValue) r).getValue())==0;
        }
        if (l instanceof StringValue && r instanceof NumberValue) {
            return((((StringValue) l).getValue()).compareTo(r.toString())==0);
        }
        if (l instanceof NoVData || r instanceof NoVData){
            return false;
        }
        throw new RuntimeException(l.toString() + "Fehler beim Equals vergleich"+r.toString());
    }

    public String[] getLabel() {
        return null;
    }

    public String getDirection() {
        return null;
    }

    public LinkedList<Filter> getAttribute(Vertex v) {
        try {
            if (operator == Operator.EQ) {
                LinkedList<Filter> result = new LinkedList<Filter>();
                if (left instanceof InputDataVData && ((InputDataVData) left).getMode() == InputDataVData.Mode.Name && right instanceof BaseAtomValue) {
                    Filter f =  new Filter(((InputDataVData) left).getFilterKey(),((BaseAtomValue) right).getValue());
                    result.add(f);
                    return result;
                }
                else if (right instanceof InputDataVData && ((InputDataVData) right).getMode() == InputDataVData.Mode.Name && left instanceof BaseAtomValue) {
                    Filter f =  new Filter(((InputDataVData) right).getFilterKey(),((BaseAtomValue) left).getValue());
                    result.add(f);
                    return result;
                } else {
                 //   System.out.println(left.getClass()+", "+right.getClass());
                    if (v!=null) {
                        VertexToCheck vtc = new VertexToCheck(null, null);
                        vtc.setRootvertex(v);


                        VData leftClass = null;
                        VData rightClass=null;
                        try {leftClass = left.evaluate(vtc);}catch(Exception e){}
                        try {rightClass= right.evaluate(vtc);} catch (Exception e){}
                        if (leftClass==null&&rightClass instanceof CollectionVData&&left instanceof  InputDataVData){
                            HashSet<StringValue> list = new HashSet<StringValue>();//Um Duplikate zu löschen
                         //   System.out.println(((CollectionVData) rightClass).size()+"rightclassize");
                            for (int i=0;i<((CollectionVData) rightClass).size();i++){
                                if (((CollectionVData) rightClass).get(i) instanceof StringValue) {

                                    list.add((StringValue)((CollectionVData) rightClass).get(i));
                                }
                            //    System.out.println(((CollectionVData) rightClass).get(i).hashCode());
                            }
                            LinkedList<Object> list2 = new LinkedList<Object>();
                            Iterator<StringValue> it =list.iterator();

                            while (it.hasNext()){
                            //    Object a = it.next();
                        //        System.out.println(a);
                               list2.add(it.next());
                            }
                            Filter f = new Filter( list2, ((InputDataVData) left).getFilterKey());
                            result.add(f);
                            return result;
                        }
                        else if (leftClass==null&&rightClass instanceof NoVData&&left instanceof  InputDataVData){
                            //Der Fall, dass die Liste leer war
                            Filter f = new Filter(true);
                            result.add(f);
                            return result;
                        }
                   //     System.err.println(rightClass.getClass());
                        return null;
                        //System.out.println(((InputDataVData) left).getFilterKey()+"Filterkey");
                        //System.out.println(((CollectionVData)right.evaluate(vtc)).size()+"Listsize");
                    }
                    return null;
                }
            } else {

                return null;
            }
        }catch(RuntimeException e){
            e.printStackTrace();
            return null;
        }
    }


  public enum Operator {
            LT, LTEQ, GT, GTEQ, EQ, NEQ
    }
}
