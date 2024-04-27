import static java.util.concurrent.TimeUnit.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.Arrays;
import stdlib.StdAudio;

class Main {
	private static int i;
	private static boolean[] truthData;
	private static boolean endProgram;
	private static ScheduledFuture<?> beatHandler; 
	
	public static void main(String[] args) throws InterruptedException {
		final Proposition proposition = new Proposition(args[0]);
		final long beatLength = Long.parseLong(args[1]);
		
		truthData = Interpreter.makeTruthTable(proposition);
		i = 0;
		endProgram = false;
		
		final Runnable playBeat = () -> {
			if (truthData[i]) {
				System.out.println("Click");
			} else {
				System.out.println(".");
			}
			i++;
			if (i >= truthData.length) {
				endProgram = true;
				beatHandler.cancel(true);
			}
		};

		beatHandler = Executors.newScheduledThreadPool(1).
			scheduleAtFixedRate(playBeat, 0, beatLength, MILLISECONDS);
		
		while (true) {
			if (endProgram) {
				System.out.println("Program ended");
			}
		}
	}
}
