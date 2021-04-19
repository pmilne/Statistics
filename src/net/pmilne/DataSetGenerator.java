package net.pmilne;

import java.util.Random;
import java.util.function.IntPredicate;
import java.util.stream.DoubleStream;

/**
 * A simple generator for a sampled distribution. Uses a binary chop to invert the cumulative frequency table, then
 * linear interpolation between the resulting bounds.
 */
public class DataSetGenerator {
    // Return c, the cumulative sample of a, where c.length = a.length + 1 and c[0] == 0.
    private static int[] cumulative(int[] a) {
        int[] result = new int[a.length + 1];
        for (int i = 0; i < a.length; i++) {
            result[i + 1] = result[i] + a[i];
        }
        return result;
    }


    // Return the index of the last element for which the test is false.
    // The predicate, test, must satisfy !test(x) => !test(x-1) and test(x) => test(x+1).
    private static int binarySearch(int[] a, IntPredicate test) {
        int min = 0;
        int max = a.length - 1;
        while (max - min > 1) {
            int mid = (min + max) >>> 1;
            if (test.test(a[mid])) {
                max = mid;
            } else {
                min = mid;
            }
        }
        return min;
    }

    // Extend a dataset by performing the integration and inversion steps on a Sample --
    // using binary chop and interpolation on the cumulative frequency function.
    public static DoubleStream generateRandomSamplesFor(Sample sample, int seed) {
        int[] cumulative = cumulative(sample.buckets);
        assert cumulative[cumulative.length - 1] == sample.size;
        int total = sample.size;
        return new Random(seed)
                .doubles(0, total) // random doubles in range [0 .. total] (excluding total)
                .map(randomY -> {
                    // Find the interval containing randomY and interpolate between the corresponding x values
                    int    minIndex = binarySearch(cumulative, y -> (y >= randomY));
                    int    maxIndex = minIndex + 1;
                    int    yMin     = cumulative[minIndex];
                    int    yMax     = cumulative[maxIndex];
                    double k        = (randomY - yMin) / (yMax - yMin);
                    double xMin     = sample.minObservationForBucket(minIndex);
                    double xMax     = sample.minObservationForBucket(maxIndex);
                    return xMin + k * (xMax - xMin);
                });
    }

    public static DoubleStream generateRandomSamplesFor(int bucketCount, double[] observations, int seed) {
        return generateRandomSamplesFor(new Sample(bucketCount, observations), seed);
    }

    public static void main(String[] args) {
        // Generate 10^5 observations of a normal distribution
        double[] observations = DoubleStream
                .generate(new Random(0)::nextGaussian)
                .limit(100000)
                .map(x -> 1000000 + 1000 * x) // mean = 100000, std = 1000
                .toArray();
        // Extend to 10^6 observations
        long time = System.currentTimeMillis();
        double[] synthetic = generateRandomSamplesFor(1000, observations, 0)
                .limit(1000000)
                .toArray();
        System.out.println("Generation time: " + (System.currentTimeMillis() - time) / 1000.0 + "s");
        Histogram.show(new Sample(100, synthetic));
    }
}
