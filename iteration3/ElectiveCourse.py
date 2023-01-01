from abc import ABC
import Course

class ElectiveCourse(Course, ABC):
    def __init__(self, code: str, name: str, credits: int, theoretical_hours: int, applied_hours: int, first_year_to_take: Grade, first_season_to_take: Season, lecturers: List[Lecturer], assistants: List[Assistant]):
        super().__init__(code, name, credits, theoretical_hours, applied_hours, first_year_to_take, first_season_to_take, lecturers, assistants)
    
    def add_prerequisite(self, prerequisite: Course):
        return