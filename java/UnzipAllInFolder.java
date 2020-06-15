package de.tp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class UnzipAllInFolder {

	public static void main(String[] args) {
		UnzipAllInFolder uaif = new UnzipAllInFolder();
		uaif.go(args[0], args[1]);
		System.out.println("Finished.");
	}

	public void go(String strZipPath, String strDestinationPath) {
		try (Stream<Path> walk = Files.walk(Paths.get(strZipPath))) {

			List<String> result = walk.map(x -> x.toString()).filter(f -> f.endsWith(".zip")).collect(Collectors.toList());
//			result.forEach(System.out::println);
//			result.forEach(UnzipAllInFolder::unzip);
			for (int i = 0; i < result.size(); i++) {
				unzip(strDestinationPath, result.get(i));
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public void unzip(String strDestinationPath, String strFilename) {
		ZipInputStream zis = null;
		ZipEntry ze = null;
        FileInputStream fis = null;
        FileOutputStream fos = null;
		File newFile = null;
//		File newFile = null;
		String fileName = null;
//		String fileName = null;
        int len;
		byte[] buffer = new byte[1024];

        File dir = new File(strDestinationPath);

		// create output directory if it doesn't exist
		if (!dir.exists()) {
			dir.mkdirs();
		}

		// buffer for read and write data to file
		try {
			fis = new FileInputStream(strFilename);
			zis = new ZipInputStream(fis);
			ze = zis.getNextEntry();
			while(ze != null){
				fileName = ze.getName();
				newFile = new File(strDestinationPath + File.separator + fileName);
				System.out.println("Unzipping to " + newFile.getAbsolutePath());

				//create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();

                fos = new FileOutputStream(newFile);
                while ((len = zis.read(buffer)) > 0) {
                	fos.write(buffer, 0, len);
                }
//                fos.close();

                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        	try {
    			//close last ZipEntry
                fos.close();
			} catch (Exception e2) {
				// do nothing
			}
        	try {
        		//close last ZipEntry
        		zis.closeEntry();
        	} catch (Exception e2) {
        		// do nothing
        	}
        	try {
        		//close last ZipEntry
        		zis.close();
        	} catch (Exception e2) {
        		// do nothing
        	}
        	try {
        		//close last ZipEntry
        		fis.close();
        	} catch (Exception e2) {
        		// do nothing
        	}
        }
	}
}
package test;

/*
 * Download todays Dilbert and save it as d1.gif
 *
 * URL Hauptseite f|r einen bestimmten Tag
 * http://www.dilbert.com/fast/2008-10-07/
 *
 */

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.imageio.ImageIO;

/**
 *
 * @author eo865
 */

public class GetOneRealDilbertForMuVToday {

        private static String writeFileName = "d1.gif";

        private GregorianCalendar gc = null;

        public GetOneRealDilbertForMuVToday() {
                gc = new GregorianCalendar();
        }

        public static void main(String[] args) {
                System.setProperty("proxyPort", "8080");
                System.setProperty("proxyHost", "webproxy.deutsche-boerse.de");

                // Argumente ausgeben
//              for (String i : args) {
//                      System.out.println(i);
//              }

                String pTag = null;
                String pMonat = null;
                String pJahr = null;
                String pFilename = null;

                for (int i=0; i < args.length; i++) {
                        if (args[i].equals("-t")) {
                                pTag = args[++i];
                        } else if (args[i].equals("-m")) {
                                pMonat = args[++i];
                        } else if (args[i].equals("-j")) {
                                pJahr = args[++i];
                        } else if (args[i].equals("-f")) {
                                pFilename = args[++i];
                        } else {
                                System.out.println("Unknown argument: " + args[i]);
                        }
                }

//        boolean prevWhitespace = true;
//        while (index < line.length()) {
//            char c = line.charAt(index++);
//            boolean currWhitespace = Character.isWhitespace);
//            if (prevWhitespace && !currWhitespace) {
//                numWords++;
//            }
//            prevWhitespace = currWhitespace;
//        }

//              String fileName = null;
//              if (args.length == 1) {
//                      fileName = args[0];
//              } else {
//                      fileName = writeFileName;
//              }

                // Brauche ich diesen Teil noch?
//              String fileName = null;
//              if (pFilename == null) {
//                      fileName = writeFileName;
//              } else {
//                      fileName = pFilename;
//              }

                if (pFilename == null) {
                        pFilename = writeFileName;
                }

                GetOneRealDilbertForMuVToday dnd = new GetOneRealDilbertForMuVToday();
//              System.out.println(dnd.gc);
                dnd.start(pTag, pMonat, pJahr, pFilename);
        }

        public void nextDay() {
                gc.add(Calendar.DATE, 1);
        }

        public void prevDay() {
                gc.add(Calendar.DATE, -1);
        }

        public String getUrlToday() {
                return "http://comics.dp.cx/" + getDatumString() + "/Dilbert-"
                                + getDatumString() + ".gif";
        }

        public String getDatumString() {
                String d_tag = fixInt(gc.get(Calendar.DATE));
                String d_monat = fixInt(gc.get(Calendar.MONTH) + 1);
                String d_jahr = Integer.toString(gc.get(Calendar.YEAR));

                String datumStr = d_jahr + "." + d_monat + "." + d_tag;

                return datumStr;
        }

        public void start(String pTag, String pMonat, String pJahr, String fileName) {
                // prevDay();

//              System.out.println("pTag: " + pTag);
//              System.out.println("pMonat: " + pMonat);
//              System.out.println("pJahr: " + pJahr);

                if (pTag != null) {
                        gc.set(Calendar.DAY_OF_MONTH, Integer.parseInt(pTag));
                }
                if (pMonat != null) {
                        gc.set(Calendar.MONTH, Integer.parseInt(pMonat) - 1);
                }
                if (pJahr != null) {
                        gc.set(Calendar.YEAR, Integer.parseInt(pJahr));
                }

                getOnePic(fileName);
        }

        private void getOnePic(String fileName) {
//                String dilbertUrl = "http://www.dilbert.com" + getPicUrlDate(gc);
//              String dilbertUrl = "http://www.dilbert.com" + getPicUrlToday();
                String dilbertUrl = getPicUrlDate(gc);

//              System.out.println("dilbertUrl: " + dilbertUrl);

                if (dilbertUrl.indexOf("null") < 0)
                {
                        // es wurde eine Picture Url gefunden, ansonsten st|nde 'null' am Ende des Strings

                        try {
                                URL url = new URL("http:" + dilbertUrl);
                                BufferedImage bi = ImageIO.read(url);
                                ImageIO.write(bi, "gif", new File(fileName));
                        } catch (javax.imageio.IIOException ioex) {
                                System.err.println("IO Exception: " + ioex);
                        } catch (Exception ex) {
                                System.err.println("Exception: " + ex);
                        }
                }
        }

        public static String fixInt(int i) {
                if (i < 10) {
                        return "0" + Integer.toString(i);
                } else {
                        return Integer.toString(i);
                }
        }

        private String getPicUrlDate(GregorianCalendar gc) {
                String dilbertPicLine = null;
                String inputLine = null;
                String strUrl = null;
                URL url = null;
                URLConnection uc = null;
                BufferedReader in = null;

//                String pTag = String.valueOf(gc.get(Calendar.DAY_OF_MONTH));
//                String pMonat = String.valueOf(gc.get(Calendar.MONTH) + 1);
                String pTag = fixInt(gc.get(Calendar.DAY_OF_MONTH));
                String pMonat = fixInt(gc.get(Calendar.MONTH) + 1);
                String pJahr = String.valueOf(gc.get(Calendar.YEAR));
//              System.out.println("pTag: " + pTag);
//              System.out.println("pMonat: " + pMonat);
//              System.out.println("pJahr: " + pJahr);

                try {
                	// p>The document has moved <a href="http://dilbert.com/strip/2018-11-21">here</a>.</p>
//                	strUrl = "http://www.dilbert.com/strip/" + pJahr + "-" + pMonat + "-" + pTag;
//                	strUrl = "http://dilbert.com/strip/" + pJahr + "-" + pMonat + "-" + pTag;
                	strUrl = "https://dilbert.com/strip/" + pJahr + "-" + pMonat + "-" + pTag;
//                	System.out.println(strUrl);

//                        url = new URL("http://www.dilbert.com/fast/" + pJahr + "-" + pMonat + "-" + pTag + "/");
                        url = new URL(strUrl);
                        uc = url.openConnection();
                        in = new BufferedReader(new InputStreamReader(uc.getInputStream()));

                        while ((inputLine = in.readLine()) != null) {
//                                System.out.println(inputLine);
//                                if (inputLine.indexOf("str_strip") > 0) {
                                if (inputLine.indexOf("img-responsive img-comic") > 0) {
//                                    System.out.println(inputLine);
                                	
                                	// System.out.println(inputLine);
                                    String strSearch1 = "src=\"";
                                    String strSearch2 = "\"";
                                        int posStart = inputLine.indexOf(strSearch1);
                                        int posEnde = inputLine.indexOf(strSearch2, posStart + strSearch1.length());

                                        dilbertPicLine = inputLine.substring(posStart + strSearch1.length(), posEnde);
//                                        System.out.println(dilbertPicLine);
                                }
                        }

//                      System.out.println("");
//                      System.out.println("-> " + dilbertPicLine);

                }
                catch (IOException ex) {
                        ex.printStackTrace();
                        return null;
                }
                finally
                {
                        try {
                                in.close();
                        } catch (IOException e) {
//                                e.printStackTrace();
                                return null;
                        }
                }

//                System.out.println("dilbertPicLine: " + dilbertPicLine);
                return dilbertPicLine;
        }

        private String getPicUrlToday() {
                String dilbertPicLine = null;
                String inputLine = null;
                URL url = null;
                URLConnection uc = null;
                BufferedReader in = null;

                try {
                        url = new URL("http://www.dilbert.com/fast/");
                        uc = url.openConnection();
                        in = new BufferedReader(new InputStreamReader(uc.getInputStream()));

                        while ((inputLine = in.readLine()) != null) {
                                // System.out.println(inputLine);
                                if (inputLine.indexOf("str_strip") > 0) {
                                        // System.out.println(inputLine);
                                        int posStart = inputLine.indexOf("\"");
                                        int posEnde = inputLine.indexOf("\"", posStart + 1);

                                        dilbertPicLine = inputLine.substring(posStart + 1, posEnde);
                                }
                        }

//                      System.out.println("");
//                      System.out.println("-> " + dilbertPicLine);

                }
                catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        return null;
                }
                finally
                {
                        try {
                                in.close();
                        } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                                return null;
                        }
                }

                return dilbertPicLine;
        }
}
[root@bart ~]# cat /usr/lib/systemd/system/jenkins.service
[Unit]
Description=Jenkins continuous build server
After=syslog.target network.target

[Service]
EnvironmentFile=-/etc/sysconfig/jenkins
ExecStart=/usr/libexec/jenkins/jenkins start
ExecStop=/usr/libexec/jenkins/jenkins stop
Type=forking
PIDFile=/var/run/jenkins.pid

[Install]
WantedBy=multi-user.target
---
#!/bin/sh
#
#
### BEGIN INIT INFO
# Provides:          jenkins
# Required-Start:    $local_fs $remote_fs $network $time $named
# Should-Start: $time sendmail
# Required-Stop:     $local_fs $remote_fs $network $time $named
# Should-Stop: $time sendmail
# Default-Start:     3 5
# Default-Stop:      0 1 2 6
# Short-Description: Jenkins continuous build server
# Description:       Start the Jenkins continuous build server
### END INIT INFO

# Check for existence of needed config file and read it
JENKINS_CONFIG=/etc/sysconfig/jenkins
test -e "$JENKINS_CONFIG" || { echo "$JENKINS_CONFIG not existing";
	if [ "$1" = "stop" ]; then exit 0;
	else exit 6; fi; }
test -r "$JENKINS_CONFIG" || { echo "$JENKINS_CONFIG not readable. Perhaps you forgot 'sudo'?";
	if [ "$1" = "stop" ]; then exit 0;
	else exit 6; fi; }

JENKINS_PID_FILE="/var/run/jenkins.pid"

# Source function library.
. /etc/init.d/functions

# Read config	
[ -f "$JENKINS_CONFIG" ] && . "$JENKINS_CONFIG"

# Set up environment accordingly to the configuration settings
[ -n "$JENKINS_HOME" ] || { echo "JENKINS_HOME not configured in $JENKINS_CONFIG";
	if [ "$1" = "stop" ]; then exit 0;
	else exit 6; fi; }
[ -d "$JENKINS_HOME" ] || { echo "JENKINS_HOME directory does not exist: $JENKINS_HOME";
	if [ "$1" = "stop" ]; then exit 0;
	else exit 1; fi; }

# clean up after my nasty symlink workaround
if [ -d "${JENKINS_HOME}/plugins" ]; then
  for f in "${JENKINS_HOME}"/plugins/*.jpi; do
    if [ -L "$f" ] && [ ! -e "$f" ]; then
      rm -f "$f"
    fi
  done
fi

# Search usable Java. We do this because various reports indicated
# that /usr/bin/java may not always point to Java 1.5
# see http://www.nabble.com/guinea-pigs-wanted-----Hudson-RPM-for-RedHat-Linux-td25673707.html
for candidate in /etc/alternatives/java /usr/lib/jvm/java-1.6.0/bin/java /usr/lib/jvm/jre-1.6.0/bin/java /usr/lib/jvm/java-1.5.0/bin/java /usr/lib/jvm/jre-1.5.0/bin/java /usr/bin/java
do
  [ -x "$JENKINS_JAVA_CMD" ] && break
  JENKINS_JAVA_CMD="$candidate"
done

JENKINS_WEBROOT="/usr/share/jenkins/webroot/"

CLASSPATH="${JENKINS_WEBROOT}/:${JENKINS_WEBROOT}/winstone.jar:`build-classpath jetty8/jetty-util-8.1.14.v20131031 jetty8/jetty-security-8.1.14.v20131031 jetty8/jetty-server-8.1.14.v20131031 jetty8/jetty-webapp-8.1.14.v20131031 jetty8/jetty-servlet-8.1.14.v20131031 jetty8/jetty-http-8.1.14.v20131031 jetty8/jetty-io-8.1.14.v20131031 jetty8/jetty-continuation-8.1.14.v20131031 jetty8/jetty-xml-8.1.14.v20131031 glassfish-servlet-api`"

JAVA_CMD="$JENKINS_JAVA_CMD $JENKINS_JAVA_OPTIONS -DJENKINS_HOME=$JENKINS_HOME -cp $CLASSPATH Main"
PARAMS="--logfile=/var/log/jenkins/jenkins.log --extractedFilesFolder=$JENKINS_WEBROOT --webroot=$JENKINS_WEBROOT --daemon"
[ -n "$JENKINS_PORT" ] && PARAMS="$PARAMS --httpPort=$JENKINS_PORT"
[ -n "$JENKINS_LISTEN_ADDRESS" ] && PARAMS="$PARAMS --httpListenAddress=$JENKINS_LISTEN_ADDRESS"
[ -n "$JENKINS_HTTPS_PORT" ] && PARAMS="$PARAMS --httpsPort=$JENKINS_HTTPS_PORT"
[ -n "$JENKINS_HTTPS_KEYSTORE" ] && PARAMS="$PARAMS --httpsKeyStore=$JENKINS_HTTPS_KEYSTORE"
[ -n "$JENKINS_HTTPS_KEYSTORE_PASSWORD" ] && PARAMS="$PARAMS --httpsKeyStorePassword='$JENKINS_HTTPS_KEYSTORE_PASSWORD'"
[ -n "$JENKINS_HTTPS_LISTEN_ADDRESS" ] && PARAMS="$PARAMS --httpsListenAddress=$JENKINS_HTTPS_LISTEN_ADDRESS"
[ -n "$JENKINS_AJP_PORT" ] && PARAMS="$PARAMS --ajp13Port=$JENKINS_AJP_PORT"
[ -n "$JENKINS_AJP_LISTEN_ADDRESS" ] && PARAMS="$PARAMS --ajp13ListenAddress=$JENKINS_AJP_LISTEN_ADDRESS"
[ -n "$JENKINS_DEBUG_LEVEL" ] && PARAMS="$PARAMS --debug=$JENKINS_DEBUG_LEVEL"
[ -n "$JENKINS_HANDLER_STARTUP" ] && PARAMS="$PARAMS --handlerCountStartup=$JENKINS_HANDLER_STARTUP"
[ -n "$JENKINS_HANDLER_MAX" ] && PARAMS="$PARAMS --handlerCountMax=$JENKINS_HANDLER_MAX"
[ -n "$JENKINS_HANDLER_IDLE" ] && PARAMS="$PARAMS --handlerCountMaxIdle=$JENKINS_HANDLER_IDLE"
[ -n "$JENKINS_ARGS" ] && PARAMS="$PARAMS $JENKINS_ARGS"

if [ "$JENKINS_ENABLE_ACCESS_LOG" = "yes" ]; then
    PARAMS="$PARAMS --accessLoggerClassName=winstone.accesslog.SimpleAccessLogger --simpleAccessLogger.format=combined --simpleAccessLogger.file=/var/log/jenkins/access_log"
fi

RETVAL=0

case "$1" in
    start)
	echo -n "Starting Jenkins "
	daemon --user "$JENKINS_USER" --pidfile "$JENKINS_PID_FILE" $JAVA_CMD $PARAMS > /dev/null
	RETVAL=$?
	if [ $RETVAL = 0 ]; then
	    success
	    echo > "$JENKINS_PID_FILE"  # just in case we fail to find it
            MY_SESSION_ID=`/bin/ps h -o sess -p $$`
            # get PID
            /bin/ps hww -u "$JENKINS_USER" -o sess,ppid,pid,cmd | \
            while read sess ppid pid cmd; do
		[ "$ppid" = 1 ] || continue
		# this test doesn't work because Jenkins sets a new Session ID
                # [ "$sess" = "$MY_SESSION_ID" ] || continue
	       	echo "$cmd" | grep $JENKINS_WEBROOT > /dev/null
		[ $? = 0 ] || continue
		# found a PID
		echo $pid > "$JENKINS_PID_FILE"
	    done
	else
	    failure
	fi
	echo
	;;
    stop)
	echo -n "Shutting down Jenkins "
	killproc jenkins
	RETVAL=$?
	echo
	;;
    try-restart|condrestart)
	if test "$1" = "condrestart"; then
		echo "${attn} Use try-restart ${done}(LSB)${attn} rather than condrestart ${warn}(RH)${norm}"
	fi
	$0 status
	if test $? = 0; then
		$0 restart
	else
		: # Not running is not a failure.
	fi
	;;
    restart)
	$0 stop
	$0 start
	;;
    force-reload)
	echo -n "Reload service Jenkins "
	$0 try-restart
	;;
    reload)
    	$0 restart
	;;
    status)
    	status jenkins
	RETVAL=$?
	;;
    probe)
	## Optional: Probe for the necessity of a reload, print out the
	## argument to this init script which is required for a reload.
	## Note: probe is not (yet) part of LSB (as of 1.9)

	test "$JENKINS_CONFIG" -nt "$JENKINS_PID_FILE" && echo reload
	;;
    *)
	echo "Usage: $0 {start|stop|status|try-restart|restart|force-reload|reload|probe}"
	exit 1
	;;
esac
exit $RETVAL
