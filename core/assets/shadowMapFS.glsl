
varying vec4 v_color; 
varying vec2 v_texCoords;
varying vec4 normal;
varying vec4 v_position;

//uniform sampler2D u_texture;


void main() {

//PREGUNTAR COMO LEVANTAR  colores del material
	//gl_FragColor = texture2D(u_texture, v_texCoords);
    gl_FragColor = vec4(1.0, 0.0, 0.0, 1.0);
}

float rand(vec2 co){
    return fract(sin(dot(co.xy ,vec2(12.9898,78.233))) * 43758.5453);
}