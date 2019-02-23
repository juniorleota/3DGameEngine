#version 330

//In values from vertext shader
in vec4 frag_color;
out vec4 out_color;

void main()
{
    //fragColor = vec4(0.0, 0.5, 0.5, 1.0);
    out_color = frag_color;
}