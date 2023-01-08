from enum import Enum

class Season(Enum):
    FALL = 0
    SPRING = 1
    SUMMER = 2

    def get_value(self):
        return self.value