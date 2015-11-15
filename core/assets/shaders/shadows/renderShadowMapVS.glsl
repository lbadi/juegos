attribute vec4 a_position;
attribute vec2 a_texCoord0;
varying vec4 v_color;
varying vec2 v_texCoords; 
void main()
{
    v_color = vec4(1, 1, 1, 1);
    v_texCoords = a_texCoord0;
    gl_Position = a_position;
}