var Animation = Java.type("me.engine.Entity.Animation");
var Vector2i = Java.type("org.joml.Vector2i");
var Texture = Java.type("me.engine.Utils.Texture");
var Math = Java.type("java.lang.Math");


function update(){
	if(e.getAnimation()==Animation.DEATH)
		return;
	var player=e.getLevel().getPlayer();
	if(e.health<=0){
		e.setAnimation(Animation.DEATH);
		return;
	}
	if(e.intersects(player)){
		if(e.getAnimation()!=Animation.ATTACKING){
			e.setAnimation(Animation.ATTACKING);
		}else{
			if(e.getFrame()>Texture.getaniframes(e.getTextureid())/2){
				player.damageEntity(10);
				e.attackEntity(player);
			}
		}
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