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
//                .map(x -> x)                              //      uniform distribution f(x) = 1
//                .map(Math::sqrt)                          //       linear distribution f(x) = x
//                .map(x -> pow(x, 1.0 / 3))                //    quadratic distribution f(x) = x^2
//                .map(x -> pow(x, 0.25))                   //       cubic distribution  f(x) = x^3
//                .map(x -> pow(x, 0.1))                    //       nonic distribution  f(x) = x^9
//                .map(Math::log)                           // exponential distribution  f(x) = e^x
//                .map(x -> x * x)
//                .map(x -> x * sqrt(x))
//                .map(x -> sqrt(-log(x)))
//                .map(Erf::erfInv)                         //      normal distribution  f(x) = e^(-x^2)
//                .map(x -> erfInv(centre(x)))              //      normal distribution mean 0
                .map(x -> sqrt(2) * erfInv(centre(x)))    //      normal distribution mean = 0, std = 1
                .toArray();
        assert Arrays.stream(mapped).noneMatch(Double::isNaN);
        Sample sample = new Sample(100, mapped);
        Histogram.show(sample);
    }
}
