
[Serializable]
public class Student : Person
{
    private static List<string> generatedStudentIDS;
    private static Random rng;
    public readonly static int CurrentSemester;

    private const float minFailChance = 0.04f;
    private const float maxFailChance = 0.45f;
    private const float minRetakeChance = 0.41f;
    private const float maxRetakeChance = 0.87f;
    private const float minNotTakeChance = 0.02f;
    private const float maxNotTakeChance = 0.06f;

    //Serializable
    public string ID { get; set; }
    public Grade Grade { get; set; }
    public string AdvisorName { get; set; }
    public List<CourseRecord> CourseRecords { get; set; }

    static Student()
    {
        generatedStudentIDS = new List<string>();
        rng = new Random();

        CurrentSemester = rng.Next(0, 2);
    }

    public Student(string firstName, string? middleName, string lastName, string advisorName) : base(firstName, middleName, lastName)
    {
        var info = GenerateStudentIDAndGrade();

        ID = info.ID;
        Grade = info.grade;
        AdvisorName = advisorName;

        CourseRecords = new List<CourseRecord>();
        GenerateStudentHistory();
    }

    private (string ID, Grade grade) GenerateStudentIDAndGrade()
    {
        StringBuilder idBuilder;
        string id;

        do
        {
            idBuilder = new StringBuilder("1501");

            idBuilder.Append(rng.Next(18, 23));

            string last3 = rng.Next(1, 300).ToString();

            for (int i = 0; i < 3 - last3.Length; i++)
            {
                idBuilder.Append(0);
            }

            idBuilder.Append(last3);

        } while (generatedStudentIDS.Contains(id = idBuilder.ToString()));

        Grade grade = (Grade)(22 - Convert.ToInt32(id.Substring(4, 2)));

        if ((int)grade > 3)
            grade = Grade.SENIOR;

        generatedStudentIDS.Add(id);

        return (id, grade);
    }

    private void GenerateStudentHistory()
    {
        float failChance = RandomBetween(minFailChance, maxFailChance);
        float retakeChance = RandomBetween(minRetakeChance, maxRetakeChance);
        float notTakechance = RandomBetween(minNotTakeChance, maxNotTakeChance);

        Grade grade = Grade.FRESHMAN;
        int semester = 0;


        List<Course> curriculum;
        List<Course> passedClasses = new List<Course>();
        List<Course> failedClasses = new List<Course>();
        List<Course> nontakenClasses = new List<Course>();

        while (Grade != grade || CurrentSemester != semester)
        {
            curriculum = new List<Course>();

            //then failed classes are the first priority
            failedClasses.ForEach(c => DecideToTakeCourse(c));

            //then nontaken classes
            for (int i = 0; i < nontakenClasses.Count; i++)
                DecideToTakeCourse(nontakenClasses[i]);

            //then mandatory classes
            Course.Mandatory.Where(c => c.Semester == semester && c.FirstGradeToTake == grade).ToList().ForEach(m => DecideToTakeCourse(m));

            //then elective courses
            if(grade == Grade.FRESHMAN && semester == 1)
            {
                //NTE
                var available = Course.NonTechnicalElective_UniversityElective.Where(nte => !passedClasses.Contains(nte)).ToList();
                DecideToTakeCourse(available[rng.Next(0,available.Count)]);
            }
            else if(grade == Grade.SENIOR && semester == 0)
            {
                //TE and UE
                var available = Course.TechnicalElective.Where(te => !passedClasses.Contains(te)).ToList();
                DecideToTakeCourse(available[rng.Next(0,available.Count)]);

                available = Course.NonTechnicalElective_UniversityElective.Where(ue => !passedClasses.Contains(ue)).ToList();
                DecideToTakeCourse(available[rng.Next(0, available.Count)]);
            }
            else if(grade == Grade.SENIOR && semester == 1)
            {
                //FTE and NTE and 3*TE

                var available = Course.FacultyTechnicalElective.Where(fte => !passedClasses.Contains(fte)).ToList();
                DecideToTakeCourse(available[rng.Next(0, available.Count)]);

                available = Course.NonTechnicalElective_UniversityElective.Where(nte => !passedClasses.Contains(nte)).ToList();
                DecideToTakeCourse(available[rng.Next(0, available.Count)]);

                available = Course.TechnicalElective.Where(te => !passedClasses.Contains(te)).ToList();

                int[] randos = new int[3] { -1, -1, -1 };

                for (int i = 0; i < 3; i++)
                {
                    int rando;
                    do
                    {
                        rando = rng.Next(0, available.Count);
                    }
                    while (randos.Contains(rando));

                    randos[i] = rando;
                }

                foreach (var rando in randos)
                    DecideToTakeCourse(available[rando]);
            }

            curriculum.ForEach(c => FinishCourse(c));

            semester = (semester + 1) % 2;

            if(semester == 0)
                grade = (Grade)((int)grade + 1);
        }

        void DecideToTakeCourse(Course course)
        {
            if(!CanTakeClass(course))
            {
                if(!nontakenClasses.Contains(course))
                    nontakenClasses.Add(course);

                return;
            }

            if(curriculum.Count > 10)
            {
                if (!failedClasses.Contains(course) && !nontakenClasses.Contains(course))
                {
                    nontakenClasses.Add(course);
                }

                return;
            }

            if (rng.NextSingle() <= notTakechance)
            {
                nontakenClasses.Add(course);
            }
            else
            {
                try
                {
                    nontakenClasses.Remove(course);
                }
                catch (Exception) { }

                curriculum.Add(course);
            }
        }

        bool CanTakeClass(Course course)
        {
            foreach (var pre in course.Prerequisites)
                if (!passedClasses.Contains(pre))
                    return false;

            return true;
        }

        void FinishCourse(Course course)
        {
            bool passed;
            float numericGrade;

            if(rng.NextSingle() <= failChance)
            {
                if(!failedClasses.Contains(course))
                    failedClasses.Add(course);

                numericGrade = RandomBetween(0.0f, (float)LetterGrade.DD - 0.01f);
                passed = false;
            }
            else
            {
                try
                {
                    failedClasses.Remove(course);
                }
                catch (Exception) { }

                passedClasses.Add(course);
                numericGrade = RandomBetween((float)LetterGrade.DD, 100.0f);
                passed = true;
            }

            LetterGrade letterGrade = GetLetterGrade(numericGrade);

            CourseRecords.Add(new CourseRecord(course.CourseCode, letterGrade, numericGrade, (Season)semester, grade, passed));
        }

        LetterGrade GetLetterGrade(float grade)
        {
            var values = Enum.GetValues(typeof(LetterGrade)).Cast<LetterGrade>().ToList();

            return values.OrderBy(v => Math.Abs(grade - (float)v)).First();
        }

        float RandomBetween(float min, float max) => rng.NextSingle() * (max - min) + min;
    }

}
