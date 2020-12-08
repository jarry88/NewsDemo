package org.tianguang.baselib.utils;

import java.math.BigDecimal;

public class BigDecimalUtils {

    public static BigDecimal div(BigDecimal b1, BigDecimal b2) {
        return b1.divide(b2, 4, BigDecimal.ROUND_UP);
    }

}
