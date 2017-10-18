import java.util.Random;

public class Assignment5 {

	public static void main(String[] args) {

		double[] array = cooldownSamples(27, 100000);
		double[] counts = countsFromArray(array, 20);
		String[][] array2d = array2dFromCounts(counts); 
		printReport(array2d, minFromArray(array), maxFromArray(array));

	}

	/**
	 * Compute the time in hours required for a body to cool down to temperature
	 * degrees. Gaussian noise is added to simulate parameter uncertainty.
	 * 
	 * @param temperature
	 *            The temperature of the body when found.
	 * @return the time in hours required for the body to cool down to
	 *         temperature degrees.
	 */
	public static double cooldown(double temperature) {
		// we need this object to generate Gaussian random variables
		Random random = new Random();

		// the average body temperature of a (living) human
		double bodyTemperature = 37;

		// add noise to simulate that the body temperature of the victim at the
		// time of death is uncertain
		bodyTemperature += random.nextGaussian();

		// compute the time required for the body to cool down from
		// bodyTemperature to temperature using Newton’s law of cooling.
		double cooldownTime = Math.log(bodyTemperature / temperature);
		cooldownTime *= 1 / bodyTemperature;

		// normalize this value such that cooling down from 37 to 32 degrees
		// takes 1 hour. we assume that we have measured this for the
		// environment that the body is found in. we add Gaussian noise to
		// simulate measurement uncertainty.
		cooldownTime *= 255 + random.nextGaussian();

		return cooldownTime;
	}

	/**
	 * Method to create a number of samples to be stored in an array
	 * 
	 * @param temperature,
	 *            the temerature to be given as input to the cooldown method
	 * @param numSamples,
	 *            number of samples 
	 * @return an array of samples
	 */
	public static double[] cooldownSamples(int temperature, int numSamples) {

		double[] samples = new double[numSamples];

		for (int i = 0; i < numSamples; i++) {
			samples[i] = cooldown(temperature);
		}

		return samples;
	}

	/**
	 * Method to find the minimum value in an array
	 * 
	 * @param array,
	 *            the array with values to be examined
	 * @return the minimum value
	 */
	public static double minFromArray(double[] array) {

		double min = array[0];

		for (int i = 1; i < array.length; i++) {
			if (array[i] < min)
				min = array[i];
		}
		return min;
	}

	/**
	 * Method to find the maximum value in an array
	 * 
	 * @param array,
	 *            the array with values to be examined
	 * @return the maximum value
	 */
	public static double maxFromArray(double[] array) {

		double max = array[0];

		for (int i = 1; i < array.length; i++) {
			if (array[i] > max)
				max = array[i];
		}
		return max;
	}


	/**
	 * Method to compute the what samples occur most frequently
	 * 
	 * @param array,
	 *            an array with the samples for the computation
	 * @param numRanges,
	 *            number of ranges
	 * @return an array with the count of the number of samples that fall within
	 *         each of the numRanges range
	 */
	public static double[] countsFromArray(double[] array, int numRanges) {

		double[] counts = new double[numRanges];

		double min = minFromArray(array);
		double max = maxFromArray(array);

		double rangeSize = (max - min) / (numRanges - 1);

		for (int k = 0; k < array.length; k++) {
			double value = array[k];
			int i = (int) ((value - min) / rangeSize);
			counts[i]++;
		}
		return counts;
	}

	/**
	 * Method to print out values from a 2d array
	 * 
	 * @param array2d,
	 *            the array that holds the values to be printed
	 */
	public static void printArray2d(String[][] array2d) {

		// Loop to print out the values
		for (int i = 0; i < array2d.length; i++) {

			for (int j = 0; j < array2d[0].length; j++) {
				System.out.print(array2d[i][j]);
			}

			// Print out to make a line break
			System.out.println("");
		}
	}

	/**
	 * Method to convert the array returned from countsFromArray to be used with
	 * printArray2d
	 * 
	 * @param counts,
	 *            the array returned from the countsFromArray method
	 * @return a String[][] array to be printed in the printArray2d method
	 */
	public static String[][] array2dFromCounts(double[] counts) {

		final int PRINT_WIDTH = 50;
		String[][] array2d = new String[counts.length][PRINT_WIDTH];

		int max = (int) maxFromArray(counts);

		for (int i = 0; i < array2d.length; i++) {

			double k = counts[i];
			double kMax = (int)(k * PRINT_WIDTH) / max;

			for (int j = 0; j < array2d[0].length; j++) {

				if (j < kMax)
					array2d[i][j] = "#";

				else
					array2d[i][j] = " ";
			}
		}
		return array2d;
	}


	/**
	 * Method to print a out a report of all the observations in a nice overview
	 * 
	 * @param array2d,
	 *            the array returned from the array2dFromCounts method
	 * @param arrayMin,
	 *            the minimum value
	 * @param arrayMax,
	 *            the maximum value
	 */
	public static void printReport(String[][] array2d, double arrayMin, double arrayMax) {

		double size = array2d.length-1;
		double sum = (arrayMax - arrayMin) / size;

		System.out.println("Time since death probability distribution");
		System.out.printf("%s %.2f %s", "– Each line corresponds to", sum , "hours.\n"); 

		System.out.println("========================================================");

		System.out.printf("%.2f %s", arrayMin, "hours\n\n");

		// Method call to do the actual printing 
		printArray2d(array2d);

		System.out.println("");
		System.out.printf("%.2f %s", arrayMax, "hours\n");
		System.out.println("========================================================");
	}
}