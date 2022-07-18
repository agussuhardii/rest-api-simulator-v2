package com.agussuhardi.restapisimulator.config;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.Charset;
import java.time.LocalDateTime;

/**
 * revisi/log_notif 03/09/21
 *
 * @author agussuhardi
 */
@Slf4j
public class CustomHttpRequestWrapper extends HttpServletRequestWrapper {

  private final String bodyInStringFormat;

  public CustomHttpRequestWrapper(HttpServletRequest request) throws IOException {
    super(request);
    bodyInStringFormat =
        readInputStreamInStringFormat(
            request.getInputStream(), Charset.forName(request.getCharacterEncoding()));

    log.info(
        "request from => {hostname: {}, port: {}, httpMethod: {}, url: {}, requestDate:{}, Body: {}}",
        request.getRemoteHost(),
        request.getServerPort(),
        request.getMethod(),
        request.getRequestURI(),
        LocalDateTime.now(),
        bodyInStringFormat);
  }

  public String getJsonBody() {
    return bodyInStringFormat;
  }

  private String readInputStreamInStringFormat(InputStream stream, Charset charset)
      throws IOException {
    final int MAX_BODY_SIZE = 1024;
    final StringBuilder bodyStringBuilder = new StringBuilder();
    if (!stream.markSupported()) {
      stream = new BufferedInputStream(stream);
    }

    stream.mark(MAX_BODY_SIZE + 1);
    final byte[] entity = new byte[MAX_BODY_SIZE + 1];
    final int bytesRead = stream.read(entity);

    if (bytesRead != -1) {
      bodyStringBuilder.append(new String(entity, 0, Math.min(bytesRead, MAX_BODY_SIZE), charset));
      if (bytesRead > MAX_BODY_SIZE) {
        bodyStringBuilder.append("...");
      }
    }
    stream.reset();

    return bodyStringBuilder.toString();
  }

  @Override
  public BufferedReader getReader() {
    return new BufferedReader(new InputStreamReader(getInputStream()));
  }

  @Override
  public ServletInputStream getInputStream() {
    final ByteArrayInputStream byteArrayInputStream =
        new ByteArrayInputStream(bodyInStringFormat.getBytes());

    return new ServletInputStream() {
      private boolean finished = false;

      @Override
      public boolean isFinished() {
        return finished;
      }

      @Override
      public int available() throws IOException {
        return byteArrayInputStream.available();
      }

      @Override
      public void close() throws IOException {
        super.close();
        byteArrayInputStream.close();
      }

      @Override
      public boolean isReady() {
        return true;
      }

      @Override
      public void setReadListener(ReadListener readListener) {
        throw new UnsupportedOperationException();
      }

      public int read() throws IOException {
        int data = byteArrayInputStream.read();
        if (data == -1) {
          finished = true;
        }
        return data;
      }
    };
  }
}
