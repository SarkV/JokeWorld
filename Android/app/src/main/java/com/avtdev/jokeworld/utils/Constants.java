package com.avtdev.jokeworld.utils;

public class Constants {

    public static final class PERMISSIONS{
        public static final int READ_USER = 1;
        public static final int READ_JOKE = 2;
    }

    public static final class CALLBACKS{
        public static final int USER_IMAGE = 1;
        public static final int JOKE_IMAGE= 2;
    }

    public static final class PREFERENCES{
        public static final String PREFERENCES_FILE = "JOKE_PREFERENCES";

        public static final String ACCESS_TOKEN = "ACCESS_TOKEN";
        public static final String REFRESH_TOKEN = "REFRESH_TOKEN";
    }

    public static final class HTTP_CODES{
        public static final int OK = 200;

        public static final int UNAUTHORIZED = 401;
    }


}
