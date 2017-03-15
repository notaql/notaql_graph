package NotaQLGraph.Parser;

import NotaQLGraph.Evaluation.Transformation;
import NotaQLGraph.model.predicate.NoPredicate;
import NotaQLGraph.model.vdata.*;
import notaql.parser.antlr.NotaGraphBaseVisitor;
import notaql.parser.antlr.NotaGraphParser;
import NotaQLGraph.model.predicate.Predicate;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * Created by yannick on 02.12.15.
 * <p>
 * Der normale Visitor für VData Daten, unten erweitert für EDGE VData
 */
public class VDataVisitor extends NotaGraphBaseVisitor<VData> {
    Transformation trans;
    public VDataVisitor(Transformation transformation){
        this.trans = transformation;
    }

    @Override
    public VData visitAtomVData(@NotNull NotaGraphParser.AtomVDataContext ctx) {

        return visit(ctx.atom());
    }

    @Override
    public NumberValue visitNumberAtom(@NotNull NotaGraphParser.NumberAtomContext ctx) {
        String string;
        if (ctx.Float() != null) {
            string = ctx.Float().getText();
        } else {
            string = ctx.Int().getText();
        }

        if (ctx.Float() != null) {
            return new NumberValue(Double.parseDouble(string));
        }
        return new NumberValue(Integer.parseInt(string));
    }

    @Override
    public StringValue visitStringAtom(@NotNull NotaGraphParser.StringAtomContext ctx) {
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
    public VData visitBracedVData(@NotNull NotaGraphParser.BracedVDataContext ctx) {
        return visit(ctx.vData());
    }

    @Override
    public VData visitMultiplicatedVData(@NotNull NotaGraphParser.MultiplicatedVDataContext ctx) {
        final VData left = visit(ctx.vData(0));
        final VData right = visit(ctx.vData(1));

        switch (ctx.op.getType()) {
            case NotaGraphParser.MULT:
                return new ArithmeticVData(left, right, ArithmeticVData.Operation.MULTIPLY);
            case NotaGraphParser.DIV:
                return new ArithmeticVData(left, right, ArithmeticVData.Operation.DIVIDE);
        }

        throw new RuntimeException("Unknown arithmetic operator: " + NotaGraphParser.tokenNames[ctx.op.getType()]);
    }

    @Override
    public VData visitAddedVData(@NotNull NotaGraphParser.AddedVDataContext ctx) {
        final VData left = visit(ctx.vData(0));
        final VData right = visit(ctx.vData(1));

        switch (ctx.op.getType()) {

            case NotaGraphParser.PLUS:
                return new ArithmeticVData(left, right, ArithmeticVData.Operation.ADD);
            case NotaGraphParser.MINUS:
                return new ArithmeticVData(left, right, ArithmeticVData.Operation.SUBTRACT);
        }

        throw new RuntimeException("Unknown arithmetic operator: " + NotaGraphParser.tokenNames[ctx.op.getType()]);
    }


    @Override
    public VData visitAggregateVData(@NotNull NotaGraphParser.AggregateVDataContext ctx) {
        VData vdata = null;
        if (ctx.vData() != null) {
            vdata = visit(ctx.vData());
        } else {
            vdata = new EmptyVData(); //keine VDAta angegeben (bei count z.b)
        }

        switch (ctx.function.getType()) {
            case NotaGraphParser.SUM:
                return new AggregatedVData(vdata, AggregatedVData.Operation.SUM);
            case NotaGraphParser.AVG:
                return new AggregatedVData(vdata, AggregatedVData.Operation.AVG);
            case NotaGraphParser.COUNT://vdata sollte hier null sein
                return new AggregatedVData(vdata, AggregatedVData.Operation.COUNT);
            case NotaGraphParser.MIN:
                return new AggregatedVData(vdata, AggregatedVData.Operation.MIN);
            case NotaGraphParser.MAX:
                return new AggregatedVData(vdata, AggregatedVData.Operation.MAX);
        }

        throw new RuntimeException("Unknown arithmetic operator: " + NotaGraphParser.tokenNames[ctx.function.getType()]);
    }

    @Override
    public VData visitRelativeInputPathVData(@NotNull NotaGraphParser.RelativeInputPathVDataContext ctx) {

        return visit(ctx.getChild(1));
    }


    @Override
    public VData visitNameID(@NotNull NotaGraphParser.NameIDContext ctx) {
        //   System.out.println("nameidp wird aufgerufen");
        return new InputDataVData(ctx.getText().toString(), InputDataVData.Mode.Name);
    }

    @Override
    public VData visitNodeIDID(@NotNull NotaGraphParser.NodeIDIDContext ctx) {
        return new InputDataVData(ctx.getText().toString(), InputDataVData.Mode.NodeID);
    }

    @Override
    public VData visitValueID(@NotNull NotaGraphParser.ValueIDContext ctx) {
        return new AtVData();
    }



    //hier fängt dann die Erweiterung für die Edges an
    @Override
    public VData visitAggregateEdgeVData(@NotNull NotaGraphParser.AggregateEdgeVDataContext ctx) {
        VData edgeVdata = null;
           edgeVdata = visit(ctx.edgevData());

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
                case NotaGraphParser.LISTE:
                    return new AggregatedEdgeVData(edgeVdata, AggregatedEdgeVData.Operation.LISTE);
                case NotaGraphParser.PATHLENGTHE:
                    return new AggregatedEdgeVData(edgeVdata, AggregatedEdgeVData.Operation.PATHLENGTH);
            }

            throw new RuntimeException("Unknown arithmetic operator: " + NotaGraphParser.tokenNames[ctx.function.getType()]);

    }

