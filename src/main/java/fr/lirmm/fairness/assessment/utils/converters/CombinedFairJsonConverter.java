package fr.lirmm.fairness.assessment.utils.converters;

import com.google.gson.JsonObject;
import fr.lirmm.fairness.assessment.CombinedFair;
import fr.lirmm.fairness.assessment.principles.AbstractPrinciple;


public class CombinedFairJsonConverter extends  AbstractJsonConverter<CombinedFair>{


    public CombinedFairJsonConverter(CombinedFair o) {
        super(o);
    }

    @Override
    public JsonObject toJson() {
        var ontologyJsonObject = new JsonObject();
        for (AbstractPrinciple principle :this.item.getFair().getPrinciples()) {
            ontologyJsonObject.add(principle.getClass().getSimpleName() , new AbstractPrincipleJsonConverter(principle).toJson());
        }
        ontologyJsonObject.add("score", gson.toJsonTree(this.item.getFair().getTotalScore()));
        ontologyJsonObject.add("normalizedScore", gson.toJsonTree(this.item.getFair().getNormalizedTotalScore()));
        ontologyJsonObject.add("maxCredits", gson.toJsonTree(this.item.getFair().getTotalScoreWeight()));
        return ontologyJsonObject;
    }


}
