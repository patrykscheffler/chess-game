package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.vector.Vector3f;

public class Camera {

    private static final float MAX_DISTANCE = 110;
    private static final float MIN_DISTANCE = 40;
    private static final float MAX_PITCH = 155;
    private static final float MIN_PITCH = 25;

    private Vector3f center = new Vector3f(0, 0, 0);
    private float distanceFromCenter = 70;
    private float angleAroundCenter = 120;

    private Vector3f position = new Vector3f(0, 0, 0);
    private float pitch = 25;
    private float yaw = 0;
    private float roll;

    public Camera() {}

    public void move() {
        calculateZoom();
        calculatePitch();
        calculateAroundCenter();
        float horizontalDistance = calculateHorizontalDistance();
        float verticalDistance = calculateVerticalDistance();
        calculateCameraPosition(horizontalDistance, verticalDistance);
        yaw = 180 - angleAroundCenter;
    }

    public Vector3f getPosition() {
        return position;
    }

    public float getPitch() {
        return pitch;
    }

    public float getYaw() {
        return yaw;
    }

    public float getRoll() {
        return roll;
    }

    private float calculateHorizontalDistance() {
        return (float) (distanceFromCenter * Math.cos(Math.toRadians(pitch)));
    }

    private float calculateVerticalDistance() {
        return (float) (distanceFromCenter * Math.sin(Math.toRadians(pitch)));
    }

    private void calculateCameraPosition(float horizontalDistance, float vertcalDistance) {
        float theta = angleAroundCenter;
        float offsetX = (float) (horizontalDistance * Math.sin(Math.toRadians(theta)));
        float offsetZ = (float) (horizontalDistance * Math.cos(Math.toRadians(theta)));
        position.x = center.x - offsetX;
        position.z = center.z - offsetZ;
        position.y = center.y + vertcalDistance;
    }

    private void calculateZoom() {
        float zoomLevel = Mouse.getDWheel() * 0.1f;
        float newValue = distanceFromCenter - zoomLevel;
        if (newValue < MAX_DISTANCE && newValue > MIN_DISTANCE) {
            distanceFromCenter -= zoomLevel;
        }
    }

    private void calculatePitch() {
        if (Mouse.isButtonDown(1)) {
            float pitchChange = Mouse.getDY() * 0.1f;
            float newValue = pitch - pitchChange;
            if (newValue < MAX_PITCH && newValue > MIN_PITCH) {
                pitch -= pitchChange;
            }
        }
    }

    private void calculateAroundCenter() {
        if (Mouse.isButtonDown(0)) {
            float angleChange = Mouse.getDX() * 0.3f;
            angleAroundCenter -= angleChange;
        }
    }
}
