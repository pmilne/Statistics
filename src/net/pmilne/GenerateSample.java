package net.pmilne;

import java.util.Arrays;

import static java.lang.Math.*;
import static org.apache.commons.math3.special.Erf.erfInv;

public class GenerateSample {
    private static double centre(double x) {
        return 2 * x - 1;
    }

    public static void main(String[] args) {
        double[] mapped = BaseSamples.uniform01(0)
                .limit(1000000)
//                .map(x -> x)
//                .map(Math::sqrt)
//                .map(x -> pow(x, 1.0 / 3))
//                .map(x -> pow(x, 0.25))
//                .map(x -> pow(x, 0.1))
//                .map(Math::log)
//                .map(x -> x * x)
//                .map(x -> x * sqrt(x))
//                .map(x -> pow(x, 1.0/3))
//                .map(x -> sqrt(-log(x)))
//                .map(Erf::erfInv)
//                .map(x -> erfInv(centre(x)))
                .map(x -> sqrt(2) * erfInv(centre(x)))
                .toArray();
        assert Arrays.stream(mapped).noneMatch(Double::isNaN);
        Sample sample = new Sample(100, mapped);
        Histogram.show(sample);
    }
}
