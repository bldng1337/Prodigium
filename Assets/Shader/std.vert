#version 450 core

layout (location = 0) in vec3 a_Position;
layout (location = 1) in vec3 a_Texture;
layout (location = 2) in vec4 a_Color;
layout (location = 3) in float a_renderargs;

uniform mat4 projection;
uniform mat4 scale;
uniform vec2 u_Transform;

out vec4 v_Color;
out vec3 v_Texture;
out float v_renderargs;
out vec2 v_Position;

void main(){
	v_renderargs=a_renderargs;
	v_Color=a_Color;
	v_Texture=a_Texture;
	vec2 pos=a_Position.xy-u_Transform.xy;
	pos.x/=1920;
	pos.y/=1080;
	pos.y=1-pos.y;
	v_Position=pos;
	gl_Position = projection*scale*vec4(a_Position-vec3(u_Transform,0.0),1.0);
}
