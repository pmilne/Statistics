package net.pmilne;

import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.data.statistics.SimpleHistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;

import static org.jfree.chart.ChartFactory.getChartTheme;

public class Histogram {
    private static IntervalXYDataset getDataset(Sample sample) {
        assert !Double.isNaN(sample.scale);
        double yScale = (double) sample.buckets.length / sample.size;
        return new SimpleHistogramDataset("Population density (relative to uniform)") {
            @Override
            public int getItemCount(int series) {
                return sample.buckets.length;
            }

            public double getStartXValue(int series, int item) {
                return sample.minObservationForBucket(item);
            }

            public double getEndXValue(int series, int item) {
                return sample.minObservationForBucket(item + 1);
            }

            public double getXValue(int series, int item) {
                return sample.minObservationForBucket(item);
            }

            @Override
            public double getYValue(int series, int item) {
                return yScale * sample.buckets[item];
            }
        };
    }

    private static ChartPanel createChartPanel(Sample sample) {
        IntervalXYDataset dataset = getDataset(sample);
        ChartTheme        theme   = getChartTheme();
        ((StandardChartTheme) theme).setXYBarPainter(new StandardXYBarPainter());
        JFreeChart chart = ChartFactory.createHistogram(
                "Generated", // Chart Title
                "Value (" + sample.buckets.length + " buckets)", // Category axis
                "Density", // Value axis
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );
        return new ChartPanel(chart);
    }

    private static void show(String title, Container container) {
        JFrame frame = new JFrame(title);
        frame.setSize(800, 400);
        frame.setContentPane(container);
        frame.setLocationRelativeTo(null); // center the window
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void show(Sample sample) {
        show(Histogram.class.getSimpleName(), createChartPanel(sample));
    }
}