public static class NameGenerator
{
    static List<string> maleNames;
    static List<string> femaleNames;
    static List<string> unisexNames;
    static List<string> surnames;

    static Random rng;

    const float middleNameChance = 0.27f;
    const float unisexNameChance = 0.11f;
    const float malePersonRatio = 0.5089f;

    static NameGenerator()
    {
        maleNames = new List<string>();
        femaleNames = new List<string>();
        unisexNames = new List<string>();
        surnames = new List<string>();

        rng = new Random();

        using (StreamReader reader = new StreamReader("../../../Resources/Names.txt"))
        {
            string? line;

            while ((line = reader.ReadLine()) is not null)
            {
                string[] nameInfo = Regex.Split(line, "\\s+");

                if (nameInfo.Length != 2)
                    continue;

                switch (nameInfo[1])
                {
                    case "E":
                        maleNames.Add(nameInfo[0]);
                        break;
                    case "K":
                        femaleNames.Add(nameInfo[0]);
                        break;
                    case "U":
                        unisexNames.Add(nameInfo[0]);
                        break;
                }
            }

        }

        using (StreamReader reader = new StreamReader("../../../Resources/Surnames.txt"))
        {
            string? line;

            while ((line = reader.ReadLine()) is not null)
            {
                surnames.Add(line);
            }
        }
    }

    private static string GetName(bool isMale)
    {
        bool isUnisex = rng.NextSingle() <= unisexNameChance;

        List<string> nameList = isUnisex ? unisexNames : isMale ? maleNames : femaleNames;

        return nameList[rng.Next(0, nameList.Count)];
    }

    private static string GetSurname() => surnames[rng.Next(0, surnames.Count)];

    public static (string firstName,string? middleName,string lastName) GenerateName()
    {
        bool isMale = rng.NextSingle() <= malePersonRatio;
        bool hasMiddleName = rng.NextSingle() <= middleNameChance;

        string firstName = GetName(isMale);
        string? middleName = hasMiddleName ? GetName(isMale) : null;
        string lastName = GetSurname();

        return (firstName, middleName, lastName);
    }
}