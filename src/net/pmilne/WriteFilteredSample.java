package net.pmilne;

import java.io.*;

@SuppressWarnings("SameParameterValue")
public class WriteFilteredSample {
    public static void main(String[] args) throws FileNotFoundException {
        PrintStream ps = new PrintStream(new BufferedOutputStream(new FileOutputStream("Sample.csv")));
        BaseSamples.gaussian(0, 1000000, 1000)
                .mapToLong(Math::round)
                .filter(l -> Long.bitCount(l) % 2 != 0)
                .limit(100)
                .forEach(ps::println);
    }
}
