package io.itmca.lifepuzzle.domain.story;

import lombok.Getter;

@Getter
public enum AgeGroup {
    UNDER_TEENAGER("10대 미만", 0L), TEENAGER("10대", 10L), TWENTIES("20대", 20L),
    THIRTY("30대",30L), FORTY("40대",40L), FIFTY("50대", 50L), SIXTY("60대", 60L),
    SEVENTY("70대", 70L), EIGHTY("80대", 80L), NINETY("90대", 90L), UPPER_NINETY("90대 이상", 100L);

    private final String displayName;
    private final Long priority;

    AgeGroup(String displayName, Long priority){
        this.displayName = displayName;
        this.priority = priority;
    }

    static public AgeGroup of(int age){
        if(age < 10) return UNDER_TEENAGER;
        else if(age < 20) return TEENAGER;
        else if(age < 30) return TWENTIES;
        else if(age < 40) return THIRTY;
        else if(age < 50) return FORTY;
        else if(age < 60) return FIFTY;
        else if(age < 70) return SIXTY;
        else if(age < 80) return SEVENTY;
        else if(age < 90) return EIGHTY;
        else if(age < 100) return NINETY;
        else return UPPER_NINETY;
    }
}
