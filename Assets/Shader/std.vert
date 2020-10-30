#version 450 core

layout (location = 0) in vec3 a_Position;
layout (location = 1) in vec3 a_Texture;
layout (location = 2) in vec4 a_Color;

uniform mat4 projection;
uniform mat4 scale;
uniform vec2 u_Transform;

out vec4 v_Color;
out vec3 v_Texture;

void main(){
	v_Color=a_Color;
	v_Texture=a_Texture;
	gl_Position = projection*scale*vec4(a_Position-vec3(u_Transform,0.0),1.0);
}
