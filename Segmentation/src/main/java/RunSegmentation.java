import JavaExtractor.Common.CommandLineValues;
import JavaExtractor.Common.Common;
import JavaExtractor.ExtractFeaturesTask;
import JavaExtractor.FeaturesEntities.ProgramRelation;
import com.github.javaparser.ast.CompilationUnit;
import evaluation.EvaluationDummy;
import evaluation.IEvaluation;
import model.EvaluationResult;
import org.kohsuke.args4j.CmdLineException;
import processor.INodeProcessor;
import processor.InferenceProcessor;
import utils.Listener;
import utils.ParsingUtils;
import utils.PythonRunner;
import visitor.InOrderVisitor;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class RunSegmentation {
    public static void main(String[] args) {
        //TODO: replace with command line args
        final String interpeterPath = "C:/Users/Shaked/Anaconda3/envs/py36/python.exe";
        final String modulePath = "C:/code2vec/Segmentation/target/classes/pythonRunner.py"; //this.getClass().getClassLoader().getResource("test.py").toURI().t.getPath();
        final String inputFilePath = "C:/code2vec/input.java";
        Listener listener = new Listener();
        PythonRunner pythonRunner;
        try {
            pythonRunner = PythonRunner.getInstance(interpeterPath, modulePath, listener);
        } catch (IOException e) {
            System.out.println(e.toString());
            return;
        }

        String code;
        CompilationUnit compilationUnit;
        try {
            code = new String(Files.readAllBytes(new File(inputFilePath).toPath()));
            compilationUnit = ParsingUtils.parseFileWithRetries(code);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        IEvaluation eval = new EvaluationDummy();
        INodeProcessor processor = new InferenceProcessor(listener, eval);
        InOrderVisitor visitor = new InOrderVisitor(processor);
        visitor.visitDepthFirst(compilationUnit);
        List<EvaluationResult> results =  visitor.getResults();
        results.stream().filter(r -> r.isBestLabeled())
                        .map(r -> r.formatResults())
                        .forEach(System.out::print);
        pythonRunner.cleanResources();
    }
}
