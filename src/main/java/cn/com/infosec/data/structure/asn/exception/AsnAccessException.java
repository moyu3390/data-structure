
package cn.com.infosec.data.structure.asn.exception;

public class AsnAccessException extends AsnException {

  public AsnAccessException(final String msg) {
    super(msg);
  }

  public AsnAccessException(final Exception e) {
    super(e);
  }

  public AsnAccessException(final String msg, final Exception e) {
    super(msg, e);
  }
}
