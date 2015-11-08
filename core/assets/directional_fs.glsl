varying vec4 v_color;
varying vec2 v_texCoords;
varying vec4 normal;
varying vec4 v_position;
varying vec4 ShadowCoord;
uniform vec4 light_vector;
uniform vec4 light_color;
uniform sampler2D u_texture;
uniform sampler2D u_shadowMap;

uniform vec4 eye;
uniform vec4 specular_color;
uniform vec4 ambient_color;

float unpackFloatFromVec4i(const vec4 value);
vec4 packFloatToVec4i(const float value);

void main() {

	float bias = 0.00;
    float visibility = 1.0;
    //Solamente hay que calcularlo si esta adentro del shadowMap
    vec2 convertedShadowCoord = (ShadowCoord.xy + vec2(1,1)) / 2.0;
    float diffCoordMap = unpackFloatFromVec4i(texture2D(u_shadowMap, convertedShadowCoord)) - ShadowCoord.z;
    if(ShadowCoord.x <= 1.0 && ShadowCoord.x >= -1.0 && ShadowCoord.y <= 1.0 && ShadowCoord.y >= -1.0){

        if ( unpackFloatFromVec4i(packFloatToVec4i(ShadowCoord.z)) + bias> unpackFloatFromVec4i(texture2D(u_shadowMap, convertedShadowCoord))){
            visibility = 0.1;
        }
    }

//PREGUNTAR COMO LEVANTAR  colores del material
    gl_FragColor = v_color * texture2D(u_texture, v_texCoords);

    float normal_dot_light = dot(normal, normalize(light_vector));

    vec4 diffusal_irradiance = normal_dot_light  * gl_FragColor  * light_color ;

    //Especular
    float m_shine = 1.0; //Brillo del material
    vec4 r = -1.0*light_vector + 2.0 * normal_dot_light * normal;
    vec4 m_spec = vec4(0.1,0.1,0.1,1); //Materia especular
    vec4 specular_irradiance = max(0.0, pow(dot(r, eye-v_position), m_shine)) * m_spec * specular_color;


    //Ambient
    vec4 m_ambient = vec4(0.0001,0.0001,0.0001,1); //Material ambiente
    vec4 ambient_irradiance = m_ambient * ambient_color;

    //Phone
    gl_FragColor =  diffusal_irradiance + specular_irradiance  + ambient_irradiance ;

    //Shadows
    gl_FragColor = vec4(gl_FragColor.xyz * visibility,gl_FragColor.a);
  //  float diffDepth = unpackFloatFromVec4i(packFloatToVec4i(ShadowCoord.z)) - unpackFloatFromVec4i(texture2D(u_shadowMap, convertedShadowCoord));
 //   gl_FragColor = gl_FragColor * 0.000001 + vec4(diffDepth,diffDepth,diffDepth,1);
  // 	gl_FragColor = gl_FragColor + vec4(normal.xyz,1);
  // gl_FragColor = gl_FragColor * visibility * 0.000001 + vec4(unpackFloatFromVec4i(texture2D(u_shadowMap, (ShadowCoord.xy + vec2(1,1)) / 2.0)),0,ShadowCoord.z,1);
 //	gl_FragColor = gl_FragColor * visibility * 0.000001 + vec4(0,0,unpackFloatFromVec4i(texture2D(u_shadowMap, (ShadowCoord.xy + vec2(1,1)) / 2.0)),1);
 //	gl_FragColor = gl_FragColor * visibility * 0.000001 + vec4(0,0,ShadowCoord.z,1);

}

float unpackFloatFromVec4i(const vec4 value)
{
  const vec4 bitSh = vec4(1.0/(256.0*256.0*256.0), 1.0/(256.0*256.0), 1.0/256.0, 1.0);
  return(dot(value, bitSh));
}
vec4 packFloatToVec4i(const float value)
{
  const vec4 bitSh = vec4(256.0*256.0*256.0, 256.0*256.0, 256.0, 1.0);
  const vec4 bitMsk = vec4(0.0, 1.0/256.0, 1.0/256.0, 1.0/256.0);
  vec4 res = fract(value * bitSh);
  res -= res.xxyz * bitMsk;
  return res;
}



