package NotaQLGraph.Parser;

/**
 * Created by yannick on 07.01.16.
 */

import NotaQLGraph.Evaluation.Transformation;
import NotaQLGraph.model.vdata.*;
import notaql.parser.antlr.NotaGraphBaseVisitor;
import notaql.parser.antlr.NotaGraphParser;
import org.antlr.v4.runtime.misc.NotNull;

public class VDataVisitorPredicateEdge extends NotaGraphBaseVisitor<VData> {
    private Transformation transformation;
    public VDataVisitorPredicateEdge(Transformation trans){
        this.transformation=trans;
    }

    @Override
    public VData visitInputDataVDataEP(@NotNull NotaGraphParser.InputDataVDataEPContext ctx) {
        return visit(ctx.edgeInputData());
    }

    @Override
    public VData visitEidName (@NotNull NotaGraphParser.EidNameContext ctx){
        return new InputDataVDataEdge(ctx.getText().toString(),InputDataVDataEdge.Mode.Name);
    }
    @Override
    public VData visitEidEdgeID (@NotNull NotaGraphParser.EidEdgeIDContext ctx){
        return new InputDataVDataEdge(ctx.getText().toString(),InputDataVDataEdge.Mode.EdgeID);
    }
    @Override
    public VData visitEidLabel (@NotNull NotaGraphParser.EidLabelContext ctx){
        return new InputDataVDataEdge(ctx.getText().toString(),InputDataVDataEdge.Mode.Label);
    }

    @Override
    public VData visitAtomVDataEP(@NotNull NotaGraphParser.AtomVDataEPContext ctx) {
        return visit(ctx.atom());
    }

    @Override
    public NumberValue visitNumberAtom(@NotNull NotaGraphParser.NumberAtomContext ctx) {
        String string;
        if(ctx.Float() != null) {
            string = ctx.Float().getText();
        } else {
            string = ctx.Int().getText();
        }

        if(ctx.Float() != null) {
            return new NumberValue(Double.parseDouble(string));
        }
        return new NumberValue(Integer.parseInt(string));
    }

    @Override
    public StringValue visitStringAtom(@NotNull NotaGraphParser.StringAtomContext ctx){
        String string = ctx.String().getText().replace("\\'", "'"); //von thomas
        string = string.substring(1, string.length() - 1);
        return new StringValue(string);

    }
    @Override
    public BooleanValue visitFalseAtom(@NotNull NotaGraphParser.FalseAtomContext ctx) {
        return new BooleanValue(false);
    }

    @Override
    public BooleanValue visitTrueAtom(@NotNull NotaGraphParser.TrueAtomContext ctx) {
        return new BooleanValue(true);
    }

    @Override
    public VData visitBracedVDataEP(@NotNull NotaGraphParser.BracedVDataEPContext ctx) {
        return visit(ctx.vDataPredicate());
    }

    @Override
    public VData visitOperationVDataEP(@NotNull NotaGraphParser.OperationVDataEPContext ctx) {
        final VData left = visit(ctx.edgevDataPredicate(0));
        final VData right = visit(ctx.edgevDataPredicate(1));

        switch (ctx.op.getType()) {
            case NotaGraphParser.MULT:
                return new ArithmeticVData(left, right, ArithmeticVData.Operation.MULTIPLY);
            case NotaGraphParser.DIV:
                return new ArithmeticVData(left, right, ArithmeticVData.Operation.DIVIDE);
        }
        throw new RuntimeException("Unknown arithmetic operator: " + NotaGraphParser.tokenNames[ctx.op.getType()]);
    }

    @Override
    public VData visitOperation2VDataEP(@NotNull NotaGraphParser.Operation2VDataEPContext ctx) {
        final VData left = visit(ctx.edgevDataPredicate(0));
        final VData right = visit(ctx.edgevDataPredicate(1));
        switch (ctx.op.getType()) {
            case NotaGraphParser.PLUS:
                return new ArithmeticVData(left, right, ArithmeticVData.Operation.ADD);
            case NotaGraphParser.MINUS:
                return new ArithmeticVData(left, right, ArithmeticVData.Operation.SUBTRACT);
        }
        throw new RuntimeException("Unknown arithmetic operator: " + NotaGraphParser.tokenNames[ctx.op.getType()]);
    }
}