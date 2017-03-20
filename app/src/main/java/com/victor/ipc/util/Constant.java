package com.victor.ipc.util;

/**
 * Created by victor on 2015/12/25.
 */
public class Constant {
    public static final String QUERY_KEY						= "QUERY_KEY";
    public static final String DATA_KEY						= "DATA_KEY";
    /**
     * 数据库配置信息
     * @author victor
     * @date 2016-2-24
     */
    public static class DbConfig {
        public static final String DB_NAME 						= "movie_db";
        public static final String SCHEME 						= "content://";
        public static final String AUTHORITY 					= "content.victor.ipcclient.content";
        public static final String CALLED_AUTHORITY 			= "content.victor.ipcserver.content";
        public static final String URI_PATH 					    = SCHEME + AUTHORITY + "/";
        public static final String CALLED_URI_PATH 				= SCHEME + CALLED_AUTHORITY + "/";
        public static final int DB_VERSION 						= 8;
    }

    public static class TB {
        public static final String MOVIE 						    = "movie";
    }

    public static class Action {
        public static final int LIVE_ACTION                     = 1;
        public static final int LOGIN                            = 2;
        public static final int REGIST                           = 3;
        public static final int CHAT                              = 4;
    }
}
