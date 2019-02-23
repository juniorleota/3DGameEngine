#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 color;

out vec4 frag_color;

uniform mat4 transformationMatrix;

void main()
{
    gl_Position = vec4(position, 1);
    frag_color = vec4(color, 1);
//    frag_color = vec4(clamp(position, 0.1, 1), 1.0f);
}