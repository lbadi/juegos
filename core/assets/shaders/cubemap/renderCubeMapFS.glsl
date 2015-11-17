    
uniform samplerCube u_cubemap;
varying vec3 v_cubeMapUV;

void main()
{
    vec4 c= textureCube(u_cubemap,v_cubeMapUV);
    gl_FragColor =  vec4(c);    
}