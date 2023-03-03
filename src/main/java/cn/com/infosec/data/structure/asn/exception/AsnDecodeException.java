
package cn.com.infosec.data.structure.asn.exception;

public class AsnDecodeException extends AsnException {

  public AsnDecodeException(final String msg) {
    super(msg);
  }

  public AsnDecodeException(final Exception e) {
    super(e);
  }

  public AsnDecodeException(final String msg, final Exception e) {
    super(msg, e);
  }
}
