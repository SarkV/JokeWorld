package com.avtdev.jokeworld.nerwork.apis;

public interface UserApi {

    @Post("pokemon")
    Call<PokemonFeed> getData();
}
