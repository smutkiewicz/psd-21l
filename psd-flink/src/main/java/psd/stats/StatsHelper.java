package psd.stats;

import org.apache.flink.shaded.curator4.com.google.common.math.Quantiles;
import org.apache.flink.shaded.curator4.com.google.common.math.Stats;

import java.util.Arrays;

public class StatsHelper {

  private StatsHelper() {
  }

  public static StatsAggregationResult calculateStats(double[] samples) {
    return new StatsAggregationResult(
            mean(samples),
            median(samples),
            quantile(samples),
            meanFromMinRates(samples),
            safetyRateAverageDeviation(samples),
            safetyRateGini(samples)
    );
  }

  public static double mean(double[] samples) {
    checkArray(samples);
    return Stats.meanOf(samples);
  }

  public static double median(double[] samples) {
    checkArray(samples);
    return Quantiles.median().compute(samples);
  }

  // Kwantyl rzędu 0,1,
  public static double quantile(double[] samples) {
    checkArray(samples);
    return Quantiles.scale(10).index(1).compute(samples);
  }

  // Średnia z 10% najmniejszych stóp zwrotu,
  public static double meanFromMinRates(double[] samples) {
    checkArray(samples);
    Arrays.sort(samples);
    int length = samples.length;
    int tenPercentIndex = (int) Math.floor(length * 0.1);
    double[] dividedSamples = Arrays.copyOfRange(samples, 1, tenPercentIndex);
    return Stats.meanOf(dividedSamples);
  }

  // Miara bezpieczeństwa oparta na odchyleniu przeciętnym
  public static double safetyRateAverageDeviation(double[] samples) {
    checkArray(samples);

    double mean = Stats.meanOf(samples);
    int t = samples.length;
    double sum = 0;
    for (double sample : samples) {
      sum += Math.abs(mean - sample);
    }
    return 1 / (2D * t) * sum;
  }

  // Miara bezpieczeństwa oparta na średniej różnicy Giniego
  public static double safetyRateGini(double[] samples) {
    checkArray(samples);

    int t = samples.length;
    double sumOfSums = 0;
    for (int i = 0; i < t; i++) {
      double sum = 0;
      for (int j = 0; j < t; j++) {
        sum += Math.abs(samples[i] - samples[j]);
      }
      sumOfSums += sum;
    }
    return 1 / (2D * t * t) * sumOfSums;
  }

  public static boolean lowerThanThreshold(double value, double threshold) {
    return threshold - value >= threshold * 0.1;
  }

  private static void checkArray(double[] samples) {
    if (samples.length == 0) {
      throw new IllegalArgumentException("Samples array can't be empty!");
    }
  }
}