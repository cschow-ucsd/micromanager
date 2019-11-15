package optaplanner;

import org.optaplanner.core.api.domain.entity.PlanningEntity;

@PlanningEntity
public class Event {
    private boolean isFlexible;
    private int startMins, endMins;
    private String type;
    public static final String SOME_TYPE = ""; // TODO: kinds constants
}
