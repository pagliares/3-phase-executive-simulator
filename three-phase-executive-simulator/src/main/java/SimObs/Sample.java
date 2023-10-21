//Sample.java
//Simple random sampling for discrete event simulation.
//M.Pidd

package SimObs;

import java.util.Random;

/** Used to create streamable random numbers and associated routines to sample
* from probability distributions.
*/
public class Sample extends Random {

  
  public Sample(long seed) {
    super(seed);
  }

  
  public int getSampleFromNegativeExponentialDistribution(int mean) {
    double m = (double)mean;
    double nE = -m*Math.log(nextDouble());
    return (int)nE;
  }

  public int getSampleFromNegativeExponentialDistribution(float mean) {
    double m = (double)mean;
    double nE = -m*java.lang.Math.log(nextDouble());
    return (int)nE;
  }
  
  public int getSampleFromNegativeExponentialDistribution(double mean) {
    double m = (double)mean;
    double nE = -mean*Math.log(nextDouble());
    return (int)nE;
  }

  /** min is the minimum and max is the maximum of its range. Both integers.
  * No checks to see if min <= max.
  */
  public int getSampleFromUniformDistribution(int min, int max) {
    double m1 = (double)min;
    double m2 = (double)max;
    double u = m1 + (m2-m1)*Math.log(nextDouble());
    return (int)u;
  }

  /**  
  * min is the minimum and max is the maximum of its range. Both floating point.
  * No checks to see if min <= max.
  */
  public int getSampleFromUniformDistribution(float min, float max) {
    double m1 = (double)min;
    double m2 = (double)max;
    double u = m1 + (m2-m1)*nextDouble();
    return (int)u;
  }

  /**  
  * min is the minimum and max is the maximum of its range. Both double precision.
  * No checks to see if min <= max.
  */
  public int getSampleFromUniformDistribution(double min, double max) {
    double u = min + (max-min)*nextDouble();
    return (int)u;
  }
}
