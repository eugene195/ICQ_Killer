package entities;

import java.util.HashMap;

/**
 * Created by eugene on 11.03.15.
 */
public interface BaseEntity {
    public Sendable create(HashMap<String, Object> parameters);
}
