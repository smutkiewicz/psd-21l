package psd.alerts;

public class SafetyRateAlert extends StatsAlert {
  private final double rate;

  public SafetyRateAlert(int assetId, double threshold, double rate) {
    super(assetId, threshold);
    this.rate = rate;
  }

  public double getRate() {
    return rate;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof SafetyRateAlert) {
      SafetyRateAlert event = (SafetyRateAlert) obj;
      return event.canEquals(this) && super.equals(event) && rate == event.rate;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return 41 * super.hashCode() + Double.hashCode(rate);
  }

  @Override
  public boolean canEquals(Object obj) {
    return obj instanceof MeanAlert;
  }

  @Override
  public String toString() {
    return "SafetyRateAlert(" + getBasicInfo() + rate + ")";
  }
}