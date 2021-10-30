public enum Sex {

    MALE("мужской"),
    FEMALE("женский");

    private String tip;

    Sex(String tip) {
        this.tip = tip;
    }

    public String getTip() {
        return tip;
    }
}
