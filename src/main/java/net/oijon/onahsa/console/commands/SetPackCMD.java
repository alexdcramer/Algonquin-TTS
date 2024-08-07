package net.oijon.onahsa.console.commands;

import net.oijon.olog.Log;
import net.oijon.onahsa.console.Console;
import net.oijon.onahsa.console.Functions;

public class SetPackCMD extends Command {

	public SetPackCMD(Log log) {
		super(log);
		name = "setpack";
		description = "Sets the sound pack for Onahsa to use.";
	}

	@Override
	public void run(String[] args) {
		if (args.length > 1) {
			String selectedPack = Functions.selectPack(args[1]);
			if (!selectedPack.equals(args[1])) {
				log.warn("Pack " + args[1] + " not found. Defaulting to " + selectedPack);
			}
			Console.setSelectedPack(selectedPack);
			log.info("Set sound pack to " + selectedPack + ".");
		} else {
			log.err("Invalid amount of parameters for 'setpack'. Expected 2, given " + args.length);
		}
	}

}
