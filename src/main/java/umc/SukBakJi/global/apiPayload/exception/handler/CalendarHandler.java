package umc.SukBakJi.global.apiPayload.exception.handler;

import umc.SukBakJi.global.apiPayload.code.BaseErrorCode;
import umc.SukBakJi.global.apiPayload.exception.GeneralException;

public class CalendarHandler extends GeneralException {
    public CalendarHandler(BaseErrorCode code) {
        super(code);
    }
}

