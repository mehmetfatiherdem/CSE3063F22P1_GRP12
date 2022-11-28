package iteration2.src.course;

public enum LetterGrade{
    AA(4),
    BA(3.5f),
    BB(3),
    CB(2.5f),
    CC(2),
    DC(1.5f),
    DD(1),
    FD(0.5f),
    FF(0),
    ZZ(0),
    NOT_GRADED(0);

    private float numVal;

    LetterGrade(float numVal) {
        this.numVal = numVal;
    }

    public float getNumVal() {
        return numVal;
    }
}
