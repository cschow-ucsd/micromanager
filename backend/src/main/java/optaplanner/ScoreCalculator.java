package optaplanner;

import org.optaplanner.core.api.score.Score;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.impl.score.director.easy.EasyScoreCalculator;
import util.OpUtilsKt;

import java.util.ArrayList;

public class ScoreCalculator implements EasyScoreCalculator<EventSchedule> {

    @Override
    public Score calculateScore(EventSchedule eventSchedule) {
        int hardScore = 0;
        int softScore = 0;
        int totalSocial = 0;
        int totalRec = 0;

        ArrayList<BaseFixedEvent> occupiedSlots = new ArrayList<>();
        occupiedSlots.addAll(eventSchedule.getUserFixedEventList());
        BaseUserPreferences up = eventSchedule.getUserPreferences();
        for (PlanningFlexibleEvent event : eventSchedule.getPlanningFlexibleEventList()) {
            if(event.getStartTime() == null) continue;
            int endTime = event.getStartTime() + event.getDuration();
                for (int i = 0; i < occupiedSlots.size(); i++) { // concurrentModificationException
                    BaseFixedEvent planned = occupiedSlots.get(i);
                if (overlap(event.getStartTime(), planned.getStartTime(), planned.getEndTime()) ||
                        (overlap(endTime, planned.getStartTime(), planned.getEndTime()))) {
                    hardScore--;
                } else {
                    BaseFixedEvent tbp = OpUtilsKt.toPlannedFixed(event);
                    occupiedSlots.add(tbp);
                }
            }
            if(event.getType().equals("Breakfast")){
                if(overlap(event.getStartTime(), up.getBfStartTime(), up.getBfEndTime()) &&
                        overlap(endTime, up.getBfStartTime(), up.getBfEndTime())) softScore++;
            }
            else if(event.getType().equals("Lunch")) {
                if(overlap(event.getStartTime(), up.getLunchStartTime(), up.getLunchEndTime()) &&
                    overlap(endTime, up.getLunchStartTime(), up.getLunchEndTime())) softScore++;
            }
            else if(event.getType().equals("Dinner")){
                if(overlap(event.getStartTime(), up.getDinnerStartTime(), up.getDinnerEndTime()) &&
                    overlap(endTime, up.getDinnerStartTime(), up.getDinnerEndTime())) softScore++;
            }
            else if(event.getType().equals("Social")){
                totalSocial += event.getDuration();
            }
            else if(event.getType().equals("Recreational")){
                totalRec += event.getDuration();
            }
        }
        if(totalSocial >= up.getSocTime()) softScore++;
        if(totalRec >= up.getRecTime()) softScore++;
        return HardSoftScore.of(hardScore, softScore);
    }

    public boolean overlap(int time, int startTime, int endTime){
        return (time > startTime && time < endTime);
    }
}