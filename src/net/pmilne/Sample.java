package net.pmilne;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;

import static java.lang.Math.floor;

public class Sample {
    public final int    size;
    public final double min;
    public final double max;
    public final double scale;
    public final int[]  buckets;

    public Sample(int bucketCount, double[] observations) {
        DoubleSummaryStatistics stats = Arrays.stream(observations).summaryStatistics();
        this.size    = observations.length;
        this.min     = stats.getMin();
        this.max     = stats.getMax();
        this.scale   = (max - min) / bucketCount;
        this.buckets = new int[bucketCount];
        // Place the observations in the appropriate buckets
        Arrays.stream(observations).forEach(observation -> buckets[getBucketFor(observation)]++);
    }

    private int getBucketFor(double observation) {
        int result = (int) floor((observation - min) / scale);
        // Special case the max observation and neighbours captured by rounding to solve the fencepost problem
        // that arises when converting from the real range to the integer range. Effectively we are saying that
        // observation max is equivalent to max - epsilon.
        if (result == buckets.length) {
            return result - 1;
        }
        return result;
    }

    public double minObservationForBucket(int bucket) {
        return min + scale * bucket;
    }
}
