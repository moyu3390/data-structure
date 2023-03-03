
package cn.com.infosec.data.structure.asn.exception;

public class AsnConfigurationException extends AsnException {

  public AsnConfigurationException(final String msg) {
    super(msg);
  }

  public AsnConfigurationException(final Exception e) {
    super(e);
  }

  public AsnConfigurationException(final String msg, final Exception e) {
    super(msg, e);
  }
}
