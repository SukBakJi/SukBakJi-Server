package umc.SukBakJi.domain.model.entity.enums;

public enum SalaryLevel {
    높아요(10),
    보통이에요(5),
    낮아요(0);

    private final int value;

    SalaryLevel(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
