from enum import Enum

class Grade(Enum):
    FRESHMAN = 0
    SOPHOMORE = 1
    JUNIOR = 2
    SENIOR = 3

    def __init__(self, value):
        self.value = value

    def getValue(self):
        return self.value