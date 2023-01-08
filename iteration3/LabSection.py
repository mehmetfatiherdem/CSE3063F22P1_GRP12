from iteration3.Section import Section
class LabSection(Section):
    def __init__(self, course, section_code, class_hours, instructor):
        super().__init__(course, section_code, class_hours, instructor)

    def get_assistant(self):
        return self.instructor