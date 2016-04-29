package com.aizenberg.intech.core.network.service;

import com.aizenberg.intech.core.model.Melodies;
import com.aizenberg.intech.core.network.IService;

import retrofit2.Call;

/**
 * Created by Yuriy Aizenberg
 */
public class MelodiesRestService extends BaseRestService<Melodies> {

    private int limit;
    private int offset;

    public MelodiesRestService(int limit, int offset) {
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    protected Call<Melodies> invoke(IService API) {
        return API.getMelodies(limit, offset);
    }
}