    @Override
    public VData visitAggregateEdgeVDataNoFunction(@NotNull NotaGraphParser.AggregateEdgeVDataNoFunctionContext ctx){
        VData edgeVdata = null;
        edgeVdata = visit(ctx.edgevData());
        return new AggregatedEdgeVData(edgeVdata, AggregatedEdgeVData.Operation.NONE);
    }


    @Override
    public VData visitEdgeID(@NotNull NotaGraphParser.EdgeIDContext ctx){
        VData edgeinputData = visit (ctx.edgeInputData());
        return new MultiEdgeVData(new NoPredicate(), edgeinputData);
    }
    @Override
    public VData visitEdgeIDeP(@NotNull NotaGraphParser.EdgeIDePContext ctx){
        Predicate edgeVdatapredicate = trans.getEdgePredicateVisitor().visit(ctx.edgePredicate());
        VData edgeinputData = visit (ctx.edgeInputData());
        return new MultiEdgeVData(edgeVdatapredicate, edgeinputData);
    }

    @Override
    public VData visitNextNode(@NotNull NotaGraphParser.NextNodeContext ctx){
        VData edgeVData = visit (ctx.edgevData());
        return new MultiEdgeVData(new NoPredicate(), new MultiVertexVData(new NoPredicate(), edgeVData, null,false));
    }
    @Override
    public VData visitNextNodeeP(@NotNull NotaGraphParser.NextNodeePContext ctx){
        Predicate edgeVdatapredicate = trans.getEdgePredicateVisitor().visit(ctx.edgePredicate());
        VData edgeVData = visit (ctx.edgevData());
        return new MultiEdgeVData(edgeVdatapredicate, new MultiVertexVData(new NoPredicate(), edgeVData, null,false));
    } @Override
    public VData visitNextNodenP(@NotNull NotaGraphParser.NextNodenPContext ctx){
        Predicate knotenPredicate = trans.getNodePredicateVisitor().visit(ctx.predicate());
        VData edgeVData = visit (ctx.edgevData());
        return new MultiEdgeVData(new NoPredicate(), new MultiVertexVData(knotenPredicate, edgeVData,null, false));
    }
    @Override
    public VData visitNextNodeePnP(@NotNull NotaGraphParser.NextNodeePnPContext ctx){
        Predicate edgeVdatapredicate = trans.getEdgePredicateVisitor().visit(ctx.edgePredicate());
        Predicate knotenPredicate = trans.getNodePredicateVisitor().visit(ctx.predicate());
        VData edgeVData = visit (ctx.edgevData());
        return new MultiEdgeVData(edgeVdatapredicate, new MultiVertexVData(knotenPredicate, edgeVData, null,false));
    }

