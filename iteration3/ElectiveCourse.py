import abc
from typing import List

from Course import Course
from Human import Assistant
from Human import Grade
from Human import Lecturer
from Human import Student

class ElectiveCourse(Course, metaclass=abc.ABCMeta):
    def __init__(self, code, name, credits, theoretical_hours, applied_hours, first_year_to_take, first_season_to_take, lecturers, assistants):
        super().__init__(code, name, credits, theoretical_hours, applied_hours, first_year_to_take, first_season_to_take, lecturers, assistants)

    @abc.abstractmethod
    def add_prerequisite(self, prerequisite):
        pass
