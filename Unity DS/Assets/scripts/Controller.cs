using System.Collections;
using System.Collections.Generic;
using comm;
using UnityEngine;


public class Controller : MonoBehaviour {

    private RecievingPacket lastRecievingPacket;
    private SendingPacket lastSendingPacket;
    
    // Start is called before the first frame update
    void Start()
    {
        ConsoleRedirect.Redirect();
        RioCommProtocol.startComm(4145, "10.41.45.2");
        lastSendingPacket = RioCommProtocol.getSending();
        lastRecievingPacket = RioCommProtocol.getRecieving();
    }

    // Update is called once per frame
    void Update() {
        RioCommProtocol.setSending(lastSendingPacket);
        lastRecievingPacket = RioCommProtocol.getRecieving();
    }

    void OnApplicationQuit() {
        RioCommProtocol.close();
    }

    public void setRobotMode(int mode) {
        lastSendingPacket.setMode((SendingPacket.ControlMode)mode);
    }

    public void setEnabled(bool enable) {
        lastSendingPacket.setWantEnabled(enable);
    }

    public string getRobotData() {
        return lastRecievingPacket.ToString();
    }
}
