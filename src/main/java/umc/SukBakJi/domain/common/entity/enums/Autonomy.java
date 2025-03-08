package umc.SukBakJi.global.entity.enums;

public enum Autonomy {
    높아요(10),
    보통이에요(5),
    낮아요(0);

    private final int value;

    Autonomy(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
