package NotaQLGraph;

import NotaQLGraph.Database.Database;
import NotaQLGraph.Database.Neo4JDatabase;
import NotaQLGraph.Database.OpenConnections;
import NotaQLGraph.Database.TinkerGraphDatabase;
import NotaQLGraph.Evaluation.Transformation;
import NotaQLGraph.Parser.*;
import NotaQLGraph.Parser.ImportVisitor;
import NotaQLGraph.model.importModel.Import;
import NotaQLGraph.notaQLCommunication.JSON;
import notaql.NotaQL;

import notaql.parser.antlr.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.File;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by yannick on 20.11.15.
 */
public class NotaQLGraph {


    public static int maxEdgeDepth = 10;
    public static int maxNodesCommit = 20000;
    public static int maxEdgesCommit = 1000;
    public static boolean printausgaben = true;//DEBUG infos
    static String notaqlgraphskript =
   /*         "IN-ENGINE: neo4j(path <- '/home/yannick/Downloads/neo4j-enterprise-2.3.0-M03/data'),\n" +
             "OUT-ENGINE: tinkergraph(path <- '/tmp/alter'),\n" +
                    "OUT._id <- IN.age;"+
                    //"OUT.anzahl <- 5;"+

*/
 //           "IN-ENGINE: json(path<-'/home/yannick/graph/following.json'),"+
   //                 " OUT-ENGINE: tinkergraph(path <- '/tmp/output2.db'),"+
     //               "OUT._id <- IN.username,"+
       //             " OUT.$(IN.?(@.name()!='following'&& @.name()!='followers').name()) <- IN.@,"+
         //           "OUT._e_?(username=IN.following) <- EDGE (_outgoing, _l <- 'kennt');"+







