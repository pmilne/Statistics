package net.pmilne;

import java.util.Arrays;
import java.util.Random;
import java.util.function.DoubleUnaryOperator;

import static java.lang.Math.sqrt;
import static org.apache.commons.math3.special.Erf.erfInv;

@SuppressWarnings("unused")
public class GenerateSample {
    public static final DoubleUnaryOperator CENTER = x -> 2 * x - 1; // map [0 .. 1] -> [-1 .. 1]
    public static final double              SQRT2  = sqrt(2);

    public static void main(String[] args) {
        double[] mapped = new Random(0)
                .doubles(1000000) // a stream of doubles between 0 (incl) and 1 (excl)
//                .map(x -> x)                                //      uniform distribution
//                .map(Math::sqrt)                            //       linear distribution
//                .map(x -> pow(x, 1.0 / 3))                  //    quadratic distribution
//                .map(x -> pow(x, 0.25))                     //        cubic distribution
//                .map(Math::log)                             //  exponential distribution
//                .map(x -> x * x)
//                .map(x -> x * sqrt(x))
//                .map(x -> sqrt(-log(x)))
//                .map(Erf::erfInv)                           //       normal distribution
//                .map(CENTER).map(Erf::erfInv)               //       normal distribution
                .map(CENTER).map(x -> SQRT2 * erfInv(x))    //       normal distribution (std = 1)
                .toArray();
        assert Arrays.stream(mapped).noneMatch(Double::isNaN);
        Sample sample = new Sample(100, mapped);
        Histogram.show(sample);
    }
}
