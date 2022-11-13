
[Serializable]
public class CourseRecord
{
    public string CourseCode { get; set; }
    public LetterGrade LetterGrade { get; set; }
    public float Grade { get; set; }
    public Season Season { get; set; }
    public Grade RecordYear { get; set; }
    public bool Passed { get; set; }

    public CourseRecord(string courseCode, LetterGrade letterGrade, float grade, Season season, Grade year, bool passed)
    {
        CourseCode = courseCode;
        LetterGrade = letterGrade;
        Grade = grade;
        Season = season;
        RecordYear = year;
        Passed = passed;
    }
}