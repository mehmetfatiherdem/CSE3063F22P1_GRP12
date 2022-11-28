package iteration2.src.human;

public enum Grade {
    FRESHMAN(0),
    SOPHOMORE(1),
    JUNIOR(2),
    SENIOR(3);

    private int value;

    private Grade(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
