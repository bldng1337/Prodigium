var Engine = Java.type("me.engine.Engine");
var Animation = Java.type("me.engine.Entity.Animation");
var Vector2f = Java.type("org.joml.Vector2f");
var attacked=false;
e.setAnimation(Animation.DEATH);
var light=Engine.getEngine().getLightRenderer().createLight(e.x,e.y,300,0x7F4411FF);
function death(){
	Engine.getEngine().getLightRenderer().removeLight(light);
}

function update(){
	light.setPos(new Vector2f(e.x+e.width/2,e.y+e.height/2));
	light.setSize(light.getSize()-10);
	if(light.getSize()<0)
		light.setSize(0);
	if(!attacked){
		var forEach = Array.prototype.forEach;
		forEach.call(Engine.getEngine().getCurrlevel().getEntitys(), function(en) {
			if(en.intersects(e)&&en!=e) {
				en.damageEntity(10);
				e.attackEntity(en);
			}
		});
		attacked=true;
	}
}