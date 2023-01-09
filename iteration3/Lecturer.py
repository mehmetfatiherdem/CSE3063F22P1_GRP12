from iteration3.FacultyMember import FacultyMember


class Lecturer(FacultyMember):
    def __init__(self, first_name: str, last_name: str):
        super().__init__(first_name, last_name)

    def __init__(self, first_name: str, middle_name: str, last_name: str):
        super().__init__(first_name, middle_name, last_name)