            //      path<-'/tmp/2.json/part-00000'
    //csv_path<-'/tmp/5.csv'
  //  "IN-ENGINE:csv(csv_path<-'/tmp/1.csv')," +
    //        "OUT-ENGINE:json(path<-'/tmp/23.json'),"+
      //      "OUT.$(IN._c) <- IN._v;"+
/*
            "IN-ENGINE:json(path<-'/home/yannick/graph/input.json'),"+
            "OUT-ENGINE:csv(csv_path<-'/tmp/23-"+Math.round(Math.random()*10000)+".csv')," +
                    //"OUT.$(IN._c) <- IN._v;"+
                        "OUT.$(IN.*.name()) <- IN.@;"+
      //      "OUT.alter <- IN.alter;"+

            "IN-ENGINE:json(path<-'/home/yannick/graph/input.json'),"+
            "OUT-ENGINE:json(path<-'/tmp/23"+Math.round(Math.random()*10000)+".json'),"+
            "OUT.$(IN.*.name()) <- IN.@;"+
    //        "OUT.alter <- IN.alter;"+





                    "IN-ENGINE: neo4j(path <- '/home/yannick/graph/input.db'),\n" +
                    "OUT-ENGINE: csv(csv_path<-'/tmp/"+Math.round(Math.random()*10000)+".csv'),\n" +
                    "IN-FILTER:  (false = false ) && !(true = false) && geschlecht && alter < (1+9)*2 , \n" +
                    "OUT._id <- IN._id, \n" +
                    "OUT.name <- IN.name," +
                    "OUT.alter <- IN.alter;"+



            "IN-ENGINE:csv(csv_path<-'/home/yannick/graph/input.csv')," +
                    "OUT-ENGINE:csv(csv_path<-'/tmp/"+Math.round(Math.random()*10000)+".csv')," +
                    "OUT.$(IN._c) <- IN._v;"+


                    "IN-ENGINE: csv(csv_path<-'/home/yannick/graph/input.csv'),\n" +
                            "OUT-ENGINE: neo4j(path <- '/home/yannick/graph/output_import.db'),\n" +

                            "OUT._id <- IN._id, \n" +
                            "OUT.name <- IN.name," +
                            "OUT.alter <- IN.alter;"+

                    "IN-ENGINE: csv(csv_path<-'/home/yannick/graph/input.csv'),\n" +
                    "OUT-ENGINE: neo4j(path <- '/home/yannick/graph/output_import2.db'),\n" +
                    "OUT._id <- IN._id,"+
                            "OUT.$(IN._c) <- IN._v;"+

/*
         "IN-ENGINE: neo4j(path <- '/home/yannick/graph/input.db'),\n" +
                    "OUT-ENGINE: tinkergraph(path <- '/home/yannick/graph/output.db'),\n" +
                    "IN-FILTER:  (false = false ) && !(true = false) && geschlecht && alter < (1+9)*2, \n" +
                    "OUT._id <- IN._id, \n" +
                    "OUT.name <- IN.name," +
                    "OUT.number <- COUNT()," +
                    "OUT.summe <- SUM(1+4+5);" +

                    "IN-ENGINE: neo4j(path <- '/home/yannick/graph/input.db'),\n" +
                    "OUT-ENGINE: tinkergraph(path <- '/home/yannick/graph/output2.db'),;\n" +
                   "OUT.$(IN._k?(@=15)) <- IN._v, \n" +
                    "OUT.$(IN._k?(!(name||alter))) <- IN._v, \n" +
                 "OUT.name2 <- IN.name;" +


                    "IN-ENGINE: neo4j(path <- '/home/yannick/graph/input.db'),\n" +
                    "OUT-ENGINE: tinkergraph(path <- '/home/yannick/graph/output3.db'),\n" +
                    "IN-FILTER: (5+5*2 = 15) && (true != false), \n" +   //IN-FILTER: name='chantal' && aalter=16 &&hund=true,
                    "OUT._id <- IN.firma, \n" +
                    "OUT.firma <- IN.firma, \n" +
                    "OUT.gehaltAllerMitarbeiter <- SUM(IN.gehalt)," +
                    "OUT.durchschnittsgehalt <- AVG(IN.gehalt)," +
                    "OUT.anzahl_Mitarbeiter_FIrma <- COUNT()," +
                    "OUT.anzahl_Mitarbeiter_ALTER <- COUNT(IN.alter)," +
                    "OUT.juengster_mitarbeiter <- MIN(IN.alter);" +


                    "IN-ENGINE: neo4j(path <- '/home/yannick/graph/input.db'),\n" +
                    "OUT-ENGINE: neo4j(path <- '/home/yannick/graph/output4.db'),\n" +
                    //"IN-FILTER: name && _e?('kennt')_.gehalt>gehalt,"+
     //               "IN-FILTER: !(label='Seite')&& name &&min( _e_.gehalt)<gehalt," +
                    "OUT._id <- IN._id," +
                    "OUT.name <- IN.name," +
                            "OUT.juengste_Freundschaft <- max(IN._e?('kennd').seit),"+
                             "OUT.anzahlFreundeDerFreunde <- count (IN._e_._e_._id),"+
                            "OUT.anzahlFreunde2015 <- count(IN._e?(seit=2015||'istChefvon')_._id)," +Graphtransformationen
                      "OUT.anzahlFreunde_outgoing <- count(IN._e?(_outgoing)_._id)," +
                       "OUT.anzahlMaennlicherFreunde <- count (IN._e_?(geschlecht='männlich')._id),"+
                        "OUT.AnzahlFreundJuwiFreundesFreundUNI <- count (IN._e_?(firma='juwi')._e_?(firma='uni kl')._id),"+
                         "OUT.durchschnittsalterFreunde <- avg(IN._e?(_outgoing&&'kennt')_.alter)," +
                          "OUT.aeltesteFreundschaft <- min(IN._e.seit),"+
                             "OUT.Chef <- IN._e?('istChefvon'&&_incoming)_.name,"+
                                   "OUT.MaxName <- max(IN._e_.name),"+
                             "OUT.gehaltFreunde <- sum(IN._e?(_incoming)_.gehalt),"+
                               "OUT.AnzahlAllerPersonen <- count(IN._e_[0, *]._id),"+
                    "OUT._e_?(name='julia' && !(url)) <- EDGE(_outgoing, _l <- 'kehrt', seit <- 2015)," +
                    "OUT._e_?(name='julia' && !(url)) <- EDGE(_incoming, _l<-'kehrt', seit <- 2015, bis <- '11833')," +
                    "OUT.BIP <- sum(IN._e?('kennt'&&_outgoing)_[0,2].gehalt);" +

                    "IN-ENGINE: neo4j(path <- '/home/yannick/graph/input.db'),\n" +
                    "OUT-ENGINE: neo4j(path <- '/home/yannick/graph/output5.db'),\n" +
                    "OUT._id <- IN._id," +
                    "OUT.$(IN._k?(true=true)) <- IN._v," +
                    "OUT._e_?(name && _id=IN._e?(_incoming)_._id) <- EDGE(_outgoing, _l <- IN._e[@]._l, seit <- IN._e[@].seit);"+

                  "IN-ENGINE: neo4j(path <- '/home/yannick/graph/input.db'),\n" +
                  "OUT-ENGINE: neo4j(path <- '/home/yannick/graph/output5b.db'),\n" +
                  "OUT._id <- IN._id," +
                  "OUT.$(IN._k?(true=true)) <- IN._v," +
                  "OUT._e_?(Label='Seite' && _id=IN._e?(_incoming)_._id) <- EDGE(_outgoing, _l <- IN._e[@]._l, seit <- IN._e[@].seit);"+


                  //Pagerank
                    "IN-ENGINE: neo4j(path <- '/home/yannick/graph/input.db'),\n" +
                    "OUT-ENGINE: neo4j(path <- '/home/yannick/graph/input.db'),\n" +
                    "IN-FILTER: Label='Seite'," +
                    "OUT._id <- IN._e?(_outgoing)_._id," +
                    "OUT.anzahl <- count(IN._e?(_outgoing)._id)," +
                    "OUT.Pagerank <- SUM(IN.Pagerank/count(IN._e?(_outgoing)._id));" +

                    "IN-ENGINE: neo4j(path <- '/home/yannick/graph/input.db'),\n" +
                    "OUT-ENGINE: neo4j(path <- '/home/yannick/graph/input.db'),\n" +
                    "IN-FILTER: Label='Seite'," +
                    "OUT._id <- IN._e?(_outgoing)_._id," +
                    "OUT.anzahl <- count(IN._e?(_outgoing)._id)," +
                    "OUT.Pagerank <- SUM(IN.Pagerank/count(IN._e?(_outgoing)._id));" +


                    "IN-ENGINE: neo4j(path <- '/home/yannick/graph/input.db'),\n" +
                    "OUT-ENGINE: neo4j(path <- '/home/yannick/graph/output6.db'),\n" +
                    "OUT._id <- IN._e_[0,2]._id,"+
                    "OUT.summ <- COUNT(); "+
*/
                  "IN-ENGINE: neo4j(path <- '/home/yannick/graph/input.db'),\n" +
                  "OUT-ENGINE: neo4j(path <- '/home/yannick/graph/output7.db'),\n" +
                  "IN-FILTER: Label='Seite'," +
                  "OUT._id <- IN._id,"+
                  "OUT.$(IN._k) <- IN._v" +
/*
            "IN-ENGINE: neo4j(path <- '/home/yannick/graph/input.db'),\n" +
                    "OUT-ENGINE: json(path <- '/tmp/test.json'),\n" +Graphtransformationen
                    "OUT._id <- IN._id,"+
                    "OUT.freunde <- list(IN._e_.name)"+


            "IN-ENGINE: neo4j(path <- '/home/yannick/Downloads/neo4j-community-2.0.4/data/graph.db'),\n" +
                    "OUT-ENGINE: json(path <- '/tmp/test.json'),\n" +

                    "OUT.name <- IN.name"+

*/

