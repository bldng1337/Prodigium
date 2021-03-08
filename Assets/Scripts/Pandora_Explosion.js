var Engine = Java.type("me.engine.Engine");
var Animation = Java.type("me.engine.Entity.Animation");
var attacked=false;
e.setAnimation(Animation.DEATH);
function update(){
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