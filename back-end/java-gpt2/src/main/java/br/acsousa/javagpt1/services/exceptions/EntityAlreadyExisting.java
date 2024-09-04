package br.acsousa.javagpt1.services.exceptions;

import java.io.Serial;
import java.io.Serializable;

public class EntityAlreadyExisting extends RuntimeException implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public EntityAlreadyExisting(String message) {
        super(message);
    }
}
