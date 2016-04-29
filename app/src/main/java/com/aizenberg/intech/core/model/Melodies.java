package com.aizenberg.intech.core.model;

import java.util.List;

/**
 * Created by Yuriy Aizenberg
 */
public class Melodies extends BaseResponseModel {

    private List<Melody> melodies;

    public List<Melody> getMelodies() {
        return melodies;
    }

    public void setMelodies(List<Melody> melodies) {
        this.melodies = melodies;
    }
}
