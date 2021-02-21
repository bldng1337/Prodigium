#version 450 core

layout (location = 0) out vec4 o_Color;

in vec4 v_Color;
in vec3 v_Texture;

uniform sampler2D u_Texture;

void main()
{
	o_Color=texture(u_Texture,v_Texture.xy)*v_Color;
}