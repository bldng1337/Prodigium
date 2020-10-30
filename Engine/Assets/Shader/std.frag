#version 450 core

layout (location = 0) out vec4 o_Color;

in vec4 v_Color;
in vec3 v_Texture;

uniform sampler2D u_Textures[7];

void main()
{
	int index=int(v_Texture.z);
	//o_Color=vec4(v_Texture.xyz,0.0);
	
	
	o_Color=texture(u_Textures[index],v_Texture.xy);
}