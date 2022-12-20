package io.itmca.lifepuzzle.domain.story;

public enum AgeGroup {
    UNDER_TEENAGER("10대 미만", 0), TEENAGER("10대", 10), TWENTIES("20대", 20),
    THIRTY("30대",30), FORTY("40대",40), FIFTY("50대", 50), SIXTY("60대", 60),
    SEVENTY("70대", 70), EIGHTY("80대", 80), NINETY("90대", 90), UPPER_NINETY("90대 이상", 100);

    private final String displayName;
    private final int priority;

    AgeGroup(String displayName, int priority){
        this.displayName = displayName;
        this.priority = priority;
    }

    static public AgeGroup of(int age){
        switch(age) {
            case 0 : return UNDER_TEENAGER;
        }
        return UPPER_NINETY;
    }
}
