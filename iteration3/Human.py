class Human:
    def __init__(self, first_name, middle_name=None, last_name=None):
        self.first_name = first_name
        self.middle_name = middle_name
        self.last_name = last_name

    def get_full_name(self):
        return self.first_name + ('' if self.middle_name is None or len(self.middle_name) <= 0 else ' ' + self.middle_name) + ' ' + self.last_name

    def get_first_name(self):
        return self.first_name

    def get_middle_name(self):
        return self.middle_name

    def get_last_name(self):
        return self.last_name
