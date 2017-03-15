package NotaQLGraph.Parser;

import NotaQLGraph.model.importModel.*;
import notaql.parser.antlr.ImportBaseVisitor;
import notaql.parser.antlr.ImportParser;

import org.antlr.v4.runtime.misc.NotNull;

import java.util.LinkedList;

/**
 * Created by yannick on 11.02.16.
 * Wird verwendet, um neue NOTAQL Skript für den Import zu erzeugen)
 */
public class ImportVisitor extends ImportBaseVisitor<Import> {

    @Override
    public Import visitTransformation(@NotNull ImportParser.TransformationContext ctx){
        LinkedList<Import> list = new LinkedList<Import>();
        for (int i=0;i<ctx.attributeSpecificatione().size();i++){
           list.add( visit (ctx.attributeSpecificatione(i)));
        }
        return new ImportTransformation(list);

    }


    @Override
    public Import visitASdollar(@NotNull ImportParser.ASdollarContext ctx){
        String a ="";
        for (int i=0;i<ctx.any().size();i++){
            a= a +ctx.any(i).getText();
        }
        return new ImportDollar(a);


    }

    @Override
    public Import visitAsNormal(@NotNull ImportParser.AsNormalContext ctx){
        String a ="";
        for (int i=0;i<ctx.any().size();i++){
            a= a +ctx.any(i).getText();
        }
        return new ImportNormal(a);
    }
    @Override
    public Import visitAsEdge(@NotNull ImportParser.AsEdgeContext ctx){
        return new ImportEdge(visit(ctx.predicate()),visit(ctx.edgeConstructor()));
    }

    @Override
    public Import visitEdgeConstructorFunction(@NotNull ImportParser.EdgeConstructorFunctionContext ctx){
        LinkedList<Import> list = new LinkedList<Import>();


        for (int i=0;i<ctx.edgeConstructorAttribute().size();i++){
            list.add( visit (ctx.edgeConstructorAttribute(i)));
        }

        return new ImportEdgeConstructor( ctx.direction().getText(),visit (ctx.edgeConstructorLabel()),list);
    }

    @Override
    public Import visitEdgeConstructorLabelLabel(@NotNull ImportParser.EdgeConstructorLabelLabelContext ctx){

        return new ImportEdgeConstructorLabel( ctx.LABEL().getText(),visit (ctx.edgeConstructorvData()));
    }

    @Override
    public Import visitEdgeConstructorAttributeLabel(@NotNull ImportParser.EdgeConstructorAttributeLabelContext ctx){

        return new ImportEdgeConstructorAttribute( ctx.Name().getText(),visit (ctx.edgeConstructorvData()));
    }

    @Override
    public Import visitECatomVData(@NotNull ImportParser.ECatomVDataContext ctx){
        return new ImportAtom(ctx.atom().getText());
    }

    @Override
    public Import visitECbracedVData(@NotNull ImportParser.ECbracedVDataContext ctx){
        return new ImportBracedVData(visit(ctx.edgeConstructorvData()));
    }

    @Override
    public Import visitECmultiplicatedVData(@NotNull ImportParser.ECmultiplicatedVDataContext ctx){
        return new ImportOperationVData(visit(ctx.edgeConstructorvData(0)),ctx.op.getText(),visit(ctx.edgeConstructorvData(1)));
    }

    @Override
    public Import visitECrelativeInputPathVData(@NotNull ImportParser.ECrelativeInputPathVDataContext ctx){
        String a ="";
        for (int i=0;i<ctx.inputData().size();i++){
            a= a +ctx.inputData(i).getText();
        }
        return new ImportInputData(a);
    }

    @Override
    public Import visitBracedPredicate(@NotNull ImportParser.BracedPredicateContext ctx){
          return new ImportBracedPredicate(visit (ctx.predicate()));
    }

    @Override
    public Import visitNegatedPredicate(@NotNull ImportParser.NegatedPredicateContext ctx){
        return new ImportNegatedPredicate(visit (ctx.predicate()));
    }

    @Override
    public Import visitAndPredicate(@NotNull ImportParser.AndPredicateContext ctx){
        return new ImportAndORPredicate(visit (ctx.left),ctx.AND().getText(),visit(ctx.right));
    }
    @Override
    public Import visitOrPredicate(@NotNull ImportParser.OrPredicateContext ctx){
        return new ImportAndORPredicate(visit (ctx.left),ctx.OR().getText(),visit(ctx.right));
    }

    @Override
    public Import visitRelationalPredicate(@NotNull ImportParser.RelationalPredicateContext ctx){
        return new ImportRelationalPredicate(visit (ctx.left),ctx.op.getText(),visit(ctx.right));
    }
    @Override
    public Import visitRelativePathExistencePredicate(@NotNull ImportParser.RelativePathExistencePredicateContext ctx){
        return new ImportRelativePathExistencePredicate(visit (ctx.relativeInputPathExistence()));
    }

    @Override
    public Import visitAtomVDataP(@NotNull ImportParser.AtomVDataPContext ctx){
        return new ImportAtom(ctx.atom().getText());
    }

    @Override
    public Import visitBracedVDataP(@NotNull ImportParser.BracedVDataPContext ctx){
        return new ImportBracedVData(visit(ctx.vDataPredicate()));
    }

    @Override
    public Import visitOperationVDataP(@NotNull ImportParser.OperationVDataPContext ctx){
        return new ImportOperationVData(visit(ctx.vDataPredicate(0)),ctx.op.getText(),visit(ctx.vDataPredicate(1)));
    }

    @Override
    public Import visitInputDataZielKnoten(@NotNull ImportParser.InputDataZielKnotenContext ctx){
        String a ="";
        for (int i=0;i<ctx.inputData().size();i++){
            a= a +ctx.inputData(i).getText();
        }
        return new ImportInputDataDestination(a);
    }

    @Override
    public Import visitInputDataVDataP(@NotNull ImportParser.InputDataVDataPContext ctx){
        String a ="";
        for (int i=0;i<ctx.inputData().size();i++){
            a= a +ctx.inputData(i).getText();
        }
        return new ImportInputData(a);
    }

    @Override
    public Import visitRelativeInputPathExistence(@NotNull ImportParser.RelativeInputPathExistenceContext ctx){
        String a ="";
        for (int i=0;i<ctx.inputData().size();i++){
            a= a +ctx.inputData(i).getText();
        }
        return new ImportRelativeInputPathExistence(a);
    }
}