package com.mhlab.br.config;

import org.hibernate.dialect.MySQL5Dialect;

/**
 * Mysql database 방언을 설정하는 클래스
 *
 * Created by MHLab on 18/10/2018..
 */

public class CustomMySql5Dialect extends MySQL5Dialect {
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8";
    }
}
