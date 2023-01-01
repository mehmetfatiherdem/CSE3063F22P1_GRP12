class NonTechnicalElectiveCourse(ElectiveCourse):
    numberOfCoursesTakeableBySemester = {
        0: 0,
        1: 1,
        2: 0,
        3: 0,
        4: 0,
        5: 0,
        6: 1,
        7: 1
    }

    def __init__(self, code, name, credits, theoreticalHours, appliedHours, firstYearToTake, firstSeasonToTake, lecturers, assistants):
       super().__init__(code, name, credits, theoreticalHours, appliedHours, firstYearToTake, firstSeasonToTake, lecturers, assistants)
    
    def canStudentTakeCourse(self, student):
        semester = student.get_student_semester()
        no_of_courses_till_semester = self.get_total_number_of_courses_until_semester(semester)
        return student.get_transcript().get_number_of_nte_courses_passed() < no_of_courses_till_semester \
            and not student.did_student_pass(self)
    
    @staticmethod
    def getTotalNumberOfCoursesUntilSemester(semester):
        no_of_courses_till_semester = 0
        for i in range(semester + 1):
            no_of_courses_till_semester += NonTechnicalElectiveCourse.number_of_courses_takeable_by_semester[i]
        return no_of_courses_till_semester
    
    def getCoursePriority():
        return 0