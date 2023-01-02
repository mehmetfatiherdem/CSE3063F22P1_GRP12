from iteration3.Section import Section
class CourseSection(Section):
    def __init__(self, course, section_code, class_hours, instructor):
        super().__init__(course, section_code, class_hours, instructor)

    def get_lecturer(self):
        return self.instructor

    def get_section_priority(self):
        return super().get_section_priority() + 1