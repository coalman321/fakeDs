using System.Collections;
using System.Collections.Generic;
using comm;
using UnityEngine;


public class controller : MonoBehaviour {

    private RecievingPacket lastRecievingPacket;
    private SendingPacket lastSendingPacket;
    
    // Start is called before the first frame update
    void Start()
    {
        RioCommProtocol.startComm(4145, "10.41.45.2");
        lastSendingPacket = RioCommProtocol.getSending();
    }

    // Update is called once per frame
    void Update() {
        lastRecievingPacket = RioCommProtocol.getRecieving();
        RioCommProtocol.setSending(lastSendingPacket);
    }

    void OnApplicationQuit() {
        RioCommProtocol.close();
    }

    void setRobotMode(SendingPacket.ControlMode mode) {
        lastSendingPacket.setMode(mode);
    }
}
