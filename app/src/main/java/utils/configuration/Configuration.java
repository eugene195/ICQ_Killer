package utils.configuration;

/**
 * Created by eugene on 27.03.15.
 */
public class Configuration {
    public static String HTTP_SERVER_URL = "http://immense-bayou-7299.herokuapp.com";
    public static String WS_SERVER_URL = "ws://immense-bayou-7299.herokuapp.com/send";
    public static String LOG_FILENAME = "/Download/logs.txt";
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

        public static class GoOut {
            public static String action = "user_went_out";

            public static class ServerToClient {
                public static String nickname = "nickname";
            }
        }
    }
}
