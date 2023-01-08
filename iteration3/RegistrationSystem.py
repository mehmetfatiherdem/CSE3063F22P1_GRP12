from iteration3.FacultyTechnicalElectiveCourse import FacultyTechnicalElectiveCourse
from iteration3.Department import Department
from iteration3.Section import Section
from iteration3.NonTechnicalElectiveCourse import NonTechnicalElectiveCourse
from iteration3.TechnicalElectiveCourse import TechnicalElectiveCourse
from iteration3.Student import Student
from iteration3.Course import Course

class RegistrationSystem:
    instance = None

    def __init__(self):
        pass

    @classmethod
    def get_instance(cls):
        if not cls.instance:
            cls.instance = RegistrationSystem()
        return cls.instance

    def get_open_mandatory_courses(self,student):
        mandatory_courses = Department.get_instance().get_mandatory_courses()
        return self.get_takeable_courses(mandatory_courses, student)

    def get_open_nte_courses(self,student):
        nte_courses = Department.get_instance().get_non_technical_elective_courses()
        return self.get_takeable_courses(nte_courses, student)

    def get_open_fte_courses(self,student):
        fte_courses = Department.get_instance().get_faculty_technical_elective_courses()
        return self.get_takeable_courses(fte_courses, student)

    def get_open_te_courses(self,student):
        te_courses = Department.get_instance().get_technical_elective_courses()
        return self.get_takeable_courses(te_courses, student)

    def get_the_number_of_te_courses_student_can_take(student):
        if student.get_completed_credits() < TechnicalElectiveCourse.REQUIRED_CREDITS:
            return 0
        no_of_te_passed = student.get_transcript().get_number_of_te_courses_passed()
        no_of_te_required = TechnicalElectiveCourse.get_total_number_of_courses_until_semester(
            student.get_student_semester())
        return no_of_te_required - no_of_te_passed

    def get_the_number_of_nte_courses_student_can_take(student):
        no_of_nte_passed = student.get_transcript().get_number_of_nte_courses_passed()
        no_of_nte_required = NonTechnicalElectiveCourse.get_total_number_of_courses_until_semester(
            student.get_student_semester())
        return no_of_nte_required - no_of_nte_passed

    def get_the_number_of_fte_courses_student_can_take(student):
        no_of_fte_passed = student.get_transcript().get_number_of_fte_courses_passed()
        no_of_fte_required = FacultyTechnicalElectiveCourse.get_total_number_of_courses_until_semester(
            student.get_student_semester())
        return no_of_fte_required - no_of_fte_passed

    def checkEnrolledSections(student):
        collisions = Section.checkForCollisions(student.getEnrolledCourses())

        highlyCollidingSections = []

        print("\n" + "*" * 50 + "\n")

        for c in collisions:
            sec1 = c[0]
            sec2 = c[1]

            collisionsBetween = sec1.getCollisionsWith(sec2)
            noOfCollisions = len(collisionsBetween)

            if noOfCollisions >= 2:
                highlyCollidingSections.append(c)

                print(f"THE COURSES {sec1}, AND {sec2} HAVE A COLLISION OF LENGTH {noOfCollisions} :")
                print("  " * 2, end="")

                for coll in collisionsBetween:
                    print(f"ON {Section.CLASS_DAYS[coll[0]]} AT {Section.CLASS_HOURS[coll[1]]}")

                print("TWO COURSES MUST NOT COLLIDE BY MORE THAN 1 HOUR!")
                continue

            for d in collisionsBetween:
                collisionDay = d[0]
                collisionHour = d[1]

                print(
                    f"WARNING : THERE IS A COLLISION BETWEEN {sec1} AND {sec2} ON {Section.CLASS_DAYS[collisionDay]} AT {Section.CLASS_HOURS[collisionHour]}")

        print("\n")

        if len(highlyCollidingSections) == 0:
            print(f"{student.getFullName()}'S SELECTED COURSES ARE ADEQUATE TO BE SENT TO ADVISOR APPROVAL!")
        else:
            print(f"THERE ARE PREVENTING COLLISIONS AMONG THE COURSES {student.getFullName()} HAS SELECTED :")
            print("  " * 2, end="")

            for coll in highlyCollidingSections:
                print(f"BETWEEN {coll[0]} AND {coll[1]}")

        print("\n" + "*" * 50 + "\n")

        return highlyCollidingSections



    def send_to_advisor_approval(student: Student): ##-> [Tuple[Section, Section]]:
        return student.get_advisor().examine_registration(student)

    def get_takeable_courses(from_: [Course], student: Student) -> [Course]:
        open_courses = []

        for c in from_:
            if c.can_student_take_course(student):
                open_courses.append(c)

        return open_courses