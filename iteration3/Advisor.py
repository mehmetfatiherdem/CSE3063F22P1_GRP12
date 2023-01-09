from iteration3.HorizontalLineType import HorizontalLineType
from iteration3.Lecturer import Lecturer
from iteration3.Logger import Logger
from iteration3.Section import Section


class Advisor(Lecturer):
    def __init__(self, first_name: str, last_name: str):
        super().__init__(first_name, last_name)
    
    def __init__(self, first_name: str, middle_name: str, last_name: str):
        super().__init__(first_name, middle_name, last_name)

    def examine_registration(self, student):
        Logger.new_line(HorizontalLineType.Dot)
        Logger.new_line()
        Logger.log(f"The advisor {self.get_full_name()} starts examining the registration of {student.get_full_name()}")

        sections = student.get_enrolled_courses()
        collisions = Section.check_for_collisions(sections)

        unaccepted = []

        for c in collisions:
            section1 = c[0]
            section2 = c[1]

            if not self.check_types_of_colliding_sections(section1, section2):
                unaccepted.append(c)
                Logger.log(f"The advisor {self.get_full_name()} does not approve the collision between {section1} and {section2}")

        Logger.new_line()

        if not unaccepted:
            Logger.log(f"The advisor {self.get_full_name()} approves the registration of {student.get_full_name()}")
        else:
            Logger.log(f"The advisor {self.get_full_name()} does not approve the registration of {student.get_full_name()} due to high priority courses having collisions :")

            Logger.increment_indentation()

            for c in unaccepted:
                Logger.log(f"Between {c[0]} and {c[1]}")

            Logger.decrement_indentation()

        Logger.new_line()
        Logger.new_line(HorizontalLineType.Dot)
        Logger.new_line()

        return unaccepted
    
    def check_types_of_colliding_sections(self, section1: Section, section2: Section) -> bool:
        total = section1.get_section_priority() + section2.get_section_priority()
        return total < 5