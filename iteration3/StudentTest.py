import unittest

from iteration3.Advisor import *
from iteration3.LetterGrade import *
from iteration3.Transcript import *
from iteration3.Season import *

from iteration3.CourseSection import *
from iteration3.MandatoryCourse import *
from iteration3.Lecturer import *
from iteration3.Assistant import *
from iteration3.CourseRecor import *

from iteration3.Section import *
from iteration3.Logger import *


class StudentTest(unittest.TestCase):
    def setUp(self):
        self.records = []
        self.student = Student("Ahmet", "Şahin ", "150115655", Grade.FRESHMAN, Advisor("Mustafa", "Ağaoğlu"),
                               self.records)
        self.transcript = Transcript(self.records)
        self.courseSection = []
        self.assistants = []
        self.lecturers = []

        self.lecturers.append(Lecturer("Borahan", "TÜMER"))
        self.lecturers.append(Lecturer("Sanem", "ARSLAN"))
        self.assistants.append(Assistant("Birol", "GENÇYILMAZ"))
        self.records.append(CourseRecord(
            MandatoryCourse("CSE 2025", "Data Structures", 8, 3, 2, Grade.JUNIOR, Season.FALL, self.lecturers,
                            self.assistants), LetterGrade.AA, Season.FALL, Grade.FRESHMAN, 90, True))
        self.records.append(CourseRecord(
            MandatoryCourse("MATH2055", "Differential Equations", 4, 3, 0, Grade.JUNIOR, Season.FALL, self.lecturers,
                            self.assistants), LetterGrade.AA, Season.FALL, Grade.FRESHMAN, 90, True))
        self.courseSection.append(CourseSection(
            MandatoryCourse("MATH2055", "Differential Equations", 4, 3, 0, Grade.JUNIOR, Season.FALL, self.lecturers,
                            self.assistants), "2", 3, self.lecturers[0]))
        self.student.get_enrolled_courses().add(self.courseSection[0])

        print()
        print("Student test starting...")
        print()

    def test_check_if_prerequisites_are_passed(self):
        self.assertEqual(True, self.transcript.check_if_prerequisites_are_passed(self.records[0].course()))

    def test_did_student_pass(self):
        for record in self.records:
            self.assertEqual(True, record.isPassed())

    def test_enroll_course_sections(self):
        for s in self.courseSection:
            s.add_to_student_list(self.student)

            self.transcript.add_course_record(s.course(), LetterGrade.NOT_GRADED, Season.FALL, None,
                                            self.student.get_grade(), False)
            self.assertEqual(True, True)

    def test_add_to_registration_list(self):
        if self.courseSection[0].is_section_full():
            Logger.log("This section of " + self.courseSection[0].course().getCode() + " is already full")
            return
        self.student.get_enrolled_courses().add(self.courseSection[0])
        self.assertEqual(self.courseSection[0], self.student.get_enrolled_courses()[1])

    def test_register(self):
        # will be added after refactoring
        pass

    def test_generate_weekly_schedule(self):
        program = self.student.getFullName() + "'s Weekly Schedule\n"

        if len(self.student.get_enrolled_courses()) == 0:
            program = "The student " + self.student.getFullName() + " doesn't have any enrolled courses"

        schedule = Section.combine_schedules(self.student.get_enrolled_courses())

        for i in range(len(schedule)):
            program += Section.classDays[i] + ": "

            day = schedule[i]

            for j in range(len(schedule[0])):
                sec = day[j]

                if sec is None:
                    pass

                program += sec.getCourse().getCode()

                program += "(" + Section.classHours[j % 8] + ") "

            program += "\n"

        self.assertEqual(program,
                         "Ahmet Şahin 's Weekly Schedule\nMonday: MATH2055(8.30-9.20) MATH2055(9.30-10.20) \nTuesday: \nWednesday: \nThursday: \nFriday: \nSaturday: \nSunday: \n")

    def test_get_student_id(self):
        self.assertEqual("150115655", self.student.get_student_id())

    def test_get_grade(self):
        self.assertEqual(Grade.FRESHMAN, self.student.get_grade())

    def test_get_advisor(self):
        self.assertEqual("Mustafa Ağaoğlu", self.student.get_advisor().getFullName())

    def test_get_course_code(self):
        for record in self.records:
            self.assertEqual("CSE 2025", record.course().getCode())

    def test_get_course_name(self):
        for record in self.records:
            self.assertEqual("Data Structures", record.course().getName())

    def test_get_course_credit(self):
        for record in self.records:
            self.assertEqual(8, record.course().getCredits())

    def test_get_theoretical_hours(self):
        for record in self.records:
            self.assertEqual(3, record.course().getTheoreticalHours())

    def test_get_applied_hours(self):
        for record in self.records:
            self.assertEqual(2, record.course().getAppliedHours())

    def test_get_first_year_to_take(self):
        for record in self.records:
            self.assertEqual(Grade.JUNIOR, record.course().getFirstYearToTake())

    def test_get_l_grade(self):
        for record in self.records:
            self.assertEqual(LetterGrade.AA, record.lGrade())

    def test_get_season(self):
        for record in self.records:
            self.assertEqual(Season.FALL, record.season())

    def test_get_score(self):
        for record in self.records:
            self.assertEqual(90, record.score())

    def test_get_is_passed(self):
        for record in self.records:
            self.assertEqual(True, record.isPassed())

    def test_get_completed_credits(self):
        completed_credits = 0
        for r in self.records:
            if r.isPassed():
                completed_credits += r.course().getCredits()
        self.assertEqual(12, completed_credits)

    def test_get_transcript(self):
        # Since I created a new transcript object, I compared the inside of transcripts
        self.assertIs(self.transcript.get_taken_course_records()()[0],
                      self.student.get_transcript().get_taken_course_records()[0])
