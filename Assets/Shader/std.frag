#version 450 core

layout (location = 0) out vec4 o_Color;

in vec4 v_Color;
in vec3 v_Texture;
in float v_renderargs;
in vec2 v_Position;

uniform sampler2D u_Textures[8];

vec4 blur(sampler2D s,vec2 pos){
	int kernelsize=3;
	vec4 points[9];
	float texelsize=0.0015f;
	for(int x=0;x<kernelsize;x++)
		for(int y=0;y<kernelsize;y++)
			points[x+y*kernelsize] = texture(s, pos + vec2(float(y-1)*texelsize,float(y-1)*texelsize));
	return (points[0] + 2.0 * points[1] + points[2] +
		2.0 * points[3] + points[4] + 2.0 * points[5] +
		points[6] + 2.0 * points[7] + points[8]) / 26.0;
}

void main()
{
	vec4 col;
	int index=int(v_Texture.z);
	if(index>7){
		col=v_Color;
	}else{
		col=texture(u_Textures[index],v_Texture.xy)*v_Color;
	}
	if(v_renderargs!=0)
		col*=blur(u_Textures[7],v_Position)*2.5f+vec4(0.1,0.1,0.1,1);
		//col*=(texture(u_Textures[7],v_Position)+vec4(0.3,0.3,0.3,1));
	o_Color=col;
}

