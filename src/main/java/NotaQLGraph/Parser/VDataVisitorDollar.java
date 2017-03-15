package NotaQLGraph.Parser;

import NotaQLGraph.Evaluation.Transformation;
import NotaQLGraph.model.vdata.*;
import notaql.parser.antlr.NotaGraphBaseVisitor;
import notaql.parser.antlr.NotaGraphParser;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * Created by yannick on 16.12.15.
 */
public class VDataVisitorDollar extends NotaGraphBaseVisitor<VData> {
    private Transformation transformation;
    public VDataVisitorDollar(Transformation trans){
        this.transformation=trans;
    }

    @Override
    public VData visitAtomVDataDP(@NotNull NotaGraphParser.AtomVDataDPContext ctx) {
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
    public VData visitBracedVDataDP(@NotNull NotaGraphParser.BracedVDataDPContext ctx) {
        return visit(ctx.vDataDollarPredicate());
    }

    @Override
    public VData visitOperationVDataDP(@NotNull NotaGraphParser.OperationVDataDPContext ctx) {
        final VData left = visit(ctx.vDataDollarPredicate(0));
        final VData right = visit(ctx.vDataDollarPredicate(1));

        switch (ctx.op.getType()) {
            case NotaGraphParser.MULT:
                return new ArithmeticVData(left, right, ArithmeticVData.Operation.MULTIPLY);
            case NotaGraphParser.DIV:
                return new ArithmeticVData(left, right, ArithmeticVData.Operation.DIVIDE);
        }

        throw new RuntimeException("Unknown arithmetic operator: " + NotaGraphParser.tokenNames[ctx.op.getType()]);
    }

    @Override
    public VData visitOperation2VDataDP(@NotNull NotaGraphParser.Operation2VDataDPContext ctx) {
        final VData left = visit(ctx.vDataDollarPredicate(0));
        final VData right = visit(ctx.vDataDollarPredicate(1));

        switch (ctx.op.getType()) {

            case NotaGraphParser.PLUS:
                return new ArithmeticVData(left, right, ArithmeticVData.Operation.ADD);
            case NotaGraphParser.MINUS:
                return new ArithmeticVData(left, right, ArithmeticVData.Operation.SUBTRACT);
        }

        throw new RuntimeException("Unknown arithmetic operator: " + NotaGraphParser.tokenNames[ctx.op.getType()]);
    }

    @Override
    public VData visitInputDataVDataDP(@NotNull NotaGraphParser.InputDataVDataDPContext ctx) {
        return visit(ctx.inputDataPredicate());
    }

    @Override
    public VData visitNameIDP (@NotNull NotaGraphParser.NameIDPContext ctx){
        return new InputDataVData(ctx.getText().toString(),InputDataVData.Mode.Name);
    }
    @Override
    public VData visitNodeIDIDP (@NotNull NotaGraphParser.NodeIDIDPContext ctx){
        return new InputDataVData(ctx.getText().toString(),InputDataVData.Mode.NodeID);
    }

    @Override
    public VData visitATVDataDP (@NotNull NotaGraphParser.ATVDataDPContext ctx){
        return new AtVData();
    }



}

