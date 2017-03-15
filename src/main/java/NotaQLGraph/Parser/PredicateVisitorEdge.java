package NotaQLGraph.Parser;

import NotaQLGraph.Evaluation.Transformation;
import NotaQLGraph.model.predicate.*;
import NotaQLGraph.model.vdata.VData;
import notaql.parser.antlr.*;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * Created by yannick on 7.1.16.
 * Wird verwendet um die Edge Predicate zu checken
 */
public class PredicateVisitorEdge extends NotaGraphBaseVisitor<Predicate> {
    private Transformation transformation;
    public PredicateVisitorEdge(Transformation trans){
        this.transformation=trans;
    }

    @Override
    public Predicate visitBracedEPredicate(@NotNull NotaGraphParser.BracedEPredicateContext ctx) {
        return visit(ctx.edgePredicate()); //wenn klammern, muss ich eigentlich nix auswerten und kann einfach das nächste Predikat betrachten
    }
    @Override
    public Predicate visitNegatedEPredicate(@NotNull NotaGraphParser.NegatedEPredicateContext ctx) {
        return new NegatedPredicate(visit(ctx.edgePredicate()));
    }

    @Override
    public Predicate visitRelativePathExistenceEPredicate(@NotNull NotaGraphParser.RelativePathExistenceEPredicateContext ctx) {
        return  visit(ctx.edgeInputPathExistence());
    }

    @Override
    public Predicate visitEipeName(@NotNull NotaGraphParser.EipeNameContext ctx) {
        return new PathExistencePredicateEdge(ctx.Name().getText(), PathExistencePredicateEdge.Mode.EdgeName);
    }
    @Override
    public Predicate visitEipeLabel(@NotNull NotaGraphParser.EipeLabelContext ctx) {
        return new PathExistencePredicateEdge(ctx.String().getText(), PathExistencePredicateEdge.Mode.EdgeLabel);
    }
    @Override
    public Predicate visitEipeDirection(@NotNull NotaGraphParser.EipeDirectionContext ctx) {
        return new PathExistencePredicateEdge(ctx.direction().getText(), PathExistencePredicateEdge.Mode.Direction);
    }

    @Override
    public Predicate visitAndEPredicate(@NotNull NotaGraphParser.AndEPredicateContext ctx){
        System.out.println("andoperator");
        return new LogicalOperationPredicate(
                visit(ctx.left),
                visit(ctx.right),
                LogicalOperationPredicate.Operator.AND
        );
    }

    @Override
    public Predicate visitOrEPredicate(@NotNull NotaGraphParser.OrEPredicateContext ctx) {
        return new LogicalOperationPredicate(
                visit(ctx.left),
                visit(ctx.right),
                LogicalOperationPredicate.Operator.OR
        );
    }

    @Override
    public Predicate visitRelationalEPredicate(@NotNull NotaGraphParser.RelationalEPredicateContext ctx)  {
        final VData left = transformation.getvDataVisitorPredicateEdge().visit(ctx.left);
        final VData right = transformation.getvDataVisitorPredicateEdge().visit(ctx.right);

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
