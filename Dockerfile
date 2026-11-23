FROM ubuntu:24.04

ENV DEBIAN_FRONTEND=noninteractive

RUN apt-get update && apt-get install -y \
    openjdk-21-jdk \
    openjfx \
    xvfb \
    x11vnc \
    fluxbox \
    novnc \
    websockify \
    && rm -rf /var/lib/apt/lists/*

# Redirect the main URL straight to the app
RUN echo '<meta http-equiv="refresh" content="0; url=vnc.html?autoconnect=true&resize=scale">' > /usr/share/novnc/index.html

WORKDIR /app
COPY render-app/ /app/
COPY start.sh /start.sh
RUN chmod +x /start.sh

EXPOSE 10000
CMD ["/start.sh"]