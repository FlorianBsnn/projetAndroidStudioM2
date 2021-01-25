import socket
import select
import time
import picamera
import os
import sys
import threading
import json
import paho.mqtt.client as mqtt

server_addr = ('', 5000)
directoryDevices=r"/sys/bus/w1/devices/"

def allSensor():
    tab = []
    for root, dirs, files in os.walk(directoryDevices):
        for f in dirs:
            if (f.startswith("28-")):
                tab.append(f)
    return tab

def getTemperature(name):
    print(directoryDevices+name+"/temperature")
    fich = open(directoryDevices+name+"/temperature")
    a = fich.read()
    fich.close()
    return a
    
def on_connect(client, userdata, flags, rc):
  print("Connected with result code "+str(rc))
  client.subscribe("topic/FB_Application")

def on_message(client, userdata, msg):
  m = msg.payload.decode("utf-8")
  j = json.loads(m)
  if j.get("order") == "sensor" :
        if j.get("value") == "init":
            tab = allSensor()
            jRep = { "order": "return","value": "init"}
            jRep["len"] = len(tab)
            for i in range (0, len(tab)):
                jRep["sensor"+str(i)] = tab[i]
                jRep["sensor"+str(i)+"val"] = getTemperature(tab[i])
            print(jRep)
            client.publish("topic/FB_Application", json.dumps(jRep))
        elif j.get("value") == "update":
            jRep = { "order": "return","value": "update"}
            tab = allSensor()
            jRep["len"] = str(len(tab))
            for i in range (0, len(tab)):
                jRep["sensor"+str(i)] = tab[i]
                jRep["sensor"+str(i)+"val"] = getTemperature(tab[i])
            print(jRep)
            client.publish("topic/FB_Application", json.dumps(jRep))
  


def MQTT_s():
    client = mqtt.Client()
    client.connect("localhost",1883,60)
    #client.connect("192.168.43.110",1883,60)

    client.on_connect = on_connect
    client.on_message = on_message

    client.loop_forever()

def takePics():
    camera = picamera.PiCamera()
    time.sleep(1.2)
    try:
        while True:
            
            camera.start_preview()
            with threadLock:
                camera.capture('/home/pi/Pictures/piCam/android_app/image.png')
                #print("photo prise")
            camera.stop_preview()
            time.sleep(0.2)
    finally:
        camera.close()

def setupServer():
    s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    print("Socket created")
    try:
        s.bind(server_addr)
    except socket.error as msg:
        print(msg)
    return s


def setupConnection(s):
    s.listen(1)
    print("Waiting for client")
    conn, addr = s.accept()
    return conn


def sendFile(conn):
    
    filename = '/home/pi/Pictures/piCam/android_app/image.png'
    
    with threadLock:
        f = open(filename, 'rb')
        line = f.read(1024)

        print("Beginning File Transfer")
        while line:
            conn.send(line)
            line = f.read(1024)
        f.close()
        print("Transfer Complete")
        conn.close()
        #time.sleep(1)





sock = setupServer()
threadLock = threading.Lock() #mutex

p = threading.Thread(name='takePicture', target = takePics)
p.start()

s = threading.Thread(name='MQTT_s', target = MQTT_s)
s.start()

while True:
    try:
        connection = setupConnection(sock)
        
        sendFile(connection)
    except:
        break

