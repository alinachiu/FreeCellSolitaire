package cs3500.freecell.controller;

import java.io.IOException;
import java.nio.CharBuffer;

/**
 * A mock simulating a failure to read from the Readable.
 */
public final class FailingReadable implements Readable {

  @Override
  public int read(CharBuffer cb) throws IOException {
    throw new IOException("Fail!");
  }
}
