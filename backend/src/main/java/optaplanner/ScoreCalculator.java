package optaplanner;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;
import util.OpUtilsKt;

import java.util.ArrayList;

public class ScoreCalculator implements EasyScoreCalculator<EventSchedule> {

    @Override
    public Score calculateScore(EventSchedule eventSchedule) { // add free time events? any unused space can be labelled as free time
        int hardScore = 0;
        int softScore = 0;
        int totalSocial = 0;
        int totalRec = 0;

        ArrayList<BaseFixedEvent> occupiedSlots = new ArrayList<>(eventSchedule.getUserFixedEventList());
        BaseUserPreferences up = eventSchedule.getUserPreferences();
        boolean conflict = false;
        for (PlanningFlexibleEvent event : eventSchedule.getPlanningFlexibleEventList()) {
            if(event.getStartTime() == null) continue;
            int endTime = event.getStartTime() + event.getDuration();
            int size = occupiedSlots.size();
                for(BaseFixedEvent planned : occupiedSlots) {
                if (overlap(event.getStartTime(), planned.getStartTime(), planned.getEndTime() + 5) ||
                        (overlap(endTime, planned.getStartTime(), planned.getEndTime() + 5))) {
                    hardScore--;
                    conflict = true;
                    break;
                }
            }
                if(!conflict) {
                    BaseFixedEvent tbp = OpUtilsKt.toPlannedFixed(event);
                    occupiedSlots.add(tbp);
                }
            if(event.getType().equals(BaseFlexibleEvent.BREAKFAST)){
                if(overlap(event.getStartTime(), up.getBfStartTime(), up.getBfEndTime()) &&
                        overlap(endTime, up.getBfStartTime(), up.getBfEndTime())) softScore++;
            }
            else if(event.getType().equals(BaseFlexibleEvent.LUNCH)) {
                if(overlap(event.getStartTime(), up.getLunchStartTime(), up.getLunchEndTime()) &&
                    overlap(endTime, up.getLunchStartTime(), up.getLunchEndTime())) softScore++;
            }
            else if(event.getType().equals(BaseFlexibleEvent.DINNER)){
                if(overlap(event.getStartTime(), up.getDinnerStartTime(), up.getDinnerEndTime()) &&
                    overlap(endTime, up.getDinnerStartTime(), up.getDinnerEndTime())) softScore++;
            }
            else if(event.getType().equals(BaseFlexibleEvent.SOCIAL)){
                totalSocial += event.getDuration();
            }
            else if(event.getType().equals(BaseFlexibleEvent.REC)){
                totalRec += event.getDuration();
            }
            else if(event.getType().equals(BaseFlexibleEvent.HW)){
                if(overlap(event.getStartTime(), up.getHwStartTime(), up.getHwEndTime()) &&
                        overlap(endTime, up.getHwStartTime(), up.getHwEndTime())) softScore++;
            }
        }
        if(totalSocial >= up.getSocTime()) softScore++;
        if(totalRec >= up.getRecTime()) softScore++;
        return HardSoftScore.of(hardScore, softScore);
    }

    private boolean overlap(int time, int startTime, int endTime){
        return (time >= startTime && time <= endTime);
    }
}