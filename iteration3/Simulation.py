import random

from iteration3.Department import Department
from iteration3.Grade import Grade
from iteration3.HorizontalLineType import HorizontalLineType
from iteration3.JsonParser import JsonParser
from iteration3.Logger import Logger
from iteration3.RegistrationSystem import RegistrationSystem
from iteration3.Season import Season
from iteration3.Transcript import Transcript


class Simulation:
    @staticmethod
    def main():
        run_simulation(init())
    
    @staticmethod
    def init():
        parser = JsonParser()
        lecturers = parser.parse_lecturers()
        advisors = parser.parse_advisors()
        lecturers.extend(advisors)
        assistants = parser.parse_assistants()
        courses = parser.parse_courses(lecturers, assistants)
        students = parser.parse_students(advisors, courses)
        season = parser.parse_semester()

        department = Department.instance()
        department.initialize(season, courses, lecturers, assistants, advisors, students)

        Logger.clear_log_file()

        return students

    @staticmethod
    def run_simulation(students):
        Logger.log_simulation_entities()

        Logger.new_line()
        Logger.log("THE SIMULATION HAS STARTED!")
        Logger.new_line()

        student_gpas = students.registration_process(students)

        Logger.new_line()
        students.grading_process(students, student_gpas)

        parser = JsonParser()
        parser.serialize_students(students)

        Logger.new_line()
        Logger.log("THE SIMULATION HAS ENDED!")

    @staticmethod
    def registration_process(students):
        system = RegistrationSystem.get_instance()

        Logger.new_line(HorizontalLineType.Star)
        Logger.new_line()
        Logger.log("THE REGISTRATION PROCESS HAS STARTED!")

        student_gpas = []

        for student in students:
            open_mandatory_courses = system.get_open_mandatory_courses(student)
            open_te_courses = system.get_open_te_courses(student)
            open_fte_courses = system.get_open_fte_courses(student)
            open_nte_courses = system.get_open_nte_courses(student)

            no_of_takeable_fte_courses = system.get_the_number_of_fte_courses_student_can_take(student)
            no_of_takeable_te_courses = system.get_the_number_of_te_courses_student_can_take(student)
            no_of_takeable_nte_courses = system.get_the_number_of_nte_courses_student_can_take(student)

            student_name = student.get_full_name()

            Logger.new_line()
            Logger.log("STUDENT INFORMATION :")
            Logger.increment_indentation()

            gpa = student.get_transcript().calculate_gpa()
            student_gpas.append(gpa)

            Logger.log("STUDENT NAME : " + student_name)
            Logger.log("STUDENT ID : " + student.get_student_id())
            Logger.log("STUDENT GRADE : " + student.get_grade().to_string())
            Logger.log("STUDENT GPA : " + round(gpa, 2))
            Logger.log("ADVISOR : " + student.get_advisor().get_full_name())

            Logger.log("COURSES OPENED FOR THE STUDENT :")
            Logger.increment_indentation()
            Logger.log_course_codes("MANDATORY COURSES : ", list(open_mandatory_courses))
            Logger.log_course_codes("TECHNICAL ELECTIVE COURSES : ", list(open_te_courses))
            Logger.log_course_codes("FACULTY TECHNICAL ELECTIVE COURSES : ", list(open_fte_courses))
            Logger.log_course_codes("NON-TECHNICAL ELECTIVE COURSES : ", list(open_nte_courses))
            Logger.decrement_indentation()

            Logger.decrement_indentation()
            Logger.new_line()

            Logger.log("THE REGISTRATION PROCESS OF " + student_name + " HAS STARTED :")
            Logger.new_line()

            student.start_registration(open_mandatory_courses, open_te_courses, open_fte_courses, open_nte_courses,
                                       no_of_takeable_te_courses, no_of_takeable_fte_courses,
                                       no_of_takeable_nte_courses)
        Logger.new_line()
        Logger.log("THE REGISTRATION PROCESS HAS ENDED")
        Logger.new_line()
        Logger.new_line(HorizontalLineType.Star)
        return student_gpas



    @staticmethod
    def grading_process(students, old_gpas):
        # Log that the grading process has started
        Logger.log("THE GRADING PROCESS HAS STARTED!")

        # Get the current semester
        semester = Department.get_instance().get_current_season()

        for i in range(len(students)):
            student = students[i]
            student_old_grade = student.get_grade()
            student_new_grade = Grade.SENIOR

            if semester == Season.SPRING:
                if student_old_grade == Grade.SENIOR:
                    # Remove the student from the list
                    del students[i]
                    i -= 1
                    continue

                # Set the new grade for the student
                student_new_grade = Grade(student_old_grade.value + 1)
                student.set_grade(student_new_grade)

            # Get the list of courses that have not been graded yet
            non_graded_courses = student.get_transcript().get_non_graded_courses()

            for r in non_graded_courses:
                # Generate a random float between 0 and 1
                rand = random.random()

                if rand <= student.get_fail_chance():
                    score = random.uniform(0.0, 39.99)
                    r.set_score(score)
                    r.set_is_passed(False)
                    r.set_l_grade(Transcript.get_letter_grade_of_score(score))
                else:
                    score = random.uniform(40.0, 100.0)
                    r.set_score(score)
                    r.set_is_passed(True)
                    r.set_l_grade(Transcript.get_letter_grade_of_score(score))

            Logger.new_line()
            Logger.new_line(HorizontalLineType.Dash)
            Logger.new_line()
            Logger.log(f"{student.getFullName()} ({student.getStudentID()}) :")

            Logger.increment_indentation()
            Logger.log(f"STUDENT'S NEW GRADE : {student_new_grade.toString()}")
            Logger.log(f"THE GPA AT THE START OF THIS SEMESTER : {round(old_gpas[i], 2)}")
            Logger.log(
                f"THE GPA AT THE END OF THIS SEMESTER : {round(student.getTranscript().calculateGPA(), 2)}")
            Logger.new_line()
            Logger.log("ALL COURSES' DETAILS OF THIS SEMESTER:")
            Logger.increment_indentation()

            for r in non_graded_courses:
                Logger.new_line()
                Logger.log(f"{r.getCourse().getName()} ({r.getCourse().getCode()}) :")

                Logger.increment_indentation()
                Logger.log(f"SCORE : {round(r.get_score(), 0)}")
                Logger.log(f"LETTER GRADE : {r.get_l_grade().toString()}")
                Logger.log(f"STATUS : {'PASSED' if r.get_is_passed() else 'FAILED'}")
                Logger.decrement_indentation()

            Logger.new_line()
            Logger.new_line(HorizontalLineType.Dash)
            Logger.decrement_indentation()
            Logger.decrement_indentation()


