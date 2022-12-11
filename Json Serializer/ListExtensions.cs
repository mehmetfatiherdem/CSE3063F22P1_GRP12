using System;
using System.Collections.Generic;

namespace ExtensionMethods
{
    public static class ListExtensions
    {
        public static void Shuffle<T>(this List<T> list)
        {
            if (list.Count == 0)
                return;

            int shuffleCount = list.Count / 2;

            if (shuffleCount == 0)
                shuffleCount = 1;

            int count = list.Count;

            int minShuffleAmount = 1;
            int maxShuffleAmount = (int)Math.Round(count * 0.05f);

            if (maxShuffleAmount == 0)
                maxShuffleAmount = 1;

            Random rng = new Random();

            for (int i = 0; i < shuffleCount; i++)
            {
                int shuffleAmount = rng.Next(minShuffleAmount,maxShuffleAmount);
                int temporaryCount = count - shuffleAmount;
                int removeStartIndex = rng.Next(temporaryCount);
                int insertStartIndex = rng.Next(temporaryCount);

                var shuffleList = list.GetRange(removeStartIndex, shuffleAmount);
                list.RemoveRange(removeStartIndex,shuffleAmount);
                list.AddRange(shuffleList);
            }
        }
    }
}
