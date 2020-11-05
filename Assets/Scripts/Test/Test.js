var Animation = Java.type("me.engine.Entity.Animation");
var Vector2i = Java.type("org.joml.Vector2i");
var Tile = Java.type("me.engine.World.Tile");
var Math = Java.type("java.lang.Math");

function update(){
	var player=e.getLevel().getPlayer();
	if(e.intersects(player)){
		e.setAnimation(Animation.ATTACKING);
		e.damageEntity(10);
		e.resetPath();
		return;
	}
	
	e.gotowards(player);
	
	if(e.motionX!=0||e.motionY!=0){
		e.currTexture=Animation.RUNNING;
	}else{
		e.currTexture=Animation.IDLE;
	}
}