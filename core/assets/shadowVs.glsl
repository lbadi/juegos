attribute vec4 a_position;
uniform mat4 u_worldView;
void main()
{
    gl_Position =  u_worldView * a_position;
}