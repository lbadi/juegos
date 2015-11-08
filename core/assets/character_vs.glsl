attribute vec3 a_position;
attribute vec3 a_normal;
attribute vec2 a_texCoord0;
//Firstly, we need to define loads of new attributes, one for each bone.
attribute vec2 a_boneWeight0;
attribute vec2 a_boneWeight1;
//We also need to take the bonematrices
uniform mat4 u_mvp;
uniform mat4 u_mv;
uniform mat4 u_normal;
uniform mat4 u_bones[12];

varying vec2 v_texCoords;

void main() {
    // Calculate skinning for each vertex
    mat4 skinning = mat4(0.0);
    skinning += (a_boneWeight0.y) * u_bones[int(a_boneWeight0.x)];
    skinning += (a_boneWeight1.y) * u_bones[int(a_boneWeight1.x)];
    //Include skinning into the modelspace position
    vec4 pos = skinning * vec4(a_position, 1.0);
    // Rest of code is justlike usual
    vec3 v = vec3((u_mv * pos).xyz);
    vec3 vsN = normalize(vec3(u_normal * skinning * vec4(a_normal, 0.0)).xyz); //viewspaceNormal
    gl_Position = u_mvp * pos;
    v_texCoords = a_texCoord0;
}