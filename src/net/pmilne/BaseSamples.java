package net.pmilne;

import java.util.Random;
import java.util.stream.DoubleStream;

public class BaseSamples {
    // Return an infinite stream of doubles taken from a uniform distribution over [0 .. 1]
    public static DoubleStream uniform01(long seed) {
        Random gen = new Random(seed);
        return DoubleStream.generate(gen::nextDouble);
    }

    // Return an infinite stream of doubles taken from a Gaussian distribution
    public static DoubleStream gaussian(long seed, double mean, double standardDeviation) {
        Random gen = new Random(seed);
        return DoubleStream.generate(() -> mean + gen.nextGaussian() * standardDeviation);
    }
}
