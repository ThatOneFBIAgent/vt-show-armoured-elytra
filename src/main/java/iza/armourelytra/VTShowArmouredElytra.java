package iza.armourelytra;

import iza.armourelytra.network.ArmouredElytraNetworking;
import iza.armourelytra.network.ArmouredElytraServerRelay;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VTShowArmouredElytra implements ModInitializer {
	public static final String MOD_ID = "vt-show-armoured-elytra";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ArmouredElytraNetworking.registerPayloads();
		ArmouredElytraServerRelay.register();
	}
}
