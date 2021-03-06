package psd.alerts;

import static psd.InvestmentWalletJob.IS_CSV_OUTPUT;

public class MeanAlert extends StatsAlert {
  private final double mean;

  public MeanAlert(int windowId, int assetId, double percentage, double threshold, double mean) {
    super(windowId, assetId, percentage, threshold);
    this.mean = mean;
  }

  @Override
  public double getValue() {
    return mean;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof MeanAlert) {
      MeanAlert event = (MeanAlert) obj;
      return event.canEquals(this) && super.equals(event) && mean == event.mean;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return 41 * super.hashCode() + Double.hashCode(mean);
  }

  @Override
  public boolean canEquals(Object obj) {
    return obj instanceof MeanAlert;
  }

  @Override
  public String toString() {
    if (IS_CSV_OUTPUT) {
      return "1," + getBasicInfo() + getValue();
    } else {
      return "MeanAlert(" + getBasicInfo() + getValue() + ")";
    }
  }
}