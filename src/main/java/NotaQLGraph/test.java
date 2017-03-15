package NotaQLGraph;

import NotaQLGraph.Parser.ImportVisitor;
import NotaQLGraph.model.importModel.Import;
import notaql.parser.antlr.ImportLexer;
import notaql.parser.antlr.ImportParser;
import notaql.parser.antlr.NotaLexer;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

/**
 * Created by yannick on 13.02.16.
 */
public class test {
    public static void main(String args[]) {


        ANTLRInputStream importinput = new ANTLRInputStream(
                "IN-ENGINE: json(path<-'/home/yannick/graph/following.json'),"+
                " OUT-ENGINE: tinkergraph(path <- '/tmp/output2.db'),"+
                " OUT.$(IN.?(@.name()!='following'&& @.name()!='followers').name()) <- IN.@,"+
                " OUT._e_?(username='user_2') <- EDGE(_outgoing, _l <- 'kennt')"
        );
        ImportLexer importlexer = new ImportLexer(importinput);
        CommonTokenStream importtokens = new CommonTokenStream(importlexer); //Gramatik in Tokens zerlegen

        ImportParser importparser = new ImportParser(importtokens);
        ImportParser.TransformationContext importcontext = importparser.transformation();
        ImportVisitor importVisitor = new ImportVisitor();
        Import a = importVisitor.visit(importcontext);
    }
}
