package net.pmilne;

import java.util.Arrays;
import java.util.Random;
import java.util.function.DoubleUnaryOperator;

import org.apache.commons.math3.special.Erf;

import static java.lang.Math.sqrt;
import static org.apache.commons.math3.special.Erf.erfInv;

public class GenerateSample {
    public static void main(String[] args) {
        DoubleUnaryOperator center = x -> 2 * x - 1;
        double[] mapped = new Random(0)
                .doubles()
                .limit(1000000)
//                .map(x -> x)                                //      uniform distribution
//                .map(Math::sqrt)                            //       linear distribution
//                .map(x -> pow(x, 1.0 / 3))                  //    quadratic distribution
//                .map(x -> pow(x, 0.25))                     //        cubic distribution
//                .map(Math::log)                             //  exponential distribution
//                .map(x -> x * x)
//                .map(x -> x * sqrt(x))
//                .map(x -> sqrt(-log(x)))
//                .map(Erf::erfInv)                           //       normal distribution
                .map(center).map(Erf::erfInv)               //       normal distribution
//                .map(center).map(x -> sqrt(2) * erfInv(x))  //      normal distribution
                .toArray();
        assert Arrays.stream(mapped).noneMatch(Double::isNaN);
        Sample sample = new Sample(100, mapped);
        Histogram.show(sample);
    }
}
