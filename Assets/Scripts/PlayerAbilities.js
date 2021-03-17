var System = Java.type("java.lang.System");
var Action = Java.type("me.engine.Utils.Event.Events.KeyPressed.Action");
var mana=0;
var MSkill=0;
var Ult = {
	cooldown:0,
	keybind:"r",
	manacost:0,
	lastcast:0,
	mcost:function(){
		return this.manacost/((MSkill*3)/(MSkill+50)+1);
	},
	canUse:function(){
		var ac=System.currentTimeMillis()-this.lastcast;
		return mana>this.mcost()&&ac>this.cooldown;
	},
	use:function(){}
}
var Ability1 = {
	cooldown:0,
	keybind:"f",
	manacost:0,
	lastcast:0,
	mcost:function(){
		return this.manacost/(MSkill*3)/(MSkill+50)+1;
	},
	canUse:function(){
		var ac=System.currentTimeMillis()-this.lastcast;
		return mana>this.mcost&&ac>this.cooldown;
	},
	use:function(){}
}
var Ability2 = {
	cooldown:0,
	keybind:"q",
	manacost:0,
	lastcast:0,
	mcost:function(){
		return this.manacost/(MSkill*3)/(MSkill+50)+1;
	},
	canUse:function(){
		var ac=System.currentTimeMillis()-this.lastcast;
		return mana>this.mcost&&ac>this.cooldown;
	},
	use:function(){}
}
var abilies=[Ult,Ability1,Ability2];
function abupdate(){
	if(mana<100){
		mana+=((mana+1)+2) / ((mana+1)*0.7) -1.4;
	}
}
function abkeypress(key,state){
	if(state.isKeyDown())
		for(var i=0;i<abilies.length;i++){
			var a=abilies[i];
			if(key.equalsIgnoreCase(a.keybind)&&a.canUse()){
				a.use();
				mana-=a.mcost();
				a.lastcast=System.currentTimeMillis();
			}
		}
}