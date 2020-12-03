var Animation = Java.type("me.engine.Entity.Animation");

function update(){
	if(e.getAnimation()==Animation.DEATH){
		return;
	}
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