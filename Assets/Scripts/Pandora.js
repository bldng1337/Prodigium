#i Scripts.PlayerMovement:js
var Engine = Java.type("me.engine.Engine");
var Animation = Java.type("me.engine.Entity.Animation");
var EventManager = Java.type("me.engine.Utils.Event.EventManager");
var MousePressed = Java.type("me.engine.Utils.Event.Events.MousePressed");
var ClickAction = Java.type("me.engine.Utils.Event.Events.MousePressed.Action");
var Space = Java.type("me.engine.Utils.Space");
EventManager.registerfor(KeyPressed.class,e);
EventManager.registerfor(MousePressed.class,e);
var attackCooldown=0;
function update(){
	if(e.getAnimation()==Animation.DEATH){
		return;
	}
	if(attackCooldown>0)
		attackCooldown--;
	print(attackCooldown);
	pmupdate();
	if(e.health<=0){
		e.setAnimation(Animation.DEATH);
		return;
	}
	if(e.currTexture!=Animation.ATTACKING){
		if(e.motionX!=0||e.motionY!=0){
			e.currTexture=Animation.RUNNING;
		}else{
			e.currTexture=Animation.IDLE;
		}
	}
}

function keypressed(key,state){
	pmkeypressed(key,state);
}

function mousepressed(x,y,state){
	if(state.equals(ClickAction.PRESSED)&&attackCooldown<=0){
		Engine.getEngine().getCurrlevel().addEntity("Entities.Pandora_Explosion:json", x-50, y-50);
		attackCooldown=80;
	}
}

	