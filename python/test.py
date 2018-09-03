import time
import DSprotocol
import os

driverprotocol = DSprotocol.DSprotocol(7303)
time.sleep(10)
print("modifying packet")
driverprotocol.toRioPacket = DSprotocol.ToRioPacket(False, False, True, DSprotocol.ControlMode.TELEOP, False, False, DSprotocol.AllianceStation.RED3)
print(driverprotocol.toRioPacket.getPacket())
print(driverprotocol.toRioPacket.getPacket())
print(driverprotocol.toRioPacket.getPacket())
time.sleep(10)
driverprotocol.close()
print("closing")
os._exit(10)