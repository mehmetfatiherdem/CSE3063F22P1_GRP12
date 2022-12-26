class Student:
    def __init__(self):
        pass

    def checkIfPrerequisitesArePassed(course):
        pass

    def didStudentPass(course):
        pass

    def startRegistration(openMandatoryCourses, openTECourses):
        pass

    def registerToElectiveCourses(openCourses, noOfTakeableCourses, insteadOf):
        pass

    def tryToRegister(course, insteadOf):
        pass
    
    def studentWantsToTake():
        pass

    def studentWantsToRetake():
        pass

    def endRegistration(openTECourses, openFTECourses, openNTECourses):
        pass

    def registrationSystemCheck(openTECourses, openFTECourses, openNTECourses, preCheckLog):
        pass

    def advisorApproval(openTECourses, openFTECourses, openNTECourses):
        pass

    def checkFixLoop(openTECourses, openFTECourses, openNTECourses, collisionCheckCallback, onReplaceCallback, collisionRecheckLog):
        pass

    def handleUnacceptedCollisions(openTECourses, openFTECourses, openNTECourses, unacceptedCollisions, alreadyTriedSections):
        pass

    def eliminateResolvedCollisions(unacceptedCollisions, i, sectionRemoved):
        pass

    def tryToReplace(alreadyTriedSections, sections):
        pass

    def removeEither(s1, s2):
        pass

    def tryToRegisterToAnotherElective(openTECourses, openFTECourses, openNTECourses, alreadyTriedSections, sectionRemoved):
        pass

    def pickAlternativeSection(alreadyTriedSections, section):
        pass

    def addIfNotAlreadyContained(sections, addition):
        pass
    
    def saveToTranscript():
        pass

    def getStudentID():
        pass

    def getGrade():
        pass

    def getAdvisor():
        pass

    def getEnrolledCourses():
        pass

    def getCompletedCredits():
        pass

    def getTranscript():
        pass

    def getStudentSemester():
        pass

    def getFailChance():
        pass

    def setGrade(grade):
        pass