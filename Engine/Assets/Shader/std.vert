#version 450 core

layout (location = 0) in vec3 a_Position;
layout (location = 1) in vec3 a_Texture;

uniform mat4 projection;
uniform mat4 scale;

out vec4 v_Color;
out vec3 v_Texture;

void main(){
	v_Color=vec4(1.0,1.0,0.0,1.0);
	v_Texture=a_Texture;
	gl_Position = projection*scale*vec4(a_Position,1.0);
}
