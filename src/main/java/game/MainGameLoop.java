package game;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.*;
import models.RawModel;
import terrains.Terrain;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainGameLoop {

    public static void main(String[] args) {

        DisplayManager.createDisplay();
        Loader loader = new Loader();

        List<Entity> entities = new ArrayList<>();
        ChessBoard chessBoard = new ChessBoard(loader);
        entities.addAll(chessBoard.getBlackPieces());
        entities.addAll(chessBoard.getWhitePieces());
        entities.add(chessBoard.getBoard());

        Terrain terrain1 = new Terrain(0, 0, loader, new ModelTexture(loader.loadTexture("wood")));
        Terrain terrain2 = new Terrain(-1, 0, loader, new ModelTexture(loader.loadTexture("wood")));
        Terrain terrain3 = new Terrain(0, -1, loader, new ModelTexture(loader.loadTexture("wood")));
        Terrain terrain4 = new Terrain(-1, -1, loader, new ModelTexture(loader.loadTexture("wood")));

        Light light = new Light(new Vector3f(0, 20, -10), new Vector3f(1, 1, 1));
        Camera camera = new Camera();
        MasterRenderer renderer = new MasterRenderer();

        while (!Display.isCloseRequested()) {
            camera.move();

            renderer.processTerrain(terrain1);
            renderer.processTerrain(terrain2);
            renderer.processTerrain(terrain3);
            renderer.processTerrain(terrain4);

            for (Entity entity : entities) {
                renderer.processEntity(entity);
            }

            renderer.render(light, camera);
            DisplayManager.updateDisplay();
        }

        renderer.cleanUp();
        loader.cleanUp();
        DisplayManager.closeDisplay();

    }

}