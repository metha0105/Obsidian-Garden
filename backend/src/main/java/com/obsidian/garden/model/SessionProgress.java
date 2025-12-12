package com.obsidian.garden.model;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SessionProgress {
    private int xp;
    private int level;
    private int streak;

    public SessionProgress initial() {
        return new SessionProgress(0, 1, 0);
    }
}
