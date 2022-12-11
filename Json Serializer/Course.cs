using ExtensionMethods;

[Serializable]
public class Course
{
    public static List<Course> Mandatory;
    public static List<Course> TechnicalElective;
    public static List<Course> FacultyTechnicalElective;
    public static List<Course> NonTechnicalElective_UniversityElective;

    private static string type;

    private List<Course> prerequisites;

    [JsonIgnore]
    public List<Course> Prerequisites => prerequisites;
    [JsonIgnore]
    public int Semester => (int)FirstSeasonToTake;


    //Serialized fields
    public string CourseCode { get; set; }
    public string CourseName { get; set; }
    public string CourseType { get; set; }
    public int Credits { get; set; }
    public int TheoreticalHours { get; set; }
    public int AppliedHours { get; set; }
    public List<string> PrerequisiteCodes { get; set; }
    public Grade FirstGradeToTake { get; set; }
    public Season FirstSeasonToTake { get; set; }
    public List<string> Lecturers { get; set; }
    public List<string> Assistants { get; set; }


    static Course()
    {
        Mandatory = new List<Course>();
        TechnicalElective = new List<Course>();
        FacultyTechnicalElective = new List<Course>();
        NonTechnicalElective_UniversityElective = new List<Course>();
        SetupCourses();
        type = string.Empty;
    }

    public static void AssignFacultyMembersToCourses(List<Person> mandatoryLecturers,List<Person> otherLecturers, List<Person> assistants)
    {
        Random rng = new Random();

        mandatoryLecturers.Shuffle();
        otherLecturers.Shuffle();
        assistants.Shuffle();

        foreach (var course in Mandatory)
        {
            int noOfLecturers = rng.Next(1, 4);
            List<Person> temp = new List<Person>();

            for (int i = 0; i < noOfLecturers; i++)
            {
                int randomIndex = rng.Next(mandatoryLecturers.Count);
                Person lecturer = mandatoryLecturers[randomIndex];
                course.Lecturers.Add(lecturer.ToString());
                mandatoryLecturers.RemoveAt(randomIndex);
                temp.Add(lecturer);
            }

            mandatoryLecturers.AddRange(temp);
        }

        var appliedCourses = Mandatory.Where(m => m.AppliedHours > 0).ToList();

        int noOfAssistants = assistants.Count;

        foreach (var course in appliedCourses)
        {
            course.Assistants.Add(assistants[rng.Next(noOfAssistants)].ToString());
        }

        foreach (var course in TechnicalElective)
        {
            course.Lecturers.Add(otherLecturers[0].ToString());
            otherLecturers.RemoveAt(0);
        }

        foreach (var course in FacultyTechnicalElective)
        {
            course.Lecturers.Add(otherLecturers[0].ToString());
            otherLecturers.RemoveAt(0);
        }

        foreach (var course in NonTechnicalElective_UniversityElective)
        {
            course.Lecturers.Add(otherLecturers[0].ToString());
            otherLecturers.RemoveAt(0);
        }
    }

    private Course(string courseCode, Grade firstGradeToTake, Season firstSeasonToTake, string courseName, int credits,
        int theoreticalHours, int appliedHours)
    {
        CourseCode = courseCode;
        FirstGradeToTake = firstGradeToTake;
        FirstSeasonToTake = firstSeasonToTake;
        CourseName = courseName;
        Credits = credits;
        TheoreticalHours = theoreticalHours;
        AppliedHours = appliedHours;
        CourseType = type;
        prerequisites = new List<Course>();
        PrerequisiteCodes = new List<string>();
        Lecturers = new List<string>();
        Assistants = new List<string>();
    }

    private void AddPrerequisite(Course course)
    {
        prerequisites.Add(course);
        PrerequisiteCodes.Add(course.CourseCode);
    }

