#version 450 core

layout (location = 0) in vec3 a_Position;

uniform mat4 projection;
uniform mat4 scale;

out vec4 v_Color;

void main(){
	v_Color=vec4(1.0,1.0,0.0,1.0);
	//u_ViewProj * u_Transform * 
	gl_Position = projection*scale*vec4(a_Position,1.0);
}
