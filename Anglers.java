package shunanglers;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import net.runelite.api.coords.WorldPoint;
import simple.hooks.filters.SimpleSkills;
import simple.hooks.scripts.Category;
import simple.hooks.scripts.ScriptManifest;
import simple.hooks.simplebot.ChatMessage;
import simple.hooks.wrappers.SimpleNpc;
import simple.hooks.wrappers.SimpleObject;
import simple.robot.script.Script;
import simple.robot.utils.WorldArea;

@ScriptManifest(author = "Shun", category = Category.FISHING, description = 
" Start with Rod & Bait in inventory. Banks when inventory is full. "
		+ " Have rod and bait in inventory and last teleport on portal as angler spot ", 

		discord = "Shun#2997", name = "Shun Anglers", servers = {"OSRSPS"}, version = "1.0")

public class Anglers extends Script {
	
	protected static boolean started;

	private int totalxp;

	private int startxp;

	private int hourlyxp;

	private int fishinglvl;

	private long startTime;

	private int fishCaught;

	private int fishPerHour;

	long start = System.currentTimeMillis();

	private static GUI gui;

	private WorldArea EDGE = new WorldArea(new WorldPoint(3073, 3516, 0), new WorldPoint(3108, 3471, 0));



	public void paint(Graphics Graphs) {
		Graphics2D g = (Graphics2D)Graphs;
		long runTime = System.currentTimeMillis() - startTime;
		fishPerHour = (int)(fishCaught / ((System.currentTimeMillis() - this.startTime) / 3600000.0D));
		g.setColor(Color.DARK_GRAY);
		//g.fillRect(5, 257, 200, 80);
		g.setColor(Color.BLACK);
		//g.drawRect(5, 257, 200, 80);
		g.setColor(Color.WHITE);
		g.drawString("Shun Anglers", 10, 272);
		g.setColor(Color.WHITE);
		g.drawString("XP per hour: " + formatValue(hourlyxp), 10, 287);
		g.drawString("Caught per hour: " + formatValue(fishPerHour), 10, 302);
		g.drawString("Current Level: " + fishinglvl, 10, 317);
		g.setColor(Color.WHITE);
		g.drawString("Time Fishing Sharks: " + formatTime(runTime), 10, 332);
		totalxp = ctx.skills.experience(SimpleSkills.Skills.FISHING) - startxp;
		long end = System.currentTimeMillis();
		float sec = (float)(end - start) / 1000.0F;
		hourlyxp = (int)(totalxp / sec * 3600.0F);
	}

	private String formatTime(long ms) {
		long s = ms / 1000L, m = s / 60L, h = m / 60L;
		s %= 60L;
		m %= 60L;
		h %= 24L;
		return String.format("%02d:%02d:%02d", new Object[] { Long.valueOf(h), Long.valueOf(m), Long.valueOf(s) });
	}

	public final String formatValue(final long l) {
		return (l > 1_000_000) ? String.format("%.2fm", ((double) l / 1_000_000))
				: (l > 1000) ? String.format("%.1fk", ((double) l / 1000)) 
						: l + "";
	}

	@Override
	public void onChatMessage(ChatMessage chatMessage) {
		String message = chatMessage.getMessage().toLowerCase();

		if(message.contains("you catch an anglerfish")) {
			fishCaught++;
		}
	}

	public void onExecute() {
		startxp = ctx.skills.experience(SimpleSkills.Skills.FISHING);
		fishinglvl = ctx.skills.realLevel(SimpleSkills.Skills.FISHING);
		startTime = System.currentTimeMillis();
		gui = new GUI(ctx, this);
		gui.setVisible(true);
		started = false;
	}

	public void onProcess() {
		if (started) {
			fishinglvl = ctx.skills.realLevel(SimpleSkills.Skills.FISHING);
			if (ctx.inventory.populate().population() < 28) {
				if (ctx.players.getLocal().getAnimation() == -1) {
					SimpleNpc fishingspot = ctx.npcs.populate().filter(6825).nearest().next();
					if (fishingspot != null && fishingspot.validateInteractable()) {
						ctx.updateStatus("Fishing");
						fishingspot.click("Bait");
						ctx.onCondition(() -> (ctx.players.getLocal().getAnimation() != -1), 1500);
					} 
				} 

			} else {
				if(!ctx.pathing.inArea(EDGE)) {
					ctx.updateStatus("Teleporting Home");
					ctx.keyboard.sendKeys("::home");
					ctx.sleep(3000);
				} else {

					if (!ctx.bank.bankOpen()) {
						SimpleNpc bank = ctx.npcs.populate().filter(new String[] { "Banker" }).nearest().next();
						if (bank != null && bank.validateInteractable()) {
							ctx.updateStatus("Opening Bank");
							bank.click("Bank");
							ctx.onCondition(() -> ctx.bank.bankOpen(), 3000);
						} 
					} else {
							ctx.updateStatus("Deposit Anglers");
							ctx.bank.depositAllExcept(new String[] { "Fishing rod", "Sandworms" });
							ctx.bank.closeBank();
							ctx.sleep(1000);
							ctx.sleepCondition(() -> !ctx.bank.bankOpen(), 3000);
							SimpleObject portal = ctx.objects.populate().filter(25401).nearest().next();
							if(portal != null && portal.validateInteractable()) {
								ctx.updateStatus("Tele to Anglers");
								if(portal.click("Last-teleport")) {
								}
							} 
						} 
					}
				}
			}
		}
	
	public void onTerminate() {
		System.out.print("ShunAnglers Stopped");
		gui.dispose();
	}
}
