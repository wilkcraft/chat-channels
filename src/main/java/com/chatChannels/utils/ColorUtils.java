package com.chatChannels.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;

public class ColorUtils {

  private static final LegacyComponentSerializer serializer = LegacyComponentSerializer.builder()
      .character('&')
      .hexColors()
      .build();

  public static Component color(String text) {
    return serializer.deserialize(text);
  }
}
