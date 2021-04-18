package net.pmilne;

import java.util.Random;
import java.util.function.IntPredicate;
import java.util.stream.DoubleStream;

/**
 * A simple generator for a sampled distribution. Uses a binary chop to invert the cumulative frequency table, then
 * linear interpolation between the resulting bounds.
 */
public class DataSetGenerator {
    // Generate infinite streams of observations from various models
    private static int[] cumulative(int[] counts) {
        int[] result = new int[counts.length + 1];
        for (int i = 0; i < counts.length; i++) {
            result[i + 1] = counts[i] + result[i];
        }
        return result;
    }

    private static int binarySearch(int[] a, IntPredicate test, boolean returnMin) {
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
        return returnMin ? min : max;
    }

    // Extend a dataset by performing the integration and inversion steps on a finite element model (by interpolation)
    public static DoubleStream generateRandomSamplesFor(Sample sample, int seed) {
        int[] cumulative = cumulative(sample.buckets);
        int   total      = cumulative[cumulative.length - 1];
        return new Random(seed)
                .ints(0, total) // random integers in the range [0 .. total]
                .mapToDouble(randomIndex -> {
                    // Find the interval containing the randomIndex and interpolate between the x values
                    int    minIndex = binarySearch(cumulative, x -> (x >= randomIndex), true);
                    int    maxIndex = binarySearch(cumulative, x -> (x > randomIndex), false);
                    int    cMin     = cumulative[minIndex];
                    int    cMax     = cumulative[maxIndex];
                    double k        = (double) (randomIndex - cMin) / (cMax - cMin);
                    double min0     = sample.minObservationForBucket(minIndex);
                    double max0     = sample.minObservationForBucket(maxIndex);
                    return min0 + k * (max0 - min0);
                });
    }

    public static DoubleStream generateRandomSamplesFor(int bucketCount, double[] observations, int seed) {
        return generateRandomSamplesFor(new Sample(bucketCount, observations), seed);
    }

    public static void main(String[] args) {
        // Generate 10^5 samples from a normal distribution
        double[] observations = DoubleStream
                .generate(new Random(0)::nextGaussian)
                .limit(100000)
                .map(x -> 1000000 + 1000 * x) // mean = 100000, std = 1000
                .toArray();
        // Produce 10^6 observations
        double[] synthetic = generateRandomSamplesFor(1000, observations, 0)
                .limit(1000000)
                .toArray();
        Histogram.show(new Sample(100, synthetic));
    }
}
