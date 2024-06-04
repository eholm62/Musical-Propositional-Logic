import static java.util.concurrent.TimeUnit.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.Arrays;
import stdlib.StdAudio;

class SimpleMain {
	private static int i;
	private static boolean[] primaryTruthData;
	private static boolean useAudio;
	
	public static void main(String[] args) throws InterruptedException {
		final Proposition primaryProposition = new Proposition(args[0]);
		final long beatLength = Long.parseLong(args[1]);
		
		if (args.length > 2) {
			useAudio = Boolean.parseBoolean(args[2]);
		} else {
			useAudio = true;
		}

		primaryTruthData = Interpreter.makeTruthTable(primaryProposition);
		
		System.out.println(Arrays.toString(primaryTruthData));

		if (useAudio) {
			StdAudio.read("accent.wav");
			StdAudio.read("click.wav");	
			StdAudio.read("ghost.wav");
		}

		i = 0;

		// the code that runs every beat
		Runnable playBeat = () -> {
			if (primaryTruthData[i]) {
			    System.out.println("-");
			} else {
				System.out.println();
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
					StdAudio.play("click.wav"); 
					System.out.println("-");
				} else {
					System.out.println();
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
