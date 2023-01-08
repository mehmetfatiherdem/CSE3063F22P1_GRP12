import iteration2.src.math_helper
from iteration2.src.human import Assistant, Grade, Lecturer, Student
from iteration2.src.course_section import CourseSection
from iteration2.src.lab_section import LabSection
from typing import List

class Course:
    MIN_QUOTA = 40
    MAX_QUOTA = 80

    def __init__(self, code: str, name: str, credits: int, theoretical_hours: int, applied_hours: int,
                 first_year_to_take: Grade, first_season_to_take: 'Season', lecturers: List[Lecturer], assistants: List[Assistant]):
        self.code = code
        self.name = name
        self.credits = credits
        self.ects = credits
        self.theoretical_hours = theoretical_hours
        self.applied_hours = applied_hours
        self.first_year_to_take = first_year_to_take
        self.first_season_to_take = first_season_to_take

        if theoretical_hours > 0:
            self.lecturers = lecturers

        if applied_hours > 0:
            self.assistants = assistants

        self.quota = math_helper.random_integer_between(MIN_QUOTA, MAX_QUOTA + 1)

    def add_prerequisite(self, prerequisite: 'Course'):
        self.prerequisites.append(prerequisite)

    def add_course_section(self, schedule: int) -> CourseSection:
        if self.theoretical_hours == 0:
            return None

        random_index = math_helper.random_integer_between(0, len(self.lecturers))
        lecturer = self.lecturers[random_index]
        section_code = str(len(self.course_sections) + 1)

        new_section = CourseSection(self, section_code, schedule, lecturer)
        self.course_sections.append(new_section)

        return new_section

    def add_lab_section(self, schedule: int) -> 'LabSection':
        if self.applied_hours == 0:
            return None

        random_index = math_helper.random_integer_between(0, len(self.assistants))
        assistant = self.assistants[random_index]

        section_code = f'{len(self.course_sections)}.{len(self.lab_sections) + 1}'
        new_section = LabSection(self, section_code, schedule, assistant)
        self.lab_sections.append(new_section)

        return new_section

    def is_student_grade_requirement_met(self, student: Student) -> bool:
        return self.get_course_semester() <= student.get_student_semester()

    def can_student_take_course(self, student: Student) -> bool:
        return self.is_student_grade_requirement_met(student) and not student.did_student_pass(self) and student.check_if_prerequisites_are_passed(self)

    def get_available_course_sections(self) -> List[CourseSection]:
        sections = []
        return [s for s in self.course_sections if not s.is_section_full()]

    def get_alternative_sections(self, section: 'Section') -> List['Section']:
        alternatives = []

        if isinstance(section, CourseSection):
            for s in self.course_sections:
                if s != section and not s.is_section_full():
                    alternatives.append(s)
        else:
            for s in self.lab_sections:
                if s != section and not s.is_section_full():
                    alternatives.append(s)

        return alternatives

    def get_code(self) -> str:
        return self.code

    def get_name(self) -> str:
        return self.name

    def get_credits(self) -> int:
        return self.credits

    def get_ects(self) -> int:
        return self.ects

    def get_theoretical_hours(self) -> int:
        return self.theoretical_hours

    def get_applied_hours(self) -> int:
        return self.applied_hours

    def get_first_year_to_take(self) -> Grade:
        return self.first_year_to_take

    def get_first_season_to_take(self) -> 'Season':
        return self.first_season_to_take

    def get_quota(self) -> int:
        return self.quota

    def get_prerequisites(self) -> List['Course']:
        return self.prerequisites

    def get_lecturers(self) -> List[Lecturer]:
        return self.lecturers

    def get_assistants(self) -> List[Assistant]:
        return self.assistants

    def get_course_sections(self) -> List[CourseSection]:
        return self.course_sections

    def get_lab_sections(self) -> List[LabSection]:
        return self.lab_sections

