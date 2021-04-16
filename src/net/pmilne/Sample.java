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
        this.scale   = (max - min) / (bucketCount - 1);
        this.buckets = new int[bucketCount];
        // Place the observations in the appropriate buckets
        Arrays.stream(observations).forEach(observation -> buckets[getBucketFor(observation)]++);
    }

    private int getBucketFor(double observation) {
        return (int) floor((observation - min) / scale);
    }

    public double minObservationForBucket(int bucket) {
        return min + scale * bucket;
    }
}
