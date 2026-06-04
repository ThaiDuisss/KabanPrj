package org.kanban.userservice.domain.model;

import lombok.Value;

@Value
public class FullName {
    String value;

    public FullName(String value) {
        this.value = value != null ? value.trim() : null;
    }
}
