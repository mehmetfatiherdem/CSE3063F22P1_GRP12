class TechnicalElectiveCourse(ElectiveCourse):
    REQUIRED_CREDITS = 155

    numberOfCoursesTakeableBySemester = {
        0: 0,
        1: 0,
        2: 0,
        3: 0,
        4: 0,
        5: 0,
        6: 1,
        7: 3
    }

    def __init__(self, code, name, credits, theoretical_hours, applied_hours, first_year_to_take, first_season_to_take, lecturers, assistants):
        super().__init__(code, name, credits, theoretical_hours, applied_hours, first_year_to_take, first_season_to_take, lecturers, assistants)

    def add_prerequisite(self, prerequisite):
        self.prerequisites.add(prerequisite)

    def can_student_take_course(self, student):
        semester = student.get_student_semester()
        no_of_courses_till_semester = self.get_total_number_of_courses_until_semester(semester)

        return student.get_transcript().get_number_of_te_courses_passed() < no_of_courses_till_semester and not student.did_student_pass(self) and self.is_credits_requirement_met(student)

    def is_credits_requirement_met(self, student):
        return self.REQUIRED_CREDITS <= student.get_completed_credits()

    @staticmethod
    def get_total_number_of_courses_until_semester(semester):
        no_of_courses_till_semester = 0

        for i in range(semester + 1):
            no_of_courses_till_semester += TechnicalElectiveCourse.numberOfCoursesTakeableBySemester[i]

        return no_of_courses_till_semester

    def get_course_priority(self):
        return 2
