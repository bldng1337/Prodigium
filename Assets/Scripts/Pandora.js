#i Scripts.PlayerMovement:js
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
	