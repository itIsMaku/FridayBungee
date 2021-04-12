package cz.maku.friday.server;

import lombok.Getter;

import java.util.Random;

@Getter
@Deprecated
public class ServerId {

    private final String hex;

    public ServerId() {
        hex = create();
    }

    private String create() {
        Random r = new Random();
        int n = r.nextInt();
        return Integer.toHexString(n);
    }

}
