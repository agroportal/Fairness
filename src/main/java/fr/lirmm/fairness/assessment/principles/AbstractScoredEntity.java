package fr.lirmm.fairness.assessment.principles;

import java.util.List;

public abstract class AbstractScoredEntity {

	public abstract List<Integer> getScores();
	
	public final Integer getTotalScore() {
		Integer score = 0;
		for (Integer s : this.getScores()) {
			score += s;
		}
		return score;
	}
	
	public final Integer getAverageScore(List <Integer> score)
	{
		Integer r =0; 
		
		for (Integer i=0; i<score.size(); i++) {
			r += score.get(i);
		}
		return r;
		
	}
}
