package NotaQLGraph.Parser;

import NotaQLGraph.Evaluation.Transformation;
import NotaQLGraph.model.predicate.*;
import NotaQLGraph.model.vdata.VData;
import notaql.parser.antlr.*;
import org.antlr.v4.runtime.misc.NotNull;

/**
 * Created by yannick on 25.11.15.
 * Wird verwendet um das Inpredikate zu checken, sowie später bei den Kanten zu überprüfen ob der nächste Knoten passend ist
 */
public class PredicateVisitor extends NotaGraphBaseVisitor<Predicate> {
    private Transformation transformation;
    public PredicateVisitor(Transformation trans){
        this.transformation=trans;
    }

    @Override
    public Predicate visitInPredicate(@NotNull NotaGraphParser.InPredicateContext ctx){
       return visit(ctx.predicate());//ganz am Anfang die Wurzel öffnen
   }

    @Override
    public Predicate visitBracedPredicate(@NotNull NotaGraphParser.BracedPredicateContext ctx) {
        return visit(ctx.predicate()); //wenn klammern, muss ich eigentlich nix auswerten und kann einfach das nächste Predikat betrachten
    }
    @Override
    public Predicate visitNegatedPredicate(@NotNull NotaGraphParser.NegatedPredicateContext ctx) {
        return new NegatedPredicate(visit(ctx.predicate()));
    }


    @Override
    public Predicate visitRelativePathExistencePredicate(@NotNull NotaGraphParser.RelativePathExistencePredicateContext ctx) {
        return  visit(ctx.relativeInputPathExistence());
    }



    @Override
    public Predicate visitRIPNAME(@NotNull NotaGraphParser.RIPNAMEContext ctx) {
        return new PathExistencePredicate(ctx.Name().getText(), PathExistencePredicate.Source.NodeName);
    }

    @Override
    public Predicate visitRIPEDGE(@NotNull NotaGraphParser.RIPEDGEContext ctx){
        return new PathExistencePredicate(transformation.getvDataVisitor().visit(ctx.edgevData()), PathExistencePredicate.Source.Edge);
    }

    @Override
    public Predicate visitAndPredicate(@NotNull NotaGraphParser.AndPredicateContext ctx){
        return new LogicalOperationPredicate(
                visit(ctx.left),
                visit(ctx.right),
                LogicalOperationPredicate.Operator.AND
        );
    }

    @Override
    public Predicate visitOrPredicate(@NotNull NotaGraphParser.OrPredicateContext ctx) {
        return new LogicalOperationPredicate(
                visit(ctx.left),
                visit(ctx.right),
                LogicalOperationPredicate.Operator.OR
        );
    }

    @Override
    public Predicate visitRelationalPredicate(@NotNull NotaGraphParser.RelationalPredicateContext ctx)  {
        final VData left = transformation.getvDataVisitorPredicate().visit(ctx.left);
        final VData right = transformation.getvDataVisitorPredicate().visit(ctx.right);

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
