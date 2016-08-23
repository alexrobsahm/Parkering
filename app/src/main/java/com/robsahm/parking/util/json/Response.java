package com.robsahm.parking.util.json;

import java.util.List;

/**
 * Created by alexrobsahm on 14/11/15.
 */
public class Response {
    public List<Feature> features;

    public CharSequence[] toCharSequence() {
        CharSequence[] charSequences = new CharSequence[features.size()];

        for (int i=0; i<features.size(); i++) {
            charSequences[i] = features.get(i).toString();
        }

        return charSequences;
    }
}