                    "";

    public static void main (String[] args){
        notaqlGraph(notaqlgraphskript);
    }

    public static void notaqlGraph(String skript) {
        ANTLRInputStream input = new ANTLRInputStream(skript);
        NotaLexer lexer = new NotaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer); //Gramatik in Tokens zerlegen
        NotaParser parser = new NotaParser(tokens);
        NotaParser.NotaqlContext context = parser.notaql(); //NotaQLGraph.Parser mit Notaql starten (Wurzel in der Gramatik)

        //Datenquellen anlegen
     /*   Database sourcebase = new Neo4JDatabase("/home/yannick/graph/input.db");
        sourcebase.initialisieren();
        sourcebase.printGraph();
        sourcebase.getDatabase().shutdown();
*/
        OpenConnections open = new OpenConnections();
        //Notaql in einzelne Teilbäume aufteilen, die jeweils eine Transformation enthalten
        List<NotaParser.TransformationContext> transformationcontext = context.transformation();
        for (NotaParser.TransformationContext t : transformationcontext) {
            HashMap<String, String> inputParameter = new HashMap<String, String>();
            HashMap<String, String> outputParameter = new HashMap<String, String>();
            for (int i = 1; i < t.inEngine().engine().Name().size(); i++) {
                inputParameter.put(t.inEngine().engine().Name(i).getText(), t.inEngine().engine().atom(i - 1).getText().substring(1, t.inEngine().engine().atom(i - 1).getText().length() - 1));//substring, damit '' wegkommt, atom-1 weil ich in gramatik ein atom element weniger habe
            }
            for (int i = 1; i < t.outEngine().engine().Name().size(); i++) {
                outputParameter.put(t.outEngine().engine().Name(i).getText(), t.outEngine().engine().atom(i - 1).getText().substring(1, t.outEngine().engine().atom(i - 1).getText().length() - 1));
            }
            String destination= null; String source= null;
            if(inputParameter.containsKey("path")) {source = inputParameter.get("path");}
            if(outputParameter.containsKey("path")) {destination= outputParameter.get("path");}


            open.refresh();
            //Wenn Ziel und Quelldatenbank kein Graph ist
            if (!(Database.supportDatabase(t.inEngine().engine().engineName.getText()))&&!Database.supportDatabase(t.outEngine().engine().engineName.getText())){
                System.out.println("tes1");
                try {
                    NotaQL.evaluate(t.getText());
                } catch (ConnectException e) {
                    e.printStackTrace();
                }
            //wenn Quelldatenbank kein Graph ist
            }else if (!(Database.supportDatabase(t.inEngine().engine().engineName.getText()))) {
                System.out.println("."+t.getText()+".");
                ANTLRInputStream importinput = new ANTLRInputStream(t.getText());
                ImportLexer importlexer = new ImportLexer(importinput);
                CommonTokenStream importtokens = new CommonTokenStream(importlexer); //Gramatik in Tokens zerlegen

                ImportParser importparser = new ImportParser(importtokens);
                ImportParser.TransformationContext importcontext = importparser.transformation();
                ImportVisitor importVisitor = new ImportVisitor();
                Import a = importVisitor.visit(importcontext);

            //    System.out.println("meine seite: " +a.importGraphSide());
             //   System.out.println("thomas seite: "+ a.importOtherSide());

                String jsonpath= "/tmp/"+Math.round(Math.random()*10000);
                String transformationSkriptThomas =t.inEngine().getText()+", OUT-ENGINE: json(path<-'"+jsonpath+"'),";
                transformationSkriptThomas=transformationSkriptThomas+a.importOtherSide();

                try {
                    System.out.println(transformationSkriptThomas);
                    NotaQL.evaluate(transformationSkriptThomas);
                } catch (ConnectException e) {
                    e.printStackTrace();
                }


                open.setIn(new TinkerGraphDatabase(),false); //In Memory datenbank bei in bekommen => import export db soll weggeworfen werden

                LinkedList<String> paths = generateJsonPaths(jsonpath);
                for (String pathss:paths){

                    JSON.readJson(pathss,open.getIn().getDatabase());
                }


              //  open.printIn();
                String transforrmationSkript ="IN-ENGINE: tinkergraph(),"+t.outEngine().getText()+",";
                if (t.getText().contains("_id")){
                    transforrmationSkript=transforrmationSkript+  "OUT._id <- IN._id,";
                }
                transforrmationSkript=transforrmationSkript+"OUT.$(IN._k?(true=true)) <- IN._v," ;
                transforrmationSkript=transforrmationSkript+a.importGraphSide();
                System.out.println(transforrmationSkript);
                transformation(transforrmationSkript,open);
                open.shutdown();

            //Wenn Zieldatenbank kein Graph ist
            }else if (!Database.supportDatabase(t.outEngine().engine().engineName.getText())){
                String transformationSkript=t.inEngine().getText()+",OUT-ENGINE: tinkergraph2(),";

                for (int i=0;i<t.any().size();i++){
                    transformationSkript=transformationSkript+t.any(i).getText();
                }

                System.out.println(transformationSkript);
                transformation(transformationSkript, open);
                String jsonpath= JSON.writeJson(open.getOut().getDatabase());
          //      open.getOut().printGraph();
                open.shutdown();
                System.out.println(jsonpath);
                String transformationSkriptThomas= "IN-ENGINE:json(path<-'"+jsonpath+"')," + t.outEngine().getText()+",";
                if (t.outEngine().engine().engineName.getText().equalsIgnoreCase("csv")) {
                    transformationSkriptThomas = transformationSkriptThomas + "OUT._id<-IN._id, OUT.$(IN.*.name()) <- IN.@;";
                }
                else if (t.outEngine().engine().engineName.getText().equalsIgnoreCase("hbase")) {
                    transformationSkriptThomas = transformationSkriptThomas + "OUT._r<-IN._r, OUT.$(IN.*.name()) <- IN.@;";
                }
                else if (t.outEngine().engine().engineName.getText().equalsIgnoreCase("json")) {
                    transformationSkriptThomas = transformationSkriptThomas + "OUT._id<-IN._id, OUT.$(IN.*.name()) <- IN.@;";
                }
                else if (t.outEngine().engine().engineName.getText().equalsIgnoreCase("mongodb")) {
                    transformationSkriptThomas = transformationSkriptThomas + "OUT._id<-IN._id, OUT.$(IN.*.name()) <- IN.@;";
                }
                else if (t.outEngine().engine().engineName.getText().equalsIgnoreCase("redis")) {
                    transformationSkriptThomas = transformationSkriptThomas + "OUT._k<-IN._k, OUT._v <- IN._v;";
                }

                try {
                    System.out.println(transformationSkriptThomas);
                    NotaQL.evaluate(transformationSkriptThomas);
                } catch (ConnectException e) {
                    e.printStackTrace();
                }
                //TODO Testen
            }
            //Ziel und Quelldatenbank sind GraphDB
            else {
                transformation(t.getText(),open);
                open.shutdown();
            }



        }
        open.refresh();
    }

    private static LinkedList<String> generateJsonPaths(String jsonpath) {
        LinkedList<String> result = new LinkedList<String>();
        int i=0;
        while (true) {

            String formatted = String.format("%05d", i);
            String s = jsonpath + "/part-"+formatted;
            File file = new File(s);
            if (file.exists()) {
                result.add(s);
                i++;
            } else {
                break;

            }
        }
        System.out.println(result);
        return result;
    }

    private static String transformation(String transformation,OpenConnections open){


        ANTLRInputStream input = new ANTLRInputStream(transformation);
        NotaGraphLexer lexer = new NotaGraphLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer); //Gramatik in Tokens zerlegen
        NotaGraphParser parser = new NotaGraphParser(tokens);
        NotaGraphParser.TransformationContext t = parser.transformation();

        System.out.println("Neue Transformation:");

        //speichere alle in/outParameter in einer eigenen Hashmap ab
        HashMap<String, String> inputParameter = new HashMap<String, String>();
        HashMap<String, String> outputParameter = new HashMap<String, String>();
        for (int i = 1; i < t.inEngine().engine().Name().size(); i++) {
            inputParameter.put(t.inEngine().engine().Name(i).getText(), t.inEngine().engine().atom(i - 1).getText().substring(1, t.inEngine().engine().atom(i - 1).getText().length() - 1));//substring, damit '' wegkommt, atom-1 weil ich in gramatik ein atom element weniger habe
        }
        for (int i = 1; i < t.outEngine().engine().Name().size(); i++) {
            outputParameter.put(t.outEngine().engine().Name(i).getText(), t.outEngine().engine().atom(i - 1).getText().substring(1, t.outEngine().engine().atom(i - 1).getText().length() - 1));
        }

        String destination= null; String source= null;
        if(inputParameter.containsKey("path")) {source = inputParameter.get("path");}
        if(outputParameter.containsKey("path")) {destination= outputParameter.get("path");}
        if (open.getIn()==null) {//der Fall das ich bereits eine Datenbank übergebe(import)
            if(t.inEngine().engine().engineName.getText().equals("tinkergraph")&&source ==null){
                System.out.println("der Fall");
                open.setIn(null,true); //inmemory nutzen
            }
            else {
                open.setIn(Database.createDatabase(t.inEngine().engine().engineName.getText(), source),false); //fall das kein inmemory
            }
        }

        if (source!=null&&source.equals(destination)) {
            if (t.inEngine().engine().engineName.getText().equals("tinkergraph")){
                open.setOut(open.getIn(),true);
            }else {
                open.setOut(open.getIn(),false);
            }

        } else {
            if (t.outEngine().engine().engineName.getText().equals("tinkergraph")&&destination == null){
                open.setOut(null,true); //inmemory nutzen
            }
            else {
                open.setOut(Database.createDatabase(t.outEngine().engine().engineName.getText(), destination), false);
            }

        }


        String name=null;
        Double percentage = null;
        int wiederholungen = 1;
        if (t.repeat()!=null){
            if (t.repeat().Name()!=null) {
                name = t.repeat().Name().getText();
            }
            if (t.repeat().wert!=null){
                percentage= Double.parseDouble(t.repeat().wert.getText());
                wiederholungen=-1; //wenn ich nach prozent gucke, muss ich das ganze natürlich unendlich oft machen
            }
            if (t.repeat().wiederholung!=null){
                wiederholungen = Integer.parseInt(t.repeat().wiederholung.getText());
            }
            if (t.repeat().unendlich!=null){
                wiederholungen=-1;
            }

        }
        int counter = 0;

        while (counter<wiederholungen||wiederholungen==-1) {//transformation solange wiederholen, bis Abbruchbedingung erfüllt oder x mal gemacht

            Transformation trans = new Transformation();
         //   System.out.println("perecnetageok:"+trans.percentageOK);
            trans.evaluate(t, open.getIn(), open.getOut(), name, percentage);
        //    System.out.println("perecnetageok:"+trans.percentageOK);
            if (wiederholungen!=1)System.out.println("Wiederholung:" +counter);
            trans.printStatistic();
            counter++;

            if((trans.outputCounter+trans.newEdgesCounter+trans.outputAggregateCounter)==0||(trans.percentageOK&&percentage!=null)){
                break;
            }
        }
        open.printOut();


        return destination;
    }


}