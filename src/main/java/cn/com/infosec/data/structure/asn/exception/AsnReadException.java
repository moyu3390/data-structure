
package cn.com.infosec.data.structure.asn.exception;

public class AsnReadException extends AsnException {

  public AsnReadException(final String msg) {
    super(msg);
  }

  public AsnReadException(final Exception e) {
    super(e);
  }

  public AsnReadException(final String msg, final Exception e) {
    super(msg, e);
  }
}
