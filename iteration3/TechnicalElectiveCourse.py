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

    def __init__(self, code, name, credits, theoreticalHours, appliedHours, firstYearToTake, firstSeasonToTake, lecturers, assistants):
        pass
    
    
    def addPrerequisite(prerequisite):
        pass
    
    
    def canStudentTakeCourse(student):
        pass
    
    def isCreditsRequirementMet(student):
        pass
    
    @staticmethod
    def getTotalNumberOfCoursesUntilSemester(semester):
        pass
    
    def getCoursePriority():
        pass