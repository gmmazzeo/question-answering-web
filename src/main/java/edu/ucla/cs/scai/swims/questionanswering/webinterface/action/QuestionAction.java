package edu.ucla.cs.scai.swims.questionanswering.webinterface.action;

/**
 *
 * @author Giuseppe M. Mazzeo <mazzeo@cs.ucla.edu>
 */
import com.opensymphony.xwork2.ActionSupport;
import edu.ucla.cs.scai.qa.questionclassifier.Parser;
import edu.ucla.cs.scai.qa.questionclassifier.PennTreebankPattern;
import edu.ucla.cs.scai.qa.questionclassifier.PennTreebankPatternMatcher;
import edu.ucla.cs.scai.qa.questionclassifier.QueryResolver2;
import edu.ucla.cs.scai.qa.questionclassifier.SyntacticTree;
import edu.ucla.cs.scai.swim.qa.ontology.QueryMapping;
import edu.ucla.cs.scai.swim.qa.ontology.QueryModel;
import edu.ucla.cs.scai.swim.qa.ontology.dbpedia.DBpediaOntology;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;

public class QuestionAction extends ActionSupport {

    private static final long serialVersionUID = 1L;

    String question;
    String step1Output, step2Output, step3Output;
    String answer;
    static Parser parser = new Parser(DBpediaOntology.getInstance());    
    static PennTreebankPatternMatcher matcher = new PennTreebankPatternMatcher(System.getProperty("patterns.path"));

    @Override
    public String execute() throws Exception {
        if (question == null) {
            question = "In which films directed by Garry Marshall was Julia Roberts starring?";
        } else {
            try {
                SyntacticTree t = parser.parse(question);
                step1Output=t.toString();
                HashMap<PennTreebankPattern, SyntacticTree> ps = matcher.match(t);
                ArrayList<QueryModel> initialModels = new ArrayList<>();                
                for (PennTreebankPattern p : ps.keySet()) {
                    step1Output+="\n"+p.getName();
                    QueryResolver2 qr = new QueryResolver2(ps.get(p));
                    initialModels.addAll(qr.resolveIQueryModels(p));
                }
                Collections.sort(initialModels);
                double threshold = 0.1;
                double maxWeight = initialModels.isEmpty() ? 0 : initialModels.get(0).getWeight();
                for (Iterator<QueryModel> it = initialModels.iterator(); it.hasNext();) {
                    QueryModel im = it.next();
                    im.setWeight(im.getWeight() / maxWeight);
                    if (im.getWeight() < threshold) {
                        it.remove();
                    }
                }
                step2Output="";
                for (QueryModel im : initialModels) {
                    step2Output+="Weight: " + im.getWeight()+"\n";
                    step2Output+=im.toString()+"\n";
                    step2Output+="-------------------------\n";
                }
                
                step3Output="";
                QueryMapping qm = new QueryMapping();
                ArrayList<QueryModel> mappedModels = qm.mapOnOntology(initialModels, DBpediaOntology.getInstance());
                for (int i = 0; i < mappedModels.size(); i++) {
                    step3Output+="Weight: " + mappedModels.get(i).getWeight()+"\n";
                    step3Output+=mappedModels.get(i).toString()+"\n";
                    step3Output+="-------------------------\n";
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                //SyntacticTree t = parser.parse(qt);
                //t.compactNamedEntities();
                //System.out.println(t);
            }
            answer = "The answer is not computed yet";

        }
        return SUCCESS;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getStep1Output() {
        return step1Output;
    }

    public void setStep1Output(String step1Output) {
        this.step1Output = step1Output;
    }

    public String getStep2Output() {
        return step2Output;
    }

    public void setStep2Output(String step2Output) {
        this.step2Output = step2Output;
    }

    public String getStep3Output() {
        return step3Output;
    }

    public void setStep3Output(String step3Output) {
        this.step3Output = step3Output;
    }

}
