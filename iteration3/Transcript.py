from iteration3.CourseRecor import CourseRecord
from iteration3.LectureGrade import LetterGrade


class Transcript:
    def __init__(self, taken_course_records):
        self.taken_course_records = taken_course_records
        self.calculate_gpa()
    
    def add_course_record(self, course, l_grade, season, score, grade, is_passed):
        course_record = CourseRecord(course, l_grade, season, grade, score, is_passed)
        self.taken_course_records.append(course_record)
        
    def calculate_gpa(self):
        gpa = 0
        credits = 0
        temp = 0
        for r in self.taken_course_records:
            temp += r.l_grade.num_val * r.course.credits
            credits += r.course.credits
        gpa = temp / credits
        return gpa
    
    def get_completed_credits(self):
        completed_credits = 0
        for r in self.taken_course_records:
            if r.is_passed:
                completed_credits += r.course.credits
        return completed_credits

    def check_if_prerequisites_are_passed(self, course):
        for c in course.prerequisites:
            if not self.did_student_pass(c):
                return False
        return True

    def did_student_pass(self, course):
        passed = True
        most_recent = None
        for record in self.taken_course_records:
            if record.course == course:
                most_recent = record
        if most_recent is None:
            return False
        return most_recent.is_passed

    def get_taken_course_records(self):
        return self.taken_course_records

    def get_non_graded_courses(self):
        course_record_list = []
        for r in self.taken_course_records:
            if r.l_grade == LetterGrade.NOT_GRADED:
                course_record_list.append(r)
        return course_record_list

    @staticmethod
    def get_letter_grade_of_score(score):
        if score >= 0.0 and score < 35.0:
            return LetterGrade.FF
        elif score >= 35.0 and score < 40.0:
            return LetterGrade.FD
        elif score >= 40.0 and score < 50.0:
            return LetterGrade.DD
        elif score >= 50.0 and score < 65.0:
            return LetterGrade.DC
        elif score >= 65.0 and score < 75.0:
            return LetterGrade.CC
        elif score >= 75.0 and score < 80.0:
            return LetterGrade.CB
        elif score >= 80.0 and score < 85.0:
            return LetterGrade.BB
        elif score >= 85.0 and score < 90.0:
            return LetterGrade.BA
        else:
            return LetterGrade.AA


    def get_number_of_te_courses_passed(self):
        i = 0
        for r in self.taken_course_records:
            course = r.course
            if isinstance(course, TechnicalElectiveCourse) and self.did_student_pass(course):
                i += 1
        return i

    def get_number_of_fte_courses_passed(self):
        i = 0
        for r in self.taken_course_records:
            course = r.course
            if isinstance(course, FacultyTechnicalElectiveCourse) and self.did_student_pass(course):
                i += 1
        return i

    def get_number_of_nte_courses_passed(self):
        i = 0
        for r in self.taken_course_records:
            course = r.course
            if isinstance(course, NonTechnicalElectiveCourse) and self.did_student_pass(course):
                i += 1
        return i

    def did_student_fail_before(self, course):
        did_fail = False
        for r in self.taken_course_records:
            if r.course == course:
                did_fail = not r.is_passed
        return did_fail