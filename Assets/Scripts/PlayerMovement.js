var KeyPressed = Java.type("me.engine.Utils.Event.Events.KeyPressed");
var Action = Java.type("me.engine.Utils.Event.Events.KeyPressed.Action");
var Math = Java.type("java.lang.Math");

var pm_dx=0,pm_dy=0;

function pmupdate(){
	if(Math.abs(e.motionX)<10)
		e.motionX+=e.speed*pm_dx;
	if(Math.abs(e.motionY)<10)
		e.motionY+=e.speed*pm_dy;
	e.motionX/=1.4;
	e.motionY/=1.4;
	if(Math.abs(e.motionX)+0.5<1)
		e.motionX=0;
	if(Math.abs(e.motionY)+0.5<1)
		e.motionY=0;
	
	if(e.currTexture!=Animation.ATTACKING){
		if(e.motionX!=0||e.motionY!=0){
			e.currTexture=Animation.RUNNING;
		}else{
			e.currTexture=Animation.IDLE;
		}
	}
}


function pmkeypressed(key,state){
	switch(key.toUpperCase()){
	case "W":
		if(state.isKeyDown())
			pm_dy=-1;
		else
			pm_dy=0;
	break;
	case "A":
		if(state.isKeyDown())
			pm_dx=-1;
		else
			pm_dx=0;
		e.setRenderflipped(true);
	break;
	case "S":
		if(state.isKeyDown())
			pm_dy=1;
		else
			pm_dy=0;
	break;
	case "D":
		if(state.isKeyDown())
			pm_dx=1;
		else
			pm_dx=0;
		e.setRenderflipped(false);
	break;
	}
}