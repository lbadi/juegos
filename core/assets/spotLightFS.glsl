varying vec4 v_color; 
varying vec2 v_texCoords;
varying vec4 normal;
varying vec4 v_position;
uniform vec4 light_color;
uniform vec4 light_position;
uniform sampler2D u_texture;
uniform sampler2D u_texture2;
uniform vec4 eye;
uniform vec4 specular_color;
uniform vec4 ambient_color;
uniform vec4 light_direction;
uniform float cosine_inner;
uniform float cosine_outter;

void main() {

//PREGUNTAR COMO LEVANTAR  colores del material
	//vec4 light_vector = normalize(light_position - v_position);
	vec4 light_vector = normalize(v_position - light_position);
	float cosine_light = dot(light_vector,normalize(light_direction));
	float spotlight = smoothstep(cosine_outter,cosine_inner,cosine_light);

   	gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
    
    float normal_dot_light = dot(normal, normalize(light_vector));
    
    vec4 diffusal_irradiance = spotlight  * gl_FragColor  * light_color ;
    
    //TODO hacer que la especular funcione como spot
    //Especular
    float m_shine = 1.2; //Brillo del material
    vec4 r = -1.0*light_vector + 2.0 * normal_dot_light * normal;
    vec4 m_spec = vec4(0.00001,0.00001,0.00001,1); //Materia especular
    vec4 specular_irradiance = max(0.0, pow(dot(r, eye-v_position), m_shine)) * m_spec * specular_color;
    
    //Ambient
    vec4 m_ambient = vec4(0.0001,0.0001,0.0001,1); //Material ambiente
    vec4 ambient_irradiance = m_ambient * ambient_color;
    
    //Phone 
    gl_FragColor =  diffusal_irradiance + specular_irradiance + ambient_irradiance;
    
    //Aca estoy usando dos texturas, cuando haya construido el shadowMap con esto voy a poder hacer cosas locas
    //gl_FragColor = gl_FragColor * 0.0001 + v_color *   texture2D(u_texture2, v_texCoords);
}

   