varying vec4 v_color; 
varying vec2 v_texCoords;
varying vec4 normal;
varying vec4 v_position;
uniform vec4 light_vector;
uniform vec4 light_color;
uniform sampler2D u_texture;
uniform vec4 eye;
uniform vec4 specular_color;
uniform vec4 ambient_color;


void main() {

//PREGUNTAR COMO LEVANTAR  colores del material
    gl_FragColor = v_color * texture2D(u_texture, v_texCoords);
    
    float normal_dot_light = dot(normal, normalize(light_vector));
    
    vec4 diffusal_irradiance = normal_dot_light * gl_FragColor  * light_color ;
    
    //Especular
    float m_shine = 1.0; //Brillo del material
    vec4 r = -1.0*light_vector + 2.0 * normal_dot_light * normal;
    vec4 m_spec = vec4(0.1,0.1,0.1,1); //Materia especular
    vec4 specular_irradiance = max(0.0, pow(dot(r, eye-v_position), m_shine)) * m_spec * specular_color;
    
    
    //Ambient
    vec4 m_ambient = vec4(0.0001,0.0001,0.0001,1); //Material ambiente
    vec4 ambient_irradiance = m_ambient * ambient_color;
    
    //Phone 
    gl_FragColor =  diffusal_irradiance + specular_irradiance + ambient_irradiance;
    
}


