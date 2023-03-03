
package cn.com.infosec.data.structure.asn.exception;

public class AsnEncodeException extends AsnException {

  public AsnEncodeException(final String msg) {
    super(msg);
  }

  public AsnEncodeException(final Exception e) {
    super(e);
  }

  public AsnEncodeException(final String msg, final Exception e) {
    super(msg, e);
  }
}