    private static void SetupCourses()
    {
        type = "Mandatory";
        //SEMESTER 1
        Course ATA121 = new Course("ATA121", Grade.FRESHMAN, Season.FALL, "Atatürk İlkeleri ve İnkılap Tarihi I", 2, 2, 0);
        Course MBG1201 = new Course("MBG1201", Grade.FRESHMAN, Season.FALL, "Introduction to Modern Biology", 5, 3, 0);
        Course CSE1200 = new Course("CSE1200", Grade.FRESHMAN, Season.FALL, "Introduction to Computer Engineering", 4, 3, 0);
        Course CSE1241 = new Course("CSE1241", Grade.FRESHMAN, Season.FALL, "Computer Programming I", 6, 3, 2);
        Course MATH1001 = new Course("MATH1001", Grade.FRESHMAN, Season.FALL, "Calculus I", 6, 4, 0);
        Course PHYS1101 = new Course("PHYS1101", Grade.FRESHMAN, Season.FALL, "Physics I", 4, 3, 0);
        Course PHYS1103 = new Course("PHYS1103", Grade.FRESHMAN, Season.FALL, "Physics Lab I", 2, 0, 2);
        Course TRD121 = new Course("TRD121", Grade.FRESHMAN, Season.FALL, "Türk Dili I", 2, 2, 0);

        //SEMESTER 2
        Course ATA122 = new Course("ATA122", Grade.FRESHMAN, Season.SPRING, "Atatürk İlkeleri ve İnkılap Tarihi II", 2, 2, 0);
        Course CSE1242 = new Course("CSE1242", Grade.FRESHMAN, Season.SPRING, "Computer Programming II", 6, 3, 2);
        Course MATH1002 = new Course("MATH1002", Grade.FRESHMAN, Season.SPRING, "Calculus II", 6, 4, 0);
        Course MATH2256 = new Course("MATH2256", Grade.FRESHMAN, Season.SPRING, "Linear Algebra for Computer Engineering", 5, 3, 0);
        Course PHYS1102 = new Course("PHYS1102", Grade.FRESHMAN, Season.SPRING, "Physics II", 4, 3, 0);
        Course PHYS1104 = new Course("PHYS1104", Grade.FRESHMAN, Season.SPRING, "Physics Lab II", 2, 0, 2);
        Course TRD122 = new Course("TRD122", Grade.FRESHMAN, Season.SPRING, "Türk Dili II", 2, 2, 0);
        //NTE

        //SEMESTER 3
        Course CSE2023 = new Course("CSE2023", Grade.SOPHOMORE, Season.FALL, "Discrete Computational Structures", 6, 3, 0);
        Course CSE2225 = new Course("CSE2225", Grade.SOPHOMORE, Season.FALL, "Data Structures", 7, 3, 2);
        Course ECON2004 = new Course("ECON2004", Grade.SOPHOMORE, Season.FALL, "Engineering Economy", 4, 2, 0);
        Course EE2031 = new Course("EE2031", Grade.SOPHOMORE, Season.FALL, "Electric Circuits", 5, 3, 0);
        Course MATH2055 = new Course("MATH2055", Grade.SOPHOMORE, Season.FALL, "Differential Equations", 4, 3, 0);
        Course MATH2059 = new Course("MATH2059", Grade.SOPHOMORE, Season.FALL, "Numerical Methods", 4, 3, 0);

        //SEMESTER 4
        Course CSE2246 = new Course("CSE2246", Grade.SOPHOMORE, Season.SPRING, "Analysis of Algorithms", 6, 3, 2);
        Course CSE2138 = new Course("CSE2138", Grade.SOPHOMORE, Season.SPRING, "Systems Programming", 7, 3, 2);
        Course CSE2260 = new Course("CSE2260", Grade.SOPHOMORE, Season.SPRING, "Principles of Programming Languages", 4, 3, 2);
        Course EE2032 = new Course("EE2032", Grade.SOPHOMORE, Season.SPRING, "Electronics", 5, 3, 2);
        Course STAT2253 = new Course("STAT2253", Grade.SOPHOMORE, Season.SPRING, "Introduction to Probability and Statistics", 5, 3, 0);

        //SEMESTER 5
        //Course CSE3000 = new Course("CSE3000", Grade.JUNIOR, Season.FALL, "Summer Practice I", 10, 0, 0); => internship
        Course CSE3215 = new Course("CSE3215", Grade.JUNIOR, Season.FALL, "Digital Logic Design", 6, 3, 2);
        Course CSE3033 = new Course("CSE3033", Grade.JUNIOR, Season.FALL, "Operating Systems", 7, 3, 2);
        Course CSE3055 = new Course("CSE3055", Grade.JUNIOR, Season.FALL, "Database Systems", 7, 3, 2);
        Course CSE3063 = new Course("CSE3063", Grade.JUNIOR, Season.FALL, "Object-Oriented Software Design", 5, 3, 0);
        Course IE3081 = new Course("IE3081", Grade.JUNIOR, Season.FALL, "Modeling and Discrete Simulation", 4, 3, 0);

        //SEMESTER 6
        Course CSE3038 = new Course("CSE3038", Grade.JUNIOR, Season.SPRING, "Computer Organization", 7, 3, 2);
        Course CSE3044 = new Course("CSE3044", Grade.JUNIOR, Season.SPRING, "Software Engineering", 7, 3, 2);
        Course CSE3048 = new Course("CSE3048", Grade.JUNIOR, Season.SPRING, "Introduction to Signals and Systems", 5, 3, 0);
        Course CSE3264 = new Course("CSE3264", Grade.JUNIOR, Season.SPRING, "Formal Languages and Automata Theory", 5, 3, 0);
        Course IE3235 = new Course("IE3235", Grade.JUNIOR, Season.SPRING, "Operations Research", 4, 3, 0);
        Course COM2202 = new Course("COM2202", Grade.JUNIOR, Season.SPRING, "Technical Comm. and Entrepreneurship", 2, 2, 0);

        //SEMESTER 7
        //Course CSE4000 = new Course("CSE4000", Grade.SENIOR, Season.FALL, "Summer Practice II", 10, 0, 0); => internship
        Course CSE4074 = new Course("CSE4074", Grade.SENIOR, Season.FALL, "Computer Networks", 5, 3, 0);
        Course CSE4219 = new Course("CSE4219", Grade.SENIOR, Season.FALL, "Principles of Embedded System Design", 6, 3, 2);
        Course CSE4297 = new Course("CSE4297", Grade.SENIOR, Season.FALL, "Engineering Project I", 5, 0, 2);
        Course ISG121 = new Course("ISG121", Grade.SENIOR, Season.FALL, "İş Sağlığı ve Güvenliği I", 2, 2, 0);
        Course CSE4288 = new Course("CSE4288", Grade.SENIOR, Season.FALL, "Introduction to Machine Learning", 5, 3, 0);
        //TE
        //UE

        //SEMESTER 8
        Course CSE4298 = new Course("CSE4298", Grade.SENIOR, Season.SPRING, "Engineering Project II", 5, 0, 2);
        Course ISG122 = new Course("ISG122", Grade.SENIOR, Season.SPRING, "İş Sağlığı ve Güvenliği II", 2, 2, 0);
        //FTE
        //NTE
        //TE
        //TE
        //TE

        Mandatory.AddRange(new Course[] { ATA121, MBG1201, CSE1200, CSE1241, MATH1001, PHYS1101, PHYS1103, TRD121 });
        Mandatory.AddRange(new Course[] { ATA122, CSE1242, MATH1002, MATH2256, PHYS1102, PHYS1104, TRD122 });
        Mandatory.AddRange(new Course[] { CSE2023, CSE2225, ECON2004, EE2031, MATH2055, MATH2059 });
        Mandatory.AddRange(new Course[] { CSE2246, CSE2138, CSE2260, EE2032, STAT2253 });
        Mandatory.AddRange(new Course[] { CSE3215, CSE3033, CSE3055, CSE3063, IE3081 });
        Mandatory.AddRange(new Course[] { CSE3038, CSE3044, CSE3048, CSE3264, IE3235, COM2202 });
        Mandatory.AddRange(new Course[] { CSE4074, CSE4219, CSE4297, ISG121, CSE4288 });
        Mandatory.AddRange(new Course[] { CSE4298, ISG122 });

        type = "Technical Elective";

        Course CSE4075 = new Course("CSE4075", Grade.SENIOR, Season.FALL, "Wireless and Mobile Networks", 5, 3, 0);
        Course CSE4034 = new Course("CSE4034", Grade.SENIOR, Season.FALL, "Advanced Unix Programming", 5, 3, 0);
        Course CSE4061 = new Course("CSE4061", Grade.SENIOR, Season.FALL, "Compiler Design", 5, 3, 0);
        Course CSE4217 = new Course("CSE4217", Grade.SENIOR, Season.FALL, "Microprocessors", 5, 3, 0);

        TechnicalElective.AddRange(new Course[]
        {
            new Course("CSE4026",Grade.SENIOR,Season.FALL,"Introduction to Robotics and Control Theory",5,3,0),
            new Course("CSE4032", Grade.SENIOR, Season.FALL,"Introduction to Distributed Systems",5,3,0),
            new Course("CSE4038", Grade.SENIOR, Season.FALL,"Introduction to Parallel Processing",5,3,0),
            new Course("CSE4040", Grade.SENIOR, Season.FALL,"Cloud Computing",5,3,0),
            new Course("CSE4044", Grade.SENIOR, Season.FALL,"Software Project Management",5,3,0),
            new Course("CSE4053", Grade.SENIOR, Season.FALL,"Information Systems: Analysis and Design",5,3,0),
            new Course("CSE4056", Grade.SENIOR, Season.FALL,"Management of Information Systems",5,3,0),
            new Course("CSE4057", Grade.SENIOR, Season.FALL,"Information Systems Security",5,3,0),
            new Course("CSE4058", Grade.SENIOR, Season.FALL,"Fundamentals of Electronic Commerce",5,3,0),
            new Course("CSE4059", Grade.SENIOR, Season.FALL,"Internet Programming",5,3,0),
            new Course("CSE4062", Grade.SENIOR, Season.FALL,"Introduction to Data Science and Analytics",5,3,0),
            new Course("CSE4063", Grade.SENIOR, Season.FALL,"Fundamentals of Data Mining",5,3,0),
            new Course("CSE4065", Grade.SENIOR, Season.FALL,"Introduction to Computational Genomics",5,3,0),
            new Course("CSE4066", Grade.SENIOR, Season.FALL,"Introduction to Cryptography",5,3,0),
            new Course("CSE4067", Grade.SENIOR, Season.FALL,"Introduction to Blockchain Programming",5,3,0),
            new Course("CSE4070", Grade.SENIOR, Season.FALL,"Software Frameworks",5,3,0),
            new Course("CSE4082", Grade.SENIOR, Season.FALL,"Artificial Intelligence",5,3,0),
            new Course("CSE4083", Grade.SENIOR, Season.FALL,"Computer Graphics",5,3,0),
            new Course("CSE4084", Grade.SENIOR, Season.FALL,"Multimedia Systems",5,3,0),
            new Course("CSE4085", Grade.SENIOR, Season.FALL,"Human Computer Interaction",5,3,0),
            new Course("CSE4086", Grade.SENIOR, Season.FALL,"Mobile Device Programming",5,3,0),
            new Course("CSE4093", Grade.SENIOR, Season.FALL,"Special Topics in Computer Engineering I",5,3,0),
            new Course("CSE4094", Grade.SENIOR, Season.FALL,"Special Topics in Computer Engineering II",5,3,0),
            new Course("CSE4095", Grade.SENIOR, Season.FALL,"Special Topics in Computer Engineering III",5,3,0),
            new Course("CSE4096", Grade.SENIOR, Season.FALL,"Special Topics in Computer Engineering IV",5,3,0),
            CSE4075,
            CSE4034,
            CSE4061,
            CSE4217
        });

        type = "Faculty Technical Elective";

        FacultyTechnicalElective.AddRange(new Course[]
        {
            new Course("BIOE4072", Grade.SENIOR, Season.SPRING,"Planning and Management of Research",5,3,0),
            new Course("CHE4060", Grade.SENIOR, Season.SPRING,"Cost Management in Chemical Industries",5,3,0),
            new Course("CHE4068", Grade.SENIOR, Season.SPRING,"Financial Engineering",5,3,0),
            new Course("CHE4080", Grade.SENIOR, Season.SPRING,"Chemical Engineering for Sustainable Biosphere",5,3,0),
            new Course("CSE4044_2", Grade.SENIOR, Season.SPRING,"Software Project Management",5,3,0),
            new Course("CSE4056_2", Grade.SENIOR, Season.SPRING,"Management of Information Systems",5,3,0),
            new Course("CSE4062_2", Grade.SENIOR, Season.SPRING,"Introduction to Data Science and Analytics",5,3,0),
            new Course("EE4056", Grade.SENIOR, Season.SPRING,"Introduction to Information Systems Management",5,3,0),
            new Course("EE4062", Grade.SENIOR, Season.SPRING,"Introduction to Image Processing",5,3,0),
            new Course("ENVE4062", Grade.SENIOR, Season.SPRING,"Energy and the Environment",5,3,0),
            new Course("ENVE4063", Grade.SENIOR, Season.SPRING,"Construction Materials for Environmental Engineering",5,3,0),
            new Course("IE4055", Grade.SENIOR, Season.SPRING,"Design of Experiments",5,3,0),
            new Course("IE4087", Grade.SENIOR, Season.SPRING,"Introduction to Total Quality Management",5,3,0),
            new Course("ME3004", Grade.SENIOR, Season.SPRING,"Mathematical Modeling in Mechanical Engineering",5,3,0),
            new Course("ME3022", Grade.SENIOR, Season.SPRING,"Mechatronics",5,3,0),
            new Course("ME4018", Grade.SENIOR, Season.SPRING,"Introduction to Robotics",5,3,0),
            new Course("ME4022", Grade.SENIOR, Season.SPRING,"Control Systems",5,3,0),
            new Course("ME4033", Grade.SENIOR, Season.SPRING,"Designing Quality Products",5,3,0),
            new Course("ME4051", Grade.SENIOR, Season.SPRING,"Automotive Engineering",5,3,0),
            new Course("MGT4076", Grade.SENIOR, Season.SPRING,"Quality Management System in Automotive Industry",5,3,0),
            new Course("MGT4082", Grade.SENIOR, Season.SPRING,"Advanced innovative Design Management for Entrepreneurship",5,3,0),
            new Course("MGT4084", Grade.SENIOR, Season.SPRING,"Innovation and New Product Development Management",5,3,0),
            new Course("MGT4086", Grade.SENIOR, Season.SPRING,"Managing Engineering and Technology",5,3,0),
            new Course("MGT4088", Grade.SENIOR, Season.SPRING,"Project Management for Engineers",5,3,0),
            new Course("MSE4073", Grade.SENIOR, Season.SPRING,"Materials Engineering Economics and Plant Design",5,3,0),
            new Course("MSE4074", Grade.SENIOR, Season.SPRING,"Total Quality Management in Materials Engineering",5,3,0)
        });

        type = "Non-Technical Elective | University Elective";

        NonTechnicalElective_UniversityElective.AddRange(new Course[]
        {
            new Course("BUS1003", Grade.FRESHMAN, Season.SPRING,"Entrepreneurship and Innovation",3,2,0),
            new Course("BUS1004", Grade.FRESHMAN, Season.SPRING,"Strategic Entrepreneurship",3,2,0),
            new Course("BUS2005", Grade.FRESHMAN, Season.SPRING,"International Business",3,2,0),
            new Course("CAS1001", Grade.FRESHMAN, Season.SPRING,"Self Management",3,2,0),
            new Course("CAS1010", Grade.FRESHMAN, Season.SPRING,"Our Biosphere",3,2,0),
            new Course("CAS1061", Grade.FRESHMAN, Season.SPRING,"Geometric Origami",3,2,0),
            new Course("CAS2001", Grade.FRESHMAN, Season.SPRING,"Energy for Physical Activity",3,2,0),
            new Course("CAS2002", Grade.FRESHMAN, Season.SPRING,"Human Performance and Nutrition",3,2,0),
            new Course("CAS2003", Grade.FRESHMAN, Season.SPRING,"Cardiopulmonary Resuscitation and First Aid Basics",3,2,0),
            new Course("CAS2004", Grade.FRESHMAN, Season.SPRING,"Interpersonal Relations and Communication",3,2,0),
            new Course("CAS2005", Grade.FRESHMAN, Season.SPRING,"Trekking and Orienteering",3,2,0),
            new Course("CAS3001", Grade.FRESHMAN, Season.SPRING,"Earthquake and Rescue Principles",3,2,0),
            new Course("CAS3044", Grade.FRESHMAN, Season.SPRING,"Positive Thinking",3,2,0),
            new Course("COMM2060", Grade.FRESHMAN, Season.SPRING,"Communication Models and Techniques",3,2,0),
            new Course("FNCE3003", Grade.FRESHMAN, Season.SPRING,"Investment Planning",3,2,0),
            new Course("HR1013", Grade.FRESHMAN, Season.SPRING,"Career Development",3,2,0),
            new Course("HR4001", Grade.FRESHMAN, Season.SPRING,"Career Orientation",3,2,0),
            new Course("HSS3002", Grade.FRESHMAN, Season.SPRING,"Ethics in Engineering and Science",3,2,0),
            new Course("IKT1054", Grade.FRESHMAN, Season.SPRING,"Industrial and Economic Sociology",3,2,0),
            new Course("INF1001", Grade.FRESHMAN, Season.SPRING,"Information Literacy in Engineering",3,2,0),
            new Course("LAW2004", Grade.FRESHMAN, Season.SPRING,"Labor Law",3,2,0),
            new Course("MAN1001", Grade.FRESHMAN, Season.SPRING,"Principles of Managerial Economics & Financial Management",3,2,0),
            new Course("MGT1021", Grade.FRESHMAN, Season.SPRING,"Design, Innovation and Entrepreneurship",3,2,0),
            new Course("MGT3082", Grade.FRESHMAN, Season.SPRING,"Creativity And Innovation Management",3,2,0),
            new Course("MRK3082", Grade.FRESHMAN, Season.SPRING,"Integrated Marketing Communication",3,2,0),
            new Course("MRK3084", Grade.FRESHMAN, Season.SPRING,"Brand Creation/Branding",3,2,0),
            new Course("NTE1002", Grade.FRESHMAN, Season.SPRING,"Effects of Human Behaviour on Environment",3,2,0),
            new Course("NTE1003", Grade.FRESHMAN, Season.SPRING,"The Culture of Radiation Safety",3,2,0),
            new Course("NTE1004", Grade.FRESHMAN, Season.SPRING,"World Cultures",3,2,0),
            new Course("NTE1006", Grade.FRESHMAN, Season.SPRING,"Basic Principles of Accessibility in Informatics and Technology",3,2,0),
            new Course("NTE1008", Grade.FRESHMAN, Season.SPRING,"Cinema",3,2,0),
            new Course("NTE1010", Grade.FRESHMAN, Season.SPRING,"Introduction to Finance for Engineers",3,2,0),
            new Course("NTE1011", Grade.FRESHMAN, Season.SPRING,"Business Communication",3,2,0),
            new Course("NTE1012", Grade.FRESHMAN, Season.SPRING,"Introduction to Corporate Governance for Engineers",3,2,0),
            new Course("NTE1013", Grade.FRESHMAN, Season.SPRING,"Business Intelligence for Managers",3,2,0),
            new Course("NTE1014", Grade.FRESHMAN, Season.SPRING,"Introduction to Strategic Human Resources Management",3,2,0),
            new Course("NTE1015", Grade.FRESHMAN, Season.SPRING,"Introduction to Leadership & Management for Engineers",3,2,0),
            new Course("NTE1016", Grade.FRESHMAN, Season.SPRING,"Introduction to Strategy for Engineers",3,2,0),
            new Course("OB2040", Grade.FRESHMAN, Season.SPRING,"Industrial Psychology",3,2,0),
            new Course("OB2041", Grade.FRESHMAN, Season.SPRING,"Behavioral Sciences for Engineers",3,2,0),
            new Course("OB3043", Grade.FRESHMAN, Season.SPRING,"Organizational Behaviour",3,2,0),
            new Course("SOC3082", Grade.FRESHMAN, Season.SPRING,"Environmental Sociology",3,2,0),
            new Course("YDA1001", Grade.FRESHMAN, Season.SPRING,"German for Beginners",3,2,0),
            new Course("YDA1002", Grade.FRESHMAN, Season.SPRING,"German",3,2,0),
            new Course("YDA1003", Grade.FRESHMAN, Season.SPRING,"German Communication",3,2,0),
            new Course("YDA2001", Grade.FRESHMAN, Season.SPRING,"Seminars in German",3,2,0),
            new Course("YDF1001", Grade.FRESHMAN, Season.SPRING,"French for Beginners",3,2,0),
            new Course("YDF1002", Grade.FRESHMAN, Season.SPRING,"French",3,2,0),
            new Course("YDI1004", Grade.FRESHMAN, Season.SPRING,"Report Writing in English",3,2,0),
            new Course("YDIS1001", Grade.FRESHMAN, Season.SPRING,"Spanish for Beginners",3,2,0),
            new Course("YDIS1002", Grade.FRESHMAN, Season.SPRING,"Spanish",3,2,0)
        });

        CSE1242.AddPrerequisite(CSE1241);

        MATH2059.AddPrerequisite(MATH1001);
        CSE2225.AddPrerequisite(CSE1242);
        EE2031.AddPrerequisite(PHYS1102);

        CSE2246.AddPrerequisite(CSE2225);
        CSE2260.AddPrerequisite(CSE1242);
        EE2032.AddPrerequisite(EE2031);

        CSE3055.AddPrerequisite(CSE2225);
        CSE3033.AddPrerequisite(CSE2225);
        CSE3063.AddPrerequisite(CSE1242);
        IE3081.AddPrerequisite(STAT2253);

        CSE3044.AddPrerequisite(CSE3055);
        CSE3264.AddPrerequisite(CSE2023);
        CSE3038.AddPrerequisite(CSE2138);
        CSE3038.AddPrerequisite(CSE3215);
        CSE3048.AddPrerequisite(MATH2055);
        IE3235.AddPrerequisite(MATH2256);

        CSE4219.AddPrerequisite(CSE3038);
        CSE4288.AddPrerequisite(STAT2253);
        CSE4288.AddPrerequisite(MATH2256);

        CSE4075.AddPrerequisite(CSE4074);
        CSE4034.AddPrerequisite(CSE3033);
        CSE4061.AddPrerequisite(CSE3264);
        CSE4217.AddPrerequisite(CSE3038);
    }
}