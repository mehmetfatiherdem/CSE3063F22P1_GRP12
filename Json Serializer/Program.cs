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
List<Person> assistants = new List<Person>();
List<Person> advisors = new List<Person>();

List<Course> courses = new List<Course>(Course.Mandatory);
courses.AddRange(Course.FacultyTechnicalElective);
courses.AddRange(Course.TechnicalElective);
courses.AddRange(Course.NonTechnicalElective_UniversityElective);

Random rng = new Random();

string coursesFile = $"../../../Output/Courses.json";
string assistantsFile = $"../../../Output/Assistants.json";
string LecturersFile = $"../../../Output/Lecturers.json";
string AdvisorsFile = $"../../../Output/Advisors.json";
string studentsDir = $"../../../Output/Students/";
string semesterFile = $"../../../Output/Semester.json";

for (int i = 0; i < 200; i++)
{
    var name = NameGenerator.GenerateName();
    lecturers.Add(new Person(name.firstName, name.middleName, name.lastName));
}

for (int i = 0; i < 100; i++)
{
    var name = NameGenerator.GenerateName();
    assistants.Add(new Person(name.firstName, name.middleName, name.lastName));
}

for (int i = 0; i < 5; i++)
{
    var name = NameGenerator.GenerateName();
    advisors.Add(new Person(name.firstName, name.middleName, name.lastName));
}

for (int i = 0; i < 300; i++)
{
    var name = NameGenerator.GenerateName();
    students.Add(new Student(name.firstName, name.middleName, name.lastName, advisors[rng.Next(0, 5)].ToString()));
}

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