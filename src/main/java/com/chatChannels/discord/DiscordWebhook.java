package com.chatChannels.discord;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DiscordWebhook {

  private final String url;
  private String content;

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
        + "\"embeds\": [{"
        + "\"title\": \"" + safeContent.split(":")[0] + "\","
        + "\"description\": \"" + safeContent.substring(safeContent.indexOf(":") + 2) + "\","
        + "\"color\": 5814783"
        + "}]"
        + "}";

    OutputStream stream = conn.getOutputStream();
    stream.write(json.getBytes());
    stream.flush();
    stream.close();

    conn.getInputStream().close();
  }
}
