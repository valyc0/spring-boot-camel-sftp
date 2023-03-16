package nwdong.camelsftp;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DemoRoute extends RouteBuilder{

  /* 
    @Override
    public void configure() throws Exception {

        from("file:./in?move=.done").to("file:./out/");
        
    }

   */
 
    @Override
    //from local to remote
    public void configure() throws Exception {
        from("file:{{sftp.localDirectory}}"
         // the sub-folder to archive files processed successfully
				//+ "&preMove=inprogress"
                + "?move=.done"
                // the sub-folder to archive files NOT processed succesfully
                + "&moveFailed=.error"
        )
        .log(LoggingLevel.INFO, log, "processing ${headers.CamelFileName}")
        .to("sftp://{{sftp.username}}@{{sftp.host}}:{{sftp.port}}/{{sftp.virtualDirectory}}"
				+ "?privateKeyFile={{private.key.file}}"
				+ "&binary=true"
                // poll interval in ms
               
        );
    }

     

/* 
    @Override
    //from remote to local
    public void configure() throws Exception {
        from("sftp://{{sftp.username}}@{{sftp.host}}:{{sftp.port}}/{{sftp.virtualDirectory}}"
				+ "?privateKeyFile={{private.key.file}}"
				+ "&binary=true"
                // poll interval in ms
                + "&delay=1000"
                // the sub-folder to archive files processed successfully
				+ "&move=.done"
                // the sub-folder to archive files NOT processed succesfully
                + "&moveFailed=.error"
                // to avoid pulling files still in-progress or being written
                + "&readLock=changed"
            ).log(LoggingLevel.INFO, log, "processing ${headers.CamelFileName}")
            .to("file:{{sftp.localDirectory}}");
    }
 */   
}
