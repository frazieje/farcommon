package com.spoohapps.farcommon.model;

import java.util.Random;

public class RandomProfileGenerator implements ProfileGenerator {

    @Override
    public Profile generate() {

        Random random = new Random();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < Profile.PROFILE_ID_LENGTH; i++) {

            sb.append(Profile.profileHexString.charAt(random.nextInt(Profile.profileHexString.length())));

        }

        return Profile.from(sb.toString());

    }

}
