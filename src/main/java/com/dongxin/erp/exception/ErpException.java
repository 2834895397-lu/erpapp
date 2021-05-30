package com.dongxin.erp.exception;

import org.jeecg.common.exception.JeecgBootException;

public class ErpException extends JeecgBootException {
    public ErpException(String message) {
        super(message);
    }

    public ErpException(Throwable cause) {
        super(cause);
    }

    public ErpException(String message, Throwable cause) {
        super(message, cause);
    }
}
