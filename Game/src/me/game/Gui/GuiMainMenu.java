package me.game.Gui;

import org.joml.Vector2f;

import UI.Minimap;
import UI.Statbar;
import me.engine.Engine;
import me.engine.Entity.Entity;
import me.engine.Gui.Menu.Background;
import me.engine.Gui.Menu.Button;
import me.engine.Gui.Menu.GuiScreen;
import me.engine.Gui.Menu.Label;
import me.engine.World.Levels.Maze.MazeLevel;

public class GuiMainMenu extends GuiScreen
{
	public GuiMainMenu() {
		this.elementlist.add(new Background(0, 0, 1920, 1080, "Textures.Main_Menu.main_menu:png"));
		this.elementlist.add(new Button(()->Engine.getEngine().setGuiscreen(new CharacterMenu(this::startGame)), 1920/2-225, 453, 450, 200, "Play", "Textures.Main_Menu.Main_Menu_button:png"));
		this.elementlist.add(new Button(()->System.out.println("TODO: Add Options"), 1920/2-225, 653, 450, 200, "Options", "Textures.Main_Menu.Main_Menu_button:png"));
		this.elementlist.add(new Button(()->System.exit(0), 1920/2-225, 853, 450, 200, "Exit", "Textures.Main_Menu.Main_Menu_button:png"));
		this.elementlist.add(new Label(1920/2, 350, 710, 600, "Prodigium", true));
	}
	
	
	
	public void startGame(String player) {
		Entity e=Engine.getEngine().getEntityManager().newEntity(player);
		e.x=500;
		e.y=500;
		Engine.getEngine().setCurrlevel(new MazeLevel(50));
		Engine.getEngine().getCurrlevel().setPlayer(e);
		Engine.getEngine().getCurrlevel().addEntity(e);
		Engine.getEngine().getRender().c.setP(()->new Vector2f(e.x,e.y));
		Engine.getEngine().getHud().add(new Minimap(0, 0, 200));
		Engine.getEngine().getHud().add(new Statbar(0,1080-100,400,100,e));
		Engine.getEngine().getPm().addParticleSystem((a)->{a.getMotion().set(0, -0.1);a.setSize(4);}).setColor(0x35FFFFFF).setMax(20).setMaxLifetime(6000).setSpawndelay(30);
		Engine.getEngine().setGuiscreen(null);
	}
}
