package com.example.nobuya.getmetar;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;


/**
 * Created by nobuya on 2014/08/28.
 */
public class GetMetar {
    static final String HOST = "www.aviationweather.l.noaa.gov";
    static final String LOC = "http://www.aviationweather.gov/adds/metars/?station_ids=%4s&std_trans=standard&chk_metars=on&hoursStr=most+recent+only&submitmet=Submit";

    static final int PORT = 80;
    static final String HTTP_VERSION = "HTTP/1.0";
    static final String DEFAULT_CCCC = "rjtt";

    boolean verboseMode = false;
    //boolean verboseMode = true;

    public static void main(String args[]) {
        String cccc = DEFAULT_CCCC;
        if (args.length >= 1)
            cccc = args[0];

        String metar = GetMetar.getMetarMessage(cccc);
        System.out.println("cccc:   " + cccc);
        System.out.println("METAR: " + metar);
        String dateTime = GetMetar.getDateAndTime(metar);
        String airport = GetMetar.getICAOCode(metar);
        System.out.println("Airpot:    " + airport);
        System.out.println("DATE_TIME: " + dateTime);
    }

    OutputStream outputStream = null;
    PrintStream printStream = null;
    InputStreamReader inputStreamReader = null;

    private final Socket connect(String host, int port) {
        Socket so = null;
        msg("*** connecting %s:%d...", host, port);
        try {
            so = new Socket(host, port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            error(String.format("GetMetar.connect(): connection refused: %s:%d",
                    host, port));
        }
        if (so == null)
            return null;
        try {
            outputStream = so.getOutputStream();
            printStream = new PrintStream(outputStream);
            inputStreamReader = new InputStreamReader(so.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        msg("done.\n");
        return so;
    }

    private final void write(String msg) {
        msg("*** write: %s", msg);
        try {
            outputStream.write(msg.getBytes());
        } catch (Exception e) {
            error("write error");
        }
    }

    private final String getCommand(String uri) {
        String command =  String.format("GET %s %s\r\n\n", uri, HTTP_VERSION);
        StringBuffer sb = new StringBuffer();
        write(command);

        int c = 0;
        for (;;) {
            try {
                c = inputStreamReader.read();
            } catch (Exception e) {
                break;
            }
            if (c == -1)
                break;
            sb.append((char)c);
        }
        return sb.toString();
    }

    static String getMetarMessage(String cccc) {
        GetMetar getMetar = new GetMetar();
        String metar = getMetar.getMetar(cccc);
        return metar;
    }

    private String getMetar(String cccc) {
        Socket so = connect(HOST, PORT);
        if (so == null) {
            return "???? ??????? ";
        }
        String command = String.format(LOC, cccc);
        //String response = getCommand(command);
        String response = getCommand(command); // 0.9.7
        msg("*** response message\n");
        if (verboseMode)
            System.out.println(response);
        msg("\n");

        try {
            msg("*** close port\n");
            so.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String metarMessage = getMetarMessageFromResponse(response, cccc);
        return metarMessage;
    }

    private String getMetarMessageFromResponse(String responseMessage,
                                         String cccc) {
        String CCCC = cccc.toUpperCase() + " ";
        int length = responseMessage.length();
        int startPos = responseMessage.indexOf(CCCC);
        StringBuffer sb = new StringBuffer();
        String metarMessage = "???? ??????? ";

    /*
TR VALIGN="top">
      <TD ALIGN="left" COLSPAN="2">
<FONT FACE="Monospace,Courier">RJAA 210500Z 32004KT 270V030 9999 FEW030 SCT060 13/M09 Q1009 BECMG 12008KT RMK
  1CU030 3SC060 A2982</FONT><BR>
      </TD>
    </TR>
     */
        if (startPos != -1) {
            boolean isPrevSpace = false;
            int pos = startPos;
            while (pos < length) {
                char c = responseMessage.charAt(pos);
                if (c == '\n' || c == '\r') {
                    // break;
                } else if (c == '<' &&
                        responseMessage.charAt(pos + 1) == '/') { // </FONT>
                    break;
                } else if (c == ' ') { // "  " --> " "
                    if (!isPrevSpace) {
                        sb.append(c);
                    }
                    isPrevSpace = true;
                } else {
                    sb.append(c);
                    isPrevSpace = false;
                }
                pos++;
            }
            //metarMessage = responseMessage.substring(startPos, pos);
            metarMessage = sb.toString();
        }
        return metarMessage;
    }

    static String getICAOCode(String metar) {
        if (metar.length() >= 13) {
            char c5 = metar.charAt(4);
            if (c5 == ' ') {
                // 0123456789012
                // RJTT 212030Z
                return metar.substring(0, 4);
            }
        }
        return "???";
    }

    static String getDateAndTime(String metar) {
        if (metar.length() >= 13) {
            char c5 = metar.charAt(4);
            if (c5 == ' ') {
                // 0123456789012
                // RJTT 212030Z
                return metar.substring(5, 12);
            }
        }
        return "???";
    }

    static String getWind(String metar) {
        if (metar.length() >= 20) {
            char c12 = metar.charAt(12);
            char c20 = metar.charAt(20);
            if (c12 == ' ' && c20 == ' ') {
                /* TODO: 21016G24KT (Gusts) */
                // 01234567890123456789012345678
                // RJTT 290900Z 07011KT 290V350
                // RJTT 010500Z VRB03KT 9999
                if (metar.length() >= 28 && metar.charAt(24) == 'V') {
                    return metar.substring(13, 28); // 07011KT 290V350
                } else {
                    return metar.substring(13, 20); // 07011KT
                }
            }
        }
        return "???";
    }

    static String getTemperatureAndDewpoint(String metar) {
        // 012345  012345678
        // 21/20   M02/M02
        int len = metar.length();
        int p = 5;
        while (p < (len - 4)) {
            if (metar.charAt(p) == '/') {
                if (metar.charAt(p - 3) == ' ') {
                    if (metar.charAt(p + 3) == ' ') {
                        return metar.substring(p - 2, p + 3); // 21/20
                    } else if (metar.charAt(p + 4) == ' ' && metar.charAt(p + 1) == 'M') {
                        return metar.substring(p - 2, p + 4); // 03/M02
                    }
                } else if (metar.charAt(p - 3) == 'M' && metar.charAt(p - 4) == ' ') {
                    if (metar.charAt(p + 3) == ' ') {
                        return metar.substring(p - 3, p + 3); // M01/01 (invalid...)
                    } else if (metar.charAt(p + 4) == ' ' && metar.charAt(p + 1) == 'M') {
                        return metar.substring(p - 3, p + 4); // M02/M02
                    }
                }
            }
            p++;
        }
        return "??/??";
    }

    private final void error(String msg) {
        System.out.println(msg);
        //System.exit(1);
    }

    private final void msg(String format, Object... args) {
        if (verboseMode) {
            System.out.printf(format, args);
            System.out.flush();
        }
    }
}

