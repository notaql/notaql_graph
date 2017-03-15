package NotaQLGraph.Parser;

import NotaQLGraph.Evaluation.Transformation;
import NotaQLGraph.model.predicate.PathExistencePredicate;
import NotaQLGraph.model.vdata.*;
import notaql.parser.antlr.NotaGraphBaseVisitor;
import notaql.parser.antlr.NotaGraphParser;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * Created by yannick on 25.11.15.
 */


public class VDataVisitorPredicate extends NotaGraphBaseVisitor<VData> {
    private Transformation transformation;
    public VDataVisitorPredicate(Transformation trans){
        this.transformation=trans;
    }


    @Override
    public VData visitInputDataVDataP(@NotNull NotaGraphParser.InputDataVDataPContext ctx) {
        return visit(ctx.inputDataPredicate());
    }

    @Override
    public VData visitInputDataVDataPEdgeDestination(@NotNull NotaGraphParser.InputDataVDataPEdgeDestinationContext ctx) {
        return new InputDataVData(visit(ctx.inputDataPredicate()), InputDataVData.Mode.EDGECreatorOWNNode);
    }


    @Override
    public VData visitNameIDP (@NotNull NotaGraphParser.NameIDPContext ctx){
     //   System.out.println("nameidp wird aufgerufen");
        return new InputDataVData(ctx.getText().toString(),InputDataVData.Mode.Name);
    }
    @Override
    public VData visitNodeIDIDP (@NotNull NotaGraphParser.NodeIDIDPContext ctx){
        return new InputDataVData(ctx.getText().toString(),InputDataVData.Mode.NodeID);
    }

    @Override
    public VData visitEdgeIDP (@NotNull NotaGraphParser.EdgeIDPContext ctx){
        VData edgeVdata = null;
        edgeVdata = transformation.getvDataVisitor().visit(ctx.edgevData());
        return new AggregatedEdgeVData(edgeVdata, AggregatedEdgeVData.Operation.NONE);
    }

    @Override
    public VData visitAggregatedEdgeIDP (@NotNull NotaGraphParser.AggregatedEdgeIDPContext ctx){
       // return new PathExistencePredicate(transformation.getvDataVisitor().visit(ctx.edgevData()), PathExistencePredicate.Source.Edge);
        VData edgeVdata = null;
        edgeVdata = transformation.getvDataVisitor().visit(ctx.edgevData());

        switch (ctx.function.getType()) {
            case NotaGraphParser.SUME:
                return new AggregatedEdgeVData(edgeVdata, AggregatedEdgeVData.Operation.SUM);
            case NotaGraphParser.AVGE:
                return new AggregatedEdgeVData(edgeVdata, AggregatedEdgeVData.Operation.AVG);
            case NotaGraphParser.COUNTE:
                return new AggregatedEdgeVData(edgeVdata, AggregatedEdgeVData.Operation.COUNT);
            case NotaGraphParser.MINE:
                return new AggregatedEdgeVData(edgeVdata, AggregatedEdgeVData.Operation.MIN);
            case NotaGraphParser.MAXE:
                return new AggregatedEdgeVData(edgeVdata, AggregatedEdgeVData.Operation.MAX);
        }
        throw new RuntimeException("Unknown arithmetic operator: " + NotaGraphParser.tokenNames[ctx.function.getType()]);
    }

    @Override
    public VData visitAtomVData(@NotNull NotaGraphParser.AtomVDataContext ctx) {
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
        //System.out.println("Numbervalue");
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
      //  System.out.println("TRUE");
        return new BooleanValue(true);
    }

    @Override
    public VData visitBracedVDataP(@NotNull NotaGraphParser.BracedVDataPContext ctx) {
        return visit(ctx.vDataPredicate());
    }

    @Override
    public VData visitOperationVDataP(@NotNull NotaGraphParser.OperationVDataPContext ctx) {
        final VData left = visit(ctx.vDataPredicate(0));
        final VData right = visit(ctx.vDataPredicate(1));

        switch (ctx.op.getType()) {
            case NotaGraphParser.MULT:
                return new ArithmeticVData(left, right, ArithmeticVData.Operation.MULTIPLY);
            case NotaGraphParser.DIV:
                return new ArithmeticVData(left, right, ArithmeticVData.Operation.DIVIDE);
        }

        throw new RuntimeException("Unknown arithmetic operator: " + NotaGraphParser.tokenNames[ctx.op.getType()]);
    }

    @Override
    public VData visitOperation2VDataP(@NotNull NotaGraphParser.Operation2VDataPContext ctx) {
        final VData left = visit(ctx.vDataPredicate(0));
        final VData right = visit(ctx.vDataPredicate(1));

        switch (ctx.op.getType()) {

            case NotaGraphParser.PLUS:
                return new ArithmeticVData(left, right, ArithmeticVData.Operation.ADD);
            case NotaGraphParser.MINUS:
                return new ArithmeticVData(left, right, ArithmeticVData.Operation.SUBTRACT);
        }

        throw new RuntimeException("Unknown arithmetic operator: " + NotaGraphParser.tokenNames[ctx.op.getType()]);
    }

}
