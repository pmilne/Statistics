package net.pmilne;

import java.awt.Container;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

import org.jfree.chart.*;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.data.statistics.SimpleHistogramBin;
import org.jfree.data.statistics.SimpleHistogramDataset;

import static org.jfree.chart.ChartFactory.getChartTheme;

public class Histogram {
    private static SimpleHistogramDataset getDataset(Sample sample) {
        assert !Double.isNaN(sample.min) && !Double.isNaN(sample.max);
        int                    count     = sample.buckets.length;
        SimpleHistogramDataset dataset   = new SimpleHistogramDataset("Bucket counts per " + String.format("%,d", sample.size) + " observations");
        dataset.setAdjustForBinSize(false);
        for (int i = 0; i < count; i++) {
            double             min   = sample.minObservationForBucket(i);
            double             max   = sample.minObservationForBucket(i + 1);
            int                value = sample.buckets[i];
            SimpleHistogramBin bin   = new SimpleHistogramBin(min, max, true, false);
            bin.setItemCount(value);
            dataset.addBin(bin);
        }
        return dataset;
    }

    private static Container createChartPanel(Sample sample) {
        SimpleHistogramDataset dataset = getDataset(sample);
        ChartTheme theme = getChartTheme();
        ((StandardChartTheme) theme).setXYBarPainter(new StandardXYBarPainter());
        JFreeChart chart = ChartFactory.createHistogram(
                "Generated", // Chart Title
                "Buckets (" + sample.buckets.length + ")", // Category axis
                "Counts", // Value axis
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );
        return new ChartPanel(chart);
    }

    public static void show(Sample sample) {
        JFrame example = new JFrame(Histogram.class.getSimpleName());
        example.setSize(800, 400);
        example.setContentPane(createChartPanel(sample));
        example.setLocationRelativeTo(null); // center the window
        example.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        example.setVisible(true);
    }
}