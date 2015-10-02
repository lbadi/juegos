attribute vec4 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;
uniform mat4 u_worldView;

void main()
{
    gl_Position =  u_worldView * a_position ;  
}