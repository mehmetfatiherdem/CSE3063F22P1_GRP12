[Serializable]
[JsonConverter(typeof(StringEnumConverter))]
public enum LetterGrade
{
    AA = 85,
    BA = 80,
    BB = 70,
    CB = 65,
    CC = 55,
    DC = 50,
    DD = 45,
    FD = 35,
    FF = 30,
}