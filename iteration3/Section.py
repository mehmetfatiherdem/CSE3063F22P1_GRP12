class Section:
    noOfWeeklyClassHours = 56
    classDays = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
    classHours = ['8.30-9.20', '9.30-10.20', '10.30-11.20', '11.30-12.20',
            '13.00-13.50', '14.00-14.50', '15.00-15.50', '16.00-16.50']
    
    def __init__ (course, sectionCode , classSchedule, instructor):
        pass
    
    def getCollisionsWith(other):
        pass
    
    @staticmethod(checkForCollisions)
    def checkForCollisions(classes):
        pass
    
    @staticmethod(combineSchedules)
    def combineSchedules(sections):
        pass
    
    @staticmethod(checkCollisionBetween)
    def checkCollisionBetween(sch1, sch2):
        pass
    
    def traverseBits(bitmask, setBitCallBack):
        pass
    
    def addToStudentList(student):
        pass
    
    def isSectionFull():
        pass
    
    def toString():
        pass
    
    def getSectionPriority():
        pass
    
    def getCourse():
        pass
    
    def getClassSchedule():
        pass