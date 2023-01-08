from iteration3.src import Department

class FacultyMember(Human):
    def __init__(self, first_name, middle_name=None, last_name=None):
        super().__init__(first_name, middle_name, last_name)
        self.department = Department.get_instance()
