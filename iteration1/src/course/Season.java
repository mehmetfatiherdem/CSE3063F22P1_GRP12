package iteration1.src.course;

public enum Season{
    FALL(0),
    SPRING(1),
    SUMMER(2);

    private int value;
    private Season(int value){
        this.value = value;
    }

    public int getValue(){
        return value;
    }
}
