from iteration3 import Department

from iteration3.Human import Human


class FacultyMember(Human):
    def __init__(self, first_name, middle_name=None, last_name=None):
        super().__init__(first_name, middle_name, last_name)
        self.department = Department.get_instance()
