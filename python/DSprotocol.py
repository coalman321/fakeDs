import socket
import time
from enum import Enum

class ControlMode(Enum):
    TELEOP = 0B00000000
    TEST =   0B00000001
    AUTO =   0B00000010

class AllianceStation(Enum):
    RED1 =  0B00000000
    RED2 =  0B00000001
    RED3 =  0B00000010
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

    def __init__(self, estop, fms, enabled, mode, restart, reset, station):
        self.isEstop = estop
        self.isFms = fms
        self.isEnabled = enabled
        self.controlmode = mode
        self.wantRestart = restart
        self.wantReset = reset
        self.allianceStation = station

    def getPacket(self):
        control = 0B00000000
        control |= self.isEstop << 7
        control |= self.isFms << 3
        control |= self.isEnabled << 2
        request = 0B00000000
        request |= self.wantRestart << 3
        request |= self.wantReset << 2
        out = bytearray()
        out.append(0x00)
        out.append(0x00)
        out.append(0x01)
        out.append(control)
        out.append(request)
        out.append(self.allianceStation.value & 0xff)
        return out

class DSprotocol:

    teamnumber = 0
    wantStop = False
    ip = ""
    toRioPacket = ToRioPacket

    def __init__(self, teamnum):
        self.teamnumber = teamnum
        self.ip = self.getAddr()
        self.toRioPacket = ToRioPacket(False, False, False, ControlMode.TELEOP, False, False, AllianceStation.RED1)
        self.packetHandler()

    def setPacket(self, newPacket):
        self.toRioPacket = newPacket

    def getAddr(self):
        try:
            return socket.gethostbyname("roborio-" + str(self.teamnumber) + "-frc.local")
        except socket.gaierror:
            print("failed to find host by name falling back to fixed IP")
            return socket.gethostbyname('10.' + str(self.teamnumber)[:2] + '.' + str(self.teamnumber)[2:] + '.2')

    def packetHandler(self):
        print("attempting connection to RIO")
        output = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        output.setsockopt(socket.SOL_SOCKET, socket.SO_BROADCAST, 1)
        try:
            output.bind(("", 1150))
            output.connect((self.ip, 1100))
            while not self.wantStop:
                output.sendto(self.toRioPacket.getPacket(), ('<broadcast>', 1100))
                time.sleep(0.018)
        except Exception as e:
            print(e)






