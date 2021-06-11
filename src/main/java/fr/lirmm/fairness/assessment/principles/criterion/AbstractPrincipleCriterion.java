package fr.lirmm.fairness.assessment.principles.criterion;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.stream.Collectors;

import fr.lirmm.fairness.assessment.Configuration;
import fr.lirmm.fairness.assessment.utils.Result;
import fr.lirmm.fairness.assessment.principles.AbstractScoredEntity;
import fr.lirmm.fairness.assessment.principles.criterion.question.AbstractCriterionQuestion;
import org.json.JSONException;

import fr.lirmm.fairness.assessment.model.Ontology;
import fr.lirmm.fairness.assessment.principles.Evaluable;

public abstract class AbstractPrincipleCriterion extends AbstractScoredEntity implements Evaluable, Serializable {
	
	private static final long serialVersionUID = -5519124612489307590L;
	protected List<AbstractCriterionQuestion> questions = null;
	protected List<Double> questionsPoints = null; //TODO remove after ending refactoring

	protected List<Result> results = new ArrayList<>();

	protected void addResult(int index, double score, String explanation) {
		this.results.add(index,new Result(score , explanation , questions.get(index)));
	}

	public List<Result> getResults() {
		return results;
	}
	
	public AbstractPrincipleCriterion() {
		super();
		this.fillQuestions();
		this.questionsPoints = this.questions.stream().map(AbstractCriterionQuestion::getPoints).collect(Collectors.toList());
	}

	@Override
	public final void evaluate(Ontology ontology) throws JSONException, IOException, MalformedURLException, SocketTimeoutException {

		System.out.println("> Evaluating '" + this.getClass().getSimpleName() + "' of ontology '" + ontology.getAcronym() + "' on repository '" + ontology.getPortalInstance().getName() + "' (" + ontology.getPortalInstance().getUrl() + "?apikey=" + ontology.getPortalInstance().getApikey() + ").");
		this.doEvaluation(ontology);
		this.scores = this.results.stream().map(x-> x.getScore()).collect(Collectors.toList());
		this.weights = this.questionsPoints;
	}
	
	protected abstract void doEvaluation(Ontology ontology) throws JSONException, IOException, MalformedURLException, SocketTimeoutException;
	




	private void fillQuestions() {
		try {
			Map<?, ?> fairConfigs = Configuration.getInstance().getFairConfigs();
			Map<? , ?> criteria = (Map<?, ?>) fairConfigs.values().stream()
					.filter(principal -> ((Map<?,?>)principal).containsKey(this.getClass().getSimpleName()))
					.findFirst().get();
			List<Map<?,?>> questionList = (List<Map<?,?>>) criteria.get(this.getClass().getSimpleName());

			this.questions = new ArrayList<>();

			for (Map<?,?> q: questionList) {
				this.questions.add(new AbstractCriterionQuestion(q.get("question").toString() , Double.parseDouble(q.get("points").toString())));
			}
		} catch(Exception ioe) {
			ioe.printStackTrace();
		}
	}



	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

}
