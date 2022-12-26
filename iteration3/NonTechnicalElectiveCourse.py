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
       pass
    
    def canStudentTakeCourse(student):
        pass
    
    @staticmethod
    def getTotalNumberOfCoursesUntilSemester(semester):
       pass
    
    def getCoursePriority():
        pass