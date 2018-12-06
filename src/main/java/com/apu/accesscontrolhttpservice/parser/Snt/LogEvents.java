/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.accesscontrolhttpservice.parser.Snt;

/**
 *
 * @author apu
 */
public class LogEvents {
    
    public static final int LOG_NULLL                  = 0;
    public static final int LOG_ALARM_ZONE             = 1;
    public static final int LOG_RESTORE_ZONE           = 2;
    public static final int LOG_ALARM_24H_ZONE         = 3;
    public static final int LOG_RESTORE_24H_ZONE       = 4;
    public static final int LOG_ALARM_FIRE_ZONE        = 5;
    public static final int LOG_RESTORE_FIRE_ZONE      = 6;

        public static final int LOG_ALL_ZONE_EVENT_MARKER  = 7;

    public static final int LOG_FAIL_220v              = 7;
    public static final int LOG_NORM_220v              = 8;
    public static final int LOG_FAIL_AKKUM             = 9;
    public static final int LOG_NORM_AKKUM             = 10;
    public static final int LOG_FAIL_POWER_12v         = 11;
    public static final int LOG_NORM_POWER_12v         = 12;

    public static final int LOG_FAIL_MODEM             = 13;
    public static final int LOG_NORM_MODEM             = 14;

    public static final int LOG_ARM_USER               = 15;
    public static final int LOG_DISARM_USER            = 16;

    public static final int LOG_AMBIENT_START          = 17;
    public static final int LOG_AMBIENT_STOP           = 18;

    public static final int LOG_CANCEL_ALARM           = 19;
    public static final int LOG_TECHNICAL_ZONE         = 20;
    public static final int LOG_REZET                  = 21;
    public static final int LOG_BALANCE                = 22;

    public static final int LOG_CHANGE_PASS            = 23;
    public static final int LOG_CHANGE_CARDCODE        = 24;

    public static final int LOG_USSD_ENTER             = 25;
    public static final int LOG_CMS_ERROR              = 26;
    public static final int LOG_CME_ERROR              = 27;
    public static final int LOG_CHANGE_SIMCARD         = 28;

    public static final int LOG_FAIL_TAMPER            = 29;
    public static final int LOG_NORM_TAMPER            = 30;
    public static final int LOG_FAIL_KLAVIATURE        = 31;
    public static final int LOG_NORM_KLAVIATURE        = 32;
    public static final int LOG_START_PROGRAMMING      = 33;
    public static final int LOG_ARMSTAY_USER           = 34;
    public static final int LOG_GPRS_ERROR             = 35;
    public static final int LOG_FAIL_ARM_OPEN_ZONE     = 36;

        public static final int LOG_NOT_ERASE_AFTER_RESET_MARKER  = 37;

    public static final int LOG_ENTER_IN_PROG_MODE     = 37;//*8 USER CODE (CALL or KLAVIATURE)
    public static final int LOG_ENTER_ENGINEER         = 38;//*8 ENGINEER CALL only
    public static final int LOG_TEMPERATURE            = 39;
    public static final int LOG_BYPASS_ZONE            = 40;
    public static final int LOG_ENTER_KLAV_ENGINEER    = 41;
    public static final int LOG_UNC_USER_CALL          = 42;
    public static final int LOG_DAY_LIMIT_SMS          = 43;

    public static final int LOG_GRANTED_DOOR           = 44;//Pазрешен
    public static final int LOG_DENIED_DATA_ERROR      = 45;//Запрещен, дата не установлена
    public static final int LOG_DENIED_DOOR            = 46;//Запрещен, точка прохода
    public static final int LOG_DENIED_ARM             = 47;//Запрещен, под охраной
    public static final int LOG_DENIED_WEEKDAY         = 48;//Запрещен, день недели
    public static final int LOG_DENIED_TIME            = 49;//Запрещен, время
    public static final int LOG_UNC_CARD               = 50;//Неизвестная карточка
    public static final int LOG_UNC_PHONE              = 51;//Неизвестный номер телефона
    public static final int LOG_PROVIDED_DOOR          = 52;//Предоставлен
    public static final int LOG_NOENTERED              = 53;//Не входили

    public static final int LOG_EXITBUTTON             = 54;//Exit Button
    public static final int LOG_FORSEOPEN              = 55;//Vzlom Dveri

    public static final int LOG_FREEPASS               = 56;
    public static final int LOG_MODENORMAL             = 57;
    public static final int LOG_LOCATION               = 58;
    public static final int LOG_ADD_USER               = 59;
    public static final int LOG_DEL_USER               = 60;
    
}
