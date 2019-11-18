package optaplanner;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;

import java.util.ArrayList;

public class ScoreCalculator implements EasyScoreCalculator<EventSchedule> {

    @Override
    public Score calculateScore(EventSchedule eventSchedule){
        int hardScore = 0;
        int softScore = 0;

        ArrayList<int[]> occupiedSlots = new ArrayList<>();
        for(PlanningFlexibleEvent event : eventSchedule.getPlanningFlexibleEventList()) {
            int endTime = event.getStartTime() + event.getDuration();
            for(int[] a : occupiedSlots) {
                if((event.getStartTime() > a[0] && event.getStartTime() < a[1]) || (endTime > a[0] && endTime < a[1])){
                    hardScore--;
                }
                else {
                    int[] slot = {event.getStartTime(), endTime};
                    occupiedSlots.add(slot);
                }
            }
        }
        return HardSoftScore.of(hardScore,softScore);
    }
}
