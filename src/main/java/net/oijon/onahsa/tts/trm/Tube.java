package net.oijon.onahsa.tts.trm;

/**
 * @author N3ther
 *
 */
public class Tube {

	private int length;
	private int sampleRate;
	private final int speedOfSound = 34300; //in cm/s
	private double loss = 0.9995; //very small amount of loss
	private float[][] delayLine;
	
	/**
	 * Creates a tube based on it's length.
	 * @param length The length of the tube in cm.
	 */
	public Tube (int length) {
		this.length = length;
		// L = c/F, where L is the length (17cm), c is the speed of sound (cm/s), and F is the sampling frequency
		sampleRate = speedOfSound/this.length;
		//Creates the delay array based off 
		delayLine = new float[2][sampleRate];
	}
	
	/** Creates and manages a delay line. Needs work.
	 * @param pushAmount The input being added to the delay line.
	 */
	public void push(float pushAmount) {
		System.out.println("pushamount: " + pushAmount);
		float[][] newDelayLine = new float[2][delayLine[0].length];
		delayLine[0][0] = pushAmount;
		for (int i = 1; i < delayLine[0].length; i++) {
			newDelayLine[0][i] = delayLine[0][i-1]*(float)loss;
			//System.out.println(newDelayLine[0][i]); //debug
		}
		
		newDelayLine[1][0] = delayLine[0][delayLine[0].length-1];
		
		for (int i = 1; i < delayLine[1].length; i++) {
			newDelayLine[1][i] = delayLine[1][i-1]*(float)loss;
			//System.out.println(newDelayLine[1][i]); //debug
		}
		
		for(int i = 0; i < newDelayLine[1].length / 2; i++)
		{
		    float temp = newDelayLine[1][i];
		    newDelayLine[1][i] = newDelayLine[1][newDelayLine[1].length - i - 1];
		    newDelayLine[1][newDelayLine[1].length - i - 1] = temp;
		}
		
		newDelayLine[0][0] += newDelayLine[1][0];
		delayLine = newDelayLine;
	}
	
	/**
	 * Converts to 44.1 kHz by adding new data to follow the slope. Needs work.
	 * I could very well be misunderstanding something about this in the paper by Manzara.
	 * If so, this method will probably be replaced.
	 * @return The standardized delay line, can be turned into a .wav file.
	 */
	public float[][] standardize() {
		float[][] standard = new float[44100][2];
		int multiple = (int)(44100/sampleRate);
		for (int h = 0; h < 2; h++) {
			for (int i = 0; i < sampleRate; i++) {
				float slope = 1;
				if (i+1 < sampleRate) { 
					slope = (delayLine[i+1][h]-delayLine[i][h])/((i+1)-i);
				} else {
					slope = (delayLine[sampleRate-1][h]-delayLine[i][h])/((sampleRate-1)-i);
				}
				for (int j = 0; j < multiple; j++) {
					//could be optimized more
					if (j == 0) {
						standard[i*multiple][h] = delayLine[i][h];
					} else {
						standard[j][h] = standard[j-1][h]*slope;
					}
				}
			}
		}
		
		return standard;
	}
	
	/**
	 * Gets the current output of the delay line.
	 * @return
	 */
	public float[] out() {
		float[] out = new float[2];
		out[0] = delayLine[0][sampleRate-1];
		out[1] = delayLine[1][sampleRate-1];
		return out;
	}
	
	/**
	 * Debug only, should not be used in production.
	 * Returns the full array.
	 * @return The delay line.
	 */
	public float[][] getDelayLine() {
		return delayLine;
	}
	/**
	 * Gets the length of the tube.
	 * @return The tube length in cm.
	 */
	public int getLength() {
		return length;
	}
	/**
	 * Gets the sample rate the delay line is operating at.
	 * @return The sample rate in Hz
	 */
	public int getSampleRate() {
		return sampleRate;
	}
	/**
	 * Gets the current loss factor. 1 == no loss, 0 == 100% loss per sample.
	 * @return The loss factor.
	 */
	public double getLoss() {
		return loss;
	}
	/**
	 * Sets the loss factor. Debug only.
	 * @param loss The loss factor. 1 == no loss, 0 == 100% loss per sample.
	 */
	public void setLoss(double loss) {
		this.loss = loss;
	}
}
