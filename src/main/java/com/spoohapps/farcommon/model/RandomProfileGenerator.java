package com.spoohapps.farcommon.model;

import java.util.Random;

public class RandomProfileGenerator implements ProfileGenerator {

    @Override
    public Profile generate() {

        Random random = new Random();

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < Profile.profileIdLength; i++) {

            sb.append(Profile.profileIdCharacterString.charAt(random.nextInt(Profile.profileIdCharacterString.length())));

        }

        return Profile.from(sb.toString());

    }

}
