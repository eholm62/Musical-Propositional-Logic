import static java.util.concurrent.TimeUnit.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.Arrays;
import stdlib.StdAudio;

class Main {
	private static int i;
	private static boolean[] primaryTruthData;
	private static boolean[] secondaryTruthData;
	private static boolean useAudio;
	
	public static void main(String[] args) throws InterruptedException {
		final Proposition primaryProposition = new Proposition(args[0]);
		final Proposition secondaryProposition = new Proposition(args[1]);
		final long beatLength = Long.parseLong(args[2]);
		
		if (args.length > 3) {
			useAudio = Boolean.parseBoolean(args[3]);
		} else {
			useAudio = true;
		}

		primaryTruthData = Interpreter.makeTruthTable(primaryProposition);
		secondaryTruthData = Interpreter.makeTruthTable(secondaryProposition);

		if (primaryTruthData.length != secondaryTruthData.length) {
			throw new RuntimeException("Primary and secondary propositions must have truth tables of the same size");
		}
		
		System.out.println(Arrays.toString(primaryTruthData));
		System.out.println(Arrays.toString(secondaryTruthData));

		if (useAudio) {
			StdAudio.read("accent.wav");
			StdAudio.read("click.wav");	
			StdAudio.read("ghost.wav");
		}

		i = 0;

		// the code that runs every beat
		Runnable playBeat = () -> {
			if (primaryTruthData[i]) {
				if (secondaryTruthData[i]) {
					System.out.println("+");
				} else {
					System.out.println("-");
				}
			} else {
				if (secondaryTruthData[i]) {
					System.out.println(".");
				} else {
					System.out.println();
				}
			}
			i++;
			if (i >= primaryTruthData.length) {
				i = 0;
				System.out.println("Cycle Completed");
			}
		};

		if (useAudio) {
			playBeat = () -> {
				if (primaryTruthData[i]) {
					if (secondaryTruthData[i]) {
						StdAudio.play("accent.wav");
						System.out.println("+");
					} else {
						StdAudio.play("click.wav"); 
						System.out.println("-");
					}
				} else {
					if (secondaryTruthData[i]) {
						StdAudio.play("ghost.wav");
						System.out.println(".");
					} else {
						System.out.println();
					}
				}
				i++;
				if (i >= primaryTruthData.length) {
					i = 0;
					System.out.println("Cycle Completed");
				}
			};
		}
		
		// initialize the scheduler
		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		// schedule the beats
		final ScheduledFuture<?> beatHandler = 
			scheduler.scheduleAtFixedRate(playBeat, 0, beatLength, MILLISECONDS);
	}
}
