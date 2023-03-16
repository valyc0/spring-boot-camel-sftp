docker run -d \
  --name=openssh-server \
  --hostname=openssh-server `#optional` \
  -e PUID=1000 \
  -e PGID=1000 \
  -e TZ=Etc/UTC \
  -e PUBLIC_KEY="$(cat ../certs/sftp_rsa.pub)" `#optional` \
  -e SUDO_ACCESS=false `#optional` \
  -e PASSWORD_ACCESS=true `#optional` \
  -e USER_PASSWORD=admin `#optional` \
  -e USER_NAME=admin `#optional` \
  -v $PWD/tmp:/config/tmp \
  -p 2222:2222 \
  --restart unless-stopped \
  lscr.io/linuxserver/openssh-server:latest
