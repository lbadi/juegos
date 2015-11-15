varying vec4 v_color; 
varying vec2 v_texCoords;
uniform sampler2D u_texture;
float unpackFloatFromVec4i(const vec4 value);
void main() {
    gl_FragColor = v_color * unpackFloatFromVec4i(texture2D(u_texture, v_texCoords));
}

float unpackFloatFromVec4i(const vec4 value)
{
  const vec4 bitSh = vec4(1.0/(256.0*256.0*256.0), 1.0/(256.0*256.0), 1.0/256.0, 1.0);
  return(dot(value, bitSh));
}
