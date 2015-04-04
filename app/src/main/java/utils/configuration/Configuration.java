package utils.configuration;


import android.os.Environment;

import base.icq_killer.loggers.ImmediateLogger;
import base.icq_killer.loggers.LazyLogger;
import base.icq_killer.loggers.Logger;

/**
 * Created by eugene on 27.03.15.
 */
public class Configuration {
    public static LazyLogger logger = (LazyLogger) LazyLogger.getInstance("AndroidApp");
    public static String HTTP_SERVER_URL = "http://immense-bayou-7299.herokuapp.com";
    public static String WS_SERVER_URL = "ws://immense-bayou-7299.herokuapp.com/message/create";
    public static String LOG_FILENAME = "/Download/logs1.txt";
    public static String PROTOCOL = "REST";
    public static String ENCRYPTION = "enabled";
    public static class Encrypt {
        public static String asin_realisation = "first";
        public static String sin_realisation = "second";
        public static String working_directory = String.valueOf(Environment.getExternalStorageDirectory());
    }
    public static class Login {
//        Default value
        public static String URL = "/user/create";

        public static class ClientToServer {
            public static String nickname = "nickname";
        }

        public static class ServerToClient {
            public static String status = "status";
            public static String OK = "OK";
            public static String FAIL = "FAIL";
            public static String clients = "clients";
        }
    }

    public static class SocketAction {
        public static String action = "action";
        public static String connparameter = "nickname";
        public static String data = "data";
        public static class Message {
            public static String action = "message";

            public static class ClientToServer {
                public static String from = "from";
                public static String to = "whom";
                public static String message = "message";
            }

            public static class ServerToClient {
                public static String from = "from";
                public static String to = "whom";
//                Correct in online protocol
                public static String message = "message";
            }
        }

        public static class ComeIn {
            public static String action = "user_come_in";

            public static class ServerToClient {
                public static String nickname = "nickname";
            }
        }

        public static class Download {
            public static String action = "download";

            public static class ServerToClient {
                public static String from = "from";
                public static String url = "url";
            }
        }

        public static class GoOut {
            public static String action = "user_went_out";

            public static class ServerToClient {
                public static String nickname = "nickname";
            }
        }

        public static class EncryptStart {
            public static String action = "encrypt_start";

            public static class ServerToClient {
                public static String simKey = "simKey";
            }
        }
    }
}
