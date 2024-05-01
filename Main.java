import static java.util.concurrent.TimeUnit.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.Arrays;
import stdlib.StdAudio;

class Main {
	private static int i;
	private static boolean[] truthData;
	private static boolean useAudio;
	
	public static void main(String[] args) throws InterruptedException {
		final Proposition proposition = new Proposition(args[0]);
		final long beatLength = Long.parseLong(args[1]);
		
		if (args.length > 2) {
			useAudio = Boolean.parseBoolean(args[2]);
		} else {
			useAudio = true;
		}

		i = 0;
		truthData = Interpreter.makeTruthTable(proposition);
		System.out.println(Arrays.toString(truthData));
		

		final Runnable playBeat = () -> {
			if (truthData[i]) {
				if (useAudio) {
					StdAudio.play("click.wav"); 
				}
				System.out.println("click");
			} else {
				System.out.println(".");
			}
			i++;
			if (i >= truthData.length) {
				i = 0;
			}
		};

		final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

		final ScheduledFuture<?> beatHandler = 
			scheduler.scheduleAtFixedRate(playBeat, 0, beatLength, MILLISECONDS);
	}
}
