package se.rydberg.handla.menu;

public enum FoodTime {
    LUNCH("Lunch"),
    MIDDAG("Middag");

    private final String displayValue;

    private FoodTime(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
