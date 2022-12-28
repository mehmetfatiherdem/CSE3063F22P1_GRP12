from abc import ABC, abstractmethod

class Course(ABC):
    minQuota = 40
    maxQuota = 80
    
    def __init__(self, code, name, credits, theoreticalHours, appliedHours, firstYearToTake, firstSeasonToTake, lecturers, assistants):
        self.code = code
        self.name = name
        self.credits = credits
        self.ects = credits
        self.theoreticalHours = theoreticalHours
        self.appliedHours = appliedHours
        self.firstYearToTake = firstYearToTake
        self.firstSeasonToTake = firstSeasonToTake

        if theoreticalHours > 0:
            self.lecturers = lecturers

        if appliedHours > 0:
            self.assistants = assistants

        self.quota = MathHelper.randomIntegerBetween(minQuota, maxQuota + 1)
        # Each and every semester, at least one section of all mandatory courses should be registrable without any collision

    def addPrerequisite(self, prerequisite):
        self.prerequisites.append(prerequisite)

    @abstractmethod
    def addCourseSection(self, schedule):
        pass