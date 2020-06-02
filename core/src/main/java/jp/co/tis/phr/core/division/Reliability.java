package jp.co.tis.phr.core.division;

public enum Reliability {

    LEVEL_1(1),
    LEVEL_2(2),
    LEVEL_3(3),
    LEVEL_4(4);

    private int level;

    Reliability(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }
}
