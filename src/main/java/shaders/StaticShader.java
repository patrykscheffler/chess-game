package shaders;

public class StaticShader extends ShaderProgram {

    public static final String VERTEX_FILE = "src/main/java/shaders/vertexShader.txt";
    public static final String FRAGEMENT_FILE = "src/main/java/shaders/fragmentShader.txt";

    public StaticShader() {
        super(VERTEX_FILE, FRAGEMENT_FILE);
    }

    @Override
    protected void bindAttributes() {

        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");

    }

}