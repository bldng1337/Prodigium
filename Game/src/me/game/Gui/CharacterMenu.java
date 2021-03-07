package me.game.Gui;

import java.util.function.Consumer;

import me.engine.Engine;
import me.engine.Entity.Entity;
import me.engine.Gui.Menu.CharacterPreview;
import me.engine.Gui.Menu.GuiScreen;
import me.engine.Utils.Event.EventTarget;
import me.engine.Utils.Event.Events.Render2D;

public class CharacterMenu extends GuiScreen{
	long brick;
	public CharacterMenu(Consumer<String> c) {
		this.elementlist.add(new CharacterPreview(()->c.accept("Entities.Pandora:json"), 300, 0, 400, 1080, "Pandora", "Textures.Pandora.Pandora_Portrait:png"));
		this.elementlist.add(new CharacterPreview(()->c.accept("Entities.TestPlayer:json"), 1300, 0, 400, 1080, "Suta", "Textures.Suta.Suta_Portrait:png"));
		this.elementlist.add(new CharacterPreview(()->c.accept("Entities.Pandora:json"), 800, 0, 400, 1080, "Loki", "Textures.Loki.Loki_Portrait:png"));
		brick=Engine.getEngine().getTex().getTexture("Textures.Wand.wand_leer:png");
	}
	
	@EventTarget
	@Override
	public void drawScreen(Render2D update) {
		Engine.getEngine().getUIrender().setColor(30, 30, 30, 100);
		Engine.getEngine().getUIrender().renderRect(0, 0, 1920, 1080, brick,0);
		Engine.getEngine().getUIrender().resetColor();
		super.drawScreen(update);
	}
	
	
}
