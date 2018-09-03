import time
import DSprotocol
#import os

driverprotocol = DSprotocol.DSprotocol(7303)
time.sleep(2)
print("modifying packet")
driverprotocol.toRioPacket = DSprotocol.ToRioPacket(False, False, True, DSprotocol.ControlMode.TELEOP, False, False, DSprotocol.AllianceStation.RED3)
time.sleep(2)
print("modifying packet")
driverprotocol.toRioPacket = DSprotocol.ToRioPacket(False, False, True, DSprotocol.ControlMode.TEST, False, False, DSprotocol.AllianceStation.RED3)
time.sleep(2)
print("modifying packet")
driverprotocol.toRioPacket = DSprotocol.ToRioPacket(False, False, True, DSprotocol.ControlMode.AUTO, False, False, DSprotocol.AllianceStation.RED3)
time.sleep(2)
print("modifying packet")
driverprotocol.toRioPacket = DSprotocol.ToRioPacket(False, False, False, DSprotocol.ControlMode.AUTO, False, False, DSprotocol.AllianceStation.RED3)
time.sleep(2)
print("modifying packet")
driverprotocol.toRioPacket = DSprotocol.ToRioPacket(False, False, False, DSprotocol.ControlMode.AUTO, True, False, DSprotocol.AllianceStation.RED3)
time.sleep(0.1)
print("modifying packet")
driverprotocol.toRioPacket = DSprotocol.ToRioPacket(False, False, False, DSprotocol.ControlMode.AUTO, False, False, DSprotocol.AllianceStation.RED3)
time.sleep(10)
print("modifying packet")
driverprotocol.toRioPacket = DSprotocol.ToRioPacket(False, False, True, DSprotocol.ControlMode.TELEOP, False, False, DSprotocol.AllianceStation.BLUE3)
print("done")

