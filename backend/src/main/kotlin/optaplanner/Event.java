package optaplanner;

import org.optaplanner.core.api.domain.entity.PlanningEntity;

@PlanningEntity
public class Event {
    private boolean isFlexible;
    private int startMins, endMins;
    private String kind;
    public static final String SOME_KIND = ""; // TODO: kinds constants

}
