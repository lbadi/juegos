uniform mat4 u_worldView;
attribute vec4 a_position;
varying vec3 v_cubeMapUV;

void main()
{
    
    vec4 g_position = u_worldView * vec4(a_position.xyz,1);
    v_cubeMapUV = normalize(g_position.xyz);    
    gl_Position = a_position ;
}