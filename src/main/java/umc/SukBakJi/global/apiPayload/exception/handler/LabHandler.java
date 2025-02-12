package umc.SukBakJi.global.apiPayload.exception.handler;

import umc.SukBakJi.global.apiPayload.code.BaseErrorCode;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

public class LabHandler extends GeneralException {
    public LabHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
