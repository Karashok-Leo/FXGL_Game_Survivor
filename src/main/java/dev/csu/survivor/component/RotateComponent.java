package dev.csu.survivor.component;

import com.almasb.fxgl.entity.component.Component;

public class RotateComponent extends Component {
    private final double rotationSpeed;
    private double angle;

    public RotateComponent(double rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
        this.angle = 0;
    }

    @Override
    public void onUpdate(double tpf) {
        angle += rotationSpeed * tpf;
        entity.setRotation(angle);
    }
}
