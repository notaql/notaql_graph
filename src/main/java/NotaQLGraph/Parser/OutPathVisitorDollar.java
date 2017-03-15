package NotaQLGraph.Parser;

import NotaQLGraph.Evaluation.Transformation;
import NotaQLGraph.model.outpath.DollarPredicate;
import NotaQLGraph.model.predicate.*;
import NotaQLGraph.model.vdata.VData;
import notaql.parser.antlr.NotaGraphBaseVisitor;
import notaql.parser.antlr.NotaGraphParser;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * Created by yannick on 10.12.15.
 * * Wird verwendet, um bei den Attributespezifikationen den linken Teil zu analysieren (wenn sich sich dabei um eine Dollarspezifikation handelt)
 */
public class OutPathVisitorDollar extends NotaGraphBaseVisitor<Predicate> {
    private Transformation transformation;
    public OutPathVisitorDollar(Transformation trans){
        this.transformation=trans;
    }

    @Override public DollarPredicate visitDollarSpecification(@NotNull NotaGraphParser.DollarSpecificationContext ctx) {
        return new DollarPredicate(visitChildren(ctx));
    }

    @Override
    public Predicate visitBracedDPredicate(@NotNull NotaGraphParser.BracedDPredicateContext ctx) {
        return visit(ctx.dollarPredicate()); //wenn klammern, muss ich eigentlich nix auswerten und kann einfach das nächste Predikat betrachten
    }
    @Override
    public Predicate visitNegatedDPredicate(@NotNull NotaGraphParser.NegatedDPredicateContext ctx) {
        return new NegatedPredicate(visit(ctx.dollarPredicate()));
    }

    @Override
    public Predicate visitExistenceDPredicate(@NotNull NotaGraphParser.ExistenceDPredicateContext ctx) {
        return  visit(ctx.existenceDollarPredicate());
    }

    @Override
    public Predicate visitEDPName(@NotNull NotaGraphParser.EDPNameContext ctx) {
        return new PathExistencePredicate(ctx.Name().getText(), PathExistencePredicate.Source.NodeName);
    }

    @Override
    public Predicate visitAndDPredicate(@NotNull NotaGraphParser.AndDPredicateContext ctx){
        return new LogicalOperationPredicate(
                visit(ctx.left),
                visit(ctx.right),
                LogicalOperationPredicate.Operator.AND
        );
    }

    @Override
    public Predicate visitOrDPredicate(@NotNull NotaGraphParser.OrDPredicateContext ctx) {
        return new LogicalOperationPredicate(
                visit(ctx.left),
                visit(ctx.right),
                LogicalOperationPredicate.Operator.OR
        );
    }

    @Override
    public Predicate visitRelationalDPredicate(@NotNull NotaGraphParser.RelationalDPredicateContext ctx)  {
        final VData left = transformation.getvDataVisitorDollar().visit(ctx.left);
        final VData right = transformation.getvDataVisitorDollar().visit(ctx.right);

        switch (ctx.op.getType()) {
            case NotaGraphParser.LT:
                return new RelationalPredicate(left, right, RelationalPredicate.Operator.LT);
            case NotaGraphParser.LTEQ:
                return new RelationalPredicate(left, right, RelationalPredicate.Operator.LTEQ);
            case NotaGraphParser.GT:
                return new RelationalPredicate(left, right, RelationalPredicate.Operator.GT);
            case NotaGraphParser.GTEQ:
                return new RelationalPredicate(left, right, RelationalPredicate.Operator.GTEQ);
            case NotaGraphParser.EQ:
                return new RelationalPredicate(left, right, RelationalPredicate.Operator.EQ);
            case NotaGraphParser.NEQ:
                return new RelationalPredicate(left, right, RelationalPredicate.Operator.NEQ);
        }
        throw new RuntimeException("Missing Operator: Switch @ visitRelationalPredicate " + ctx.op.getType());
    }

}
