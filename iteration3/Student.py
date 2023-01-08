import random
from iteration3.Department import Department
from iteration3.LabSection import LabSection
from iteration3.LectureGrade import LetterGrade
from iteration3.Logger import Logger
from iteration3.ElectiveCourse import ElectiveCourse
from iteration3.HorizontalLineType import HorizontalLineType
from iteration3.Human import Human
from iteration3.MathHelper import MathHelper

from iteration3.Transcript import Transcript


class Student(Human):
    min_fail_chance = 0.0
    max_fail_chance = 1.0
    min_retake_chance = 0.0
    max_retake_chance = 1.0
    min_not_take_chance = 0.0
    max_not_take_chance = 1.0

    def __init__(self, first_name, middle_name, last_name, student_id, grade, advisor, transcript):
        super().__init__(first_name, middle_name, last_name)

        self.student_id = student_id
        self.grade = grade
        self.advisor = advisor
        self.transcript = Transcript(transcript)

        self.fail_chance = MathHelper.random_float_between(Student.min_fail_chance, Student.max_fail_chance)
        self.retake_chance = MathHelper.random_float_between(Student.min_retake_chance, Student.max_retake_chance)
        self.not_take_chance = MathHelper.random_float_between(Student.min_not_take_chance, Student.max_not_take_chance)

    def __init__(self, first_name, last_name, student_id, grade, advisor, transcript):
        self.__init__(first_name, None, last_name, student_id, grade, advisor, transcript)


    def check_if_prerequisites_are_passed(self, course):
        return self.transcript.check_if_prerequisites_are_passed(course)

    def did_student_pass(self, course):
        return self.transcript.did_student_pass(course)
    
    def start_registration(self, open_mandatory_courses, open_te_courses, open_fte_courses, open_nte_courses, no_of_takeable_te_courses, no_of_takeable_fte_courses, no_of_takeable_nte_courses):
        Logger.new_line(HorizontalLineType.EQUALS_SIGN)
        Logger.new_line()
        Logger.log(self.get_full_name() + " starts registering to courses")
        Logger.new_line()

        for c in open_mandatory_courses:
            if not self.student_wants_to_take():
                Logger.log(self.get_full_name() + " does not want to take " + c.get_code() + " in the current semester")
                continue

            self.try_to_register(c, None)

        self.register_to_elective_courses(open_te_courses, no_of_takeable_te_courses, None)
        self.register_to_elective_courses(open_fte_courses, no_of_takeable_fte_courses, None)
        self.register_to_elective_courses(open_nte_courses, no_of_takeable_nte_courses, None)

        self.end_registration(open_te_courses, open_fte_courses, open_nte_courses)


    def register_to_elective_courses(open_courses, no_of_takeable_courses, instead_of):
        last_removed = None

        for i in range(no_of_takeable_courses):
            random_index = MathHelper.random_integer_between(0, len(open_courses))
            course = open_courses[random_index]

            if Transcript.did_student_fail_before(course) and not student_wants_to_retake():
                Logger.log(f"{get_full_name()} does not want to retake the {course.get_code()} which they failed earlier")
                continue
            if not student_wants_to_take():
                continue

            try_to_register(course, instead_of)
            last_removed = open_courses.pop(random_index)

        return last_removed
        
        

    def try_to_register(course, instead_of):
        available_section = False
        course_sections = course.get_course_sections()

        if len(course_sections) == 0:
            Logger.increment_indentation()
            Logger.log(f"(!) THERE ARE NO COURSE SECTIONS AVAILABLE FOR {course.get_code()}")
            Logger.decrement_indentation()

        for s in course_sections:
            if not s.is_section_full():
                enrolled_sections.add(s)

                if instead_of is None:
                    Logger.log(f"{get_full_name()} registers to {s.to_string()}")
                else:
                    Logger.log(f"{get_full_name()} registers to {s.to_string()} in place of the {instead_of.to_string()} which they removed previously")

                available_section = True
                break
            else:
                Logger.log(f"{get_full_name()} tries to register to {s.to_string()}")
                Logger.increment_indentation()
                Logger.log(f"(!) THE QUOTA OF {s.to_string()} IS FULL")
                Logger.decrement_indentation()

        if not available_section:
            course.request_new_course_section()
            available_sections = course.get_available_course_sections()

            if len(available_sections) > 0:
                s = available_sections[0]
                enrolled_sections.add(available_sections[0])

                if instead_of is None:
                    Logger.log(f"{get_full_name()} registers to {s.to_string()}")
                else:
                    Logger.log(f"{get_full_name()} registers to {s.to_string()} in place of the {instead_of.to_string()} which they removed previously")

        available_sections = course.get_available_lab_sections()

        if len(available_sections) > 0:
            s = available_sections[0]
            enrolled_sections.add(s)
            Logger.log(f"{get_full_name()} registers to {s.to_string()}")
        else:
            Logger.increment_indentation()
            Logger.log(f"(!) THERE ARE NO LAB SECTIONS AVAILABLE FOR {course.get_code()}")
            Logger.decrement_indentation()

    
    def student_wants_to_take():
        rand = MathHelper.random_float()
        return rand > not_take_chance

    def student_wants_to_retake():
        rand = MathHelper.random_float()
        return rand <= retake_chance

    def end_registration(open_te_courses, open_fte_courses, open_nte_courses):
        Logger.new_line()
        registration_system_check(open_te_courses, open_fte_courses, open_nte_courses, f"{get_full_name()} ends registering and checks their schedule to see if they can send their registration to advisor approval :")

        advisor_approval(open_te_courses, open_fte_courses, open_nte_courses)
        Logger.new_line()

        Logger.log("THE STUDENT'S REGISTRATION IS COMPLETED!")
        Logger.log("TRANSCRIPT IS BEING UPDATED WITH THE NEWLY REGISTERED COURSES")

        save_to_transcript()

        Logger.log("STUDENT'S TRANSCRIPT IS UPDATED!")
        Logger.log("STUDENT'S SCHEDULE IS BEING CREATED :")
        Logger.new_line()
        Logger.log(f"STUDENT NAME : {get_full_name()}")
        Logger.log(f"STUDENT ID : {get_student_id()}")
        Logger.log(f"ADVISOR NAME : {get_advisor().get_full_name()}")

        Logger.log_student_schedule(enrolled_sections, horizontal_line_type.DASH, '|')
        Logger.new_line(horizontal_line_type.EQUALS_SIGN)

    def registration_system_check(open_te_courses, open_fte_courses, open_nte_courses, pre_check_log):
        def check_collision_callback():
            return registration_system.get_instance().check_enrolled_sections(self)

        Logger.new_line()
        Logger.log(pre_check_log)
        Logger.new_line()

        check_fix_loop(open_te_courses, open_fte_courses, open_nte_courses, check_collision_callback, None, f"{get_full_name()} checks again to see if they can send their registration to advisor approval :")

    def advisor_approval(open_te_courses, open_fte_courses, open_nte_courses):
        def check_collision_callback():
            return registration_system.get_instance().send_to_advisor_approval(self)

        def registration_system_recheck():
         registration_system_check(open_te_courses, open_fte_courses, open_nte_courses, f"{get_full_name()} checks their schedule to see if they can send their registration to advisor approval :")

        Logger.new_line()
        Logger.log(f"{get_full_name()} sends an approval request of their registration to their advisor {advisor.get_full_name()}")
        Logger.new_line()

        check_fix_loop(open_te_courses, open_fte_courses, open_nte_courses, check_collision_callback, registration_system_recheck, f"{get_full_name()} sends another approval request of their registration to their advisor")

    def check_fix_loop(open_te_courses, open_fte_courses, open_nte_courses, collision_check_callback, on_replace_callback, collision_recheck_log):
        unaccepted_collisions = []
        already_tried_sections = []

        while len(unaccepted_collisions := collision_check_callback()) > 0:
            Logger.log(f"{get_full_name()} starts looking into their collision issues")
            Logger.new_line()

            replacement = False

            replacement = handle_unaccepted_collisions(open_te_courses, open_fte_courses, open_nte_courses, unaccepted_collisions, already_tried_sections)

            if replacement and on_replace_callback is not None:
                on_replace_callback()

            Logger.new_line()
            Logger.log(collision_recheck_log)
            Logger.new_line()

    def handle_unaccepted_collisions(self, open_te_courses, open_fte_courses, open_nte_courses, unaccepted_collisions, already_tried_sections):
        any_replacement = False

        for collision in unaccepted_collisions:
            s1 = collision[0]
            s2 = collision[1]

            self.add_if_not_already_contained(already_tried_sections, s1)
            self.add_if_not_already_contained(already_tried_sections, s2)

        for i in range(len(unaccepted_collisions)):
            collision = unaccepted_collisions[i]
            s1 = collision[0]
            s2 = collision[1]

            section_removed = None

            if (section_removed := self.try_to_replace(already_tried_sections, [s1, s2])) is not None:
                self.eliminate_resolved_collisions(unaccepted_collisions, i, section_removed)
                any_replacement = True
                continue

            section_removed = self.remove_either(s1, s2)
            self.eliminate_resolved_collisions(unaccepted_collisions, i, section_removed)

            if not isinstance(section_removed.get_course(), ElectiveCourse):
                continue

            any_replacement |= self.try_to_register_to_another_elective(open_te_courses,open_fte_courses,open_nte_courses,already_tried_sections,section_removed)
        
        return any_replacement

    def eliminate_resolved_collisions(unaccepted_collisions, i, section_removed):
        for j in range(i + 1, len(unaccepted_collisions)):
            collision = unaccepted_collisions[j]
            s1 = collision.get_key()
            s2 = collision.get_value()
            
            if section_removed == s1 or section_removed == s2:
                unaccepted_collisions.remove(j)
                j -= 1

    def try_to_replace(already_tried_sections, sections):
        section_removed = None

        for i in range(2):
            s = sections[i]
            alternative_section = pick_alternative_section(already_tried_sections, s)

            if alternative_section is not None:
                not_removed_before = enrolled_sections.remove(s)

                if not_removed_before:
                    section_removed = s
                    Logger.log(f"{get_full_name()} replaces {s.to_string()} with {alternative_section.to_string()}")
                else:
                    Logger.log(f"{get_full_name()} registers to {alternative_section.to_string()} in place of {s.to_string()} which they removed earlier")

                enrolled_sections.add(alternative_section)
                add_if_not_already_contained(already_tried_sections, alternative_section)
                break

        return section_removed

    def remove_either(s1, s2):
        s1_priority = s1.get_section_priority()
        s2_priority = s2.get_section_priority()
        section_to_remove = None

        if s1_priority == s2_priority:
            if MathHelper.random_integer_between(0, 2) == 0:
                section_to_remove = s1
            else:
                section_to_remove = s2
        else:
            section_to_remove = s1 if s1_priority > s2_priority else s2

        not_removed_before = enrolled_sections.remove(section_to_remove)

        if not_removed_before:
            Logger.log(f"{get_full_name()} removes {section_to_remove}")

        return section_to_remove


    def try_to_register_to_another_elective(open_te_courses, open_fte_courses, open_nte_courses, already_tried_sections, section_removed):
        electives = None
        removed_course = section_removed.get_course()

        if isinstance(removed_course, TechnicalElectiveCourse):
            electives = open_te_courses
        elif isinstance(removed_course, FacultyTechnicalElectiveCourse):
         electives = open_fte_courses
        elif isinstance(removed_course, NonTechnicalElectiveCourse):
            electives = open_nte_courses

        if electives is None or len(electives) == 0:
            return False

        new_registration = register_to_elective_courses(electives, 1, section_removed)

        if new_registration is None:
            return False

        new_section = None
        for s in enrolled_sections:
            if s.get_course() == new_registration:
                new_section = s
                break

        if new_section is None:
            return False

        add_if_not_already_contained(already_tried_sections, new_section)
        return True


    def pick_alternative_section(already_tried_sections, section):
        alternative_sections = section.get_course().get_alternative_sections(section)

        for i, s in enumerate(alternative_sections):
            if i == -1:
                break

            if s in already_tried_sections:
                alternative_sections.remove(i)
                i -= 1

        if len(alternative_sections) == 0:
            return None

        return random.choice(alternative_sections)

    def add_if_not_already_contained(sections, addition):
        if addition not in sections:
            sections.append(addition)

    
    def save_to_transcript(self):
        for s in self.enrolled_sections:
            s.add_to_student_list(self)

            if isinstance(s, LabSection) and s.get_course().get_theoretical_hours() != 0:
                continue

            self.transcript.add_course_record(s.get_course(), LetterGrade.NOT_GRADED,
            Department.get_instance().get_current_season(), None, self.get_grade(), False)


    def get_student_id(self):
        return self.student_id

    def get_grade(self):
        return self.grade

    def get_advisor(self):
        return self.advisor

    def get_enrolled_courses(self):
        return self.enrolled_sections

    def get_completed_credits(self):
        return self.transcript.get_completed_credits()

    def get_transcript(self):
        return self.transcript

    def get_student_semester(self):
        return 2 * self.grade.get_value() + Department.get_instance().get_current_season().get_value()

    def get_fail_chance(self):
        return self.fail_chance

    def set_grade(self, grade):
        self.grade = grade