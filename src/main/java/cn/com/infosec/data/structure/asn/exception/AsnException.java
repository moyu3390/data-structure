
package cn.com.infosec.data.structure.asn.exception;

import cn.com.infosec.data.structure.exception.StructureException;

public abstract class AsnException extends StructureException {

  public AsnException(final String msg) {
    super(msg);
  }

  public AsnException(final Exception e) {
    super(e);
  }

  public AsnException(final String msg, final Exception e) {
    super(msg, e);
  }
}
