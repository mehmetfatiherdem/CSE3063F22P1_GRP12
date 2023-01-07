import abc

class Section(abc.ABC):
    NO_OF_WEEKLY_CLASS_HOURS = 56
    classDays = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday']
    classHours = ['8.30-9.20', '9.30-10.20', '10.30-11.20', '11.30-12.20',
                  '13.00-13.50', '14.00-14.50', '15.00-15.50', '16.00-16.50']

    def __init__(self, course, section_code, class_schedule, instructor):
        self.course = course
        self.class_schedule = class_schedule
        self.instructor = instructor
        self.section_code = section_code

    def get_collisions_with(self, other):
        collisions = []

        # Gets the bits that are set in both schedules (collision hours)
        collision_detector = self.class_schedule & other.class_schedule

        # The callback for calculating the day and hour of a collision and adding it to the list
        def collision_callback(collisions, i):
            collisions.append(Tuple(i // 8, i % 8))

        # Calls the collision_callback for each set bit (1) in the collision_detector with their positions from left to right
        self.traverse_bits(collision_detector, collision_callback, collisions)

        return collisions

    @staticmethod
    def check_for_collisions(classes):
        # A list of all the sections that are combined, this helps with finding which two (or more) sections cause a collision
        combined_sections = []
        collisions = []

        # The bitmask representation of the combined schedule
        combined_schedule = 0

        for sec in classes:
            if classes.check_collision_between(combined_schedule, sec.class_schedule):
                # If there's a collision between a schedule and the combined bitmask, then check all the combined sections to find the collision(s) between sections
                for s in combined_sections:
                    if classes.check_collision_between(s.class_schedule, sec.class_schedule):
                        collisions.append(Tuple(sec, s))

            # Adds the schedule to the combined_schedule
            combined_schedule |= sec.class_schedule
            combined_sections.append(sec)

        return collisions

    @staticmethod
    def combine_schedules(sections):
        schedule = []

        # Initialize the list and the arrays
        for i in range(7):
            day = [None] * 8
            schedule.append(day)

        for sec in sections:
            # The callback for calculating the position of a class hour in the bitmask and assigning it in the corresponding position in the schedule
            def combine_callback(schedule, i):
                schedule[i // 8][i % 8] = sec

            # Calls the combine_callback for each set bit (1) in the class schedule of each section in the sections list
            sections.traverse_bits(sec.class_schedule, combine_callback, schedule)

        return schedule

    @staticmethod
    def check_collision_between(sch1, sch2):
        return (sch1 & sch2) != 0

    @staticmethod
    def traverse_bits(bitmask, set_bit_callback):
        for i in range(Section.NO_OF_WEEKLY_CLASS_HOURS):
            # If the rightmost bit is 1
            if (bitmask & 1) == 1:
                set_bit_callback(i)

            # Right shift the bitmask and assign it to bitmask again
            bitmask >>= 1

            if bitmask == 0:
                return

    def add_to_student_list(self, student):
        self.student_list.append(student)

    def is_section_full(self):
        return self.course.get_quota() <= len(self.student_list)

    def to_string(self):
        return self.course.get_code() + "." + self.section_code

    def get_section_priority(self):
        return self.course.get_course_priority()

    def get_course(self):
        return self.course

    def get_class_schedule(self):
        return self.class_schedule
    