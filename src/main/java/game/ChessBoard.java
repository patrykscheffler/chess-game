package game;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import org.lwjgl.util.vector.Vector3f;
import renderEngine.Loader;
import renderEngine.OBJLoader;
import textures.ModelTexture;

import java.util.*;

public class ChessBoard {
    private Map<String, Entity> whitePieces = new HashMap<>();
    private Map<String, Entity> blackPieces = new HashMap<>();
    private List<Entity> removedWhitePieces = new ArrayList<>();
    private List<Entity> removedBlackPieces = new ArrayList<>();
    private Entity board;
    private Map<Character, Entity[]> squares;
    private boolean animation = false;

    public boolean isAnimation() {
        return animation;
    }

    public void setAnimation(boolean animation) {
        this.animation = animation;
    }

    public ChessBoard(Loader loader) {
        this.squares = new HashMap<Character, Entity[]>();
        this.squares.put('a', new Entity[9]);
        this.squares.put('b', new Entity[9]);
        this.squares.put('c', new Entity[9]);
        this.squares.put('d', new Entity[9]);
        this.squares.put('e', new Entity[9]);
        this.squares.put('f', new Entity[9]);
        this.squares.put('g', new Entity[9]);
        this.squares.put('h', new Entity[9]);

        RawModel model = OBJLoader.loadObjModel("board", loader);
        TexturedModel texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("board")));
        texturedModel.getTexture().setReflectivity(0.1f);
        texturedModel.getTexture().setShineDamper(3);
        this.board = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 9.8f);

        Entity entity;
        model = OBJLoader.loadObjModel("pawn", loader);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white/pawn")));
        for (int i = 0; i < 8; i++) {
            entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
            this.squares.get((char) ('a' + (7 - i)))[2] = entity;
            this.whitePieces.put("pawn" + (i + 1), entity);
        }

        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("black/pawn")));
        for (int i = 0; i < 8; i++) {
            entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
            this.squares.get((char) ('a' + (7 - i)))[7] = entity;
            this.blackPieces.put("pawn" + (i + 1), entity);
        }

        model = OBJLoader.loadObjModel("rook", loader);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white/rook")));
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
        this.squares.get('a')[1] = entity;
        this.whitePieces.put("rook2", entity);
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
        this.squares.get('h')[1] = entity;
        this.whitePieces.put("rook1", entity);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("black/rook")));
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
        this.squares.get('a')[8] = entity;
        this.blackPieces.put("rook2", entity);
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
        this.squares.get('h')[8] = entity;
        this.blackPieces.put("rook1", entity);

        model = OBJLoader.loadObjModel("knight", loader);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white/knight")));
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
        this.squares.get('b')[1] = entity;
        this.whitePieces.put("knight2", entity);
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
        this.squares.get('g')[1] = entity;
        this.whitePieces.put("knight1", entity);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("black/knight")));
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
        this.squares.get('b')[8] = entity;
        this.blackPieces.put("knight2", entity);
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
        this.squares.get('g')[8] = entity;
        this.blackPieces.put("knight1", entity);

        model = OBJLoader.loadObjModel("bishop", loader);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white/bishop")));
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
        this.squares.get('c')[1] = entity;
        this.whitePieces.put("bishop2", entity);
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
        this.squares.get('f')[1] = entity;
        this.whitePieces.put("bishop1", entity);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("black/bishop")));
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
        this.squares.get('c')[8] = entity;
        this.blackPieces.put("bishop2", entity);
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
        this.squares.get('f')[8] = entity;
        this.blackPieces.put("bishop1", entity);

        model = OBJLoader.loadObjModel("queen", loader);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white/queen")));
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
        this.squares.get('d')[1] = entity;
        this.whitePieces.put("queen", entity);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("black/queen")));
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
        this.squares.get('d')[8] = entity;
        this.blackPieces.put("queen", entity);

        model = OBJLoader.loadObjModel("king", loader);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("white/king")));
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
        this.squares.get('e')[1] = entity;
        this.whitePieces.put("king", entity);
        texturedModel = new TexturedModel(model, new ModelTexture(loader.loadTexture("black/king")));
        entity = new Entity(texturedModel, new Vector3f(0, 0, 0), 0, 0, 0, 10);
        this.squares.get('e')[8] = entity;
        this.blackPieces.put("king", entity);

        startPosition();
    }

    public void makeMove(String move) {
        if (move.length() == 4) {
            char fromX = move.charAt(0);
            int fromY = Character.getNumericValue(move.charAt(1));
            char toX = move.charAt(2);
            int toY = Character.getNumericValue(move.charAt(3));

            float CYCLES_NUMBER = 200;
            float deltaX = (toX - fromX) *  4 / CYCLES_NUMBER;
            float deltaY = - (toY - fromY) * 4 / CYCLES_NUMBER;
            float deltaZ = 0;

            Entity entity = squares.get(fromX)[fromY];
            String type = findPiece(entity);
            if (type.equals("knight1") || type.equals("knight2")) deltaZ = 40 / CYCLES_NUMBER / 2;
            Entity caputureEntity = squares.get(toX)[toY];
            squares.get(toX)[toY] = entity;
            squares.get(fromX)[fromY] = null;

            float finalDeltaZ = deltaZ;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    float delta = finalDeltaZ;
                    for (int i = 0; i < CYCLES_NUMBER; i++) {
                        if (i == CYCLES_NUMBER / 2) {
                            delta = -delta;
                        }

                        entity.increasePosition(deltaX, delta, deltaY);

                        try {
                            if (i < 170 && delta != 0) Thread.sleep(4);
                            else Thread.sleep(5);

                            if (i == 180 && caputureEntity != null) removePiece(caputureEntity);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    animation = false;
                }
            }).start();

        } else if (move.length() == 9) {
            char fromX = move.charAt(0);
            int fromY = Character.getNumericValue(move.charAt(1));
            char toX = move.charAt(2);
            int toY = Character.getNumericValue(move.charAt(3));

            char fromX2 = move.charAt(5);
            int fromY2 = Character.getNumericValue(move.charAt(6));
            char toX2 = move.charAt(7);
            int toY2 = Character.getNumericValue(move.charAt(8));

            float CYCLES_NUMBER = 200;
            float deltaX = 0;
            if ((toX - fromX) > 0) deltaX = -1;
            else if ((toX - fromX) < 0) deltaX = 1;
            deltaX = (toX - fromX + deltaX) * 4 / CYCLES_NUMBER;

            float deltaX2 = 0;
            if ((toX2 - fromX2) > 0) deltaX2 = -1;
            else if ((toX2 - fromX2) < 0) deltaX2 = 1;
            deltaX2 = (toX2 - fromX2 + deltaX2) * 4 / CYCLES_NUMBER;

            Entity entity1 = squares.get(fromX)[fromY];
            squares.get(toX)[toY] = entity1;
            squares.get(fromX)[fromY] = null;

            Entity entity2 = squares.get(fromX2)[fromY2];
            squares.get(toX2)[toY2] = entity2;
            squares.get(fromX2)[fromY2] = null;

            float finalDeltaX = deltaX;
            float finalDeltaX1 = deltaX2;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    for (int i = 0; i < CYCLES_NUMBER; i++) {
                        entity1.increasePosition(finalDeltaX, 0, 0);
                        entity2.increasePosition(finalDeltaX1, 0, 0);

                        try {
                            if (i < 170) Thread.sleep(4);
                            else Thread.sleep(5);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    Vector3f position = entity1.getPosition();
                    entity1.setPosition(entity2.getPosition());
                    entity2.setPosition(position);

                    animation = false;
                }
            }).start();
        }
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

    public String findPiece(Entity entity) {
        String key = "";
        for (Map.Entry<String, Entity> entry : this.whitePieces.entrySet()) {
            if (entry.getValue() == entity) {
                return entry.getKey();
            }
        }

        for (Map.Entry<String, Entity> entry : this.blackPieces.entrySet()) {
            if (entry.getValue() == entity) {
                return entry.getKey();
            }
        }

        return "";
    }

    public void removePiece(Entity entity) {
        String key = "";
        for (Map.Entry<String, Entity> entry : this.whitePieces.entrySet()) {
            if (entry.getValue() == entity) {
                key = entry.getKey();
            }
        }

        if (key.length() > 0) {
            Entity removedPiece = this.whitePieces.get(key);
            removedPiece.setPosition(new Vector3f(22, 0, -16 + (this.removedWhitePieces.size() * 4)));
            this.removedWhitePieces.add(removedPiece);
            this.whitePieces.remove(key);
            key = "";
        }

        for (Map.Entry<String, Entity> entry : this.blackPieces.entrySet()) {
            if (entry.getValue() == entity) {
                key = entry.getKey();
            }
        }

        if (key.length() > 0) {
            Entity removedPiece = this.blackPieces.get(key);
            removedPiece.setPosition(new Vector3f(-22, 0, 16 + (this.removedBlackPieces.size() * (-4))));
            this.removedBlackPieces.add(removedPiece);
            this.blackPieces.remove(key);
        }
    }

    public List<Entity> getRemovedWhitePieces() {
        return removedWhitePieces;
    }

    public List<Entity> getRemovedBlackPieces() {
        return removedBlackPieces;
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
