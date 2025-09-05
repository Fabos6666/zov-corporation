package ru.zov_corporation.api.system.font.entry;

import ru.zov_corporation.api.system.font.glyph.Glyph;

public record DrawEntry(float atX, float atY, int color, Glyph toDraw) {
}
