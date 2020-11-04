var Animation = Java.type("me.engine.Entity.Animation");

function update(){
	if(e.motionX!=0||e.motionY!=0){
		e.currTexture=Animation.RUNNING;
	}else{
		e.currTexture=Animation.IDLE;
	}
}
