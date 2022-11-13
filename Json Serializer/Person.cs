[Serializable]
public class Person
{
    public string FirstName { get; set; }
    public string? MiddleName { get; set; }
    public string LastName { get; set; }

    public Person(string firstName, string? middleName, string lastName)
    {
        this.FirstName = firstName;
        this.MiddleName = middleName;
        this.LastName = lastName;
    }

    public override string ToString()
    {
        return FirstName + (MiddleName is null ? string.Empty : " " + MiddleName) + " " +  LastName;
    }
}
