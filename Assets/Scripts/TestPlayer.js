#i Scripts.PlayerMovement:js
var Engine = Java.type("me.engine.Engine");
var Animation = Java.type("me.engine.Entity.Animation");
var EventManager = Java.type("me.engine.Utils.Event.EventManager");
EventManager.registerfor(KeyPressed.class,e);

function update(){
	if(e.getAnimation()==Animation.DEATH){
		return;
	}
	pmupdate();
	if(e.health<=0){
		e.setAnimation(Animation.DEATH);
		return;
	}
	
}


function keypressed(key,state){
	pmkeypressed(key,state);
	if(key.equalsIgnoreCase("r")&&state.equals(Action.PRESSED)&&e.getAnimation()!=Animation.ATTACKING){
		e.setAnimation(Animation.ATTACKING);
		var forEach = Array.prototype.forEach;
		forEach.call(Engine.getEngine().getCurrlevel().getEntitys(), function(en) {
			if(en.intersects(e)&&en!=e) {
				en.damageEntity(10);
				e.attackEntity(en);
			}
		});
	}
		
}

