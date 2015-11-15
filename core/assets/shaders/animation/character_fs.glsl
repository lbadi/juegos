varying vec2 v_texCoords;
uniform sampler2D u_texture;

void main() {
    gl_FragColor = texture2D(u_texture, v_texCoords);
//    gl_FragColor = vec4(0.7, 0.7, 0.7, 1);
}