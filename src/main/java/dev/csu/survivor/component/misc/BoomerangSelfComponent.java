package dev.csu.survivor.component.misc;

import com.almasb.fxgl.entity.component.Component;

public class BoomerangSelfComponent extends Component
{
    private final double rotationSpeed;
    private double angle;

    public BoomerangSelfComponent(double rotationSpeed)
    {
        this.rotationSpeed = rotationSpeed;
        this.angle = 0;
    }

    @Override
    public void onUpdate(double tpf)
    {
        angle += rotationSpeed * tpf;
        entity.setRotation(angle);
    }
}
