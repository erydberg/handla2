package se.rydberg.handla.menu;

public enum Grade {
    UN_GRADED("Inte utvärderat ännu"),
    NEVER_EAT_AGAIN("Inte äta igen"),
    EAT_AGAIN("God, den tar vi igen någon gång"),
    FANTASTIC("Fantastiskt gott");

    private final String displayValue;

    private Grade(String displayValue){
        this.displayValue = displayValue;
    }

    public String getDisplayValue(){
        return displayValue;
    }
}
