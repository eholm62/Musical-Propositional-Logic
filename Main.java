import static java.util.concurrent.TimeUnit.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.Arrays;
import stdlib.StdAudio;

class Main {
	private static int i;
	private static boolean[] truthData;
	
	public static void main(String[] args) throws InterruptedException {
		final Proposition proposition = new Proposition(args[0]);
		final long beatLength = Long.parseLong(args[1]);
		
		i = 0;
		truthData = Interpreter.makeTruthTable(proposition);
		System.out.println(Arrays.toString(truthData));
		
		final Runnable playBeat = () -> {
			if (truthData[i]) {
				StdAudio.play("click.wav");
				// System.out.println("true");
			} else {
				// System.out.println("false");
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
