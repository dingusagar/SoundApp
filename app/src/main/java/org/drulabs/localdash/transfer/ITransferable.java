package org.drulabs.localdash.transfer;

import java.io.Serializable;


public interface ITransferable extends Serializable {

    int getRequestCode();

    String getRequestType();

    String getData();
}
