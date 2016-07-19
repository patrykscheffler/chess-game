package game;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import textures.ModelTexture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChessBoard {

    private Map<String, Entity> whitePieces = new HashMap<>();
    private Map<String, Entity> blackPieces = new HashMap<>();
    private Entity board;

    public ChessBoard(Loader loader) {
        RawModel model = OBJLoader.loadObjModel("board", loader);
        TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("board")));
        texturedModel.getTexture().setReflectivity(0.1f);
        texturedModel.getTexture().setShineDamper(3);
        this.board = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 9.8f);

        model = OBJLoader.loadObjModel("pawn", loader);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white/pawn")));
        for (int i = 0; i < 8; i++) {
            this.whitePieces.put("pawn" + (i + 1), new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));
        }

        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("black/pawn")));
        for (int i = 0; i < 8; i++) {
            this.blackPieces.put("pawn" + (i + 1), new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));
        }

        model = OBJLoader.loadObjModel("rook", loader);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white/rook")));
        this.whitePieces.put("rook1", new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));
        this.whitePieces.put("rook2", new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("black/rook")));
        this.blackPieces.put("rook1", new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));
        this.blackPieces.put("rook2", new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));

        model = OBJLoader.loadObjModel("knight", loader);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white/knight")));
        this.whitePieces.put("knight1", new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));
        this.whitePieces.put("knight2", new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("black/knight")));
        this.blackPieces.put("knight1", new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));
        this.blackPieces.put("knight2", new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));

        model = OBJLoader.loadObjModel("bishop", loader);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white/bishop")));
        this.whitePieces.put("bishop1", new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));
        this.whitePieces.put("bishop2", new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("black/bishop")));
        this.blackPieces.put("bishop1", new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));
        this.blackPieces.put("bishop2", new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));

        model = OBJLoader.loadObjModel("queen", loader);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white/queen")));
        this.whitePieces.put("queen", new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("black/queen")));
        this.blackPieces.put("queen", new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));

        model = OBJLoader.loadObjModel("king", loader);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white/king")));
        this.whitePieces.put("king", new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("black/king")));
        this.blackPieces.put("king", new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10));

        startPosition();
    }

    public void startPosition() {
        for (Map.Entry<String, Entity> entry : whitePieces.entrySet()) {
            String key = entry.getKey();
            Entity entity = entry.getValue();

            piecePosition(key, entity, 1);
        }

        for (Map.Entry<String, Entity> entry : blackPieces.entrySet()) {
            String key = entry.getKey();
            Entity entity = entry.getValue();

            piecePosition(key, entity, -1);
        }
    }

    public void piecePosition(String key, Entity entity, int factor) {
        float y = 2;

        switch (key) {
            case "pawn1":
                entity.increasePosition(14, y, 10 * factor);
                break;
            case "pawn2":
                entity.increasePosition(10, y, 10 * factor);
                break;
            case "pawn3":
                entity.increasePosition(6, y, 10 * factor);
                break;
            case "pawn4":
                entity.increasePosition(2, y, 10 * factor);
                break;
            case "pawn5":
                entity.increasePosition(-2, y, 10 * factor);
                break;
            case "pawn6":
                entity.increasePosition(-6, y, 10 * factor);
                break;
            case "pawn7":
                entity.increasePosition(-10, y, 10 * factor);
                break;
            case "pawn8":
                entity.increasePosition(-14, y, 10 * factor);
                break;
            case "rook1":
                entity.increasePosition(14, y, 14 * factor);
                break;
            case "knight1":
                entity.increasePosition(10, y, 14 * factor);
                if (factor == -1) entity.setRotY(180);
                break;
            case "bishop1":
                entity.increasePosition(6, y, 14 * factor);
                break;
            case "king":
                entity.increasePosition(2, y, 14 * factor);
                break;
            case "queen":
                entity.increasePosition(-2, y, 14 * factor);
                break;
            case "bishop2":
                entity.increasePosition(-6, y, 14 * factor);
                break;
            case "knight2":
                entity.increasePosition(-10, y, 14 * factor);
                if (factor == -1) entity.setRotY(180);
                break;
            case "rook2":
                entity.increasePosition(-14, y, 14 * factor);
                break;
        }
    }

    public List<Entity> getWhitePieces() {
        return new ArrayList<>(whitePieces.values());
    }

    public List<Entity> getBlackPieces() {
        return new ArrayList<>(blackPieces.values());
    }

    public Entity getBoard() {
        return board;
    }
}
