
package cn.com.infosec.data.structure.asn.exception;

public class AsnConvertException extends AsnException {

  public AsnConvertException(final String msg) {
    super(msg);
  }

  public AsnConvertException(final Exception e) {
    super(e);
  }

  public AsnConvertException(final String msg, final Exception e) {
    super(msg, e);
  }
}
