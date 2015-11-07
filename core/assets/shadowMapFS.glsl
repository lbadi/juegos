varying vec4 ShadowCoord;
vec4 packFloatToVec4i(const float value);
void main() {
    gl_FragColor = packFloatToVec4i(ShadowCoord.z);
}

vec4 packFloatToVec4i(const float value)
{
  const vec4 bitSh = vec4(256.0*256.0*256.0, 256.0*256.0, 256.0, 1.0);
  const vec4 bitMsk = vec4(0.0, 1.0/256.0, 1.0/256.0, 1.0/256.0);
  vec4 res = fract(value * bitSh);
  res -= res.xxyz * bitMsk;
  return res;
}