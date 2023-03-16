package nwdong.camelsftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.commons.io.FileUtils;
import com.jcraft.jsch.*;

public class SftpClient {

    private String sftpHost;
    private int sftpPort;
    private String sftpUsername;
    private String sftpPassword;
    private String sftpPrivateKeyPath;
    private String sftpRemoteDirectory;
    private String sftpLocalDirectory;

    public SftpClient(String propertiesFilePath) throws IOException {
        Properties props = new Properties();
        props.load(new FileInputStream(propertiesFilePath));

        this.sftpHost = props.getProperty("sftp.host");
        this.sftpPort = Integer.parseInt(props.getProperty("sftp.port"));
        this.sftpUsername = props.getProperty("sftp.username");
        this.sftpPassword = props.getProperty("sftp.password");
        this.sftpPrivateKeyPath = props.getProperty("sftp.privateKeyPath");
        this.sftpRemoteDirectory = props.getProperty("sftp.remoteDirectory");
        this.sftpLocalDirectory = props.getProperty("sftp.localDirectory");
    }

    public void sendFiles() throws JSchException, SftpException, IOException {
        JSch jsch = new JSch();

        if (sftpPrivateKeyPath != null) {
            jsch.addIdentity(sftpPrivateKeyPath);
        }

        System.out.println("Connecting to SFTP server " + sftpHost + " on port " + sftpPort + " as user " + sftpUsername);

        Session session = jsch.getSession(sftpUsername, sftpHost, sftpPort);

        if (sftpPassword != null) {
            session.setPassword(sftpPassword);
        }

        session.setConfig("StrictHostKeyChecking", "no");
        session.connect();

        System.out.println("Connected to SFTP server");

        ChannelSftp channelSftp = (ChannelSftp) session.openChannel("sftp");
        channelSftp.connect();

        System.out.println("Connected to SFTP channel");

        File directory = new File(sftpLocalDirectory);
        File[] files = directory.listFiles();

        for (File file : files) {
            if (file.isFile()) {
                String remoteFilePath = sftpRemoteDirectory + "/" + file.getName(); ;
                channelSftp.put(new FileInputStream(file), remoteFilePath);
                FileUtils.moveFileToDirectory(file, new File("archive"), true);
                System.out.println("Sent file " + file.getName() + " to remote directory " + sftpRemoteDirectory + " and moved it to archive directory");
            }
        }

        channelSftp.disconnect();
        session.disconnect();

        System.out.println("Disconnected from SFTP channel and server");
    }

    public static void main(String[] args) {
        try {
            SftpClient sftpClient = new SftpClient("config.properties");
            while (true) {
                sftpClient.sendFiles();
                Thread.sleep(5000); // wait 2 seconds before polling again
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
