
package cn.com.infosec.data.structure.asn.exception;

public class AsnParseException extends AsnException {

  public AsnParseException(final String msg) {
    super(msg);
  }

  public AsnParseException(final Exception e) {
    super(e);
  }

  public AsnParseException(final String msg, final Exception e) {
    super(msg, e);
  }
}
