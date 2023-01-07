from iteration3.Course import Course

from iteration3.Department import Department
from iteration3.Assistant import Assistant
from iteration3.Grade import Grade
from iteration3.Lecturer import Lecturer
from iteration3.Student import Student


class MandatoryCourse(Course):
    def init(self, code, name, credits, theoretical_hours, applied_hours, first_year_to_take,
                 first_season_to_take, lecturers, assistants):
        super().init(code, name, credits, theoretical_hours, applied_hours, first_year_to_take,
                         first_season_to_take, lecturers, assistants)

    def get_available_lab_sections(self):
        sections = super().get_available_lab_sections()

        if not sections and self.applied_hours > 0:
            Department.get_instance().add_new_lab_section(self)
            sections = super().get_available_lab_sections()

        return sections

    def request_new_course_section(self):
        Department.getInstance().addNewCourseSection(self)

    def get_course_priority(self):
        return 3