    @Override
    public VData visitNextNodeFinish(@NotNull NotaGraphParser.NextNodeFinishContext ctx){
        VData edgeVData = visit (ctx.inputData());
        return new MultiEdgeVData(new NoPredicate(), new MultiVertexVData(new NoPredicate(), null,edgeVData, true));
    }
    @Override
    public VData visitNextNodeFinisheP(@NotNull NotaGraphParser.NextNodeFinishePContext ctx){
        Predicate edgeVdatapredicate = trans.getEdgePredicateVisitor().visit(ctx.edgePredicate());
        VData edgeVData = visit (ctx.inputData());
        return new MultiEdgeVData(edgeVdatapredicate, new MultiVertexVData(new NoPredicate(),null, edgeVData, true));
    }
    @Override
    public VData visitNextNodeFinishnP(@NotNull NotaGraphParser.NextNodeFinishnPContext ctx){
        Predicate knotenPredicat = trans.getNodePredicateVisitor().visit(ctx.predicate());
        VData edgeVData = visit (ctx.inputData());
        return new MultiEdgeVData(new NoPredicate(), new MultiVertexVData(knotenPredicat,null, edgeVData, true));
    }
    @Override
    public VData visitNextNodeFinishePnP(@NotNull NotaGraphParser.NextNodeFinishePnPContext ctx) {
        Predicate edgeVdatapredicate = trans.getEdgePredicateVisitor().visit(ctx.edgePredicate());
        Predicate knotenPredicat = trans.getNodePredicateVisitor().visit(ctx.predicate());
        VData edgeVData = visit(ctx.inputData());
        return new MultiEdgeVData(edgeVdatapredicate, new MultiVertexVData(knotenPredicat, null,edgeVData, true));
    }

    @Override
    public VData visitNextNodeLoop(@NotNull NotaGraphParser.NextNodeLoopContext ctx){
        VData edgeVData = visit (ctx.inputData());
        RangeVData range = (RangeVData)visit(ctx.range());
        return new AggregatedEdgeLoopVData(new NoPredicate(), new NoPredicate(), edgeVData, range);
    }
    @Override
    public VData visitNextNodeLoopeP(@NotNull NotaGraphParser.NextNodeLoopePContext ctx){
        Predicate edgeVdatapredicate = trans.getEdgePredicateVisitor().visit(ctx.edgePredicate());
        VData edgeVData = visit (ctx.inputData());
        RangeVData range = (RangeVData)visit(ctx.range());
        return new AggregatedEdgeLoopVData(edgeVdatapredicate, new NoPredicate(), edgeVData,range);
    }
    @Override
    public VData visitNextNodeLoopnP(@NotNull NotaGraphParser.NextNodeLoopnPContext ctx){
        Predicate knotenPredicat = trans.getNodePredicateVisitor().visit(ctx.predicate());
        VData edgeVData = visit (ctx.inputData());
        RangeVData range = (RangeVData)visit(ctx.range());
        return new AggregatedEdgeLoopVData(new NoPredicate(),knotenPredicat, edgeVData, range);
    }
    @Override
    public VData visitNextNodeLoopePnP(@NotNull NotaGraphParser.NextNodeLoopePnPContext ctx) {
        Predicate edgeVdatapredicate = trans.getEdgePredicateVisitor().visit(ctx.edgePredicate());
        Predicate knotenPredicat = trans.getNodePredicateVisitor().visit(ctx.predicate());
        VData edgeVData = visit(ctx.inputData());
        RangeVData range = (RangeVData)visit(ctx.range());
        return new AggregatedEdgeLoopVData(edgeVdatapredicate, knotenPredicat, edgeVData, range);
    }

    @Override
    public  VData visitRangeExact(@NotNull NotaGraphParser.RangeExactContext ctx){
        return new RangeVData(ctx.Int().getSymbol().getText());
    }

    @Override
    public  VData visitRangeLoop(@NotNull NotaGraphParser.RangeLoopContext ctx){
        return new RangeVData(ctx.Int(0).getSymbol().getText(),ctx.Int(1).getSymbol().getText());
    }

    @Override
    public  VData visitRangeInfinity(@NotNull NotaGraphParser.RangeInfinityContext ctx){
        return new RangeVData(ctx.Int().getSymbol().getText(),"Infinity");
    }

    @Override
    public VData visitEidName(@NotNull NotaGraphParser.EidNameContext ctx){
        return new InputDataVDataEdge(ctx.getText().toString(), InputDataVDataEdge.Mode.Name);
    }

    @Override
    public VData visitEidEdgeID(@NotNull NotaGraphParser.EidEdgeIDContext ctx) {
        return new InputDataVDataEdge(ctx.getText().toString(), InputDataVDataEdge.Mode.EdgeID);
    }

    @Override
    public VData visitEidLabel(@NotNull NotaGraphParser.EidLabelContext ctx) {
        return new InputDataVDataEdge(ctx.getText().toString(), InputDataVDataEdge.Mode.Label);
    }


}
