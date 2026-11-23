#!/bin/bash
export DISPLAY=:99
Xvfb :99 -screen 0 1280x720x24 &
sleep 2
fluxbox &
x11vnc -display :99 -forever -nopw -rfbport 5900 -noxdamage &
websockify --web=/usr/share/novnc/ ${PORT:-10000} localhost:5900 &
cd /app
java -Xmx256m -Xss512k -XX:+UseSerialGC -XX:TieredStopAtLevel=1 \
  -Dprism.order=sw \
  --module-path /usr/share/openjfx/lib \
  --add-modules javafx.controls,javafx.fxml,javafx.web,javafx.media,javafx.swing \
  -jar /app/voNation.jar