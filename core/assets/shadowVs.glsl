attribute vec4 a_position;
uniform mat4 u_worldView;
varying vec4 ShadowCoord;
void main()
{
    gl_Position = u_worldView * a_position;
    ShadowCoord = u_worldView * a_position;
}