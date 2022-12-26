class Department:
    instance
    
    def __init__ (self):
        self.departmentHead = 'lol'
        pass
    
    def getInstance():
        pass
    
    def initialize(currentSeason, courses, lecturers, assistants, advisors, students):
        pass
    
    def addNewCourseSection(mandatoryCourse):
        pass
    
    def addNewLabSection(course):
        pass
    
    def getDepartmentCode():
        pass
    
    def getDepartmentName():
        pass
    
    def getCurrentSeason():
        pass
    
    def getStudents():
        pass
    
    def getLecturers():
        pass
    
    def getAssistants():
        pass
    
    def getAdvisors():
        pass
    
    def getAllCourses():
        pass
    
    def getMandatoryCourses():
        pass
    
    def getTechnicalElectiveCourses():
        pass
    
    def getFacultyTechnicalElectiveCourses():
        pass
    
    def getNonTechnicalElectiveCourses():
        pass
    
    def getNewSectionSchedule(course, classHours):
        pass
    
    def generateWeeklyScheduleForAllCourses():
        pass
    
    def generateWeeklyScheduleForMandatoryCourses():
        pass
    
    def generateWeeklyScheduleForElectiveCourses():
        pass
    
    def generateCollisionlessWeeklyScheduleForCourses(courses):
        pass
    
    def getDivisions(courses):
        pass
    
    def getDivision(noOfHours):
        pass
    
    def validateDivision(division, noOfHours):
        pass
    
    def assignScheduleToSection(availableClassHours, division):
        pass
    
    def getAvailableDays(schedule, count):
        pass
    
    def getAvailablePositionsOnDay(schedule, dayIndex, count):
        pass
    
    def getScheduleAtPosition(position, count):
        pass