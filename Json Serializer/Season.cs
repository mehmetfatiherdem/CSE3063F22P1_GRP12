[Serializable]
[JsonConverter(typeof(StringEnumConverter))]
public enum Season
{
    FALL,
    SPRING,
    SUMMER
}