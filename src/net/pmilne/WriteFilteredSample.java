package net.pmilne;

import java.io.*;
import java.util.Random;
import java.util.stream.DoubleStream;

public class WriteFilteredSample {
    public static void main(String[] args) throws FileNotFoundException {
        PrintStream ps  = new PrintStream(new BufferedOutputStream(new FileOutputStream("Sample.csv")));
        Random      gen = new Random(0);
        DoubleStream.generate(gen::nextGaussian)
                .map(x -> 1000000 + 1000 * x) // mean = 1000000, std = 1000
                .mapToLong(Math::round)
                .filter(l -> Long.bitCount(l) % 2 != 0)
                .limit(100)
                .forEach(ps::println);
        ps.close();
    }
}
