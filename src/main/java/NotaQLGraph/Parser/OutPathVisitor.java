package NotaQLGraph.Parser;

import NotaQLGraph.model.outpath.Name;
import NotaQLGraph.model.outpath.NodeID;
import NotaQLGraph.model.outpath.Outpath;
import notaql.parser.antlr.NotaGraphBaseVisitor;
import notaql.parser.antlr.NotaGraphParser;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * Created by yannick on 02.12.15.
 * Wird verwendet, um bei den Attributespezifikationen den linken Teil zu analysieren (wenn sich sich dabei nicht um eine Dollarspezifikation handelt)
 */
public class OutPathVisitor extends NotaGraphBaseVisitor<Outpath> {
    @Override public Outpath visitNodeIdOutPath(@NotNull NotaGraphParser.NodeIdOutPathContext ctx) {
        return new NodeID(ctx.NODEID().getText());

    }

    @Override public Outpath visitNameOutPath(@NotNull NotaGraphParser.NameOutPathContext ctx) {
        return new Name(ctx.Name().getText());

    }

    @Override public Outpath visitNodeSpecification(@NotNull NotaGraphParser.NodeSpecificationContext ctx) {
        return visitChildren(ctx); }

}
