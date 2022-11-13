[Serializable]
[JsonConverter(typeof(StringEnumConverter))]
public enum Grade
{
    FRESHMAN,
    SOPHOMORE,
    JUNIOR,
    SENIOR
}
