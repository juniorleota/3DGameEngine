#version 330

layout (location=0) in vec3 position;

out vec4 frag_color;

void main()
{
    gl_Position = vec4(position, 1);
    frag_color = vec4(clamp(position, 0, 1), 1.0f);
}