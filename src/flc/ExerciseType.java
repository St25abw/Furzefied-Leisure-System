package flc;

public enum ExerciseType {
    YOGA("Yoga", 12.00),
    ZUMBA("Zumba", 10.00),
    AQUACISE("Aquacise", 9.00),
    BOX_FIT("Box Fit", 14.00),
    BODY_BLITZ("Body Blitz", 11.00);

    private final String displayName;
    private final double price;

    ExerciseType(String displayName, double price) {
        this.displayName = displayName;
        this.price = price;
    }

    public String getDisplayName() { return displayName; }
    public double getPrice()       { return price; }

    @Override
    public String toString() { return displayName; }

   
    public static ExerciseType fromName(String name) {
        for (ExerciseType t : values()) {
            if (t.displayName.equalsIgnoreCase(name)) return t;
        }
        return null;
    }
}
