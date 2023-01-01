class Logger:
    logTxt = "iteration3/output/log.txt"
    unitIndentation = "    "

    enabled = True
    indentation = 0
    ignoreIndentation = False

    cellWidth = 14

    def __init__(self):
        pass

    @staticmethod
    def enable():
        pass
    
    @staticmethod
    def disable():
        pass
    
    @staticmethod
    def clearLogFile():
        pass
    
    @staticmethod
    def decrementIndentation():
        pass
    
    @staticmethod
    def incrementIndentation():
        pass
    
    @staticmethod
    def setIndentation(indent):
        pass
    
    @staticmethod
    def getIndentation():
        pass
    
    @staticmethod
    def getIndentationString():
        pass
    
    @staticmethod
    def newLine():
        pass
    
    @staticmethod
    def newLine(line_type):
        pass

    @staticmethod
    def log(message):
        pass

    @staticmethod
    def logSchedule(schedule):
        pass

    @staticmethod
    def logStudentSchedule(enrolledSections, horizontalLineType, verticalLine):
        pass

    @staticmethod
    def logCourseCodes(frontText, courses):
        pass

    @staticmethod
    def logSimulationEntities():
        pass
    
    @staticmethod
    def addCell(cellText, verticalLine, schedule):
        pass

    @staticmethod
    def appendGap(width, builder):
        pass

    @staticmethod
    def logPeopleNames(people):
        pass

    @staticmethod
    def logCourses(courses):
        pass

    @staticmethod
    def logStudents(students):
        pass

    @staticmethod
    def openLogFile(append):
        pass

    @staticmethod
    def closeLogFile(writer):
        pass
