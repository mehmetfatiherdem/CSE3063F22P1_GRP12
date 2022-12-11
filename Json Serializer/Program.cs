global using System;
global using System.Text;
global using System.Text.RegularExpressions;
global using Newtonsoft.Json;
global using Newtonsoft.Json.Converters;

/*
 * This program is neither meant to be efficient nor structurally fine.
 * This is just a quick written program for generating random student, course and lecturer data in json format.
 */

List<Student> students = new List<Student>();
List<Person> lecturers = new List<Person>();
List<Person> assistants;
List<Person> advisors;

List<Course> courses = new List<Course>(Course.Mandatory); //44
courses.AddRange(Course.FacultyTechnicalElective); //26
courses.AddRange(Course.TechnicalElective); //29
courses.AddRange(Course.NonTechnicalElective_UniversityElective); //51

Random rng = new Random();

string coursesFile = @"../../../Output/Courses.json";
string assistantsFile = @"../../../Output/Assistants.json";
string LecturersFile = @"../../../Output/Lecturers.json";
string AdvisorsFile = @"../../../Output/Advisors.json";
string studentsDir = @"../../../Output/Students/";
string semesterFile = @"../../../Output/Semester.json";
string studentConfigFile = @"../../../Output/StudentConfig.json";

for (int i = 0; i < 115; i++)
{
    var name = NameGenerator.GenerateName();
    lecturers.Add(new Person(name.firstName, name.middleName, name.lastName));
}

assistants = new List<Person>()
{
    new Person("Birol",null,"GENÇYILMAZ"),
    new Person("Lokman",null,"ALTIN"),
    new Person("Muhammed","Nur","AVCİL"),
    new Person("Serap",null,"KORKMAZ"),
    new Person("Zuhal",null,"ALTUNTAŞ"),
    new Person("Zuhal",null,"ÖZTÜRK"),
    new Person("Kübra",null,"ULUDAĞ")
};

advisors = new List<Person>()
{
    new Person("Haluk","Rahmi","TOPÇUOĞLU"),
    new Person("Çiğdem","Eroğlu","ERDEM"),
    new Person("Ali","Fuat","ALKAYA"),
    new Person("Murat","Can","GANİZ"),
    new Person("Müjdat",null,"SOYTÜRK"),
    new Person("Borahan",null,"TÜMER"),
    new Person("Mustafa",null,"AĞAOĞLU"),
    new Person("Mehmet",null,"BARAN"),
    new Person("Betül","Demiröz","BOZ"),
    new Person("Fatma","Corut","ERGİN"),
    new Person("Ömer",null,"KORÇAK"),
    new Person("Ali","Haydar","ÖZER"),
    new Person("Sanem",null,"ARSLAN")
};

{
    List<Person> mandatoryLecturers = new List<Person>(advisors);
    mandatoryLecturers.AddRange(lecturers.GetRange(0, 9));
    List<Person> otherLecturers = lecturers.GetRange(9, lecturers.Count - 9);
    Course.AssignFacultyMembersToCourses(mandatoryLecturers, otherLecturers, assistants);
}

for (int i = 0; i < 500; i++)
{
    var name = NameGenerator.GenerateName();
    students.Add(new Student(name.firstName, name.middleName, name.lastName, advisors[rng.Next(0, advisors.Count)].ToString()));
}

DirectoryInfo d = new DirectoryInfo(studentsDir);
FileInfo[] Files = d.GetFiles("*.json");

foreach (FileInfo file in Files)
    File.Delete(file.FullName);

File.Delete(semesterFile);
File.Delete(studentConfigFile);
File.Delete(AdvisorsFile);
File.Delete(LecturersFile);
File.Delete(coursesFile);
File.Delete(assistantsFile);

foreach (var student in students)
{
    string json = JsonConvert.SerializeObject(student, Formatting.Indented);
    Stream stream = File.Open(studentsDir + student.ID + ".json", FileMode.OpenOrCreate);

    using (StreamWriter w = new StreamWriter(stream))
    {
        w.Write(json);
    }

    stream.Close();
}

Serialize(Student.GetStudentStatistics(), studentConfigFile);
Serialize((Season)Student.CurrentSemester, semesterFile);
Serialize(courses, coursesFile);
Serialize(assistants, assistantsFile);
Serialize(lecturers, LecturersFile);
Serialize(advisors, AdvisorsFile);

void Serialize(object? obj, string path)
{
    string json = JsonConvert.SerializeObject(obj, Formatting.Indented);
    Stream stream = File.Open(path, FileMode.OpenOrCreate);

    using (StreamWriter w = new StreamWriter(stream))
    {
        w.Write(json);
    }

    stream.Close();
}

Console.WriteLine("Done");
Console.ReadLine();