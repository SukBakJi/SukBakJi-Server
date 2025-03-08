package umc.SukBakJi.domain.common.entity.enums;

public enum LeadershipStyle {
    좋았어요(10),
    보통이에요(5),
    아쉬워요(0);

    private final int value;

    LeadershipStyle(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
