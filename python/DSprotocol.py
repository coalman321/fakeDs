import socket
import time
import threading
import struct
from enum import Enum


class ControlMode(Enum):
    TELEOP = 0B00000000
    TEST = 0B00000001
    AUTO = 0B00000010


class AllianceStation(Enum):
    RED1 = 0B00000000
    RED2 = 0B00000001
    RED3 = 0B00000010
    BLUE1 = 0B00000011
    BLUE2 = 0B00000100
    BLUE3 = 0B00000101


class ToRioPacket:

    isEstop = False
    isFms = False
    isEnabled = False
    controlmode = ControlMode.TELEOP
    wantRestart = False
    wantReset = False
    allianceStation = AllianceStation.RED1
    counter = 0

    def __init__(self, estop, fms, enabled, mode, restart, reset, station):
        self.isEstop = estop
        self.isFms = fms
        self.isEnabled = enabled
        self.controlmode = mode
        self.wantRestart = restart
        self.wantReset = reset
        self.allianceStation = station

    def getpacket(self):
        self.counter += 1
        control = 0B00000000
        control |= self.isEstop << 7
        control |= self.isFms << 3
        control |= self.isEnabled << 2
        control |= self.controlmode.value
        request = 0B00000000
        request |= self.wantRestart << 3
        request |= self.wantReset << 2
        return struct.pack('BBBBBB', self.getcountermsb(), self.getcounterlsb(), 0x01,
                           control, request, self.allianceStation.value)

    def getcountermsb(self):
        return self.counter >> 8

    def getcounterlsb(self):
        return self.counter & 0xff


class DSprotocol:

    teamnumber = 0
    wantStop = False
    ip = ''
    toRioPacket = ToRioPacket
    packetThread = threading.Thread

    def __init__(self, teamnum):
        self.teamnumber = teamnum
        self.ip = self.getaddr()
        self.toRioPacket = ToRioPacket(False, False, False, ControlMode.TELEOP, False, False, AllianceStation.RED1)
        print(self.toRioPacket.getpacket())
        self.packetThread = threading.Thread(target=self.packethandler)
        self.packetThread.start()

    def getaddr(self):
        try:
            return socket.gethostbyname("roborio-" + str(self.teamnumber) + "-frc.local")
        except socket.gaierror:
            print("failed to find host by name falling back to fixed IP")
            return socket.gethostbyname('10.' + str(self.teamnumber)[:2] + '.' + str(self.teamnumber)[2:] + '.3')

    def packethandler(self):
        print("attempting connection to RIO")
        print(self.ip)
        output = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        output.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
        try:
            output.bind(("", 1150))
            print("connected to RIO begining broadcast")
            while not self.wantStop:
                output.sendto(self.toRioPacket.getpacket(), (self.ip, 1110))
                # s = output.recv(100)
                time.sleep(0.019)
        except Exception as e:
            print(e)

    def close(self):
        self.wantStop = True





