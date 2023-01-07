import abc
from typing import List

from iteration3.Course import Course
from iteration3.Assistant import Assistant
from iteration3.Grade import Grade
from iteration3.Lecturer import Lecturer
from iteration3.Student import Student

class ElectiveCourse(Course):
    def __init__(self, code, name, credits, theoretical_hours, applied_hours, first_year_to_take, first_season_to_take, lecturers, assistants):
        super().__init__(code, name, credits, theoretical_hours, applied_hours, first_year_to_take, first_season_to_take, lecturers, assistants)

    @abc.abstractmethod
    def add_prerequisite(self, prerequisite):
        pass
