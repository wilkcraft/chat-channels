package com.chatChannels.discord;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordWebhook {

  private final String url;
  private String content;
  private String username;
  private String avatarUrl;

  public void setUsername(String username) {
    this.username = username;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public DiscordWebhook(String url) {
    this.url = url;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public void execute() throws Exception {
    URL url = new java.net.URI(this.url).toURL();
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    conn.setRequestMethod("POST");
    conn.addRequestProperty("Content-Type", "application/json");
    conn.setDoOutput(true);

    String safeContent = content
        .replace("\\", "\\\\")
        .replace("\"", "\\\"")
        .replace("\n", "\\n");

    String json = "{"
        + "\"content\": \"" + safeContent + "\","
        + "\"username\": \"" + (username != null ? username : "Minecraft") + "\","
        + "\"avatar_url\": \"" + (avatarUrl != null ? avatarUrl : "") + "\""
        + "}";

    OutputStream stream = conn.getOutputStream();
    stream.write(json.getBytes());
    stream.flush();
    stream.close();

    conn.getInputStream().close();
  }
}
