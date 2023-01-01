from iteration3.FacultyMember import FacultyMember
class Assistant(FacultyMember):
    def __init__(self, firstName: str, lastName: str):
        super().__init__(firstName, lastName)

    def __init__(self, firstName: str, middleName: str, lastName: str):
        super().__init__(firstName, middleName, lastName)
