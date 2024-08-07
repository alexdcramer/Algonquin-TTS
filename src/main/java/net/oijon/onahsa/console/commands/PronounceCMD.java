package net.oijon.onahsa.console.commands;

import net.oijon.olog.Log;
import net.oijon.onahsa.console.Console;
import net.oijon.onahsa.console.Functions;

public class PronounceCMD extends Command {

	public PronounceCMD(Log log) {
		super(log);
		name = "pronounce";
		description = "Converts the given IPA string to sound.";
	}

	@Override
	public void run(String[] args) {
		if (args.length < 2) {
			log.err("Invalid amount of parameters for 'pronounce'. Expected 2 or more, given " + args.length);
		} else {
			String input = "";
			for (int i = 1; i < args.length; i++) {
				input += args[i];
			}
			log.info("Pronouncing \"" + input + "\"...");
			Functions.pronounce(Console.getSelectedPack(), input, Console.getOutputName());
			log.info("Pronounced \"" + input + "\"!");
		}
	}

}
